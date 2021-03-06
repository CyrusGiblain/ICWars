
package ch.epfl.cs107.play.game.icwars.actor.players.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import java.util.ArrayList;
import java.util.List;

public class Attack extends Action {

    private final String name = "(A)ttack";
    private Unit attackedUnit;
    private List<Unit> enemyUnitsInRange = new ArrayList<>();
    private int attackedUnitIndex = 0;
    ImageGraphics cursor;
    private int defenseStars;

    /**
     * The Attack constructor
     *
     * @param unit (Unit): The unit used to attack
     * @param area (ICWarsArea): The area where the attack takes place
     */
    public Attack(Unit unit, ICWarsArea area) {
        super(unit, area);
        cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f, new RegionOfInterest(4 * 18, 26 * 18, 16, 16));
    }

    @Override
    public void draw(Canvas canvas) {
        if (!enemyUnitsInRange.isEmpty()) {
            cursor.setAnchor(canvas.getPosition().add(1, 0));
            cursor.draw(canvas);
            cursor.setDepth(2);
        }
    }

    //make a List of enemy units, look through those units to see if any of them are in range (if(nodeExist())
    //then create a list of all them
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
        if (enemyUnitsInRange.size() == 0) {
            goBack(player);
            System.out.println("Nothing is in the Range!");
        } else {
            if (right.isReleased() && attackedUnitIndex < enemyUnitsInRange.size() - 1) {
                ++attackedUnitIndex;
            } else if (right.isReleased() && attackedUnitIndex == enemyUnitsInRange.size() - 1) {
                attackedUnitIndex = 0;
            }
            if (left.isReleased() && attackedUnitIndex > 0) {
                --attackedUnitIndex;
            } else if (left.isReleased() && attackedUnitIndex == 0) {
                attackedUnitIndex = enemyUnitsInRange.size() - 1;
            }
            enemyUnitsInRange.get(attackedUnitIndex).centerCamera();

            if (enter.isReleased()) {

                attackedUnit = enemyUnitsInRange.get(attackedUnitIndex);

                int nouveauxHP = attackedUnit.getHp() - currentUnit.getDamage() + attackedUnit.getDefenseStars();
                attackedUnit.setHp(attackedUnit, nouveauxHP);

                if (attackedUnit.getHp() == 0) {
                    enemyUnitsInRange.remove(attackedUnitIndex);
                    attackedUnit.leaveArea();
                }

                player.selectedUnit.setIsUsed(true);
                player.setCurrentState(ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL);
            }
        }
    }

    @Override
    public void doAutoAction(float dt, ICWarsPlayer player, Unit attackedUnit, Unit attackingUnit) {

        int nouveauxHP = attackedUnit.getHp() - unit.getDamage() + attackedUnit.getDefenseStars();
        attackedUnit.setHp(attackedUnit, nouveauxHP);

        if (attackedUnit.isDead()) {
            attackedUnit.leaveArea();
        }
        unit.setIsUsed(true);
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param player (ICWarsPlayer): The player who is checking for enemy units in his range
     * @return the list of enemy units that are inside his range
     */
    public List<Unit> getEnemyUnitsInRange(ICWarsPlayer player) {
        List<Unit> listOfEnemyUnitsInRange = new ArrayList<>();
        Unit selectedUnit = player.getSelectedUnit();
        for (int i = 0; i < area.units.size(); ++i) {
            Unit unit = area.units.get(i);
            DiscreteCoordinates coords = new DiscreteCoordinates((int) unit.getPosition().x, (int) unit.getPosition().y);
            if (selectedUnit.getRange().nodeExists(coords) &&
                    selectedUnit.getCamp() != unit.getCamp()
            && !unit.isDead()) {
                listOfEnemyUnitsInRange.add(unit);
            }
        }
        return listOfEnemyUnitsInRange;
    }

    /**
     * Method to set his current state back to ACTION_SELECTION
     *
     * @param player involved in the attack
     */
    public void goBack(ICWarsPlayer player) {
        player.centerCamera();
        player.setCurrentState(ICWarsPlayer.ICWarsPlayerCurrentState.ACTION_SELECTION);
    }
}