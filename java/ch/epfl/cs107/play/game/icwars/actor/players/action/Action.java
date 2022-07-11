package ch.epfl.cs107.play.game.icwars.actor.players.action;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public abstract class Action implements Graphics {
    Unit unit;
    ICWarsArea area;
    String name;
    int actionKey;

    /**
     * Constructor of Action
     *
     * @param unit (Unit): The unit involved in the action
     * @param area (ICWarsArea): The area on which the action takes place
     */
    public Action(Unit unit, ICWarsArea area){
        this.unit = unit;
        this.area = area;
    }

    /**
     * Method to draw the action
     *
     * @param canvas (Canvas) The canvas used to draw the action
     */
    public abstract void draw(Canvas canvas);

    /**
     * Method to do the action
     *
     * @param dt       (float): The time dt
     * @param player   (ICWarsPlayer): The ICWarsPlayer involved
     * @param keyboard (Keyboard): The keyboard used
     */
    public abstract void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);

    /**
     * @return the name of action
     */
    public abstract String getName();

    /**
     * Method corresponding to the action done by the AIPlayer
     *
     * @param dt           (float): The time dt
     * @param player       (ICWarsPlayer): The ICWarsPlayer involved
     * @param attackedUnit (Unit): The attacked unit
     */
    public abstract void doAutoAction(float dt, ICWarsPlayer player, Unit attackedUnit);

    /**
     * @return the action key
     */
    public int getActionKey(){return actionKey;}

}
