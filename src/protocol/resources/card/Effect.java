package protocol.resources.card;

import protocol.resources.Board;
import protocol.resources.Player;

/*
 * Creatures may get additional effects from other cards
 * these will be added in the form of implementations of this class
 *  
 */
public abstract class Effect {
	/*
	 * trigger has to be set in implementations of this class
	 */
	public abstract void execute(Board b, String[] loc, Player p, Card card);
	public abstract String getTrigger();
}
