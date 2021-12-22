package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Tanks;
import ch.epfl.cs107.play.game.icwars.actor.players.unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import static java.lang.System.out;

public class ICWarsPlayerGUI implements Graphics {

    private ICWarsPlayer player;
    private ICWarsActionsPanel actionsPanel;
    private ICWarsInfoPanel infoPanel;
    protected static final int FONT_SIZE = 20;

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
        Unit unitOnWhichPlayerIsLocated = player.getUnitOnWhichHeIsLocated();
        ICWarsBehavior.ICWarsCellType cellType = unitOnWhichPlayerIsLocated.getCellType();

        Unit selectedUnit = player.getSelectedUnit();

            selectedUnit.drawRangeAndPathTo(destination, canvas);
            if(player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.ACTION_SELECTION) {
                out.println("azertyujhgfghjijhgfdfghjhgfv");
                actionsPanel.setActions(selectedUnit.getPossibleActions());
                actionsPanel.draw(canvas);
            }
            if (player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL ||
            player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.SELECT_CELL) {
                infoPanel.setCurrentCell(cellType);
                infoPanel.draw(canvas);
            }
    }
}
//Le RealPlayer devra communiquer, quand il y a intéraction, les infos sur la cellule sur laquelle il se trouve.