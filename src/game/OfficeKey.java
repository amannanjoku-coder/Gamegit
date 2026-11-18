package game;

public class OfficeKey extends Item {
	private boolean usedOkey;
	public OfficeKey() {
		super("officekey");
		usedOkey = false;
	}
	@Override
	public void use() {
		if (Game.getCurrentRoom().getName().equals("Living Room")) {
			if (usedOkey) {//(Game.getCurrentRoom().getExit(1).getLocked()) {
				Game.print("The door is already unlocked.");
			}else {
				Game.getCurrentRoom().getExit(1).setLocked(false);
				usedOkey = true;
				Game.print("You open the Office door");
			}
		}
		else
			Game.print("You can't use the office key for this.");
			}
}