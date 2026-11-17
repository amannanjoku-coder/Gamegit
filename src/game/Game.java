package game;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game {
	public static HashMap<String, String> roomDescriptions = new HashMap<String, String>();
    public static Room currentRoom;// = World.buildWorld();
    public static ArrayList<Item> inventory = new ArrayList<Item>();
    
    public static void main(String[] args) {
    	loadDescription("Room Descriptions.txt");
    	currentRoom = World.buildWorld();
        runGame();
    }
    
    public static void loadDescription(String filename) {
    	try {
    		Scanner scanner = new Scanner(new File(filename));
    		while (scanner.hasNextLine()) {
    			String roomName = scanner.nextLine().trim();
    			if (roomName.isEmpty()) {
    				continue;
    			}
    			String description = " ";
    			while (scanner.hasNextLine()) {
    				String line = scanner.nextLine().trim();
    				if (line.isEmpty()) {
    					break;
    				}
    				if (!description.isEmpty()) {
    					description += "\n";
    				}
    				description += line;
    			}
    			roomDescriptions.put(roomName, description);
    		}
    		scanner.close();
    	} catch (FileNotFoundException e) {
    		print("COuld not find room description file: " + filename);
    	}
    }
    
    public static void print(Object obj) {
        System.out.println(obj);
    }

    public static void move(String direct) {
        int card = -1;

        if (direct.equals("east")) {
            card = 0;
        } else if (direct.equals("west")) {
            card = 1;
        } else if (direct.equals("north")) {
            card = 2;
        } else if (direct.equals("south")) {
            card = 3;
        } else if (direct.equals("up")) {
            card = 4;
        } else if (direct.equals("down")) {
            card = 5;
        }

        if (card == -1) {
            Game.print("Invalid direction.");
            return;
        }
        
        Room nxtRoom = currentRoom.getExit(card);
        if (nxtRoom == null) {
            Game.print("You can't go that way.");
        } else if (nxtRoom.getLocked()) {
        	Game.print("Door is locked");
        }else {
            currentRoom = nxtRoom;
        }
    }
    public static Room getCurrentRoom() {
    	return currentRoom;
    }
    public static void takeItem(String name) {
    	Item item = currentRoom.getItem(name);
    	if (item == null) {
    		Game.print("The " + name + " is not here");
    	} else if (item.Takes())
    	{
    		return;
    	}
    	else {
    		currentRoom.removeItem(name);
    		inventory.add(item);
    		Game.print("You take the " + item + " and carry it with you");
    	}
    }
    public static void useItem(String name) {
    	for (Item i : inventory) {
    		if (i.getName().equalsIgnoreCase(name)) {
    			i.use();
    			return;
    		}
    	}
    	Game.print("You don't have a " + name );
    	}
  
    public static void showInventory() {
    	if (inventory.isEmpty()) {
    		Game.print("You are carrying nothing.");
    	} else {
    		Game.print("You are carrying:");
    		for (Item i : inventory) {
    			Game.print(i);
    	}
    }
}
    public static void saveGame() {
    	try {
    		 File saveFile = new File ("save0.sav");
    		    saveFile.createNewFile();
    		    ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(saveFile));
    		    outStream.writeObject(currentRoom);
    		    outStream.writeObject(inventory);
    		    outStream.writeObject(Room.rooms);
    		    outStream.close();
    		    Game.print("Game saved.");
    	} catch (IOException e) {
    		Game.print("Could not save game.");
    	}
    }
  
    	public static void loadGame() {
    		try {
    			File saveFile = new File ("save0.sav");
    			ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(saveFile));
    			currentRoom = (Room) inStream.readObject();
    			inventory = (ArrayList<Item>) inStream.readObject();
    			Room.rooms = (ArrayList<Room>) inStream.readObject();
    			inStream.close();
    			Game.print("Game loaded succesfully.");
    		} catch (IOException | ClassNotFoundException e) {
    			Game.print("Could not load the game.");
    		}
    	}
    public static void runGame() {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            Game.print("\nYou are currently in: " + currentRoom);
            System.out.print("Enter command: ");
            String command = input.nextLine().trim().toLowerCase();
            String[] words = command.split(" ");
            String com = words[0];

            switch (com) {
                case "north":
                case "south":
                case "east":
                case "west":
                case "up":
                case "down":
                    move(com);
                    break;
                case "take":
                    if (words.length < 2) {
                        Game.print("Take what?");
                    } else {
                        takeItem(words[1]);
                    }
                    break;
                case "use":
                	if (words.length < 2) {
                		Game.print("Use what?");
                	} else {
                		useItem(words[words.length - 1]);
                	}
                	break;
                case "i":
                	showInventory();
                	break;
                case "save":
                	saveGame();
                	break;
                case "load":
                	loadGame();
                	break;
                case "exit":
                	Game.print("Thanks for playing");
                	running = false;
                	break;
                default:
                	Game.print("Invalid commands.");
                	break;
            }
        }
        input.close();
    }
}