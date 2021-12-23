package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Tanks;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import static java.lang.System.out;

public class ICWarsPlayerGUI implements Graphics {

    private ICWarsPlayer player;
    private ICWarsInfoPanel infoPanel;
    private ICWarsActionsPanel actionsPanel;
    Unit unit;
    Unit cellUnit;
    protected static final float FONT_SIZE = 20.f;

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
        if(selectedUnit != null && !selectedUnit.theUnitHasBeenUsed())
            selectedUnit.drawRangeAndPathTo(destination, canvas);

        if (player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL ||
            player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.SELECT_CELL||
            player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.MOVE_UNIT) {
            infoPanel.setCurrentCell(((RealPlayer)player).getCellType());
            infoPanel.draw(canvas);
        }

        if(player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.ACTION_SELECTION){
            actionsPanel.setActions(player.getSelectedUnit().getPossibleActions());
            actionsPanel.draw(canvas);
        }
    }
    public void setCellUnit(Unit cellUnit){infoPanel.setUnit(cellUnit);
        this.cellUnit = cellUnit;}
    public void setUnit(Unit unit){this.unit = unit;}
}
//Le RealPlayer devra communiquer, quand il y a intéraction, les infos sur la cellule sur laquelle il se trouve.