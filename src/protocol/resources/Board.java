package protocol.resources;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.soap.SAAJResult;

import deckBuilding.hero.BasicHero;
import protocol.actions.Activation;
import protocol.actions.Evolve;
import protocol.actions.Movement;
import protocol.actions.Placement;
import protocol.actions.PlayFromHand;
import protocol.resources.card.*;

@SuppressWarnings("unused")
public class Board implements Serializable, Cloneable {
	private static final long serialVersionUID = -8762993582509086157L;
	public static final int groundcost = 1;
	public static final int rescost = 4;
	private boolean isFirstTurn = true;
	
	public Movement movement;
	
	public Placement placement;
	
	public PlayFromHand fromhand;
	
	public Activation activation;
	
	public Evolve evolve;
	
	public String[] playerNames = new String[2];
	
	public Player[] players = new Player[2];
	
	public HashMap<String,Deck> deck;
	
	public HashMap<String,CardList> sidedeck;
	
	public HashMap<String,CardList> hand;
	
	public HashMap<String,CardList> grave;
	
	public HashMap<String,CardList> removed;
	
	/**
	 * resources includes the following numeric values:
	 * [0]energy: required to play cards (value is lost), gained at the start of a turn
	 * [1]energyGain: amount of energy gained per turn
	 * [2]altEnergy: required to "evolve" creatures (value is lost), TODO protocol.actions.Evolution, gained by aesources & destroying enemy creatures
	 * [3]-[6]res1-res4: required to play some cards (value isn't lost), gained by placing res1-res4 Ground
	 */
	public HashMap<String,int[]> resources;
	
	/**
	 * content maps the name of all tilenames
	 * to their related data structure
	 */
	private HashMap<String,Tile> content;
	
	/**
	 * geo represents the location of each tile's name,
	 * allowing other functions to get adjecent tiles or tiles within the same lane
	 */
	private String[][] geo;
	
	
	public Board(Player[] player) {
		try {
			playerNames[0] = player[0].pname;
			playerNames[1] = player[1].pname;
			players[0] = player[0];
			players[1] = player[1];
		}catch (Exception e) {}
		content = new HashMap<>();
		geo = new String[9][9];
		setup();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (geo[i][j] == null) {
					System.out.print("__, ");
					continue;
				}
				System.out.print(geo[i][j] + ", ");
			}
			System.out.println();
		}
		movement = new Movement(this);
		placement = new Placement(this);
		fromhand = new PlayFromHand(this);
		activation = new Activation(this);
		evolve = new Evolve(this);
		grave = new HashMap<>();
		hand = new HashMap<>();
		removed = new HashMap<>();
		resources = new HashMap<>();
		deck = new HashMap<>();
		sidedeck = new HashMap<>();
		grave.put(playerNames[0], new CardList());
		grave.put(playerNames[1], new CardList());
		hand.put(playerNames[0], new CardList());
		hand.put(playerNames[1], new CardList());
		removed.put(playerNames[0], new CardList());
		removed.put(playerNames[1], new CardList());
		resources.put(playerNames[0], new int[7]);
		resources.get(playerNames[0])[1] = 5;
		resources.put(playerNames[1], new int[7]);
		resources.get(playerNames[1])[1] = 5;
		deck.put(playerNames[0], new Deck());
		deck.put(playerNames[1], new Deck());
		sidedeck.put(playerNames[0], new CardList());
		sidedeck.put(playerNames[1], new CardList());
		System.out.println(getHeroLoc(playerNames[0]) + "," + getCreature("E1").getCType());
	}
	
	private void setup() {
		for (int i = -4; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				if (i < 0) {
					try {
						geo[i+j][j] = (char)('A' + i + 4) + "" + (j+i+1);
						content.put((char)('A' + i + 4) + "" + (j+i+1), new Tile());
					}catch (Exception e) {}
				} else {
					try {
						geo[i+j][j] = (char)('A' + i + 4) + "" + (j+1);
						content.put((char)('A' + i + 4) + "" + (j+1), new Tile());
					}catch (Exception e) {}
				}
			}
		}
		
		content.get("E1").getGround().gType = 1;
		content.get("E2").getGround().gType = 1;
		content.get("F1").getGround().gType = 1;
		content.get("D1").getGround().gType = 1;
		content.get("E1").getGround().owner = playerNames[0];
		content.get("E2").getGround().owner = playerNames[0];
		content.get("F1").getGround().owner = playerNames[0];
		content.get("D1").getGround().owner = playerNames[0];
		content.get("E1").setCreature(new BasicHero(playerNames[0]));
		content.get("E9").getGround().gType = 1;
		content.get("E8").getGround().gType = 1;
		content.get("D8").getGround().gType = 1;
		content.get("F8").getGround().gType = 1;
		content.get("E9").getGround().owner = playerNames[1];
		content.get("E8").getGround().owner = playerNames[1];
		content.get("D8").getGround().owner = playerNames[1];
		content.get("F8").getGround().owner = playerNames[1];
		content.get("E9").setCreature(new BasicHero(playerNames[1]));
		getGround("A1").aeSourceCount = 3;
		getGround("A3").aeSourceCount = 3;
		getGround("A5").aeSourceCount = 3;
		getGround("C3").aeSourceCount = 3;
		getGround("C5").aeSourceCount = 3;
		getGround("G3").aeSourceCount = 3;
		getGround("G5").aeSourceCount = 3;
		getGround("I1").aeSourceCount = 3;
		getGround("I3").aeSourceCount = 3;
		getGround("I5").aeSourceCount = 3;
	}
	
	/**
	 * getAdjecent(String) returns the names of all tiles which are directly adjecent to the origin tile
	 * @param (String) origin
	 * @return String[]
	 */
	public String[] getAdjecent(String origin) {
		Point p = getLocation(origin);
		ArrayList<String> tmp = new ArrayList<>();
		try {
			if (geo[p.x + 1][p.y] != null)
			tmp.add(geo[p.x + 1][p.y]);
		}catch (Exception e) {}
		try {
			if (geo[p.x][p.y + 1] != null)
			tmp.add(geo[p.x][p.y + 1]);
		}catch (Exception e) {}
		try {
			if (geo[p.x - 1][p.y] != null)
			tmp.add(geo[p.x - 1][p.y]);
		}catch (Exception e) {}
		try {
			if (geo[p.x][p.y - 1] != null)
			tmp.add(geo[p.x][p.y - 1]);
		}catch (Exception e) {}
		try {
			if (geo[p.x + 1][p.y + 1] != null)
			tmp.add(geo[p.x + 1][p.y + 1]);
		}catch (Exception e) {}
		try {
			if (geo[p.x - 1][p.y - 1] != null)
			tmp.add(geo[p.x - 1][p.y - 1]);
		}catch (Exception e) {}
		return tmp.toArray(new String[0]);
	}
	
	/**
	 * getAdjecent(String,int) returns the names of all tiles within the given RANGE of the origin tile
	 * @param (String) origin
	 * @param (int) range
	 * @return String[]
	 */
	public String[] getAdjecent(String origin, int range) {
		if (range >= 8) return content.keySet().toArray(new String[0]);
		Point p = getLocation(origin);
		HashSet<String> tmp = new HashSet<>();
		tmp.add(origin);
		for (int i = 0; i < range; i++) {
			@SuppressWarnings("unchecked")
			HashSet<String> clone = (HashSet<String>) tmp.clone();
			for (String s1 : clone) {
				p = getLocation(s1);
				try {
					if (geo[p.x + 1][p.y] != null)
					tmp.add(geo[p.x + 1][p.y]);
				}catch (Exception e) {}
				try {
					if (geo[p.x][p.y + 1] != null)
					tmp.add(geo[p.x][p.y + 1]);
				}catch (Exception e) {}
				try {
					if (geo[p.x - 1][p.y] != null)
					tmp.add(geo[p.x - 1][p.y]);
				}catch (Exception e) {}
				try {
					if (geo[p.x][p.y - 1] != null)
					tmp.add(geo[p.x][p.y - 1]);
				}catch (Exception e) {}
				try {
					if (geo[p.x + 1][p.y + 1] != null)
					tmp.add(geo[p.x + 1][p.y + 1]);
				}catch (Exception e) {}
				try {
					if (geo[p.x - 1][p.y - 1] != null)
					tmp.add(geo[p.x - 1][p.y - 1]);
				}catch (Exception e) {}
			}
		}
		tmp.remove(origin);
		return tmp.toArray(new String[0]);
	}
	
	/**
	 * get(String) returns the Tile mapped to the String
	 * @return Tile
	 */
	public Tile get(String target) {
		return content.get(target);
	}
	
	/**
	 * get(String) returns the Ground of the Tile mapped to the String
	 * @return Ground
	 */
	public Ground getGround(String target) {
		return content.get(target).getGround();
	}
	
	/**
	 * get(String) returns the Creature of the Tile mapped to the String
	 * @return Creature
	 */
	public Creature getCreature(String target) {
		return content.get(target).getCreature();
	}
	
	/**
	 * getLanes(String) returns the names of all tiles which are reachable
	 * by following a straight direction
	 * @param (String) origin
	 * @return String[]
	 */
	public String[] getLanes(String origin) {
		Point p = getLocation(origin);
		ArrayList<String> tmp = new ArrayList<>();
		try {
			int i = 1;
			while (geo[p.x + i][p.y] != null) {tmp.add(geo[p.x + i][p.y]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while (geo[p.x][p.y + i] != null) {tmp.add(geo[p.x][p.y + i]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while (geo[p.x - i][p.y] != null) {tmp.add(geo[p.x - i][p.y]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while (geo[p.x][p.y - i] != null) {tmp.add(geo[p.x][p.y - i]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while (geo[p.x + i][p.y + i] != null) {tmp.add(geo[p.x + i][p.y + i]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while (geo[p.x - i][p.y - i] != null) {tmp.add(geo[p.x - i][p.y - i]);i++;}
		}catch (Exception e) {}
		return tmp.toArray(new String[0]);
	}
	
	/**
	 * getLanes(String,int) returns the names of all tiles which are reachable within the 
	 * LIMIT of steps by following a straight direction
	 * @param (String) origin
	 * @param (int) limit
	 * @return String[]
	 */
	public String[] getLanes(String origin, int limit) {
		Point p = getLocation(origin);
		ArrayList<String> tmp = new ArrayList<>();
		try {
			int i = 1;
			while ((geo[p.x + i][p.y] != null)&&(i <= limit)) {tmp.add(geo[p.x + i][p.y]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x][p.y + i] != null)&&(i <= limit)) {tmp.add(geo[p.x][p.y + i]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x - i][p.y] != null)&&(i <= limit)) {tmp.add(geo[p.x - i][p.y]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x][p.y - i] != null)&&(i <= limit)) {tmp.add(geo[p.x][p.y - i]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x + i][p.y + i] != null)&&(i <= limit)) {tmp.add(geo[p.x + i][p.y + i]);i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x - i][p.y - i] != null)&&(i <= limit)) {tmp.add(geo[p.x - i][p.y - i]);i++;}
		}catch (Exception e) {}
		return tmp.toArray(new String[0]);
	}
	
	/**
	 * gets all TileNames reachable 
	 * (move or attack) by a non-flying 
	 * creature within a given range
	 * @param (String) origin
	 * @param (int) limit
	 * @return String[]
	 */
	public String[] getLanesGround(String origin, int limit) {
		Point p = getLocation(origin);
		HashSet<String> tmp = new HashSet<>();
		try {
			int i = 1;
			while ((geo[p.x + i][p.y] != null)&&(i <= limit)) {tmp.add(geo[p.x + i][p.y]); if ((getCreature(geo[p.x + i][p.y]) != null)||(getGround(geo[p.x + i][p.y]).gType == 0)) {tmp.add(geo[p.x + i][p.y]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x][p.y + i] != null)&&(i <= limit)) {tmp.add(geo[p.x][p.y + i]); if ((getCreature(geo[p.x][p.y + i]) != null)||(getGround(geo[p.x][p.y + i]).gType == 0)) {tmp.add(geo[p.x][p.y + i]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x - i][p.y] != null)&&(i <= limit)) {tmp.add(geo[p.x - i][p.y]); if ((getCreature(geo[p.x - i][p.y]) != null)||(getGround(geo[p.x - i][p.y]).gType == 0)) {tmp.add(geo[p.x + i][p.y]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x][p.y - i] != null)&&(i <= limit)) {tmp.add(geo[p.x][p.y - i]); if ((getCreature(geo[p.x][p.y - i]) != null)||(getGround(geo[p.x][p.y - i]).gType == 0)) {tmp.add(geo[p.x][p.y - i]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x + i][p.y + i] != null)&&(i <= limit)) {tmp.add(geo[p.x + i][p.y + i]); if ((getCreature(geo[p.x + i][p.y + i]) != null)||(getGround(geo[p.x + i][p.y + i]).gType == 0)) {tmp.add(geo[p.x + i][p.y + i]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x - i][p.y - i] != null)&&(i <= limit)) {tmp.add(geo[p.x - i][p.y - i]); if ((getCreature(geo[p.x - i][p.y - i]) != null)||(getGround(geo[p.x - i][p.y - i]).gType == 0)) {tmp.add(geo[p.x - i][p.y - i]);break;}i++;}
		}catch (Exception e) {}
		return tmp.toArray(new String[0]);
	}
	
	/**
	 * gets all TileNames reachable 
	 * (move or attack) by a flying creature
	 * within a given range
	 * @param (String) origin
	 * @param (int) limit
	 * @return String[]
	 */
	public String[] getLanesFly(String origin, int limit) {
		//System.out.print("limit: " + limit + ", ");
		Point p = getLocation(origin);
		ArrayList<String> tmp = new ArrayList<>();
		try {
			int i = 1;
			while ((geo[p.x + i][p.y] != null)&&(i <= limit)) {tmp.add(geo[p.x + i][p.y]);if ((getCreature(geo[p.x + i][p.y]) != null)) {tmp.add(geo[p.x + i][p.y]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x][p.y + i] != null)&&(i <= limit)) {tmp.add(geo[p.x][p.y + i]); if ((getCreature(geo[p.x][p.y + i]) != null)) {tmp.add(geo[p.x][p.y + i]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x - i][p.y] != null)&&(i <= limit)) {tmp.add(geo[p.x - i][p.y]); if ((getCreature(geo[p.x - i][p.y]) != null)) {tmp.add(geo[p.x + i][p.y]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x][p.y - i] != null)&&(i <= limit)) {tmp.add(geo[p.x][p.y - i]); if ((getCreature(geo[p.x][p.y - i]) != null)) {tmp.add(geo[p.x][p.y - i]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x + i][p.y + i] != null)&&(i <= limit)) {tmp.add(geo[p.x + i][p.y + i]); if ((getCreature(geo[p.x + i][p.y + i]) != null)) {tmp.add(geo[p.x + i][p.y + i]);break;}i++;}
		}catch (Exception e) {}
		try {
			int i = 1;
			while ((geo[p.x - i][p.y - i] != null)&&(i <= limit)) {tmp.add(geo[p.x - i][p.y - i]); if ((getCreature(geo[p.x - i][p.y - i]) != null)) {tmp.add(geo[p.x - i][p.y - i]);break;}i++;}
		}catch (Exception e) {}
		return tmp.toArray(new String[0]);
	}
	/*
	 * gets the geo-location of a tile by it's name
	 * only used internally
	 */
	private Point getLocation(String origin) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				try {
					if (geo[i][j].equals(origin)) return new Point(i,j);
				}catch (Exception e) {}
			}
		}
		return new Point(-2,-2);
	}
	
	/**
	 * clearBoard() removes all creatures which are considered dead (cHealth >= 0) 
	 * from the board and adds them to their respective graves
	 * additionally it triggers DEAD creatures "death"-Effects
	 * and "creaturedeath" for each removed creature
	 * @throws "creaturedeath"
	 */
	public void clearBoard() {
		HashMap<String,ArrayList<String>> deathcount = new HashMap<>();
		HashMap<String,Creature> crs = new HashMap<>();
		deathcount.put(playerNames[0], new ArrayList<>());
		deathcount.put(playerNames[1], new ArrayList<>());
		for (String s : content.keySet()) {
			Creature c = content.get(s).getCreature();
			if (c != null) {
				if(c.isDead()) {
					if (c.isEffectTriggered("death"))
						crs.put(s,c);
					grave.get(c.owner).add(c);
					content.get(s).setCreature(null);
					deathcount.get(c.owner).add(s);
				}
			}
		}
		for (String s : crs.keySet()) {
			Creature c = crs.get(s);
			try {
				c.execute("death", this, new String[] {s}, getPlayer(c.owner));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		for (String s : deathcount.keySet()) for (String s2 : deathcount.get(s)) {
			triggerExecutableEffects(getPlayer(s), "creaturedeath", new String[] {s2});
			
		}
	}
	
	/**
	 * clones the entire Board
	 * @return Board
	 */
	public Board clone() {
		Board b = new Board(new Player[] {players[0], players[1]});
		for (String s : content.keySet()) {
			b.content.put(s, this.content.get(s).clone());
		}
		for (String s : grave.keySet()) {
			b.grave.put(s, grave.get(s).clone());
		}
		for (String s : deck.keySet()) {
			b.deck.put(s, deck.get(s).clone());
		}
		for (String s : hand.keySet()) {
			b.hand.put(s, hand.get(s).clone());
		}
		for (String s : removed.keySet()) {
			b.hand.put(s, removed.get(s).clone());
		}
		return b;
	}
	
	/**
	 * gets the combined vision range of
	 * all tiles and creatures owned by a
	 * specified player's name
	 * @param (String) player
	 * @return String[]
	 */
	public String[] getVisionRange(String player) {
		HashSet<String> tmp = new HashSet<>();
		for (String s : content.keySet()) {
			Tile t = get(s);
			Ground g = t.getGround();
			if (g != null)
			if (g.owner.equals(player)) {
				tmp.addAll(Arrays.asList(getAdjecent(s)));
				tmp.add(s);
			}
			Creature c = t.getCreature();
			if (c != null)
			if (c.owner.equals(player)) {
				tmp.addAll(Arrays.asList(getAdjecent(s,c.vRange)));
				tmp.add(s);
			}
		}
		return tmp.toArray(new String[0]);
	}
	
	/**
	 * returns all TileNames on the Board
	 * in an ordered Array
	 * @return String[]
	 */
	public String[] getOrderedFullBoard() {
		String[] tmp = getAdjecent("E1", 8);
		Arrays.sort(tmp);
		return tmp;
	}
	
	/**
	 * returns the Player-Object 
	 * associated with a pname
	 * @param (String) pname
	 * @return Player
	 */
	public Player getPlayer(String pname) {
		for (Player p : players) {
			if (p.pname.equals(pname)) return p;
		}
		return null;
	}
	
	/**
	 * triggers the effects of all creatures responding to the given trigger
	 * PSA: even if no locations are given, give this function an empty String[] instead of null
	 */
	public void triggerExecutableEffects(Player p, String triggername, String[] locations) {
		for (String s : getOrderedFullBoard()) {
			Creature c = getCreature(s);
			if (c != null) {
				String[] loc = new String[locations.length + 1];
				loc[0] = s;
				for (int i = 0; i < locations.length; i++) loc[i + 1] = locations[i];
				if (c.isEffectTriggered(triggername)) {
					System.out.println(triggername + " triggered");
					try {
						c.execute(triggername, this, loc, p);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (String s : playerNames) for (Card c : deck.get(s).get()) {
			if (c != null) {
				String ctrigger = triggername + "deck";
				if (c.isEffectTriggered(ctrigger))
					try {
						c.execute(ctrigger, this, locations, p);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		for (String s : playerNames) for (Card c : grave.get(s).get()) {
			if (c != null) {
				String ctrigger = triggername + "grave";
				if (c.isEffectTriggered(ctrigger))
					try {
						c.execute(ctrigger, this, locations, p);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		for (String s : playerNames) for (Card c : removed.get(s).get()) {
			if (c != null) {
				String ctrigger = triggername + "removed";
				if (c.isEffectTriggered(ctrigger))
					try {
						c.execute(ctrigger, this, locations, p);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		for (String s : playerNames) for (Card c : hand.get(s).get()) {
			if (c != null) {
				String ctrigger = triggername + "hand";
				if (c.isEffectTriggered(ctrigger))
					try {
						c.execute(ctrigger, this, locations, p);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
	
	/**
	 * resolves everything associated
	 * with the start of a turn:
	 * - gain energy
	 * - draw a card
	 * - trigger turnstart effects
	 * @param (Player) p
	 * @throws "turnstart"
	 */
	public void turnStart(Player p) {
		if (isFirstTurn) {
			resources.get(p.pname)[0] += 3;
			isFirstTurn = false;
		} else {
			resources.get(p.pname)[0] += resources.get(p.pname)[1];
		}
		triggerExecutableEffects(p, "turnstart", new String[0]);
		for (String s : getOrderedFullBoard()) {
			Creature c = getCreature(s);
			if (c != null) {
				if (c.owner.equals(p.pname)) {
					c.turnStart();
					Ground g = getGround(s);
					if ((g.aeSourceCount > 0) && (g.gType > 0)) {
						g.aeSourceCount -= 1;
						resources.get(p.pname)[2]++;
						if (g.aeSourceCount == 0) {
							resources.get(p.pname)[1]++;
						}
					}
				}
			}
		}
		CardList h = hand.get(p.pname);
		
		if (h.size() > 9) {
			deck.get(p.pname).draw();
		} else {
			h.add(deck.get(p.pname).draw());
		}
	}
	
	/**
	 * returns the current location of
	 * the hero owned by the given pname
	 * @param (String) pname
	 * @return String
	 */
	public String getHeroLoc(String pname) {
		for (String s : getAdjecent("E1",8)) {
			Creature c = getCreature(s);
			if (c != null) {
				//System.out.println(s + ", " + c.hero + ", " + c.owner + ", " + pname);
				if (c.hero) if (c.owner.equals(pname)) return s;
			}
		}
		return "";
	}
}
