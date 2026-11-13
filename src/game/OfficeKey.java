package game;

public class OfficeKey extends Item {
	public OfficeKey() {
		super("officekey");
	}
	@Override
	public void use() {
		if (Game.currentRoom().getName().equals("Living Room")) {
			Room office = Game.rooms.get("Office");
			if (office.isLocked() {
				office.setLocked(false);
				Game.print
			}
		}
	}

}
