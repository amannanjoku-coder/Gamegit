package game;

public class Notebook extends Item {
	public boolean passWord;
	
	public Notebook() {
		super("notebook");
		passWord = false;
	}
	@Override
	public void use() {
		if (Game.inventory.contains(this)) {
			Game.print("You read the " + getName() + ",nothing valuable is in here.");
		}
		else {
			Game.inventory.add(new Item("password"));
			passWord = true;
			Game.print("You read the " + getName() + "and find the password.");
		}
	}

}