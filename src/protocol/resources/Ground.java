package protocol.resources;

import java.io.Serializable;

public class Ground implements Serializable, Cloneable {
	private static final long serialVersionUID = 2443110111058176223L;
	public int gType, aeSourceCount;
	public String owner;
}
