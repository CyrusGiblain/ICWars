package ch.epfl.cs107.play.game.icwars.actor.players.unit;

import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

import static ch.epfl.cs107.play.game.icwars.actor.ICWarsActor.faction.*;

public class Tanks extends Unit{

    private int inflictedDamage = 7;
    private int rayon = 4;
    private final int maxHp = 10;
    private Sprite tankAllie = new Sprite("icwars/friendlyTank", 1.5f, 1.5f,  this, null, new Vector(-0.25f, -0.25f));
    private Sprite tankEnnemi = new Sprite("icwars/enemyTank", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
    private Sprite tankEnnemi2 = new Sprite("icwars/enemy2Tank", 1f, 1f, this, null, new Vector(0f, 0f));
    private ICWarsArea area;
    private faction camp;
    Sprite tank;
    private List<Action> listOfActions;

    /**
     * Tanks constructor
     *
     * @param area        (Area): The area the tank will be displayed in
     * @param position    (DiscreteCoordinate): The initial position of the tank
     * @param faction     (faction): The faction of the tank
     */
    public Tanks(ICWarsArea area,  DiscreteCoordinates position, faction faction) {
        super(area, position, faction, 4);
        this.camp = faction;
        this.area = area;
        this.listOfActions = List.of(new Attack(this, this.area), new Wait(this, this.area));
    }

    @Override
    public String getName() {
        if (camp.equals(ALLIE)) {
            return "icwars/friendlyTank";
        } else if (camp.equals(ENNEMI1)){
            return "icwars/enemyTank";
        } else {
            return "icwars/enemy2Tank";
        }
    }


    @Override
    public void draw(Canvas canvas) {
        if (!theUnitHasBeenUsed()) {
            if (camp == ALLIE) {
                tankAllie.setAlpha(1.0f);
                tankAllie.draw(canvas);
            }
            if (camp == ENNEMI1) {
                tankEnnemi.setAlpha(1.0f);
                tankEnnemi.draw(canvas);
            }
            if (camp == ENNEMI2) {
                tankEnnemi2.setAlpha(1.0f);
                tankEnnemi2.draw(canvas);
            }
        } else {
            if (camp == ALLIE) tankAllie.draw(canvas);
            if (camp == ENNEMI1) tankEnnemi.draw(canvas);
            if (camp == ENNEMI2) tankEnnemi2.draw(canvas);
            if (unit !=null) {
                unit.setAlpha(0.5f);
            } else {
                unit.setAlpha(0.5f);
            }
        }
    }

    /**
     * @return the HP of the tank
     */
    public int getHp(){
        return super.getHp();
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
     * @return the Sprite of the ally tank
     */
    public Sprite getTankAllie() {
        return tankAllie;
    }

    /**
     * @return the Sprite of the enemy 1 tank
     */
    public Sprite getTankEnnemi1() {
        return tankEnnemi;
    }

    /**
     * @return the Sprite of the enemy 2 tank
     */
    public Sprite getTankEnnemi2() {
        return tankEnnemi2;
    }
}