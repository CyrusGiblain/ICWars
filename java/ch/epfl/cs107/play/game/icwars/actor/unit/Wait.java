package ch.epfl.cs107.play.game.icwars.actor.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import static ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL;
//may be an interface
public class Wait extends Action{
    private String name = "(W)ait";
    private int actionKey = 87;
    public Wait (Unit unit, ICWarsArea area) {
        super(unit, area);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        Unit unit;
        unit = player.getSelectedUnit();
        ICWarsPlayer.ICWarsPlayerCurrentState currentState;
        dt = 0;
        currentState = ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL;
        unit.setIsUsed(true);
    }

    public String getName(){
        return this.name;
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player, Unit attackedUnit) {
        ICWarsPlayer.ICWarsPlayerCurrentState currentState = ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL;
    }
}