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

public class Soldats extends Unit{
    private int inflictedDamage = 2;
    private int rayon = 2;
    private int hp;
    private int maxHp = 5;
    private Sprite soldatAllie = new Sprite("icwars/friendlySoldier", 1.5f, 1.5f,  this, null, new Vector(-0.25f, -0.25f));
    private Sprite soldatEnnemi = new Sprite("icwars/enemySoldier", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    faction camp;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param faction
     */
    public Soldats(Area area, DiscreteCoordinates position, ICWarsActor.faction faction) {
        super(area, position, faction, 2);
        int hp = maxHp;
        this.camp = faction;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!theUnitHasBeenUsed()) {
            if (camp == ALLIE) soldatAllie.draw(canvas);
            if (camp == ENNEMIE) soldatEnnemi.draw(canvas);
        } else {
            if (camp == ALLIE) soldatAllie.draw(canvas);
            if (camp == ENNEMIE) soldatEnnemi.draw(canvas);
            unit.setAlpha(0.5f);
        }
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
    public void acceptInteraction(AreaInteractionVisitor v){
        ((ICWarsInteractionVisitor)v).interactWith(this);
    }

    @Override
    public int getDamage() {
        return inflictedDamage;
    }

    @Override
    public int movement() {
        return this.rayon;
    }

    public Sprite getSoldatAllie() {
        return soldatAllie;
    }

    public Sprite getSoldatEnnemi() {
        return soldatEnnemi;
    }
}
