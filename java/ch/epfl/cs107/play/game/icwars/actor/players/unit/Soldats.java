
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
import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMI1;
import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMI2;
//import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.ENNEMIE3;

public class Soldats extends Unit {

    private int inflictedDamage = 2;
    private int rayon = 2;
    private int maxHp = 5;
    private Sprite soldatAllie = new Sprite("icwars/friendlySoldier", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    private Sprite soldatEnnemi = new Sprite("icwars/enemySoldier", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    private Sprite soldatEnnemi2 = new Sprite("icwars/enemy2Soldier", 1f, 1f, this, null, new Vector(0f, 0f));
    private ICWarsArea area;
    faction camp;
    private List<Action> listOfActions;

    /**
     * Soldats constructor
     *
     * @param area     (Area): The area the soldier will be displayed in
     * @param position (DiscreteCoordinates): Initial position of the soldier
     * @param faction  (faction): The faction of the soldier
     */

    public Soldats(ICWarsArea area, DiscreteCoordinates position, faction faction) {
        super(area, position, faction, 2);
        this.camp = faction;
        this.area = area;
        this.listOfActions = List.of(new Attack(this, this.area), new Wait(this, this.area));
    }

    @Override
    public int getHp(){
        return super.getHp();
    }

    @Override
    public String getName() {
        if (camp.equals(ALLIE)) {
            return "icwars/friendlySoldier";
        } else if (camp.equals(ENNEMI1)){
            return "icwars/enemySoldier";
        } else {
            return "icwars/enemy2Soldier";
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!theUnitHasBeenUsed()) {
            if (camp == ALLIE) {
                soldatAllie.setAlpha(1.0f);
                soldatAllie.draw(canvas);
            }
            if (camp == ENNEMI1) {
                soldatEnnemi.setAlpha(1.0f);
                soldatEnnemi.draw(canvas);
            }
            if (camp == ENNEMI2) {
                soldatEnnemi2.setAlpha(1.0f);
                soldatEnnemi2.draw(canvas);
            }
        } else {
            if (camp == ALLIE) soldatAllie.draw(canvas);
            if (camp == ENNEMI1) soldatEnnemi.draw(canvas);
            if (camp == ENNEMI2) soldatEnnemi2.draw(canvas);
            if (unit != null) {
                unit.setAlpha(0.5f);
            } else {
                if (unit != null) {
                    unit.setAlpha(0.5f);
                }
            }
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

    /**
     * @return the Sprite of the ally soldier
     */
    public Sprite getSoldatAllie() {
        return soldatAllie;
    }

    /**
     * @return the Sprite of the enemy 1 soldier
     */
    public Sprite getSoldatEnnemi1() {
        return soldatEnnemi;
    }

    /**
     * @return the Sprite of the enemy 2 soldier
     */
    public Sprite getSoldatEnnemi2() {
        return soldatEnnemi2;
    }
}