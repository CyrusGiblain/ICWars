package ch.epfl.cs107.play.game.tutosSolution.actor;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
//import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
//import ch.epfl.cs107.play.window.Keyboard;

public class SimpleGhost extends Entity {
	 private float hp;
	 private TextGraphics message;
	 private Sprite sprite;
	    /**
	     * Default DemoActor constructor
	     * @param position (Vector): initial position vector of the ghost
	     * @param text (String): initial text moving with the ghost
	     */
	 public SimpleGhost(Vector position, String spriteName) {
		 //super(position, new ImageGraphics(ResourcePath.getSprite(spriteName),  1.0f,1.0f, null, Vector.ZERO, 1.0f, -Float.MAX_VALUE));
		 super(position);
		 this.hp = 10;
		 sprite = new Sprite(spriteName, 1.f, 1.f,this);
		 message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
		 message.setParent(this);
		 message.setAnchor(new Vector(-0.3f, 0.1f));
		
	 }

	 public void moveUp(float delta) {
		 setCurrentPosition(getPosition().add(0.f, delta));
	 }
	 public void moveDown(float delta) {
		 setCurrentPosition(getPosition().add(0.f, -delta));
	 }
	 public void moveLeft(float delta) {
		 setCurrentPosition(getPosition().add(-delta, 0.f));
	 }
	 public void moveRight(float delta) {
		 setCurrentPosition(getPosition().add(delta, 0.f));
	 }
	 @Override
	 public void draw(Canvas canvas) {
		 sprite.draw(canvas);	
		 message.draw(canvas);
	 }
	 @Override
	 public void update(float deltaTime) {
		 if (hp > 0) {
			 hp -=deltaTime;
			 message.setText(Integer.toString((int)hp));
		 }
		 if (hp < 0) hp = 0.f;
		 
	 }
	 
	 public boolean isWeak() {
		 return (hp <= 0.f);
	 }
	 
	 public void strengthen() {
		 hp = 10;
	 }
}

