package ch.epfl.cs107.play.game.icwars.actor.players;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RealPlayer extends ICWarsPlayer {
    private float hp;
    private Sprite sprite;
    private String spriteName;
    protected List<Unit> memory = Collections.emptyList();
    private ICWarsPlayerGUI icWarsPlayerGUI;
    private boolean theUnitIsUsed;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 8;
    /**
     * Demo actor
     *
     */
    public RealPlayer(Area owner, DiscreteCoordinates position, faction camp, Unit... units) {
        super(owner, position, camp, units);
        if (camp == faction.ALLIE) {
            spriteName = "icwars/allyCursor";
        } else {
            spriteName = "icwars/enemyCursor";
        }
        sprite = new Sprite(spriteName, 1.f, 1.f,this);
        this.icWarsPlayerGUI = new ICWarsPlayerGUI(1.0f, this); // @toDO : change the scale factor
        resetMotion();
    }

    /**
     * Center the camera on the player
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

        switch (currentState) {

            case IDLE:
                currentState = ICWarsPlayerCurrentState.NORMAL;
                break;

            case NORMAL:

                canMove();

                centerCamera();

                if (enter.isDown()) {
                    currentState = ICWarsPlayerCurrentState.SELECT_CELL;
                }
                if (tab.isDown()) {
                    currentState = ICWarsPlayerCurrentState.IDLE;
                }
                break;

            case SELECT_CELL:

                canMove();

                if (selectedUnit != null) {
                    memory.add(selectedUnit);
                    currentState = ICWarsPlayerCurrentState.MOVE_UNIT;
                }else{
                    currentState = ICWarsPlayerCurrentState.NORMAL;
                }
                break;

            case MOVE_UNIT:

                canMove();

                if (enter.isDown()) {
                    currentState = ICWarsPlayerCurrentState.NORMAL;
                }
                break;

            case ACTION_SELECTION:
                // Rien pour l'instant
                break;

            case ACTION:
                // Rien pour l'instant
                break;

        }
    }
    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, ch.epfl.cs107.play.window.Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     * Leave an area by unregister this player
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
        sprite.draw(canvas);
        if (currentState == ICWarsPlayerCurrentState.MOVE_UNIT) {
            icWarsPlayerGUI.draw(canvas);
        }
    }

    public boolean isWeak() {
        return (hp <= 0.f);
    }

    public void strengthen() {
        hp = 10;
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
        ICWarsPlayerInteractionHandler handler = new ICWarsPlayerInteractionHandler();
        other.acceptInteraction(handler);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor)v).interactWith(this); }

    public void canMove() {

        Keyboard keyboard2 = getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard2.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard2.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard2.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard2.get(Keyboard.DOWN));
    }
    //De façon analogue, le dessin des possibilités offertes pour déplacer l’unité sélectionnée
    // (appel de drawRangeAndPathTo) ne doit se faire que dans l’état MOVE_UNIT.

    public faction getCamp() {
        return camp;
    }

    private class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {

        @Override
        public void interactWith(Unit unit) {
            System.out.println("interactionWithUnit");
            if (getCamp().equals(unit.getCamp())) {
                selectedUnit = unit;
                // Rajouter le graphisme.
            }
        }
    }

    public void selectUnit(int index) {
        if (index < units.size()) {
            this.selectedUnit = units.get(index);
        }
    }
}

