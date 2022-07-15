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
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.*;

import static ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer.ICWarsPlayerCurrentState.*;

public class AIPlayer extends ICWarsPlayer implements Interactor {

    private ICWarsArea area;
    private Action action;
    ICWarsPlayerGUI icWarsPlayerGUI;
    DiscreteCoordinates coordUniteIA = new DiscreteCoordinates(10, 10);

    private int counter = 0;
    private boolean counting = false;

    Unit hisSelectedUnit;

    List<Unit> autreUnits;

    int nombre = 0;

    boolean plusDennemis = false;

    public AIPlayer(ICWarsArea area, DiscreteCoordinates position, faction camp, Unit... units) {

        super(area, position, camp, units);
        this.area = area;
        this.icWarsPlayerGUI = new ICWarsPlayerGUI(10.0f, this);
        autreUnits = new ArrayList<>();
        autreUnits.addAll(this.getUnits());
        resetMotion();


    }

    @Override
    public void update(float deltatime) {

        super.update(deltatime);

        switch (currentState) {

            case IDLE:

                if (autreUnits.size() != 0) {
                    autreUnits.clear();
                }

                break;

            case NORMAL:

                centerCamera();

                currentState = SELECT_CELL;

                autreUnits.addAll(this.getUnits());

                break;

            case SELECT_CELL:

                if (waitFor(50, 1)) {

                    if (autreUnits.size() != 0) {
                        Unit unitSelec = autreUnits.get(0);
                        if (!unitSelec.isDead()) {
                            this.hisSelectedUnit = unitSelec;
                            autreUnits.remove(0);
                            if (autreUnits.size() == 0) {
                                System.out.println("LÀ");
                            }
                            currentState = MOVE_UNIT;
                            nombre++;
                            System.out.println(nombre);
                        }

                    } else {
                        currentState = IDLE;
                        autreUnits.addAll(this.getUnits());
                    }

                    if (plusDennemis) {
                        currentState = IDLE;
                    }
                    counting = false;
                    counter = 0;
                }

                break;

            case MOVE_UNIT:

                movement(this.hisSelectedUnit);

                currentState = ACTION;
                break;

            case ACTION:

                if (waitFor(50, 1)) {

                        action = new Attack(this.hisSelectedUnit, area);

                        float dt = 0;

                        List<Unit> unitsInRange = getUnitsInRange(this.hisSelectedUnit);

                        if (unitsInRange.size() != 0) {
                            Unit unitPlusPetite = findSmallestHp(unitsInRange);
                            action.doAutoAction(dt, this, unitPlusPetite, this.hisSelectedUnit);
                            currentState = SELECT_CELL;
                        } else {
                            hisSelectedUnit.setIsUsed(true);
                            currentState = IDLE;

                        }

                    counter = 0;
                    counting = false;
                }
                break;
        }
    }

    public void movement(Unit unit) {

        float x = unit.getPosition().x;
        float y = unit.getPosition().y;

        DiscreteCoordinates c = new DiscreteCoordinates((int) x, (int) y);
        this.changePosition(c);
        this.centerCamera();

        List<Unit> enemyUnits = new ArrayList<>();

        for (Unit unit1 : area.units) {
            if ((unit1.getCamp() != unit.getCamp()) && !unit1.isDead()) {
                enemyUnits.add(unit1);
            }
        }

        /*if (enemyUnits.size() == 0) {
            plusDennemis = true;
        }*/

        Unit unitLaPlusProche = unitLaPlusProche(enemyUnits, unit);

        if (unitLaPlusProche != null) {
            int d = (int) x;
            int e = (int) y;

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
                /*unit.changePosition(new DiscreteCoordinates((int) x, (int) y));
                this.changePosition(new DiscreteCoordinates((int) unit.getPosition().x,
                        (int) unit.getPosition().y));
                centerCamera();*/
            } else {
                if (deltaX > 0) y = y + 1;
                if (deltaY > 0) x = x + 1;
                if (deltaX < 0) y = y - 1;
                if (deltaY < 0) x = x - 1;
                /*unit.changePosition(new DiscreteCoordinates((int) x, (int) y));
                this.changePosition(new DiscreteCoordinates((int) unit.getPosition().x,
                        (int) unit.getPosition().y));
                centerCamera();*/
            }
            int newX = (int) x;
            int newY = (int) y;
            coordUniteIA = new DiscreteCoordinates(newX, newY);
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

        if (counting) {
            counter += dt;
            if (counter > value) {
                counting = false;
                counter = 0;
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
    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        //icWarsPlayerGUI.draw(canvas);

        if(currentState == ICWarsPlayerCurrentState.ACTION && action != null) {
            action.draw(canvas);
        }
    }
}

// Le AIPlayer ne suit pas ses unités.
// Draw le curseur ?
//Probleme de draw du soldier (unite marquée comme nulle) dans la console