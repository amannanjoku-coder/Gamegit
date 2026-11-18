package game;

public class Safe extends Item {
	private boolean open;
	private boolean close;
	
	public Safe() {
		super("safe", true);
		open = false;
		close = false;
	}
	@Override
	public void use() {
		if(open) {
			Game.print("The safe has been open already.");
			return;
		}
		open = true;
		Game.currentRoom.addItem(new Notebook());
		Game.print("You open the " + getName() + "and find a notebook.");
	}

}
