package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Queue;

public abstract class Unit extends ICWarsActor{
    private String name;
    private int hp;         //hp cannot be <0 or unit isDead()
    private int maxHp;
    private Sprite unit;
    ICWarsRange range;
    int fromX;
    int fromY;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param faction
     */
    public Unit(Area area, DiscreteCoordinates position, ICWarsActor.faction faction, int radius) {

        super(area, position, faction);

        this.fromX = position.x;
        this.fromY = position.y;
        this.range = new ICWarsRange();

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
                range.addNode(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
    }

    public String getName(){return name;}

    public int getHp(Unit unit){
        int hpCopie;
        if (hp > maxHp) hp = maxHp;
        if(hp < 0) hp = 0;
        hpCopie = hp;
        return hpCopie;
    }

    public boolean isDead(Unit unit){return getHp(unit) < 0;}

    public int receiveDamage(int hp, int damage){return hp - damage;}

    public int healDamage(int hp, int healing){
        if(hp + healing > maxHp) return maxHp;
        return hp + healing;
    }

    public abstract int getDamage();

    public int inflictDamage(int damage){
        return hp-getDamage();
    }

    public abstract int movement();

    public boolean takeCellSpace(){return true;}

    public boolean theUnitHasBeenUsed() {
        return true;
    }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {
        boolean changePosition = true;
        if (!super.changePosition(newPosition) || !range.nodeExists(newPosition))
        changePosition = false;
        else {
            fromX = newPosition.x;
            fromY = newPosition.y;
        }
        return changePosition;
    }

    public faction getCamp() {
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

}
