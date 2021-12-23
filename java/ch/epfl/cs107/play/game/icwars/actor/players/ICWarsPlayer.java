package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICWarsPlayer extends ICWarsActor implements Interactor {
    protected List<Unit> units = new ArrayList<>();
    protected Sprite sprite;
    public Unit selectedUnit;
    protected ICWarsPlayer.ICWarsPlayerCurrentState currentState;
    private DiscreteCoordinates coords;
    private Unit unitOnWhichHeIsLocated;


    public ICWarsPlayer(Area area, DiscreteCoordinates position, faction camp, Unit... units) {
        super(area, position, camp);
        this.units.addAll(Arrays.asList(units));
        registerUnits();
        this.currentState = ICWarsPlayerCurrentState.IDLE;
        this.coords = position;
    }

    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Unit unit : this.getUnits()) {
            if ((this.getCurrentMainCellCoordinates().x) == (unit.getFromX()) &&
                    (this.getCurrentMainCellCoordinates().y) == (unit.getFromY())) {
                unitOnWhichHeIsLocated = unit;
            }
        }
        for (int i = 0; i < units.size(); ++i) {
            if (units.get(i).isDead(units.get(i))) {
                getOwnerArea().unregisterActor(units.get(i));
                units.remove(i);
            }
        }
    }

    public void registerUnits(){
        for (Unit unit : units) {
            getOwnerArea().registerActor(unit);
        }
    }

    /**
     * Get this Interactor's current field of view cells coordinates
     *
     * @return (List of DiscreteCoordinates). May be empty but not null
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    /**
     * @return (boolean): true if this require cell interaction
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * @return (boolean): true if this require view interaction
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * Do this Interactor interact with the given Interactable
     * The interaction is implemented on the interactor side !
     *
     * @param other (Interactable). Not null
     */
    @Override
    public void interactWith(Interactable other) {
    }

    public enum ICWarsPlayerCurrentState {
        IDLE,
        NORMAL,
        SELECT_CELL,
        MOVE_UNIT,
        ACTION_SELECTION,
        ACTION;
    }

    public void startTurn() {
        for (Unit unit : this.getUnits()) {
            unit.setIsUsed(false);
        }
        currentState = ICWarsPlayerCurrentState.NORMAL;
    }

    @Override
    public void draw(Canvas canvas) {
        for (Unit unit : units) {
            unit.draw(canvas);
        }
        sprite.draw(canvas);
    }

    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }


    public void leaveArea(){
        for (Unit unit : units) {
            getOwnerArea().unregisterActor(unit);
        }
    }

    public boolean isDefeated(){
        return units.isEmpty();
    }

    //4 methods of Interactable
    /**
     * Indicate if the current Interactable take the whole cell space or not
     * i.e. only one Interactable which takeCellSpace can be in a cell
     * (how many Interactable which don't takeCellSpace can also be in the same cell)
     *
     * @return (boolean)
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * @return (boolean): true if this is able to have cell interactions
     */
    @Override
    public boolean isCellInteractable() {
        return false;
    }

    /**
     * @return (boolean): true if this is able to have view interactions
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Call directly the interaction on this if accepted
     *
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor)v).interactWith(this); }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (!getCurrentCells().equals(coordinates) && currentState == ICWarsPlayerCurrentState.SELECT_CELL) {
            currentState = ICWarsPlayerCurrentState.NORMAL;
        }
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public ICWarsPlayerCurrentState getCurrentState() {
        return currentState;
    }

    public void setCurrentPosition(DiscreteCoordinates coords) {
        this.coords = coords;
    }

    public List<Unit> getUnits() {
        return units;
    }


    public void setCurrentState(ICWarsPlayerCurrentState currentState){
        this.currentState = currentState;
    }

}