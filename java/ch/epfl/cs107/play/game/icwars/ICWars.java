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

    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard= getWindow().getKeyboard();

        Button keyN = keyboard.get(Keyboard.N);
        Button keyR =  keyboard.get(Keyboard.R);
        Button tab = keyboard.get(Keyboard.TAB);
        Button G = keyboard.get(Keyboard.G);

            //if (areaIndex == 0) {
            //if (keyN.isReleased()) {
            //     switchArea();
            //}

            if (keyR.isReleased()) {
                listOfPlayers.clear();
                initArea(areas[0]);
                listOfPlayersWaitingForTheCurrentRound.clear();
                listOfPlayersWaitingForNextTurn.clear();
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
                    currentPlayer = listOfPlayersWaitingForTheCurrentRound.get(0);
                    listOfPlayersWaitingForTheCurrentRound.remove(0);
                    icWarsCurrentState = ICWarsCurrentState.START_PLAYER_TURN;
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
                if (currentPlayer.isDefeated()) {
                    area.unregisterActor(currentPlayer);
                            } else if(tab.isReleased()){
                    listOfPlayersWaitingForNextTurn.add(currentPlayer);
                    for (int i = 0; i < currentPlayer.getUnits().size(); ++i) {
                        currentPlayer.getUnits().get(i).setIsUsed(false);
                    }
                    icWarsCurrentState = ICWarsCurrentState.CHOOSE_PLAYER;
                }
                break;

            case END_TURN:
                for (int i = 0; i < listOfPlayersWaitingForNextTurn.size(); ++i) {
                    if (listOfPlayersWaitingForNextTurn.get(i).isDefeated()) {
                        listOfPlayersWaitingForNextTurn.remove(i);
                    }
                }
                for (int i = 0; i < listOfPlayers.size(); ++i) {
                    if (listOfPlayers.get(i).isDefeated()) {
                        listOfPlayers.remove(i);
                    }
                }
                if (listOfPlayersWaitingForNextTurn.size() == 1) {
                    icWarsCurrentState = ICWarsCurrentState.END;
                } else {
                    listOfPlayersWaitingForTheCurrentRound.clear();
                    listOfPlayersWaitingForTheCurrentRound.addAll(listOfPlayersWaitingForNextTurn);
                    icWarsCurrentState = ICWarsCurrentState.CHOOSE_PLAYER;
                }
                break;

            case END:
                switchArea();
                }
        }
        //EFFECTUER LA RETOUCHE GRAPHIQUE.

        //if (keyboard.get(Keyboard.U).isReleased()) { ((RealPlayer)player).selectUnit(0);} // 0, 1 ...

    @Override
    public String getTitle() {
        return "ICWars";
    }

    public void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {

        area = (ICWarsArea) setCurrentArea(areaKey, true);

        DiscreteCoordinates coordsALLY = area.getPlayerSpawnPosition();
        DiscreteCoordinates coordsEnnemy1 = area.getEnemySpawnPosition();
        //DiscreteCoordinates coordsEnnemy2 = area.getEnemySpawnPosition2();
        //DiscreteCoordinates coordsEnnemy3 = area.getEnemySpawnPosition3();


        //DiscreteCoordinates coordsSoldat = new DiscreteCoordinates(5,6);
        //DiscreteCoordinates coordsTank = new DiscreteCoordinates(3, 2);
        //player = new RealPlayer(area, coords, ALLIE, new Soldats(area, coordsSoldat, ALLIE), new Tanks(area, coordsTank, ALLIE));
        //player.enterArea(area, coords);
        //player.centerCamera();
        //player.startTurn();

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

        //thirdPlayerOfTheList = new RealPlayer(area, coordsEnnemy2, ENNEMIE2, new Tanks(area, coordsOfTheTankOfTheThirdRealPlayer, ENNEMIE2),
                //new Soldats(area, coordsOfTheSoldatOfTheThirdRealPlayer, ENNEMIE2));

        //fourthPlayerOfTheList = new RealPlayer(area, coordsEnnemy3, ENNEMIE3, new Tanks(area, coordsOfTheTankOfTheFourthRealPlayer, ENNEMIE3),
                //new Soldats(area, coordsOfTheSoldatOfTheFourthRealPlayer, ENNEMIE3));

        listOfPlayers.add(firstPlayerOfTheList);
        listOfPlayers.add(secondPlayerOfTheList);
        //listOfPlayers.add(thirdPlayerOfTheList);
        //listOfPlayers.add(fourthPlayerOfTheList);

        firstPlayerOfTheList.centerCamera();
        firstPlayerOfTheList.enterArea(area, coordsALLY);

        secondPlayerOfTheList.enterArea(area, coordsEnnemy1);

        //thirdPlayerOfTheList.enterArea(area, coordsEnnemy2);
        //fourthPlayerOfTheList.enterArea(area, coordsEnnemy3);

        //Tanks tank = new Tanks(area, new DiscreteCoordinates(3, 4), ALLIE);
        //Soldats soldat = new Soldats(area, new DiscreteCoordinates(5, 6), ALLIE);
        //units.add(tank);
        //units.add(soldat);
    }
    public void end(){
        super.end();
    }

    protected void switchArea() {
        //player.leaveArea();
        for (ICWarsPlayer player : listOfPlayersWaitingForNextTurn) {
            player.leaveArea();
        }
        areaIndex = (areaIndex==0) ? 1 : 0;
        ICWarsArea currentArea = (ICWarsArea) setCurrentArea(areas[areaIndex], false);
        //player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        for (ICWarsPlayer player : listOfPlayers) {
            player.enterArea(area, currentArea.getPlayerSpawnPosition());
        }
    }
    public ArrayList<Unit> getUnits(){return units;}

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