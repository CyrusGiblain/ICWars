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
    private String spriteName;

    /**
     * Constructor of ICWarsPlayer
     *
     * @param area     (Area): The area on which the ICWarsPlayer is displayed
     * @param position (DiscreteCoordinates): The coordinates of the ICWarsPlayer
     * @param camp     (faction): The faction of the ICWarsPlayer
     * @param units    (Unit...): The units of the ICWarsPlayer
     */
    public ICWarsPlayer(Area area, DiscreteCoordinates position, faction camp, Unit... units) {
        super(area, position, camp);
        this.units.addAll(Arrays.asList(units));
        registerUnits();
        this.currentState = ICWarsPlayerCurrentState.IDLE;

        this.coords = position;
        if (camp == faction.ALLIE) {
            spriteName = "icwars/allyCursor";
        } else if (camp == faction.ENNEMI1) {
            spriteName = "icwars/enemyCursor";
        } else {
            spriteName = "cellOver";
        }
        sprite = new Sprite(spriteName, 1.f, 1.f,this);
    }

    /**
     * Method to update the ICWarsPlayer
     *
     * @param deltaTime (float): The time since the last update
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Unit unit : this.getUnits()) {
            if ((this.getCurrentMainCellCoordinates().x) == (unit.getFromX()) &&
                    (this.getCurrentMainCellCoordinates().y) == (unit.getFromY())) {
                unitOnWhichHeIsLocated = unit;
            }
        }

        for (int i = 0; i < units.size(); ++i) {
            if (units.get(i).isDead()) {
                units.remove(i);
            }
        }
    }

    /**
     * Method to register the unit to the area
     */
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
        ICWarsPlayerInteractionHandler handler = new ICWarsPlayerInteractionHandler();
        if (!isDisplacementOccurs()) {
            other.acceptInteraction(handler);
        }
    }

    // The enumeration that gives the current state of the ICWarsPlayer
    public enum ICWarsPlayerCurrentState {
        IDLE,
        NORMAL,
        SELECT_CELL,
        MOVE_UNIT,
        ACTION_SELECTION,
        ACTION;
    }

    /**
     * Method to start the given ICWarsPlayer's turn
     */
    public void startTurn() {
        for (Unit unit : this.getUnits()) {
            unit.setIsUsed(false);
        }
        currentState = ICWarsPlayerCurrentState.NORMAL;
    }

    /**
     * Method to draw the ICWarsPlayer
     *
     * @param canvas (Canvas): The canvas used to draw
     */
    @Override
    public void draw(Canvas canvas) {
        for (Unit unit : units) {
            unit.draw(canvas);
        }
        if (!this.isDefeated())
        sprite.draw(canvas);
    }

    // Method to center the camera on ICWarsPlayer
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }


    // Method to unregister the unit from the area
    public void leaveArea(){
        for (Unit unit : units) {
            unit.leaveArea();
        }
    }

    /**
     * @return true is the ICWarsPlayer is defeated
     */
    public boolean isDefeated(){
        return units.size() == 0;
    }

    /**
     * @return the unit the ICWarsPlayer selected
     */
    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * @return the ICWarsPlayer current state
     */
    public ICWarsPlayerCurrentState getCurrentState() {
        return currentState;
    }

    /**
     * Method to set the current position of the ICWarsPlayer
     *
     * @param coords (DiscreteCoordinates): The coordinates we want to the ICWarsPlayer to be set at
     */
    public void setCurrentPosition(DiscreteCoordinates coords) {
        this.coords = coords;
    }

    /**
     * @return the units of the ICWarsPlayer
     */
    public List<Unit> getUnits() {
        return units;
    }

    /**
     * @param currentState (ICWarsPlayerCurrentState): The state we want the ICWarsPlayer to be set at
     */
    public void setCurrentState(ICWarsPlayerCurrentState currentState){
        this.currentState = currentState;
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
        return true;
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

    public faction getCamp() {
        return camp;
    }

    // The handler class
    private class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            //icWarsPlayerGUI.setCellUnit(unit);
            if (getCamp().equals(unit.getCamp()) && currentState == ICWarsPlayerCurrentState.SELECT_CELL) {
                selectedUnit = unit;
                //icWarsPlayerGUI.setUnit(unit);
                currentState = ICWarsPlayerCurrentState.MOVE_UNIT;
            }

        }
    }
}