package communication;

import java.io.Serializable;

/**
 * Message Received by Players (and their JFXApplications)
 * used to highlight certain objects (content -> refers to a list of Board-locations or indexes)
 * in a certain location (declared by description var)
 * 
 * if description is "selection", the given String[] contains cardnames
 * @author alexk
 */
public class CommMsg implements Serializable, Cloneable {
	private static final long serialVersionUID = -6733998219994098392L;
	public String description;
	public Object content; //String[] or int[]
	public CommMsg() {
		description = "";
		content = null;
	}
	public CommMsg(String desc, Object cont) {
		description = desc;
		content = cont;
	}
}
