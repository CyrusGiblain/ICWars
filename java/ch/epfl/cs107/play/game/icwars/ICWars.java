package ch.epfl.cs107.play.game.icwars;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.Soldats;
import ch.epfl.cs107.play.game.icwars.actor.Tanks;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
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
import java.util.ArrayList;

import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ALLIE;

public class ICWars extends AreaGame {
    ICWarsArea area;
    private RealPlayer player;
    private ArrayList<Unit> units = new ArrayList<>();

    private final String[] areas = {"icwars/Level0", "icwars/Level1"};
    private int areaIndex;
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard= getWindow().getKeyboard();

        Button keyN = keyboard.get(Keyboard.N);
        Button keyR =  keyboard.get(Keyboard.R);

        if (areaIndex == 0) {

            if (keyN.isDown()) {
                switchArea();
                System.out.println(35);
            }
            if (keyR.isDown()) {
                begin(getWindow(), getFileSystem());
            }
        } else {
            if (keyN.isDown()) {
                System.out.println("Game over");
            } else {
                if (keyR.isDown()) {
                    begin(getWindow(), getFileSystem());
                }
            }
        }

        //if (keyboard.get(Keyboard.U).isReleased()) { ((RealPlayer)player).selectUnit(0);} // 0, 1 ...
    }

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
        ICWarsArea area = (ICWarsArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        DiscreteCoordinates coordsSoldat = new DiscreteCoordinates(5,6);
        DiscreteCoordinates coordsTank = new DiscreteCoordinates(3, 2);
        player = new RealPlayer(area, coords, ALLIE, new Soldats(area, coordsSoldat, ALLIE), new Tanks(area, coordsTank, ALLIE));
        player.enterArea(area, coords);
        player.centerCamera();
        player.startTurn();

        Tanks tank = new Tanks(area, new DiscreteCoordinates(3, 4), ALLIE );
        Soldats soldat = new Soldats(area, new DiscreteCoordinates(5, 6), ALLIE);
        units.add(tank);
        units.add(soldat);
    }
    public void end(){
        super.end();
    }

    protected void switchArea() {
        player.leaveArea();
        areaIndex = (areaIndex==0) ? 1 : 0;
        ICWarsArea currentArea = (ICWarsArea) setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
    }
    public ArrayList<Unit> getUnits(){return units;}
}