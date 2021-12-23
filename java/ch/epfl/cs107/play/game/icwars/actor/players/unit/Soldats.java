
package ch.epfl.cs107.play.game.icwars.actor.players.unit;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ALLIE;
import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMIE;
//import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMIE2;
//import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMIE3;

public class Soldats extends Unit {
    private int inflictedDamage = 2;
    private int rayon = 2;
    private int hp = getHp();
    private int maxHp = 5;
    private Sprite soldatAllie = new Sprite("icwars/friendlySoldier", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    private Sprite soldatEnnemi = new Sprite("icwars/enemySoldier", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    private ICWarsArea area;
    faction camp;
    private List<Action> listOfActions;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area     (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param faction
     */

    public Soldats(ICWarsArea area, DiscreteCoordinates position, ICWarsActor.faction faction) {
        super(area, position, faction, 2);
        this.setHp(this, maxHp);
        this.camp = faction;
        this.area = area;
        this.listOfActions = List.of(new Attack(this, this.area), new Wait(this, this.area));
    }

    @Override
    public int getHp(){
        return hp;
    }

    @Override
    public String getName() {
        if (camp.equals(ALLIE)) {
            return "icwars/friendlySoldier";
        } else {
            return "icwars/enemySoldier";
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!theUnitHasBeenUsed()) {
            if (camp == ALLIE) {
                soldatAllie.setAlpha(1.0f);
                soldatAllie.draw(canvas);
            }
            if (camp == ENNEMIE) {
                soldatEnnemi.setAlpha(1.0f);
                soldatEnnemi.draw(canvas);
            }
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
    public int getDamage() {
        return inflictedDamage;
    }

    @Override
    public List<Action> getPossibleActions() {
        return listOfActions;
    }

    public Sprite getSoldatAllie() {
        return soldatAllie;
    }

    public Sprite getSoldatEnnemi() {
        return soldatEnnemi;
    }

}
