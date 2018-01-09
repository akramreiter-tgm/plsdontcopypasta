package protocol.resources.card;

public abstract class Hero extends Creature {
	private static final long serialVersionUID = 3258350196969582719L;
	
	public Hero(String owner) {
		super(owner);
		hero = true;
	}

	@Override
	public String getCType() {
		return "hero";
	}
}
