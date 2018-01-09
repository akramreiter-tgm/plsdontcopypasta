package protocol.resources.card;

import protocol.resources.Board;
import protocol.resources.Player;

/**
 * Creatures may get additional effects from other cards
 * these will be added in the form of implementations of this class
 */
public abstract class Effect {
	/**
	 * executes the Effect (the only thing an Effect actually does)
	 * damaging effects should trigger Board.clearBoard before returning
	 * @param (String) trigger
	 * @param (Board) b
	 * @param (String) loc
	 * @param (Player) p
	 * @param (Card) card
	 */
	public abstract void execute(String trigger, Board b, String[] loc, Player p, Card card);
	
	/**
	 * returns a list of possible triggers for this effect
	 * @return String
	 */
	public abstract String[] getTrigger();
	
	/**
	 * returns the tag of this effect.
	 * this allows the implementation unique/mutually exclusive effects 
	 * @return
	 */
	public abstract String getTag();
}
