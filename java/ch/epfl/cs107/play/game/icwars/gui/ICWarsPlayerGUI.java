package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Tanks;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class ICWarsPlayerGUI implements Graphics {

    private ICWarsPlayer player;
    private ICWarsInfoPanel infoPanel;
    private ICWarsActionsPanel actionsPanel;
    Unit unit;
    Unit cellUnit;
    protected static final float FONT_SIZE = 20.f;

    /**
     * Constructor of ICWarsPlayerGUI
     *
     * @param cameraScaleFactor  (float): The zoom of the camera
     * @param player             (ICWarsPlayer) The ICWarsPlayer involved
     */
    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player) {
        this.player = player;
        this.actionsPanel = new ICWarsActionsPanel(cameraScaleFactor);
        this.infoPanel = new ICWarsInfoPanel(cameraScaleFactor);
    }

    /**
     * Renders itself on specified canvas.
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {

        DiscreteCoordinates destination = player.getCurrentCells().get(0);

        Unit selectedUnit = player.getSelectedUnit();
        if(selectedUnit != null && !selectedUnit.theUnitHasBeenUsed() &&
        player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.MOVE_UNIT)
            selectedUnit.drawRangeAndPathTo(destination, canvas);

        infoPanel.setCurrentCell((((RealPlayer)player).getCellType()));

        if (player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL ||
                player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.SELECT_CELL) {
            infoPanel.draw(canvas);
        }

        if(player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.ACTION_SELECTION){
            actionsPanel.setActions(player.getSelectedUnit().getPossibleActions());
            actionsPanel.draw(canvas);
        }
    }

    /**
     * Method to set a cell unit
     *
     * @param cellUnit
     */
    public void setCellUnit(Unit cellUnit){
        infoPanel.setUnit(cellUnit);
    }

    /**
     * Method to set a unit
     *
     * @param unit  (Unit): The unit we want to set
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}