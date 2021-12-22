package ch.epfl.cs107.play.game.icwars.actor.players.action;

import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public abstract class Action{
    Unit unit;
    ICWarsArea area;
    String name;
    int actionKey;

    public Action(Unit unit, ICWarsArea area){
        this.unit = unit;
        this.area = area;
    }
    public abstract void draw(Canvas canvas);
    public abstract void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);
    public abstract String getName();
    public abstract void doAutoAction(float dt, ICWarsPlayer player, Unit attackedUnit);
    public int getActionKey(){return actionKey;}
}

