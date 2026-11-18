package game;

public class Notebook extends Item {
	public boolean read;
	
	public Notebook() {
		super("notebook");
		read = false;
	}
	@Override
	public void read() {
		if (read) {
			Game.print("You read the " + getName() + ",nothing valuable is in here.");
		}
		else {
			Game.inventory.add(new Item("password"));
			read = true;
			Game.print("You read the " + getName() + "and find the password.");
		}
	}

}