package ch.epfl.cs107.play.game.areagame.io;


public class ResourcePath {

	private static final String SPRITE = "images/sprites/";
	private static final String BEHAVIORS = "images/behaviors/";
	private static final String BACKGROUNDS = "images/backgrounds/";
	private static final String FOREGROUNDS = "images/foregrounds/";
	private static final String SOUNDS = "sounds/";
	private static final String STRINGS = "strings/";
	public static final String FONTS = "fonts/";

	private static final String IMAGE_EXTENSION = ".png";
	private static final String SOUNDS_EXTENSION = ".wav";
	private static final String STRINGS_EXTENSION = ".xml";

	public static String getSprite(String name){
		return SPRITE+name+IMAGE_EXTENSION;
	}

	public static String getBehavior(String name){
		return BEHAVIORS+name+IMAGE_EXTENSION;
	}

	public static String getBackground(String name){
		return BACKGROUNDS+name+IMAGE_EXTENSION;
	}

	public static String getForeground(String name){
		return FOREGROUNDS+name+IMAGE_EXTENSION;
	}

	public static String getSound(String name){
		return SOUNDS+name+SOUNDS_EXTENSION;
	}

	public static String getString(String name){
		return STRINGS+name+STRINGS_EXTENSION;
	}


}
