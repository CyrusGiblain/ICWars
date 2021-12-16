//package ch.epfl.cs107.play.game.icwars.gui;
//
//import ch.epfl.cs107.play.game.actor.Graphics;
//import ch.epfl.cs107.play.game.actor.ImageGraphics;
//import ch.epfl.cs107.play.game.actor.ShapeGraphics;
//import ch.epfl.cs107.play.game.actor.TextGraphics;
//import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
//import ch.epfl.cs107.play.game.icwars.actor.unit.Unit;
//import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
//import ch.epfl.cs107.play.math.*;
//import ch.epfl.cs107.play.math.Polygon;
//import ch.epfl.cs107.play.math.Shape;
//import ch.epfl.cs107.play.window.Canvas;
//
//import java.awt.*;
//
//
//public class ICWarsInfoPanel implements Graphics {
//
//    private final float fontSize;
//
//    private ICWarsBehavior.ICWarsCellType cellType;
//    private Unit unit;
//
//    /// Sprite and text graphics line
//    private final ShapeGraphics cellDetailsBackground, unitDetailsBackground;
//    private final TextGraphics cellTypeText, cellDefenseText;
//    private final TextGraphics unitNameText, unitHealthText, unitDamageText;
//    private final ImageGraphics woodSprite, plainSprite, citySprite, mountSprite, riverSprite, roadSprite;
//
//    /**
//     * Default Dialog Constructor
//     */
//    public ICWarsInfoPanel(float cameraScaleFactor) {
//        final float height = cameraScaleFactor/4;
//        final float width = cameraScaleFactor/8;
//
//        fontSize = cameraScaleFactor/ICWarsPlayerGUI.FONT_SIZE;
//
//        Shape rect = new Polygon(0,0, 0,height, width,height, width,0);
//        cellDetailsBackground = new ShapeGraphics(rect, Color.DARK_GRAY, Color.BLACK, 0f, .8f, 3000f);
//        unitDetailsBackground = new ShapeGraphics(rect, Color.DARK_GRAY, Color.BLACK, 0f, .8f, 3000f);
//
//        Vector anchor = new Vector(.1f, -.2f);
//        woodSprite = new ImageGraphics(ResourcePath.getSprite("icwars/wood"), 1f, 1f, null, anchor, 1f, 3001f);
//        plainSprite = new ImageGraphics(ResourcePath.getSprite("icwars/plain"), 1f, 1f, null, anchor, 1f, 3001f);
//        citySprite = new ImageGraphics(ResourcePath.getSprite("icwars/neutralBuilding"), 1f, 1f, null, anchor, 1f, 3001f);
//        mountSprite = new ImageGraphics(ResourcePath.getSprite("icwars/mountain"), 1f, 1f, null, anchor, 1f, 3001f);
//        roadSprite = new ImageGraphics(ResourcePath.getSprite("icwars/road"), 1f, 1f, null, anchor, 1f, 3001f);
//        riverSprite = new ImageGraphics(ResourcePath.getSprite("icwars/river"), 1f, 1f, null, anchor, 1f, 3001f);
//
//        cellTypeText = new TextGraphics("", fontSize, Color.WHITE, null, 0.0f,
//                false, false, new Vector(0, -0.3f),
//                TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);
//
//        cellDefenseText = new TextGraphics("", fontSize, Color.WHITE, null, 0.0f,
//                false, false, new Vector(0, -2.5f*fontSize-0.7f),
//                TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);
//
//
//        unitNameText = new TextGraphics("", fontSize, Color.WHITE, null, 0.0f,
//                false, false, new Vector(0, -0.3f),
//                TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);
//
//        unitHealthText = new TextGraphics("", fontSize, Color.WHITE, null, 0.0f,
//                false, false, new Vector(0, -1.25f*fontSize-0.5f),
//                TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);
//
//        unitDamageText = new TextGraphics("", fontSize, Color.WHITE, null, 0.0f,
//                false, false, new Vector(0, -2.5f*fontSize-0.4f),
//                TextAlign.Horizontal.LEFT, TextAlign.Vertical.MIDDLE, 1.0f, 3001f);
//    }
//
//
//
//    public void setCurrentCell(ICWarsBehavior.ICWarsCellType cellType) {
//        this.cellType = cellType;
//    }
//
//    public void setUnit(Unit unit) {
//        this.unit = unit;
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//    	// Compute width, height and anchor
//		float width = canvas.getXScale();
//		float height = canvas.getYScale();
//
//		if (cellType != null)
//		    drawCellDetails(canvas, height, width);
//		if (unit != null)
//		    drawUnitDetails(canvas, height, width);
//    }
//
//    private void drawCellDetails(Canvas canvas, float height, float width) {
//        final Transform transform = Transform.I.translated(canvas.getPosition().add(3*width/8, -height/2));
//        cellDetailsBackground.setRelativeTransform(transform);
//        cellDetailsBackground.draw(canvas);
//
//
//        final Transform textTransform = Transform.I.translated(canvas.getPosition().add(3*width/8+.1f, -height/4));
//
//        cellTypeText.setRelativeTransform(textTransform);
//        cellTypeText.setText(cellType.typeToString());
//        cellTypeText.draw(canvas);
//
//        cellDefenseText.setText("Def: " + cellType.getDefenseStar());
//        cellDefenseText.setRelativeTransform(textTransform);
//        cellDefenseText.draw(canvas);
//
//        final Transform spriteTransform = Transform.I.translated(canvas.getPosition().add(3*width/8, -13*height/32));
//
//        switch (cellType) {
//            case WOOD:
//                woodSprite.setRelativeTransform(spriteTransform);
//                woodSprite.draw(canvas);
//                break;
//            case CITY:
//                citySprite.setRelativeTransform(spriteTransform);
//                citySprite.draw(canvas);
//                break;
//            case MOUNTAIN:
//                mountSprite.setRelativeTransform(spriteTransform);
//                mountSprite.draw(canvas);
//                break;
//            case RIVER:
//                riverSprite.setRelativeTransform(spriteTransform);
//                riverSprite.draw(canvas);
//                break;
//            case ROAD:
//                roadSprite.setRelativeTransform(spriteTransform);
//                roadSprite.draw(canvas);
//                break;
//            default:
//                plainSprite.setRelativeTransform(spriteTransform);
//                plainSprite.draw(canvas);
//                break;
//        }
//    }
//
//    private void drawUnitDetails(Canvas canvas, float height, float width) {
//        final Transform transform = Transform.I.translated(canvas.getPosition().add(width/4, -height/2));
//        unitDetailsBackground.setRelativeTransform(transform);
//        unitDetailsBackground.draw(canvas);
//
//        final Transform nameTransform = Transform.I.translated(canvas.getPosition().add(width/4+.1f, -height/4));
//
//        unitNameText.setRelativeTransform(nameTransform);
//        unitNameText.setText(unit.getName());
//        unitNameText.draw(canvas);
//
//        final Transform characteristicsTransform = Transform.I.translated(canvas.getPosition().add(width/4+.1f, -height/4));
//
//        unitHealthText.setRelativeTransform(characteristicsTransform);
//        unitHealthText.setText("HP: " + unit.getHp());
//        unitHealthText.draw(canvas);
//
//        unitDamageText.setRelativeTransform(characteristicsTransform);
//        unitDamageText.setText("DMG: " + unit.getDamage());
//        unitDamageText.draw(canvas);
//    }
//
//}
