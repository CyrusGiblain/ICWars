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

    //Method to know the zoom of the camera
    public final float getCameraScaleFactor(){return 10.f;}

    /**
     * These two methods will be overridden in Level0 and Level1
     * to get the specified coordinates of the spawn of the player
     * and the enemy
     *
     * @return the DiscretePosition of the position
     */
    public abstract DiscreteCoordinates getPlayerSpawnPosition();
    public abstract DiscreteCoordinates getEnemy1SpawnPosition();
    public abstract DiscreteCoordinates getEnemy2SpawnPosition();

    /**
     * Method that enables to start the game
     *
     * @param window (Window) The window on which the game is played
     * @param fileSystem (FileSystem) Thee fileSystem necessary
     * @return true if the game can be started
     */
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
