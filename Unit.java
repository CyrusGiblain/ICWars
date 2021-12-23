package ch.epfl.cs107.play.game.icwars.actor.players.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
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
    private int hp;         //hp cannot be <0 or unit isDead()
    private int maxHp;
    protected Sprite unit;
    private ICWarsRange range;
    private int fromX;
    private int fromY;
    private int radius;
    private boolean unitIsUsed;
    private ICWarsBehavior.ICWarsCellType cellType = null;
    private ICWarsRange updatedRange = new ICWarsRange();
    private int cellStars;
    private ICWarsUnitInteractionHandler handler;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param faction
     */
    public Unit( Area area, DiscreteCoordinates position, ICWarsActor.faction faction, int radius) {
        super(area, position, faction);

        this.fromX = position.x;
        this.fromY = position.y;
        this.radius = radius;
        this.range = new ICWarsRange();

        handler = new ICWarsUnitInteractionHandler();

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
                if (y  < fromY + radius && y < getOwnerArea().getHeight()) {
                    hasUpEdge = true;
                }
                if (x < fromX + radius && x < getOwnerArea().getWidth()){
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

    public String getName(){return name;}

    public void setHp(Unit unit, int hp){
        if(hp > maxHp) hp = unit.maxHp;
        if(hp < 0) {
            hp = 0;
            this.isDead(this);
        }
        unit.hp = hp;
    }

    public int getHp(){
        int hpCopie;
        if (hp > maxHp) hp = maxHp;
        if(hp < 0){
            hp = 0;
            this.isDead(this);
        }
        hpCopie = hp;
        return hpCopie;
    }

    public boolean isDead(Unit unit){return this.getHp() < 0;}

    public int receiveDamage(int hp, int damage){return hp - damage;}

    public int healDamage(int hp, int healing){
        if(hp + healing > maxHp) return maxHp;
        return hp + healing;
    }

    public abstract int getDamage();

    public int damageTaken(Unit other) {
        ICWarsUnitInteractionHandler handler = new ICWarsUnitInteractionHandler();
        return hp - other.getDamage() + cellStars;
    }
    public int getCellStars(){return cellStars;}
    public void setCellStars(int cellStars) {
        this.cellStars = cellStars;
    }


    public boolean takeCellSpace(){return true;}

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
            unitIsUsed = true;
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

    public ICWarsActor.faction getCamp() {
        return camp;
    }

    public void drawRangeAndPathTo(DiscreteCoordinates destination, Canvas canvas) {

        range.draw(canvas); Queue<Orientation> path =
                range.shortestPath(getCurrentMainCellCoordinates(), destination);
        //Draw path only if it exists (destination inside the range)
        if (path != null){
            new Path(getCurrentMainCellCoordinates().toVector(),
                    path).draw(canvas);
        }
    }

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

    public void setIsUsed(boolean value) {
        this.unitIsUsed = value;
    }

    public int getRadius() {
        return radius;
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public abstract List<Action> getPossibleActions();

    //trying to get current cell stars

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

    public ICWarsRange getRange(){
        return updatedRange;
    }


    @Override
    public void interactWith(Interactable other){
        other.acceptInteraction(handler);
    }
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }

    private class ICWarsUnitInteractionHandler implements ICWarsInteractionVisitor {

        @Override
        public void interactWith(ICWarsBehavior.ICWarsCellType cellType){
            setCellStars(cellType.getDefenseStar());
        }
    }
}

