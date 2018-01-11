package communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import protocol.resources.Board;
import protocol.resources.card.Card;

/**
 * Creates a copy of the board that displays all
 * information that's relevant to a player
 * @author alexk
 *
 */
public class CommBoard implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6547927927335360806L;
	public CommMsg[] messages;
	public HashMap<String,CommTile> board;
	public CommCard[] hand, grave, removed, side, deck, enemygrave, enemyremoved;
	public int enemyhandsize, enemydecksize, energy, enemyenergy, aen, enemyaen, energygain;
	public String player;
	
	public CommBoard(Board bd, CommMsg[] msg, String pname) {
		player = pname;
		board = new HashMap<>();
		String[] vr = bd.getVisionRange(pname);
		hi: for (String s : bd.getAdjecent("E1",8)) {
			for (String s2 : vr) {
				if (s.equals(s2)) {
					board.put(s, new CommTile(bd.get(s),bd,s));
					continue hi;
				}
			}
			board.put(s, new CommTile());
		}
		String p2name = "";
		for (String s : bd.playerNames) {
			if (s != pname) p2name = s;
		}
		messages = msg;
		hand = new CommCard[bd.hand.get(pname).size()];
		ArrayList<Card> cd = bd.hand.get(pname).get();
		for (int i = 0; i < cd.size(); i++) {
			hand[i] = new CommCard(cd.get(i), bd, "hand");
		}
		grave = new CommCard[bd.grave.get(pname).size()];
		cd = bd.grave.get(pname).get();
		for (int i = 0; i < cd.size(); i++) {
			grave[i] = new CommCard(cd.get(i), bd, "grave");
		}
		removed = new CommCard[bd.removed.get(pname).size()];
		cd = bd.removed.get(pname).get();
		for (int i = 0; i < cd.size(); i++) {
			removed[i] = new CommCard(cd.get(i), bd, "removed");
		}
		side = new CommCard[bd.sidedeck.get(pname).size()];
		cd = bd.sidedeck.get(pname).get();
		for (int i = 0; i < cd.size(); i++) {
			side[i] = new CommCard(cd.get(i), bd, "side");
		}
		deck = new CommCard[bd.deck.get(pname).deck.size()];
		cd = bd.deck.get(pname).get();
		for (int i = 0; i < cd.size(); i++) {
			deck[i] = new CommCard(cd.get(i), bd, "deck");
		}
		try {
			Arrays.sort(deck);
		}catch (Exception e) {
			// TODO: handle exception
		}
		enemygrave = new CommCard[bd.grave.get(p2name).size()];
		enemyremoved = new CommCard[bd.removed.get(p2name).size()];
		enemyhandsize = bd.hand.get(p2name).size();
		enemydecksize = bd.deck.get(p2name).deck.size();
		energygain = bd.resources.get(pname)[1];
		energy = bd.resources.get(pname)[0];
		aen =  bd.resources.get(pname)[2];
		enemyenergy = bd.resources.get(p2name)[0];
		enemyaen = bd.resources.get(p2name)[2];
	}
}
