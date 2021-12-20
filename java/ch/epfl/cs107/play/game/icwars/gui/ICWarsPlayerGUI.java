package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.players.RealPlayer;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Objects;

public class ICWarsPlayerGUI implements Graphics {

    protected ICWarsPlayer player;
    ICWarsActionsPanel panel;
    protected static final int FONT_SIZE = 13;

    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player) {
        this.player = player;
    }

    /**
     * Renders itself on specified canvas.
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        DiscreteCoordinates destination = player.getCurrentCells().get(0);

        Unit selected = player.getSelectedUnit();
        if(selected != null){
            selected.drawRangeAndPathTo(destination, canvas);

            if(player.getCurrentState() == ICWarsPlayer.ICWarsPlayerCurrentState.ACTION_SELECTION)
                panel.setActions(player.getSelectedUnit().getPossibleActions());
        }
    }
}
