package game;

public class Notebook extends Item {
	public String passWord;
	
	public Notebook(String p) {
		super("notebook");
		passWord = p;
	}
	@Override
	public void use() {
		if (Game.inventory.contains(this)) {
			Game.print("You read the " + getName() + "and find the password.");
			Game.inventory.add(new Item("password"));
			passWord = true;
		}
	}

}
