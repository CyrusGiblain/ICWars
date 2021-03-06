package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior.ICWarsCellType;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class RealPlayer extends ICWarsPlayer implements Interactable {
    private float hp;
    //private Sprite sprite;
    //private String spriteName;
    private ICWarsPlayerGUI icWarsPlayerGUI;
    // Animation duration in frame number
    //to Change before rendu
    private final static int MOVE_DURATION = 3;
    private ICWarsArea area;
    private Action action;
    private ICWarsCellType cellType;

    /**
     * Constructor of RealPlayer
     *
     * @param owner    (ICWarsArea): The ICWarsArea of the RealPlayer
     * @param position (DiscreteCoordinates): The initial position of the RealPLayer
     * @param camp     (faction): The faction of the RealPlayer
     * @param units    (Unit...): The units owned by the RealPlayer
     */
    public RealPlayer(ICWarsArea owner, DiscreteCoordinates position, faction camp, Unit... units) {
        super(owner, position, camp, units);
        this.area = owner;
        this.icWarsPlayerGUI = new ICWarsPlayerGUI(10.0f, this);
        resetMotion();
    }

    /**
     * Center the camera on the RealPlayer
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);

        Keyboard keyboard1= getOwnerArea().getKeyboard();

        Button enter = keyboard1.get(Keyboard.ENTER);
        Button tab = keyboard1.get(Keyboard.TAB);
        Button A = keyboard1.get(Keyboard.A);
        Button W = keyboard1.get(Keyboard.W);

        switch (currentState) {

            case IDLE:

                break;

            case NORMAL:

                canMove();

                centerCamera();

                if (enter.isReleased()) {
                    for (Unit unit : this.getUnits()) {
                        if (unit.getCurrentCells().equals(this.getCurrentCells()) &&
                        !unit.theUnitHasBeenUsed()) {
                            currentState = ICWarsPlayerCurrentState.SELECT_CELL;
                        }

                    }
                }

                if(tab.isReleased()) {
                currentState = ICWarsPlayerCurrentState.IDLE;
                }


                break;

            case SELECT_CELL:

                canMove();

                if (!selectedUnit.theUnitHasBeenUsed()) {
                    if (selectedUnit != null) {
                        currentState = ICWarsPlayerCurrentState.MOVE_UNIT;
                    } else {
                        currentState = ICWarsPlayerCurrentState.NORMAL;
                    }
                }

                break;

            case MOVE_UNIT:

                canMoveOnlyInTheRange();

                if (enter.isReleased()) {
                    boolean deplacement = selectedUnit.changePosition(getCurrentMainCellCoordinates());
                    if (deplacement) {
                        selectedUnit.setIsUsed(false);
                        currentState = ICWarsPlayerCurrentState.ACTION_SELECTION;
                    }
                }
                if (tab.isReleased()) {
                    currentState = ICWarsPlayerCurrentState.NORMAL;
                }
                break;

            case ACTION_SELECTION:

                for (int i = 0; i < selectedUnit.getPossibleActions().size(); ++i) {
                    action= selectedUnit.getPossibleActions().get(i);
                    if (A.isReleased()) {
                        action = new Attack(this.getSelectedUnit(), this.area);
                        currentState = ICWarsPlayerCurrentState.ACTION;
                    } else if (W.isReleased()) {
                        action = new Wait(this.getSelectedUnit(), this.area);
                        currentState = ICWarsPlayerCurrentState.ACTION;
                    }
                }

                break;

            case ACTION:

                float dt = 0;
                action.doAction(dt, this, keyboard1);
                break;

        }
    }
    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b){
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     * Leave an area by unregistering this player
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /**
     *
     * @param area (Area): initial area, not null
     * @param position (DiscreteCoordinates): initial position, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        icWarsPlayerGUI.draw(canvas);

        if(currentState == ICWarsPlayerCurrentState.ACTION && action != null){
            action.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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

    @Override
    public void interactWith(Interactable other) {
        ICWarsRealPlayerInteractionHandler handler = new ICWarsRealPlayerInteractionHandler();
        if (!isDisplacementOccurs()) {
            other.acceptInteraction(handler);
        }
    }

    // Method that means that the RealPlayer can move where he wants inside the area

    public void canMove() {

        Keyboard keyboard2 = getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard2.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard2.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard2.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard2.get(Keyboard.DOWN));
    }

    // Method that means that the RealPlayer can only move inside his range
    public void canMoveOnlyInTheRange() {

        Keyboard keyboard2 = getOwnerArea().getKeyboard();

        if (selectedUnit.getFromX() - getCurrentMainCellCoordinates().x < getSelectedUnit().getRadius())
            moveIfPressed(Orientation.LEFT, keyboard2.get(Keyboard.LEFT));

        if (getCurrentMainCellCoordinates().y - selectedUnit.getFromY() < getSelectedUnit().getRadius())
            moveIfPressed(Orientation.UP, keyboard2.get(Keyboard.UP));

        if (getCurrentMainCellCoordinates().x - selectedUnit.getFromX() < getSelectedUnit().getRadius())
            moveIfPressed(Orientation.RIGHT, keyboard2.get(Keyboard.RIGHT));

        if (selectedUnit.getFromY() - getCurrentMainCellCoordinates().y < getSelectedUnit().getRadius())
            moveIfPressed(Orientation.DOWN, keyboard2.get(Keyboard.DOWN));
    }

    /**
     * @return the faction of the RealPlayer
     */
    public faction getCamp() {
        return camp;
    }


    // The handler class
    private class ICWarsRealPlayerInteractionHandler implements ICWarsInteractionVisitor {
        @Override
        public void interactWith(Unit unit) {
            icWarsPlayerGUI.setCellUnit(unit);
            if (getCamp().equals(unit.getCamp()) && currentState == ICWarsPlayerCurrentState.SELECT_CELL) {
                selectedUnit = unit;
                icWarsPlayerGUI.setUnit(unit);
                currentState = ICWarsPlayerCurrentState.MOVE_UNIT;
            }

        }

        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cell){
            cellType = cell.getType();
            icWarsPlayerGUI.setCellUnit(null);

        }
    }

    @Override
    public ICWarsPlayerCurrentState getCurrentState(){
        return currentState;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }

    public ICWarsCellType getCellType(){
        return cellType;
    }
}

