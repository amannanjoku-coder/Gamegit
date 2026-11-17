package game;

public class World {

	public static void main(String[] args) {
		Game.loadDescription("Room Descriptions.txt");
		Room currentRoom = buildWorld();
		System.out.println(currentRoom); 
		currentRoom = currentRoom.getExit(0); 
		System.out.println(currentRoom); 
		currentRoom = currentRoom.getExit(3); 
		System.out.println(currentRoom); 
		currentRoom = currentRoom.getExit(2); 
		System.out.println(currentRoom); 
		currentRoom = currentRoom.getExit(1);
		System.out.println(currentRoom); 
		currentRoom = currentRoom.getExit(5); 
		System.out.println(currentRoom); 
		currentRoom = currentRoom.getExit(4); 
		System.out.println(currentRoom); 
		
	}
	
	public static Room buildWorld() {
		Item officekey = new OfficeKey();
		Item tool = new Tool();
		Item musics = new Item("musicbox");
		Item flash = new Item("flashlight");
		Item knife = new Item("knife");
		Item orbs = new Item("mysteriousorb");
		Item safe = new Item("safe", true);
		Item couch = new Item("couch", true);
		Item bed = new Item ("bed", true);
		Item notebook = new Item ("Notebook");
		
		Room livingRoom = new Room("Living Room"); /*"Small living room with a couch and a coffee table.", tool, musics);*/
		Room office = new Room("Office");// "Go get work done.");
		Room attic = new Room("Attic"); // "More items you have almost never seen before.");
		Room garage = new Room("Garage"); // "Garage with old items you almost have never seen before.");
		Room kitchen = new Room("Kitchen"); // "Go and eat.", knife, flash, orbs);
		Room bedroom = new Room("Bedroom");// "Your room where you sleep.", officekey);
		
		livingRoom.addItem(tool);
		livingRoom.addItem(musics);
		livingRoom.addItem(couch);
		kitchen.addItem(knife);
		kitchen.addItem(flash);
		kitchen.addItem(orbs);
		bedroom.addItem(officekey);
		bedroom.addItem(bed);
		attic.addItem(safe);
		office.addItem(notebook);
		
		livingRoom.addExit(office, 1);
		livingRoom.addExit(bedroom, 2);
		livingRoom.addExit(kitchen, 0);
		
		office.addExit(livingRoom, 0);
		office.setLocked(true);
		
		bedroom.addExit(livingRoom,  3);
		bedroom.addExit(attic, 4);
		
		attic.addExit(bedroom, 5);
		attic.setLocked(true);
		
		kitchen.addExit(livingRoom, 1);
		kitchen.addExit(garage, 3);
		
		garage.addExit(kitchen, 2);
		garage.setLocked(true);
		return livingRoom;
		
		
	}
}