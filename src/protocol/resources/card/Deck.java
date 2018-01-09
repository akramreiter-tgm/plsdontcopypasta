package protocol.resources.card;

import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Serializable, Cloneable {
	private static final long serialVersionUID = -1559936952251582694L;
	public ArrayList<Card> deck;
	
	public Deck() {
		deck = new ArrayList<>();
	}
	public Deck(ArrayList<Card> deck) {
		this.deck = deck;
	}
	/*
	 * draws the card from the top of the deck (top = lowest index)
	 */
	public Card draw() {
		if (deck.size() > 0) return deck.remove(0);
		return null;
	}
	
	/*
	 * puts a card from a deck on top of the deck (top = lowest index)
	 */
	public void addOnTop(int dIndex) {
		if (dIndex < deck.size()) {
			try {
				Card c = deck.remove(dIndex);
				if (c != null) deck.add(0,c);
			} catch (Exception e) {}
		}
	}
	
	public void addOnBottom(int dIndex) {
		if (dIndex < deck.size()) {
			try {
				Card c = deck.remove(dIndex);
				if (c != null) deck.add(c);
			} catch (Exception e) {}
		}
	}
	
	/*
	 * get() returns the entire deck
	 */
	public ArrayList<Card> get() {
		return deck;
	}
	
	/**
	 * adds a card to the deck, then shuffles it
	 * @see protocol.resources.card.Deck#shuffle()
	 */
	public void addToDeck(Card c) {
		deck.add(c);
		shuffle();
	}
	
	/**
	 * randomizes the order of the cards in the deck
	 */
	public void shuffle() {
		ArrayList<Card> ndeck = new ArrayList<>();
		while (deck.size() > 0) {
			ndeck.add(deck.remove((int)(Math.random() * deck.size())));
		}
		deck = ndeck;
	}
	
	/**
	 * returns the size of the deck (no fucking shit sherlock)
	 */
	public int getSize() {
		return deck.size();
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public Deck clone() {
		ArrayList<Card> tmp = get();
		Deck cl = new Deck();
		for (Card c : tmp) {
			cl.addToDeck(c.clone());
		}
		return cl;
	}
	
	/**
	 * returns the Card at the given index i
	 * @param (int) i
	 * @return Card
	 */
	public Card get(int i) {
		return deck.get(i);
	}
}
