package protocol.resources;

import java.io.Serializable;

public abstract class Trap implements Cloneable, Serializable {
	private static final long serialVersionUID = -2916581608506007043L;
	public String[] trigger;
	public int range;
	public abstract void execute(Board board, String[] location, Object player);
	public boolean checkForTrigger(String t) {
		for (String s : trigger) {
			if (s.equals(t)) return true;
		}
		return false;
	}
}
