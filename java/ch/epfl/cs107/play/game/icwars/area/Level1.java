package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1 extends ICWarsArea{

    @Override
    public String getTitle() {
        return "icwars/Level1";
    }

    protected void createArea() {
        registerActor(new Background(this));
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() { return new DiscreteCoordinates(2,5); }

    public DiscreteCoordinates getEnemySpawnPosition() {
        return new DiscreteCoordinates(16, 5);
    }
    //public DiscreteCoordinates getEnemySpawnPosition2() {
        //return new DiscreteCoordinates(17, 5);
    //}
    //public DiscreteCoordinates getEnemySpawnPosition3() {
        //return new DiscreteCoordinates(18, 5);
    //}
}