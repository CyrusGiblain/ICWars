package ch.epfl.cs107.play.game.icwars.actor.players.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

public abstract class Unit extends ICWarsActor implements Interactable, Interactor {

    private String name;
    private int hp;
    private int maxHp;
    protected Sprite unit;
    private ICWarsRange range;
    private int fromX;
    private int fromY;
    private int radius;
    private boolean unitIsUsed;
    private ICWarsBehavior.ICWarsCellType cellType;
    private ICWarsRange updatedRange = new ICWarsRange();
    private int cellStars;
    private int defenseStars;
    private UnitInteractionHandler handler;

    /**
     * Unit constructor
     *
     * @param area     (Area): The area on which is displayed the unit
     * @param position (Coordinate): Initial position of the unit
     * @param faction  (faction): The faction of the unit
     * @param radius   (int): The radius of the unit
     */
    public Unit(Area area, DiscreteCoordinates position, ICWarsActor.faction faction, int radius) {
        super(area, position, faction);

        this.fromX = position.x;
        this.fromY = position.y;
        this.radius = radius;
        this.range = new ICWarsRange();

        if (this instanceof Tanks) {
            this.hp = 10;
            this.maxHp = 10;
        }
        if (this instanceof Soldats) {
            this.hp = 5;
            this.maxHp = 5;
        }

        //handler = new ICWarsUnitInteractionHandler();

        for (int x = Math.max(0, fromX - radius); x <= Math.min(getOwnerArea().getWidth() - 1, fromX + radius); ++x) {
            for (int y = Math.max(0, fromY - radius); y <= Math.min(getOwnerArea().getHeight() - 1, fromY + radius); ++y) {
                DiscreteCoordinates coordinates = new DiscreteCoordinates(x, y);

                boolean hasLeftEdge = false;
                boolean hasUpEdge = false;
                boolean hasRightEdge = false;
                boolean hasDownEdge = false;

                if (x > fromX - radius && x > 0) {
                    hasLeftEdge = true;
                }
                if (y < fromY + radius && y < getOwnerArea().getHeight()) {
                    hasUpEdge = true;
                }
                if (x < fromX + radius && x < getOwnerArea().getWidth()) {
                    hasRightEdge = true;
                }
                if (y > fromY - radius && y > 0) {
                    hasDownEdge = true;
                }
                range.addNode(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
        this.unitIsUsed = false;
    }

    /**
     * @return the name of the unit
     */
    public String getName() {
        return name;
    }

    /**
     * Method to change the Hp of the unit
     *
     * @param unit (Unit): The unit we want the Hp to be changed
     * @param hp   (int): The new Hp
     */
    public void setHp(Unit unit, int hp) {

        if (hp > unit.maxHp) {
            unit.hp = unit.maxHp;
        } else {
            unit.hp = Math.max(hp, 0);
        }
    }

    // Method to get the Hp of a unit
    public int getHp(){
        return this.hp;
    }

    /**
     * @return true if the unit is dead
     */
    public boolean isDead(){return this.getHp() == 0;}

    public int receiveDamage(int hp, int damage){return hp - damage;}

    public int healDamage(int hp, int healing){
        return Math.min(hp + healing, maxHp);
    }

    /**
     * @return the damage number
     */
    public abstract int getDamage();

    /**
     * @return the number of stars of the cell
     */
    public int getCellStars(){return cellStars;}

    /**
     * Method to set the cell stars
     *
     * @param cellStars (int): The stars we want to assign to the cell
     */
    public void setCellStars(int cellStars) {
        this.cellStars = cellStars;
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * @return true if the unit has been used
     */
    public boolean theUnitHasBeenUsed() {
        return unitIsUsed;
    }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        boolean changePosition = true;
        DiscreteCoordinates leftPosition = new DiscreteCoordinates(fromX, fromY);
        if (!range.nodeExists(newPosition) || !super.changePosition(newPosition) || newPosition.equals(leftPosition)) {
            changePosition = false;
        }
        else {
            updateRange(radius, newPosition);
        }
        if (changePosition) {
            unitIsUsed = false;
            if (this instanceof Tanks) {
                if (this.camp == faction.ALLIE) {
                    unit = ((Tanks) this).getTankAllie();
                } else {
                    if (this.camp == faction.ENNEMIE) {
                        unit = ((Tanks) this).getTankEnnemi();
                    }
                }
            } else {
                if (this instanceof Soldats) {
                    if (this.camp == faction.ALLIE) {
                        unit = ((Soldats) this).getSoldatAllie();
                    } else {
                        if (this.camp == faction.ENNEMIE) {
                            unit = ((Soldats) this).getSoldatEnnemi();
                        }
                    }
                }
            }
        }
        return changePosition;
    }

    /**
     * @return the faction of the unit
     */
    public faction getCamp() {
        return camp;
    }

    /**
     * Method to draw the range of a unit and his path to a destination inside its range
     *
     * @param destination (DiscreteCoordinates): The destination of the unit inside the range
     * @param canvas      (Canvas): The canvas used to draw
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination, Canvas canvas) {

        range.draw(canvas);
        Queue<Orientation> path =
                range.shortestPath(getCurrentMainCellCoordinates(), destination);
        //Draw path only if it exists (destination inside the range)
        if (path != null){
            new Path(getCurrentMainCellCoordinates().toVector(),
                    path).draw(canvas);
        }
    }

    /**
     * Method used to update the range after a unit has been moved
     *
     * @param radius (int): The radius of the unit
     * @param newPosition (DiscreteCoordinates): The new position of the unit after he has been moved
     */
    public void updateRange(int radius, DiscreteCoordinates newPosition) {


        this.fromX = newPosition.x;
        this.fromY = newPosition.y;

        for (int x = Math.max(0, fromX - radius); x <= Math.min(getOwnerArea().getWidth(), fromX + radius); ++x) {
            for (int y = Math.max(0, fromY - radius); y <= Math.min(getOwnerArea().getHeight(), fromY + radius); ++y) {

                DiscreteCoordinates coordinates = new DiscreteCoordinates(x, y);

                boolean hasLeftEdge = false;
                boolean hasUpEdge = false;
                boolean hasRightEdge = false;
                boolean hasDownEdge = false;

                if (x > fromX - radius && x > 0) {
                    hasLeftEdge = true;
                }
                if (y < fromY + radius && y < getOwnerArea().getHeight()) {
                    hasUpEdge = true;
                }
                if (x < fromX + radius && x < getOwnerArea().getWidth()) {
                    hasRightEdge = true;
                }
                if (y > fromY - radius && y > 0) {
                    hasDownEdge = true;
                }
                updatedRange.addNode(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
        this.range = updatedRange;
    }

    /**
     *
     * @param value (boolean): value is true if we want to mark a unit as used
     */
    public void setIsUsed(boolean value) {
        this.unitIsUsed = value;
    }

    /**
     * @return the radius of the unit
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return the x coordinate of the unit
     */
    public int getFromX() {
        return fromX;
    }

    /**
     * @return the y coordinate of the unit
     */
    public int getFromY() {
        return fromY;
    }

    /**
     * @return the list of the possible actions a unit can do
     */
    public abstract List<Action> getPossibleActions();


     // Method used to center the camera on the unit
    public void centerCamera(){
        getOwnerArea().setViewCandidate(this);
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

    /**
     * @return the range of the unit
     */
    public ICWarsRange getRange(){
        return updatedRange;
    }

    @Override
    public void interactWith(Interactable other) {
        UnitInteractionHandler handler = new UnitInteractionHandler();
        other.acceptInteraction(handler);
    }

    public int getDefenseStars() {
        return this.defenseStars;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }

    private class UnitInteractionHandler implements ICWarsInteractionVisitor {
        /**
         * Do this Interactor interact with the given Interactable
         * The interaction is implemented on the interactor side !
         *
         * @param cell
         */
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cell) {
            defenseStars = cell.getType().getDefenseStar();
        }

        @Override
        public void interactWith(ICWarsPlayer player) {

        }
    }
}

// Déplacer l'unit vers l'unit éliminée.
// Gérer le "fantôme" de l'unit éliminée.
// Gérer la fin de partie / Changement de niveau.

