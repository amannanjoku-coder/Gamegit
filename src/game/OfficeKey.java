package game;

public class OfficeKey extends Item {
	
	public OfficeKey() {
		super("officekey");
	}
	@Override
	public void use() {
		if (Game.getCurrentRoom().getName().equals("Living Room")) {
			if (Game.getCurrentRoom().getExit(1).getLocked()) {
				Game.print("The door is already unlocked.");
			}else {
				Game.getCurrentRoom().getExit(1).setLocked(false);
				Game.print("You open the Office door");
			}
		}
		else
			Game.print("You can't use the Office key for this.");
			}
	}