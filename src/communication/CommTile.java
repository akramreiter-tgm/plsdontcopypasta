package communication;

import java.io.Serializable;

import protocol.resources.Board;
import protocol.resources.Ground;
import protocol.resources.Tile;

/**
 * Used to display a tile on the board
 * contains the original Ground and
 * a CommCard if a Creature is found at that location 
 */
public class CommTile implements Serializable, Cloneable {
	private static final long serialVersionUID = 8943677173601285976L;
	public Ground ground;
	public CommCard creature;
	
	public CommTile() {
		creature = null;
		ground = new Ground();
		ground.aeSourceCount = 0;
		ground.gType = -1;
	}
	
	public CommTile (Tile t, Board bd, String loc) {
		ground = t.getGround();
		if (t.getCreature() != null) {
			creature = new CommCard(t.getCreature(),bd,loc);
		}
	}
}
