package ch.epfl.cs107.play.game.icwars.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Soldats;
import ch.epfl.cs107.play.game.icwars.actor.Tanks;
import ch.epfl.cs107.play.game.icwars.actor.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;

public interface ICWarsInteractionVisitor extends AreaInteractionVisitor {

    default void interactWith(Unit unit) {}
    default void interactWith(ICWarsBehavior.ICWarsCellType cell) {}
}
