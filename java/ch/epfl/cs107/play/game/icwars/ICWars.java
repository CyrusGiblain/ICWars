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

import java.util.*;

import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.*;

public class ICWars extends AreaGame {

    public ICWarsArea area;
    //private RealPlayer player;
    private List<ICWarsPlayer> listOfPlayers = new ArrayList<>();
    private RealPlayer firstPlayerOfTheList;
    private RealPlayer secondPlayerOfTheList;
    //private RealPlayer thirdPlayerOfTheList;
    //private RealPlayer fourthPlayerOfTheList;
    private ArrayList<Unit> units = new ArrayList<>();
    private ICWarsCurrentState icWarsCurrentState = ICWarsCurrentState.INIT;
    private List<ICWarsPlayer> listOfPlayersWaitingForTheCurrentRound;
    private List<ICWarsPlayer> listOfPlayersWaitingForNextTurn = new ArrayList<>();
    private ICWarsPlayer currentPlayer;
    private int pointsJoueurBleu = 0;
    private int pointsJoueurOrange = 0;

    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex = 0;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard = getWindow().getKeyboard();

        Button keyR =  keyboard.get(Keyboard.R);

        if (keyR.isReleased()) {
            listOfPlayers.clear();
            listOfPlayersWaitingForTheCurrentRound.clear();
            listOfPlayersWaitingForNextTurn.clear();
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
                        System.out.println("Le joueur bleu a vaincu le joueur orange lors du niveau " + (areaIndex + 1) + ".");
                        pointsJoueurBleu++;
                    } else {
                        System.out.println("Le joueur orange a vaincu le joueur bleu lors du niveau " + (areaIndex + 1) + ".");
                        pointsJoueurOrange++;
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

        area = (ICWarsArea) setCurrentArea(areaKey, true);

        DiscreteCoordinates coordsALLY = area.getPlayerSpawnPosition();
        DiscreteCoordinates coordsEnnemy1 = area.getEnemySpawnPosition();

        DiscreteCoordinates coordsOfTheTankOfTheFirstRealPlayer = new DiscreteCoordinates(2, 5);
        DiscreteCoordinates coordsOfTheSoldatOfTheFirstRealPlayer = new DiscreteCoordinates(3, 5);

        DiscreteCoordinates coordsOfTheTankOfTheSecondRealPlayer = new DiscreteCoordinates(8, 5);
        DiscreteCoordinates coordsOfTheSoldatOfTheSecondRealPlayer = new DiscreteCoordinates(9, 5);

        Tanks tankFirstPlayer = new Tanks(area, coordsOfTheTankOfTheFirstRealPlayer, ALLIE);
        Soldats soldatFirstPlayer = new Soldats(area, coordsOfTheSoldatOfTheFirstRealPlayer, ALLIE);
        firstPlayerOfTheList = new RealPlayer(area, coordsALLY, ALLIE, tankFirstPlayer, soldatFirstPlayer);


        Tanks tankSecondPlayer = new Tanks(area, coordsOfTheTankOfTheSecondRealPlayer, ENNEMIE);
        Soldats soldatSecondPlayer = new Soldats(area, coordsOfTheSoldatOfTheSecondRealPlayer, ENNEMIE);
        secondPlayerOfTheList = new RealPlayer(area, coordsEnnemy1, ENNEMIE, tankSecondPlayer, soldatSecondPlayer);

        area.units.add(tankFirstPlayer);
        area.units.add(soldatFirstPlayer);
        area.units.add(tankSecondPlayer);
        area.units.add(soldatSecondPlayer);

        listOfPlayers.add(firstPlayerOfTheList);
        listOfPlayers.add(secondPlayerOfTheList);

        firstPlayerOfTheList.centerCamera();
        firstPlayerOfTheList.enterArea(area, coordsALLY);

        secondPlayerOfTheList.enterArea(area, coordsEnnemy1);
    }

    // Method to end the game
    public void end(){

        super.end();

        if (pointsJoueurBleu > pointsJoueurOrange) {
            System.out.println("Le joueur bleu a remporté la partie " + pointsJoueurBleu + "-" + pointsJoueurOrange + ".");
        } else if (pointsJoueurBleu == pointsJoueurOrange) {
            System.out.println("La partie se termine sur un match nul " + pointsJoueurBleu + "-" + pointsJoueurOrange + ".");
        } else {
            System.out.println("Le joueur orange a remporté la partie " + pointsJoueurOrange + "-" + pointsJoueurBleu + ".");
        }
        System.exit(0);
    }

    //Method to switch from the first area to the second
    protected void switchArea() {

        listOfPlayers.clear();
        listOfPlayersWaitingForTheCurrentRound.clear();
        listOfPlayersWaitingForNextTurn.clear();

        if (areaIndex == 0) {
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
// Interaction avec les unités à finir.
// IA à faire.