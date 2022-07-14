package ch.epfl.cs107.play.game.icwars.actor.players;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Wait;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.*;

import static ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer.ICWarsPlayerCurrentState.*;

public class AIPlayer extends ICWarsPlayer implements Interactor {

    private ICWarsArea area;
    private Action action;
    ICWarsPlayerGUI icWarsPlayerGUI;
    DiscreteCoordinates coordUniteIA = new DiscreteCoordinates(10, 10);

    public AIPlayer(ICWarsArea area, DiscreteCoordinates position, faction camp, Unit... units) {

        super(area, position, camp, units);
        this.area = area;
        this.icWarsPlayerGUI = new ICWarsPlayerGUI(10.0f, this);
        resetMotion();

    }

    @Override
    public void update(float deltatime) {
        Action act;
        super.update(deltatime);

        Keyboard keyboard = getOwnerArea().getKeyboard();

        Button tab = keyboard.get(Keyboard.TAB);
        Button enter = keyboard.get(Keyboard.ENTER);

        switch (currentState) {

            case IDLE:

                break;

            case NORMAL:

                centerCamera();

                //Wait à ajouter.
                currentState = MOVE_UNIT;

                break;


            case MOVE_UNIT:

                for (Unit unit : this.getUnits()) {
                    action = new Attack(unit, area);
                    movementAction(unit, action);
                }
                currentState = IDLE;
                break;
        }
    }

    public void movementAction(Unit unit, Action action) {

        float x = unit.getPosition().x;
        float y = unit.getPosition().y;

        List<Unit> enemyUnits = new ArrayList<>();

        for (Unit unit1 : area.units) {
            System.out.println("Unit is dead ? : " + !unit1.isDead());
            System.out.println("camp ? :" + (unit1.getCamp() != unit.getCamp()));
            if ((unit1.getCamp() != unit.getCamp()) && !unit1.isDead()) {
                enemyUnits.add(unit1);
            }
        }

        Unit unitLaPlusProche = unitLaPlusProche(enemyUnits, unit);

        if (unitLaPlusProche != null) {
            int d = (int) x;
            int e = (int) y;

            // condition sur uniteLaPlus proche qui doit être differente de null
            if (unitLaPlusProche.getPosition().x - unit.getPosition().x > unit.getRadius()) {
                x = x + unit.getRadius();
            } else if (unitLaPlusProche.getPosition().x - unit.getPosition().x < -unit.getRadius()) {
                x = x - unit.getRadius();
            } else if (unitLaPlusProche.getPosition().x - unit.getPosition().x < unit.getRadius() &&
                    unitLaPlusProche.getPosition().x - unit.getPosition().x > 0) {
                x = x + unitLaPlusProche.getPosition().x - unit.getPosition().x - 1;
            } else if (unitLaPlusProche.getPosition().x - unit.getPosition().x == 0 &&
                    unitLaPlusProche.getPosition().y - unit.getPosition().y != 0) {
            } else if (unitLaPlusProche.getPosition().x - unit.getPosition().x == unit.getRadius()) {
                x = x + unit.getRadius() - 1;
            } else if (unitLaPlusProche.getPosition().x - unit.getPosition().x == -unit.getRadius()) {
                x = x - unit.getRadius() + 1;
            } else {
                x = x - Math.abs(unitLaPlusProche.getPosition().x - unit.getPosition().x) + 1;
            }

            if (unitLaPlusProche.getPosition().y - unit.getPosition().y > unit.getRadius()) {
                y = y + unit.getRadius();
            } else if (unitLaPlusProche.getPosition().y - unit.getPosition().y < -unit.getRadius()) {
                y = y - unit.getRadius();
            } else if (unitLaPlusProche.getPosition().y - unit.getPosition().y < unit.getRadius() &&
                    unitLaPlusProche.getPosition().y - unit.getPosition().y > 0) {
                y = y + unitLaPlusProche.getPosition().y - unit.getPosition().y - 1;
            } else if (unitLaPlusProche.getPosition().y - unit.getPosition().y == 0 &&
                    unitLaPlusProche.getPosition().x - unit.getPosition().x != 0) {
            } else if (unitLaPlusProche.getPosition().y - unit.getPosition().y == unit.getRadius()) {
                y = y + unit.getRadius() - 1;
            } else if (unitLaPlusProche.getPosition().y - unit.getPosition().y == -unit.getRadius()) {
                y = y - unit.getRadius() + 1;
            } else {
                y = y - Math.abs(unitLaPlusProche.getPosition().y - unit.getPosition().y) + 1;
            }

            int deltaX = (int) (x - d);
            int deltaY = (int) (y - e);

            if (x != coordUniteIA.x || y != coordUniteIA.y) {
                unit.changePosition(new DiscreteCoordinates((int) x, (int) y));
            } else {
                if (deltaX > 0) y = y + 1;
                if (deltaY > 0) x = x + 1;
                if (deltaX < 0) y = y - 1;
                if (deltaY < 0) x = x - 1;
                unit.changePosition(new DiscreteCoordinates((int) x, (int) y));
            }
            int newX = (int) x;
            int newY = (int) y;
            coordUniteIA = new DiscreteCoordinates(newX, newY);

            List<Unit> unitsInRange = getUnitsInRange(unit);

            if (unitsInRange.size() != 0) {

                Unit unitPlusPetite = findSmallestHp(unitsInRange);

                int nouveauxHP = unitPlusPetite.getHp() - unit.getDamage() + unitPlusPetite.getDefenseStars();
                unitPlusPetite.setHp(unitPlusPetite, nouveauxHP);

                if (unitPlusPetite.isDead()) {
                    unitPlusPetite.leaveArea();
                }
                unit.setIsUsed(true);
                //waitFor(5, 5);
            }
        }
    }

    public Unit findSmallestHp(List<Unit> units) {

        Unit unit = units.get(0);

        for (int i = 1; i < units.size(); ++i) {
            if (units.get(i).getHp() < unit.getHp())
                unit = units.get(i);
        }

        return unit;
    }

    /**
     * Ensures that value time elapsed before returning true
     *
     * @param dt    elapsed time
     * @param value waiting time (in seconds)
     * @return true if value seconds has elapsed , false otherwise
     */
    private boolean waitFor(float value, float dt) {
        int counter = 0;
        boolean counting = false;
        if (counting) {
            counter += dt;
            if (counter > value) {
                counting = false;
                return true;
            }
        } else {
            counter = (int) 0f;
            counting = true;
        }
        return false;
    }


    private Unit unitLaPlusProche(List<Unit> enemyUnits, Unit unit) {

        Unit unitLaPlusProche = null;

        float distanceMinimale = 20;

        if (enemyUnits.size() != 0) {

            for (Unit unit2 : enemyUnits) {
                if (Math.abs(unit2.getPosition().x - unit.getPosition().x) +
                        Math.abs(unit2.getPosition().y - unit.getPosition().y) < distanceMinimale) {
                    distanceMinimale = Math.abs(unit2.getPosition().x - unit.getPosition().x) +
                            Math.abs(unit2.getPosition().y - unit.getPosition().y);
                    unitLaPlusProche = unit2;
                }
            }
        }
        return unitLaPlusProche;
    }

    public List<Unit> getUnitsInRange(Unit selectedUnit) {
        List<Unit> listOfEnemyUnitsInRange = new ArrayList<>();
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
}
// Wait for à faire.
// Action à effectuer.