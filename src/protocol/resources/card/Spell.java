package protocol.resources.card;

public abstract class Spell extends Card {
	private static final long serialVersionUID = 2640183214721814100L;

	public Spell(String own) {
		super(own);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCType() {
		return "spell";
	}
}
