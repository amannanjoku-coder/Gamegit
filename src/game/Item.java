package game;

import java.io.Serializable;

public class Item implements Serializable {

	private String name;
	private boolean heavy;
	
	public Item (String n) {
		this(n, false);
	}
	
	public Item (String n, boolean h) {
		name = n;
		heavy = h;
	}
	
	public String getName() {
	return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String toString() {
		return name;
	}
	
	public boolean isHeavy() {
		return heavy;
	}
	
	public void setHeavy(boolean h) {
		heavy = h;
	}
	
	public boolean Takes() {
		if (heavy) {
			Game.print("You can't lift the " + getName() + "!");
			return true;
		}
		else
			return false;
	}
	
	public void use() {
		Game.print("You can't use the " + getName() + ".");
	}
}	