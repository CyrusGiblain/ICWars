package ch.epfl.cs107.play.game.icwars.actor.players;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;

import static ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer.ICWarsPlayerCurrentState.*;

public class AIPlayer extends ICWarsPlayer {
    private ArrayList<Unit> Enemyunits = new ArrayList<>();
    private ArrayList<Unit> AIControlledunits = new ArrayList<>();
    Keyboard keyboard = getOwnerArea().getKeyboard();
    private faction camp;

    public AIPlayer(ICWarsArea area, DiscreteCoordinates position, faction camp, Unit... units) {
        super(area, position, camp, units);
        this.camp = camp;
        for(int i = 0; i < area.units.size(); ++i){
            if(area.units.get(i).getCamp() == camp) AIControlledunits.add(area.units.get(i));
            else {Enemyunits.add(area.units.get(i));}
        }
    }

    @Override
    public void update(float deltatime){
        Action act;
        super.update(deltatime);
        switch (currentState) {

            case IDLE:
                currentState = ICWarsPlayerCurrentState.NORMAL;
                break;

            case NORMAL:

                centerCamera();

                currentState = MOVE_UNIT;
                break;


            case MOVE_UNIT:
                movement();
                //this.theSelectedUnitHasBeenUsed = selectedUnit.theUnitHasBeenUsed();
                currentState = ICWarsPlayerCurrentState.NORMAL;
                break;
        }
    }

    public void movement() {
        ArrayList<Double> Distances = new ArrayList<>();
        ArrayList<Double> xDistances = new ArrayList<>();
        ArrayList<Double> yDistances = new ArrayList<>();
        ArrayList<Unit> unitsInRange = new ArrayList<>();
        int position;
        Unit AICurrentlyControlledunit = null;
        Unit smallestHp;
        //action might not happen because act is not initialized
        Action act = null;
        double smallestDistance = 0;
        //determination of the closes unit to the current one
        // if not work use other method from DiscreteCoordinates
        for (int j = 0; j < AIControlledunits.size(); ++j) {
            for (int i = 0; i < Enemyunits.size(); ++i) {
                Unit AIControlledunit = AIControlledunits.get(j);
                Unit Enemyunit = Enemyunits.get(i);
                Enemyunit.centerCamera();
                waitFor(1, 4);
                double xDistance = Math.abs(AIControlledunit.getPosition().x - Enemyunit.getPosition().x);
                double yDistance = Math.abs(AIControlledunit.getPosition().y - Enemyunit.getPosition().y);
                xDistances.add(xDistance);
                yDistances.add(yDistance);
                Distances.add(Math.sqrt(xDistance * xDistance - yDistance * yDistance));
                AIControlledunit.centerCamera();
                waitFor(1,4);
            }
            waitFor(2, 24);
            position = smallestDistance(Distances);
            AICurrentlyControlledunit = AIControlledunits.get(j);
            for(int l = 0; l< Distances.size(); ++l) {
                //if(Distances.get(l) < Math.sqrt(2 * AICurrentlyControlledunit.radius * AICurrentlyControlledunit.radius))
                    unitsInRange.add(Enemyunits.get(l));
            }
            smallestHp = findSmallestHp(unitsInRange);
            // if there are unitsInRange
            if (unitsInRange != null){
                //Calculate the distance and move the units to the right place
                double xDistance = Math.abs(AICurrentlyControlledunit.getPosition().x - smallestHp.getPosition().x);
                double yDistance = Math.abs(AICurrentlyControlledunit.getPosition().y - smallestHp.getPosition().y);
                //if (xDistance <= AICurrentlyControlledunit.radius && yDistance <= AICurrentlyControlledunit.radius) {
                    DiscreteCoordinates LowLifeEnemyPosition = new DiscreteCoordinates((int) smallestHp.getPosition().x, (int) smallestHp.getPosition().y);
                    act.equals(AICurrentlyControlledunit.getPossibleActions().get(0));
                    waitFor(2, 48);
                    act.doAutoAction(8, this, smallestHp);
                }
            //} else {
                // if the distance is greater we move to the closest Unit
                double xDistance = Enemyunits.get(position).getPosition().x;
                double yDistance = Enemyunits.get(position).getPosition().y;
                DiscreteCoordinates newPosition = new DiscreteCoordinates((int)xDistance, (int)yDistance);
                AICurrentlyControlledunit.changePosition(newPosition);
                AICurrentlyControlledunit.centerCamera();
            }
        }
    //}
    public Unit findSmallestHp(ArrayList<Unit> units) {
        int j = 0;
        Unit unitWithSmallestHp = null;
        //bubble sort
        for(int i = 0; i < units.size()-1; ++i) {
            if(units.get(i).getHp() > units.get(i+1).getHp() &&
                    (unitWithSmallestHp.getHp() > units.get(i+1).getHp())){
                unitWithSmallestHp = units.get(i+1);
            }
        }
        return unitWithSmallestHp;
    }

    public int smallestDistance (ArrayList < Double > Distances) {
        // the aim of this method is to go and get the index of the smallest distance, of the distance in the list
        double smallDistance = 0;
        int index = 0;
        //get the smallest distance
        int j = 0;
        for (int c = 0; c < Distances.size(); ++c) {
            if (Distances.get(j) > Distances.get(c)) {
                smallDistance = Distances.get(c);
                index = c;
                j = index;
            }
        }
        return index;
    }
    /**
     * Ensures that value time elapsed before returning true
     * @param dt elapsed time
     * @param value waiting time (in seconds)
     * @return true if value seconds has elapsed , false otherwise
     */
    private boolean waitFor(float value , float dt) {
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


}