package ch.epfl.cs107.play.game;

import ch.epfl.cs107.play.game.actor.Draggable;

public class DragHelper {
	private static Draggable currentDraggedElement;
	
	public static Draggable getCurrentDraggedElement() {
		return currentDraggedElement;
	}
	
	public static void setCurrentDraggedElement(Draggable newElement) {
		currentDraggedElement = newElement;
	}
}
