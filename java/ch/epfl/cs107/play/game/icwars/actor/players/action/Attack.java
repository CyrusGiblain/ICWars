
package ch.epfl.cs107.play.game.icwars.actor.players.action;


import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsActionsPanel;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import javax.lang.model.type.UnionType;
import java.util.ArrayList;
import java.util.List;

import static ch.epfl.cs107.play.window.Keyboard.*;

//may be an interface
public class Attack extends Action{

    private String name = "(A)ttack";
    private Unit attackedUnit;
    private List<Unit> enemyUnitsInRange = new ArrayList<>();
    private int attackedUnitIndex = 0;
    ImageGraphics cursor;

    public Attack(Unit unit, ICWarsArea area){
        super(unit, area);
        cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f, new RegionOfInterest(4*18, 26*18,16,16));
    }

    @Override
    public void draw(Canvas canvas) {
        if(!enemyUnitsInRange.isEmpty()) {
            System.out.println("Drawing");
            cursor.setAnchor(canvas.getPosition().add(1, 0));
            cursor.draw(canvas);
        }
    }

    //make List of enemy units, look through those units to see if any of them are in range (if(nodeExist())
    //then create a list of all those ones
    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        Button enter = keyboard.get(Keyboard.ENTER);
        Button left = keyboard.get(Keyboard.LEFT);
        Button right = keyboard.get(Keyboard.RIGHT);
        Button tab = keyboard.get(Keyboard.TAB);

        Unit currentUnit = player.getSelectedUnit();
        enemyUnitsInRange = getEnemyUnitsInRange(player);

        if (tab.isReleased()) {
            goBack(player);
        }
        if(enemyUnitsInRange.size() == 0) {
            goBack(player);
            System.out.println("Nothing is in the Range!");
        }
        else{
            if (right.isReleased() && attackedUnitIndex < enemyUnitsInRange.size() - 1) {
                ++attackedUnitIndex;
            } else if(right.isReleased() && attackedUnitIndex == enemyUnitsInRange.size()-1) {
                attackedUnitIndex = 0;
            }
            if (left.isReleased() && attackedUnitIndex > 0) {
                --attackedUnitIndex;
            } else if(left.isReleased() && attackedUnitIndex == 0){
                attackedUnitIndex = enemyUnitsInRange.size()-1;
            }
            enemyUnitsInRange.get(attackedUnitIndex).centerCamera();
            //System.out.println(enemyUnitsInRange.get(attackedUnitIndex));
            if (enter.isReleased()) {
                //attack
                attackedUnit = enemyUnitsInRange.get(attackedUnitIndex);
                impactOnHP(currentUnit, attackedUnit);
                if (attackedUnit.isDead(attackedUnit)) {
                    currentUnit.changePosition(new DiscreteCoordinates((int) player.getPosition().x, (int) player.getPosition().y));
                }
                player.selectedUnit.setIsUsed(true);
                player.setCurrentState(ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL);
            }
        }
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player, Unit attackedUnit) {
        Unit currentUnit = player.getSelectedUnit();
        DiscreteCoordinates coords = new DiscreteCoordinates((int)attackedUnit.getPosition().x, (int)attackedUnit.getPosition().y);
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

    public void impactOnHP(Unit attackingUnit, Unit attackedUnit){
        // have to work on the interactor
        //int defense_stars = attackedUnit.getCurrentCellTypeStars(attackedUnit);
        int damage = attackedUnit.getHp() - attackingUnit.getDamage() + attackingUnit.getCellStars();
        attackedUnit.setHp(attackedUnit, damage);
    }

    public String getName(){
        return this.name;
    }

    public List<Unit> getEnemyUnitsInRange(ICWarsPlayer player){
        List<Unit> listOfEnemyUnitsInRange = new ArrayList<>();
        Unit selectedUnit = player.getSelectedUnit();
         for(int i = 0; i < area.units.size(); ++i) {
             Unit unit = area.units.get(i);
             DiscreteCoordinates coords = new DiscreteCoordinates((int)unit.getPosition().x, (int)unit.getPosition().y);
              if(selectedUnit.getRange().nodeExists(coords) &&
                      selectedUnit.getCamp() != unit.getCamp()){
                  listOfEnemyUnitsInRange.add(unit);
              }
         }
         return listOfEnemyUnitsInRange;
    }

    public void goBack(ICWarsPlayer player) {
        player.centerCamera();
        player.setCurrentState(ICWarsPlayer.ICWarsPlayerCurrentState.ACTION_SELECTION);
    }
}

