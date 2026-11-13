package game;

public class NPC {
	private String name;
	
	public NPC(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public void say(String says) {
		System.out.println(says);
		
	}
	
	public void talk() {
		System.out.println("You can't talk to Bobo.");
	}
	
	public int getResponse() {
		
	}
	Game.print(0:"Got any gum?"); 
	Game.print(1: "You can't help me, I'm here to help you."); 
		Game.print(2: "Can I have 50 cents?")
}
