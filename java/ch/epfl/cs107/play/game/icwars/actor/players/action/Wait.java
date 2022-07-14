package ch.epfl.cs107.play.game.icwars.actor.players.action;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.List;


public class Wait extends Action {

    private final String name = "(W)ait";
    private final int actionKey = Keyboard.W;

    public Wait (Unit unit, ICWarsArea area) {
        super(unit, area);
    }

    @Override
    public void draw(Canvas canvas) {}

    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        Unit unit = player.getSelectedUnit();
        unit.setIsUsed(true);
        player.setCurrentState(ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL);
        dt = 0;

    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player, Unit attackedUnit) {
    }
}