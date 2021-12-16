//package ch.epfl.cs107.play.game.icwars.gui;
//
//import ch.epfl.cs107.play.game.actor.Graphics;
//import ch.epfl.cs107.play.game.actor.ShapeGraphics;
//import ch.epfl.cs107.play.game.actor.TextGraphics;
////import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
////import ch.epfl.cs107.play.game.icwars.actor.unit.action.Action;
////import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
//import ch.epfl.cs107.play.math.*;
//import ch.epfl.cs107.play.math.Polygon;
//import ch.epfl.cs107.play.math.Shape;
//import ch.epfl.cs107.play.window.Canvas;
//
//import java.awt.*;
//import java.util.List;
//
//
//public class ICWarsActionsPanel implements Graphics {
//	
//    private final float fontSize;
//
//    private List<Action> actions;
//
//    /// Sprite and text graphics line
//    private final ShapeGraphics background;
//    private TextGraphics[] actionsText;
//
//    /**
//     * Default Dialog Constructor
//     */
//    public ICWarsActionsPanel(float cameraScaleFactor) {
//        final float height = cameraScaleFactor/4;
//        final float width = cameraScaleFactor/4;
//
//        fontSize = cameraScaleFactor/ICWarsPlayerGUI.FONT_SIZE;
//
//        Shape rect = new Polygon(0,0, 0,height, width,height, width,0);
//        background = new ShapeGraphics(rect, Color.DARK_GRAY, Color.BLACK, 0f, 0.7f, 3000f);
//    }
//
//    private void createActionsText() {
//        actionsText = new TextGraphics[actions.size()];
//        for (int i = 0; i < actions.size(); ++i) {
//            TextGraphics text = new TextGraphics(actions.get(i).getName(), fontSize, Color.WHITE, null, 0.0f,
//                    false, false, new Vector(0, -i*1.25f*fontSize-0.35f),
//                    TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);
//
//            text.setFontName("Kenney Pixel");
//
//            actionsText[i] = text;
//        }
//    }
//
//    public void setActions(List<Action> actions) {
//        this.actions = actions;
//        createActionsText();
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//        // Compute width, height and anchor
//        float width = canvas.getXScale();
//        float height = canvas.getYScale();
//
//        final Transform transform = Transform.I.translated(canvas.getPosition().add(width/4, height/4));
//        background.setRelativeTransform(transform);
//        background.draw(canvas);
//
//        final Transform textTransform = Transform.I.translated(canvas.getPosition().add(width/4+.1f, height/2));
//        for (TextGraphics text : actionsText) {
//            text.setRelativeTransform(textTransform);
//            text.draw(canvas);
//        }
////    }
//
//}
