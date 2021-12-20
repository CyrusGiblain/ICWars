package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ALLIE;
import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMIE;
//import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMIE2;
//import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMIE3;
public class Tanks extends Unit{
    private int inflictedDamage = 7;
    private int rayon = 4;
    private int hp;
    private int maxHp = 10;
    private Sprite tankAllie = new Sprite("icwars/friendlyTank", 1.5f, 1.5f,  this, null, new Vector(-0.25f, -0.25f));
    private Sprite tankEnnemi = new Sprite("icwars/enemyTank", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    private Area area;
    private faction camp;
    Sprite tank;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param faction
     */
    public Tanks(Area area,  DiscreteCoordinates position, ICWarsActor.faction faction) {
        super(area, position, faction, 4);
        /*
        if (camp == ALLIE && selectedUnit().hasMoved()) {
            tank = tankAllie;
        } else {
            if (camp == ENNEMIE)
        }
        */
        int hp = maxHp;
        this.camp = faction;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!theUnitHasBeenUsed()) {
            if (camp == ALLIE) tankAllie.draw(canvas);
            if (camp == ENNEMIE) tankEnnemi.draw(canvas);
        } else {
            if (camp == ALLIE) tankAllie.draw(canvas);
            if (camp == ENNEMIE) tankEnnemi.draw(canvas);
            unit.setAlpha(0.5f);
        }
    }

    @Override
    public int getDamage() {
        return inflictedDamage;
    }

    @Override
    public int movement() {
        return this.rayon;
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
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }

    public Sprite getTankAllie() {
        return tankAllie;
    }

    public Sprite getTankEnnemi() {
        return tankEnnemi;
    }
}