package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0 extends ICWarsArea{

    /**
     * @return the name of the area Level0
     */
    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    /**
     * Method to create the visual of the Background of the area
     */
    protected void createArea() {
        registerActor(new Background(this));
    }

    /**
     * The 2 concrete implementations of the abstract method of ICWarsArea
     *
     * @return the spawn position of the ally and the enemy on Level0
     */
    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(5, 5);
    }
    @Override
    public DiscreteCoordinates getEnemy1SpawnPosition() {
        return new DiscreteCoordinates(9, 9);
    }

    public DiscreteCoordinates getEnemy2SpawnPosition() {
        return new DiscreteCoordinates(9, 0);
    }
}

