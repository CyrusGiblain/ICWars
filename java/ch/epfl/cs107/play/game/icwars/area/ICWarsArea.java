package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICWarsArea extends Area {
    private ICWarsBehavior behavior;
    public List<Unit> units = new ArrayList<>();


    protected abstract void createArea();


    public final float getCameraScaleFactor(){return 10.f;}

    public abstract DiscreteCoordinates getPlayerSpawnPosition();
    public abstract DiscreteCoordinates getEnemySpawnPosition();

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }
}
