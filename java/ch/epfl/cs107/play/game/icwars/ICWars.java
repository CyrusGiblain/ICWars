package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Soldats;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Tanks;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.sql.SQLOutput;
import java.util.*;

import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.*;

public class ICWars extends AreaGame {

    public ICWarsArea area;
    //private RealPlayer player;
    private List<ICWarsPlayer> listOfPlayers = new ArrayList<>();
    private RealPlayer firstPlayerOfTheList;
    private RealPlayer secondPlayerOfTheList;
    private RealPlayer thirdPlayerOfTheList;
    private ArrayList<Unit> units = new ArrayList<>();
    private ICWarsCurrentState icWarsCurrentState = ICWarsCurrentState.INIT;
    private List<ICWarsPlayer> listOfPlayersWaitingForTheCurrentRound;
    private List<ICWarsPlayer> listOfPlayersWaitingForNextTurn = new ArrayList<>();
    private ICWarsPlayer currentPlayer;
    private int pointsJoueurBleu = 0;
    private int pointsJoueurOrange = 0;
    private int pointsJoueur3 = 0;
    private boolean joueurSeul = false;
    private boolean deuxJoueurs = false;
    private boolean troisJoueurs = false;

    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex = 0;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard = getWindow().getKeyboard();

        Button keyR =  keyboard.get(Keyboard.R);

        // PROBLEME
        if (keyR.isReleased()) {
            listOfPlayers.clear();
            listOfPlayersWaitingForTheCurrentRound.clear();
            listOfPlayersWaitingForNextTurn.clear();
            areaIndex = 0;
            joueurSeul = false;
            deuxJoueurs = false;
            troisJoueurs = false;
            begin(getWindow(), getFileSystem());
            icWarsCurrentState = ICWarsCurrentState.INIT;
        }

        switch (icWarsCurrentState) {

            case INIT:

                listOfPlayersWaitingForTheCurrentRound = new ArrayList<>();
                if (!listOfPlayers.isEmpty()) {
                    listOfPlayersWaitingForTheCurrentRound = new ArrayList<>(listOfPlayers);
                }

                icWarsCurrentState = ICWarsCurrentState.CHOOSE_PLAYER;

                break;

            case CHOOSE_PLAYER:

                if (listOfPlayersWaitingForTheCurrentRound.isEmpty()) {
                    icWarsCurrentState = ICWarsCurrentState.END_TURN;

                } else {

                    if (!listOfPlayersWaitingForTheCurrentRound.get(0).isDefeated()) {
                        currentPlayer = listOfPlayersWaitingForTheCurrentRound.get(0);
                        listOfPlayersWaitingForTheCurrentRound.remove(0);
                        icWarsCurrentState = ICWarsCurrentState.START_PLAYER_TURN;
                    }
                }

                break;

            case START_PLAYER_TURN:

                if (currentPlayer != null) {
                    currentPlayer.startTurn();
                    icWarsCurrentState = ICWarsCurrentState.PLAYER_TURN;
                }

                break;

            case PLAYER_TURN:

                if (currentPlayer.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.IDLE) {
                    icWarsCurrentState = ICWarsCurrentState.END_PLAYER_TURN;
                }

                break;

            case END_PLAYER_TURN:

                for (ICWarsPlayer player : listOfPlayers) {
                    if (player.isDefeated()) {
                        listOfPlayersWaitingForTheCurrentRound.remove(player);
                    } else {
                        for (Unit unit : player.getUnits()) {
                            unit.setIsUsed(false);
                        }
                    }
                }

                if (currentPlayer.isDefeated()) {
                    currentPlayer.leaveArea();
                } else if (!listOfPlayersWaitingForNextTurn.contains(currentPlayer)) {
                    listOfPlayersWaitingForNextTurn.add(currentPlayer);
                }
                icWarsCurrentState = ICWarsCurrentState.CHOOSE_PLAYER;
                this.currentPlayer.selectedUnit = null;

                break;

            case END_TURN:

                for (ICWarsPlayer player : listOfPlayers) {
                    if (player.isDefeated()) {
                        listOfPlayersWaitingForNextTurn.remove(player);
                    }
                }

                if (listOfPlayersWaitingForNextTurn.size() == 1) {
                    ICWarsActor.faction faction = ((RealPlayer) listOfPlayersWaitingForNextTurn.get(0)).getCamp();
                    if (faction == ALLIE) {
                        System.out.println("Le joueur bleu a gagné la manche n°" + (areaIndex + 1) + ".");
                        pointsJoueurBleu++;
                    } else if (faction == ENNEMI1){
                        System.out.println("Le joueur orange a gagné la manche n°" + (areaIndex + 1) + ".");
                        pointsJoueurOrange++;
                    } else {
                        System.out.println("Le joueur n°3 a gagné la manche n°" + (areaIndex + 1) + ".");
                        pointsJoueur3++;
                    }
                    icWarsCurrentState = ICWarsCurrentState.END;
                } else {
                    listOfPlayersWaitingForTheCurrentRound.clear();
                    listOfPlayersWaitingForTheCurrentRound.addAll(listOfPlayersWaitingForNextTurn);
                    icWarsCurrentState = ICWarsCurrentState.CHOOSE_PLAYER;
                }

                break;

            case END:

                switchArea();
                break;
        }
    }

    @Override
    public String getTitle() {
        return "ICWars";
    }

    /**
     * Method to create the 2 ares
     */
    public void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            initArea(areas[areaIndex]);
            icWarsCurrentState = ICWarsCurrentState.INIT;
            return true;
        }
        return false;
    }

    /**
     * Method to initialise an area
     *
     * @param areaKey (String): The String that designs an area
     */
    private void initArea(String areaKey) {

        //First player
        area = (ICWarsArea) setCurrentArea(areaKey, true);

        DiscreteCoordinates coordsALLY = area.getPlayerSpawnPosition();

        DiscreteCoordinates coordsOfTheTankOfTheFirstRealPlayer = new DiscreteCoordinates(0, 5);
        DiscreteCoordinates coordsOfTheSoldatOfTheFirstRealPlayer = new DiscreteCoordinates(1, 5);

        Tanks tankFirstPlayer = new Tanks(area, coordsOfTheTankOfTheFirstRealPlayer, ALLIE);
        Soldats soldatFirstPlayer = new Soldats(area, coordsOfTheSoldatOfTheFirstRealPlayer, ALLIE);
        firstPlayerOfTheList = new RealPlayer(area, coordsALLY, ALLIE, tankFirstPlayer, soldatFirstPlayer);

        area.units.add(tankFirstPlayer);
        area.units.add(soldatFirstPlayer);
        listOfPlayers.add(firstPlayerOfTheList);
        firstPlayerOfTheList.enterArea(area, coordsALLY);

        firstPlayerOfTheList.centerCamera();

        if (areaIndex == 0) {

            while (!joueurSeul && !deuxJoueurs && !troisJoueurs) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Souhaitez-vous jouer seul ?");
                String reponse1 = scanner.nextLine();

                if (reponse1.equals("oui") || reponse1.equals("Oui")) {
                    joueurSeul = true;
                } else {
                    System.out.println("Souhaitez-vous jouer à 2 ?");
                    String reponse2 = scanner.nextLine();
                    if (reponse2.equals("oui") || reponse2.equals("Oui")) {
                        deuxJoueurs = true;
                    } else {
                        System.out.println("Souhaitez-vous jouer à 3 ?");
                        String reponse3 = scanner.nextLine();
                        if (reponse3.equals("oui") || reponse3.equals("Oui")) {
                            troisJoueurs = true;
                        }
                    }
                }
            }
        }

        if (joueurSeul) {
            // Ajouter le joueur IA ici.
        }

        if (deuxJoueurs) {

            //Second player
            DiscreteCoordinates coordsEnemy1 = area.getEnemy1SpawnPosition();

            DiscreteCoordinates coordsOfTheTankOfTheSecondRealPlayer = new DiscreteCoordinates(9, 5);
            DiscreteCoordinates coordsOfTheSoldatOfTheSecondRealPlayer = new DiscreteCoordinates(8, 5);

            Tanks tankSecondPlayer = new Tanks(area, coordsOfTheTankOfTheSecondRealPlayer, ENNEMI1);
            Soldats soldatSecondPlayer = new Soldats(area, coordsOfTheSoldatOfTheSecondRealPlayer, ENNEMI1);
            secondPlayerOfTheList = new RealPlayer(area, coordsEnemy1, ENNEMI1, tankSecondPlayer, soldatSecondPlayer);

            area.units.add(tankSecondPlayer);
            area.units.add(soldatSecondPlayer);
            listOfPlayers.add(secondPlayerOfTheList);
            secondPlayerOfTheList.enterArea(area, coordsEnemy1);
        }

        if (troisJoueurs) {

            //Second player
            DiscreteCoordinates coordsEnemy1 = area.getEnemy1SpawnPosition();

            DiscreteCoordinates coordsOfTheTankOfTheSecondRealPlayer = new DiscreteCoordinates(9, 5);
            DiscreteCoordinates coordsOfTheSoldatOfTheSecondRealPlayer = new DiscreteCoordinates(8, 5);

            Tanks tankSecondPlayer = new Tanks(area, coordsOfTheTankOfTheSecondRealPlayer, ENNEMI1);
            Soldats soldatSecondPlayer = new Soldats(area, coordsOfTheSoldatOfTheSecondRealPlayer, ENNEMI1);
            secondPlayerOfTheList = new RealPlayer(area, coordsEnemy1, ENNEMI1, tankSecondPlayer, soldatSecondPlayer);

            area.units.add(tankSecondPlayer);
            area.units.add(soldatSecondPlayer);
            listOfPlayers.add(secondPlayerOfTheList);
            secondPlayerOfTheList.enterArea(area, coordsEnemy1);

            //Third player
            DiscreteCoordinates coordsEnemy2 = area.getEnemy2SpawnPosition();

            DiscreteCoordinates coordsOfTheTankOfTheThirdRealPlayer = new DiscreteCoordinates(4, 0);
            DiscreteCoordinates coordsOfTheSoldatOfTheThirdRealPlayer = new DiscreteCoordinates(5, 0);

            Tanks tankThirdPlayer = new Tanks(area, coordsOfTheTankOfTheThirdRealPlayer, ENNEMI2);
            Soldats soldatThirdPlayer = new Soldats(area, coordsOfTheSoldatOfTheThirdRealPlayer, ENNEMI2);
            thirdPlayerOfTheList = new RealPlayer(area, coordsEnemy2, ENNEMI2, tankThirdPlayer, soldatThirdPlayer);

            area.units.add(tankThirdPlayer);
            area.units.add(soldatThirdPlayer);
            listOfPlayers.add(thirdPlayerOfTheList);
            thirdPlayerOfTheList.enterArea(area, coordsEnemy2);
        }
    }

    // Method to end the game
    public void end(){

        super.end();

        if (listOfPlayers.size() == 3) {
            if (pointsJoueurBleu > pointsJoueurOrange && pointsJoueurBleu > pointsJoueur3) {
                System.out.println("Le joueur bleu a gagné la partie avec " + pointsJoueurBleu + " points.");
            } else if (pointsJoueurOrange > pointsJoueurBleu && pointsJoueurOrange > pointsJoueur3) {
                System.out.println("Le joueur orange a gagné la partie avec " + pointsJoueurOrange + " points.");
            } else if (pointsJoueur3 > pointsJoueurBleu && pointsJoueur3 > pointsJoueurOrange) {
                System.out.println("Le joueur n°3 a gagné la partie avec " + pointsJoueur3 + " points.");
            } else if (pointsJoueurBleu == 0 && pointsJoueurOrange != 0 && pointsJoueur3 != 0){
                System.out.println("Le joueur bleu a perdu la partie puisqu'il n'a pas marqué de points.");
            } else if (pointsJoueurOrange == 0 && pointsJoueurBleu != 0 && pointsJoueur3 != 0) {
                System.out.println("Le joueur orange a perdu la partie puisqu'il n'a pas marqué de points.");
            } else if (pointsJoueur3 == 0 && pointsJoueurBleu != 0 && pointsJoueurOrange != 0) {
                System.out.println("Le joueur n°3 a perdu la partie puisqu'il n'a pas marqué de points.");
            }
        } else {
            if (pointsJoueurBleu > pointsJoueurOrange) {
                System.out.println("Le joueur bleu a gagné la partie 2 à 0.");
            } else if (pointsJoueurBleu == pointsJoueurOrange) {
                System.out.println("La partie se termine par un match nul 1-1.");
            } else {
                System.out.println("Le joueur orange a gagné la partie 2 à 0");
            }
        }
        System.exit(0);
    }

    //Method to switch from the first area to the second
    protected void switchArea() {

        if (areaIndex == 0) {
            listOfPlayers.clear();
            listOfPlayersWaitingForTheCurrentRound.clear();
            listOfPlayersWaitingForNextTurn.clear();
            areaIndex = 1;
            begin(getWindow(), getFileSystem());
        } else {
            end();
        }
    }

    /**
     * @return the ArrayList of the units
     */
    public ArrayList<Unit> getUnits(){return units;}

    // The enumeration that designs the current state of ICWars
    public enum ICWarsCurrentState {
        INIT,
        CHOOSE_PLAYER,
        START_PLAYER_TURN,
        PLAYER_TURN,
        END_PLAYER_TURN,
        END_TURN,
        END;
    }
}
// IA à faire.