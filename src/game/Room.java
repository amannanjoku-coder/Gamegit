package game;

import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList; 

public class Room implements Serializable {
	public static ArrayList<Room> rooms = new ArrayList<>();
	private String name;
	private Room[] room;
	private HashMap<String, Item> items;
	private boolean locked;
	
public Room(String n) {
		name = n;
		room = new Room[6];
		items = new HashMap<>();
		rooms.add(this);
		locked = false;
	}
	
	public String getName() {
	return name;
	}
	
	public String getDescription() {
	return Game.roomDescriptions.get(name); 
	}
	
	public Item getItem(String itemName) {
		if (items.containsKey(itemName)) {
			return items.get(itemName);
		} else { 
			return null;
		}
				
	}
	public void setDescr(String d) {
		Game.roomDescriptions.put(name, d);
	}
	
	public void setName(String n) {
		name = n;
	}
	public String toString() {
		String s = name + "\n" + Game.roomDescriptions.get(name);
		return s;
	
	}
	 public void addExit(Room r, int idx) {
		 room[idx] = r;
	 }
	 
	 public Room getExit(int ex) {
		 return room[ex];
	 }
	 
	 public void addItem(Item item) {
			items.put(item.getName(), item);
		}
	public Item removeItem(String itemName) {
		if (items.containsKey(itemName)) {
			return items.remove(itemName);
		}else{
			return null;
		}
	}
	public boolean getLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}