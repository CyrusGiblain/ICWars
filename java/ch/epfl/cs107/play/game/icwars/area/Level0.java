package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Soldats;
import ch.epfl.cs107.play.game.icwars.actor.Tanks;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0 extends ICWarsArea{

    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    protected void createArea() {
        registerActor(new Background(this));
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(0, 0);
    }
    public DiscreteCoordinates getEnemySpawnPosition() { return new DiscreteCoordinates(6, 4); }
    //public DiscreteCoordinates getEnemySpawnPosition2() { return new DiscreteCoordinates(7, 4); }
    //public DiscreteCoordinates getEnemySpawnPosition3() { return new DiscreteCoordinates(8, 4); }
}

