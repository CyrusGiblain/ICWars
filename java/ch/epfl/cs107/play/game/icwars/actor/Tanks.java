package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Tanks extends Unit{
    private int inflictedDamage = 7;
    private int rayon = 4;
    private int hp;
    private int maxHp = 10;
    private Sprite tankAllie = new Sprite("icwars/friendlyTank", 1.5f, 1.5f,  this, null, new Vector(-0.25f, -0.25f));
    private Sprite tankEnnemi = new Sprite("icwars/enemyTank", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    private Area area;
    private faction camp;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param faction
     */
    public Tanks(Area area,  DiscreteCoordinates position, ICWarsActor.faction faction) {
        super(area, position, faction, 4);
        int hp = maxHp;
    }

    @Override
    public void draw(Canvas canvas) {
        tankAllie.draw(canvas);
        tankEnnemi.draw(canvas);
    }


    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {}

    @Override
    public int getDamage() {
        return inflictedDamage;
    }

    @Override
    public int movement() {
        return this.rayon;
    }
}