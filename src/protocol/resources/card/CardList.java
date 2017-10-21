package protocol.resources.card;

import java.io.Serializable;
import java.util.ArrayList;

public class CardList implements Serializable, Cloneable{
	private static final long serialVersionUID = -7631644622047588834L;
	
	private ArrayList<Card> content;
	public CardList() {
		content = new ArrayList<>();
	}
	
	/**
	 * returns the Card at the given index
	 * @param int index
	 * @return Card
	 */
	public Card get(int index) {
		try {
			return content.get(index);
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * returns the CardList's content
	 * @return ArrayList<Card>
	 */
	public ArrayList<Card> get() {
		return content;
	}
	
	/**
	 * returns the amount of Cards currently stored
	 * in the CardList
	 * @return int
	 */
	public int size() {
		return content.size();
	}
	
	/**
	 * returns the Card at the given index
	 * and removes it from the CardList
	 * @param int index
	 * @return Card
	 */
	public Card remove(int index) {
		try {
			return content.remove(index);
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * adds a Card to the Cardlist
	 * @param (Card) c
	 */
	public void add(Card c) {
		if (c != null) content.add(c);
	}
	
	/**
	 * adds multiple Cards to the Cardlist
	 * @param ArrayList<Card> c
	 */
	public void addAll(ArrayList<Card> c) {
		if (c != null) if (c.size() != 0) content.addAll(c);
	}
	
	/**
	 * returns a deep copy of the CardList
	 * @return CardList
	 */
	public CardList clone() {
		CardList cl = new CardList();
		ArrayList<Card> tmp = get();
		for (Card c : tmp) {
			cl.add(c.clone());
		}
		return cl;
	}
}
