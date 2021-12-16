package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

/**
 * A drawable AreaGraph
 */
public class ICWarsRange extends AreaGraph implements Graphics {

    @Override
    public void addNode(DiscreteCoordinates coordinates, boolean hasLeftEdge, boolean hasUpEdge, boolean hasRightEdge, boolean hasDownEdge) {
        getNodes().putIfAbsent(coordinates, new RangeNode(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge));
    }

    private class RangeNode extends AreaNode implements Graphics{
        private final ImageGraphics nodeSprite;

        private RangeNode(DiscreteCoordinates coordinates, boolean hasLeftEdge, boolean hasUpEdge, boolean hasRightEdge, boolean hasDownEdge) {
            super(coordinates, hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);

            if (!hasUpEdge && !hasRightEdge && !hasDownEdge && !hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(3*18, 5*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (!hasUpEdge && hasRightEdge && hasDownEdge && !hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(0*18, 5*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (!hasUpEdge && hasRightEdge && hasDownEdge && hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(1*18, 5*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (!hasUpEdge && !hasRightEdge && hasDownEdge && hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(2*18, 5*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (hasUpEdge && !hasRightEdge && hasDownEdge && hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(2*18, 6*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (hasUpEdge && !hasRightEdge && !hasDownEdge && hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(2*18, 7*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (hasUpEdge && hasRightEdge && !hasDownEdge && hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(1*18, 7*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (hasUpEdge && hasRightEdge && !hasDownEdge && !hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(0*18, 7*18,16,16), coordinates.toVector(), 0.6f, 500);

            else if (hasUpEdge && hasRightEdge && hasDownEdge && !hasLeftEdge)
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(0*18, 6*18,16,16), coordinates.toVector(), 0.6f, 500);

            else
                nodeSprite = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1f, 1f,
                        new RegionOfInterest(1*18, 6*18,16,16), coordinates.toVector(), 0.6f, 500);
        }

        @Override
        public void draw(Canvas canvas) {
            nodeSprite.draw(canvas);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (AreaNode node : getNodes().values()) {
            ((RangeNode) node).draw(canvas);
        }
    }
}
