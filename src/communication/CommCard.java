package communication;

import java.util.HashSet;
import java.io.Serializable;
import java.util.Arrays;

import protocol.resources.Board;
import protocol.resources.card.Card;
import protocol.resources.card.Creature;

/**
 * simple, easy to display image of a card
 * @author alexk
 *
 */
public class CommCard implements Serializable, Cloneable, Comparable<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8787556910165446831L;
	public int atk = -1337, health = -1337, cost = 0, mrange = -1337, vrange = -1337, arange = -1337; //for spells atk, health, vrange, arange and mrange should be -1337
	public String[] tags = new String[0]; //Card.tags + all built-in booleans (shield, flying, etc.)
	public boolean freemove = false, freeatk = false;
	public String cname = "", ctype = "";
	
	/**
	 * empty constructor (doesn't do anything)
	 */
	public CommCard() {
		
	}
	
	/**
	 * automatically generates a CommCard out of an actual Card
	 * to determine if active effects are available, board and location of the card are required
	 * exceptions for board == null and loc == null are 
	 * handled accordingly (meaning they aren't handled at all)
	 * @param c
	 */
	public CommCard(Card c, Board bd, String loc) {
		if (c == null) System.out.println("c is null");
		//System.out.println(c.toString() + " " + c.getcCost());
		cost = c.getcCost();
		ctype = c.getCType();
		cname = c.getCName();
		tags = c.getTags();
		if (c.getCType().contains("creature")) {
			Creature cr = (Creature) c;
			atk = cr.getcAttack();
			health = cr.getcHealth();
			mrange = cr.mRange;
			vrange = cr.vRange;
			arange = cr.aRange;
			if (cr.mType == "free") freemove = true;
			if (cr.aType == "free") freeatk = true;
			if (cr.flying) addTag("flying");
			if (cr.shield) addTag("shield");
			if (cr.root) addTag("root");
			if (cr.hero) addTag("hero");
			if (cr.stun) addTag("stun");
			if (cr.taunt) addTag("taunt");
			if (cr.terrified) addTag("terrified");
			if (cr.actEffAvailable(bd, loc)) addTag("acteffav");
			if (cr.moveAv > 0) addTag("moveav");
			try {
				if ((cr.AtkAv > 0)&&(cr.actEffAvailable(bd, loc))) addTag("atkav");
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * adds a new tag to the CommCard
	 * @param s
	 */
	public void addTag(String s) {
		HashSet<String> al = new HashSet<>(Arrays.asList(tags));
		al.add(s.toLowerCase());
		tags = al.toArray(new String[0]);
	}
	
	/**
	 * removes the given tag s from the array of tags
	 * @param s
	 */
	public void remove(String s) {
		HashSet<String> al = new HashSet<>(Arrays.asList(tags));
		al.remove(s);
		tags = al.toArray(new String[0]);
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof CommCard) {
			cname.compareTo(((CommCard)o).cname);
		}		
		return cname.compareTo(o.toString());
	}
}
