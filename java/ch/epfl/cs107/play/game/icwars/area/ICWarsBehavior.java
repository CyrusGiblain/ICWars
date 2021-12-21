package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.players.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

public class ICWarsBehavior extends AreaBehavior {
    public enum ICWarsCellType {
        NONE(0, 0),          // Should never be used except
        ROAD(-16777216, 0), // the second value is the number of defense stars
        PLAIN(-14112955, 1),
        WOOD(-65536, 3),
        RIVER(-16776961, 0),
        MOUNTAIN(-256, 4),
        CITY(-1, 2);

        private final int type;
        private final int stars;

        /**
         * Default Cell constructor
         *
         * @param type
         * @param stars
         */

        ICWarsCellType(int type, int stars) {
            this.type = type;
            this.stars = stars;
        }

        public static ICWarsBehavior.ICWarsCellType toType(int type) {
            for (ICWarsBehavior.ICWarsCellType ict : ICWarsBehavior.ICWarsCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NONE;
        }

        public String typeToString() {
            String name = null;
            switch (this) {
                case NONE:
                    name = "None";
                    break;
                case ROAD:
                    name = "Road";
                    break;
                case PLAIN:
                    name = "Plain";
                    break;
                case WOOD:
                    name = "Wood";
                    break;
                case RIVER:
                    name = "River";
                    break;
                case MOUNTAIN:
                    name = "Mountain";
                    break;
                case CITY:
                    name = "City";
                    break;
            }
            return name;
        }

        public ICWarsCellType getType() {
            ICWarsCellType cellType = RIVER;
            switch (cellType) {
                case NONE:
                    cellType = NONE;
                    break;

                case ROAD:
                    cellType = ROAD;
                    break;

                case PLAIN:
                    cellType = PLAIN;
                    break;

                case WOOD:
                    cellType = WOOD;
                    break;

                case RIVER:
                    cellType = RIVER;
                    break;

                case MOUNTAIN:
                    cellType = MOUNTAIN;
                    break;

                case CITY:
                    cellType = CITY;
                    break;
            }
            return cellType;
        }


        public int getDefenseStar() {
            return stars;
        }

        /**
         * Get this Interactor's current occupying cells coordinates
         *
         * @return (List of DiscreteCoordinates). May be empty but not null
         */
    }

    /**
     * Default AreaBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public ICWarsBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICWarsBehavior.ICWarsCellType color = ICWarsBehavior.ICWarsCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICWarsBehavior.ICWarsCell(x, y, color));
            }
        }
    }


    public class ICWarsCell extends Cell implements Interactable {
        private final ICWarsBehavior.ICWarsCellType type;

        /**
         * Default Cell constructor
         *
         * @param x    (int): x-coordinate of this cell
         * @param y    (int): y-coordinate of this cell
         * @param type
         */

        public ICWarsCell(int x, int y, ICWarsCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if (entity.takeCellSpace()) {
                for (Interactable otherEntity : entities) {
                    if (otherEntity.takeCellSpace()) {
                        return false;
                    }
                }
            }
            return true;
        }

            @Override
            public boolean takeCellSpace () {
                return false;
            }

            @Override
            public boolean isCellInteractable () {
                return true;
            }

            @Override
            public boolean isViewInteractable () {
                return false;
            }

            @Override
            public void acceptInteraction (AreaInteractionVisitor v){
                ((ICWarsInteractionVisitor) v).interactWith(type.getType());

            }
        }
    }
