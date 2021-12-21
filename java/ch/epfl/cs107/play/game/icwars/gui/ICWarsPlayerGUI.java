package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Tanks;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.game.icwars.actor.unit.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;
import java.util.Objects;

public class ICWarsPlayerGUI implements Graphics {

    protected ICWarsPlayer player;
    private ICWarsActionsPanel actionsPanel;
    private ICWarsInfoPanel infoPanel;
    protected static final int FONT_SIZE = 13;

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
        if(selectedUnit != null){
            selectedUnit.drawRangeAndPathTo(destination, canvas);

            if(player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.ACTION_SELECTION) {
                System.out.println("azertyujhgfghjijhgfdfghjhgfv");
                actionsPanel.setActions(((Tanks)selectedUnit).getPossibleActions());
                actionsPanel.draw(canvas);
            }
            if (player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.NORMAL ||
            player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.SELECT_CELL) {
                infoPanel.setCurrentCell(cellType);
                infoPanel.setUnit(unitOnWhichPlayerIsLocated);
                infoPanel.draw(canvas);
            }
        }
    }
}
//Le RealPlayer devra communiquer, quand il y a intéraction, les infos sur la cellule sur laquelle il se trouve.