package game;

public class Safe extends Item {
	
	public Safe() {
		super("safe", true);
		opened = false;
	}
	@Override
	public void use() {
		if(opened) {
			Game.print("The sade has been open already.");
			return;
		}
		opened = true;
		Game.currentRoom.addItem(new Notebook());
		Game.print("You open the " + getName() + "and find a notebook.");
	}

}
