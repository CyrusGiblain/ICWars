
package ch.epfl.cs107.play.game.icwars.actor.unit;


import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.cs107.play.window.Keyboard.*;

//may be an interface
public class Attack extends Action{

    private String name = "(A)ttack";
    int actionKey = 65;
    Keyboard keyboard = area.getKeyboard();
    Button enter = keyboard.get(ENTER);
    Button left = keyboard.get(LEFT);
    Button right = keyboard.get(RIGHT);
    Button tab = keyboard.get(TAB);
    List<Unit> enemyUnitsInRange = new ArrayList<>();
    Unit attackedUnit;

    public Attack(Unit unit, ICWarsArea area){
        super(unit, area);
    }


    public void draw(Canvas canvas) {
        ImageGraphics cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f, new RegionOfInterest(4*18, 26*18,16,16));
        attackedUnit.centerCamera();// now center the camera on the unit that is attacking
        cursor.setAnchor(canvas.getPosition().add(1,0));
        cursor.draw(canvas);
    }

    //make List of enemy units, look through those units to see if any of them are in range (if(nodeExist())
    //then create a list of all those ones
    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        enemyUnitsInRange = getUnitsinRange(player, player.getSelectedUnit());
        int attackedUnitIndex = 0;
        Unit currentUnit = player.getSelectedUnit();
        if(enemyUnitsInRange != null) {
            DiscreteCoordinates coords = new DiscreteCoordinates((int) enemyUnitsInRange.get(attackedUnitIndex).getPosition().x,
                    (int) enemyUnitsInRange.get(attackedUnitIndex).getPosition().y);
            if (right.isReleased()) {
                ++attackedUnitIndex;
                if (attackedUnitIndex >= enemyUnitsInRange.size()) attackedUnitIndex = 0;
                enemyUnitsInRange.get(attackedUnitIndex).centerCamera();
                player.setCurrentPosition(coords);
            }
            if (left.isReleased()) {
                --attackedUnitIndex;
                if (attackedUnitIndex < 0) attackedUnitIndex = enemyUnitsInRange.size() - 1;
                enemyUnitsInRange.get(attackedUnitIndex).centerCamera();
                player.setCurrentPosition(coords);
            }
            if (enter.isReleased()) {
                //normal that attackedUnit isn't initialzied, need to get it from the ArrayList
                attackedUnit = enemyUnitsInRange.get(attackedUnitIndex);
                impactOnHP(currentUnit, attackedUnit);
                if (attackedUnit.isDead(attackedUnit)) {
                    currentUnit.changePosition(coords);
                }
                attackedUnit.theUnitHasBeenUsed();
                ICWarsPlayer.ICWarsPlayerCurrentState currentState;
                currentState = ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL;
                attackedUnit.centerCamera();
            }
            //Dans la 4.3 on nous demande de modifier cette méthode, ainsi :
            if (enemyUnitsInRange.isEmpty() || tab.isReleased()) {
                // player repasse en état SELECT_ACTION?? Il y a surement une erreur.
                player.centerCamera();
            }
        }
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player, Unit attackedUnit) {
        Unit currentUnit = player.getSelectedUnit();
        DiscreteCoordinates coords = new DiscreteCoordinates((int)attackedUnit.getPosition().x, (int)attackedUnit.getPosition().y);
        if(attackedUnit != null){
            //normal that attackedUnit isn't initialzied, need to get it from the ArrayList
            impactOnHP(currentUnit, attackedUnit);
            if(attackedUnit.isDead(attackedUnit)){
                currentUnit.changePosition(coords);
            }
            attackedUnit.setIsUsed(true);
            ICWarsPlayer.ICWarsPlayerCurrentState currentState;
            currentState = ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL;
            attackedUnit.centerCamera();
        }
    }

    public void impactOnHP(Unit attackingUnit, Unit attackedUnit){
        // have to work on the interactor
        //int defense_stars = attackedUnit.getCurrentCellTypeStars(attackedUnit);
        int damage = attackedUnit.getHp() - attackingUnit.getDamage();
        attackedUnit.setHp(attackedUnit, damage);
    }

    public String getName(){
        return this.name;
    }

    public List<Unit> getUnitsinRange(ICWarsPlayer player, Unit selectedUnit){
        List<Unit> listOfUnitsInRange = new ArrayList<>();

        for(int i = selectedUnit.getFromX() - player.getSelectedUnit().getRadius();
            i  < selectedUnit.getFromX() + selectedUnit.getRadius() ; ++i) {
            for (int j = selectedUnit.getFromY() - player.getSelectedUnit().getRadius();
                 j < selectedUnit.getFromY() + player.getSelectedUnit().getRadius(); ++j) {
                for(int c = 0; c < area.units.size(); ++c) {
                    Unit unit = area.units.get(c);
                    if (!unit.getCamp().equals(selectedUnit.getCamp())){
                        if (unit.getPosition().equals(new Vector(i,j))) {
                            listOfUnitsInRange.add(unit);
                        }
                    }
                }
            }
        }
        return listOfUnitsInRange;
    }
}

