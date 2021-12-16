package ch.epfl.cs107.play.game.tutosSolution.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.tutosSolution.Tuto1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

abstract public class SimpleArea extends Area {
	private Window window;
	
	/**
     * Create the area by adding all its actors
     * called by the begin method, when the area starts to play
     */
	protected abstract void createArea();
	
	@Override
	public int getWidth() {
		 Image   behaviorMap = window.getImage(ResourcePath.getBehavior(getTitle()), null, false);
	     return  behaviorMap.getWidth();

	}
	
	@Override
	public int getHeight() {
		 Image behaviorMap = window.getImage(ResourcePath.getBehavior(getTitle()), null, false);
		 return  behaviorMap.getHeight();

	}
	
	 @Override
	    public boolean begin(Window window, FileSystem fileSystem) {
		 	this.window = window;
	        if (super.begin(window, fileSystem)) {
	            // Set the behavior map
	            createArea();
	            return true;
	        }
	        return false;
	    }
	 
	 @Override
	    public final float getCameraScaleFactor() {
	        return Tuto1.CAMERA_SCALE_FACTOR;
	    }
}
