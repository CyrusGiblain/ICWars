package ch.epfl.cs107.play.game.tutosSolution.area.tuto1;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.tutosSolution.area.SimpleArea;


/**
 * Specific area
 */
public class Ferme extends SimpleArea {
	
	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}

	@Override
	protected void createArea() {
        // Base
        //registerActor(new Background(this));
	}
	
}

