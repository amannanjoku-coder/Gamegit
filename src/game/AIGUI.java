package game;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI for the house text adventure: Widener blue, white, and gold.
 *
 * Layout:
 *   - Top:    "1821" in blue, the room name, and an inventory strip
 *   - Center: large scrolling story area (blue text on white)
 *   - Bottom: the command input field (Up/Down arrows recall earlier commands)
 *
 * To connect your game, edit handleCommand(...) and call
 * setRoomName(...), appendText(...), and setInventory(...) as needed.
 */
public class AIGUI extends JFrame {

    // Widener University brand colors (from widener.edu brand guide)
    private static final Color WHITE      = Color.WHITE;
    private static final Color BLUE       = new Color(0, 114, 188);    // WU Blue #0072BC
    private static final Color MID_BLUE   = new Color(123, 175, 222);  // WU Light Blue #7BAFDE
    private static final Color LIGHT_BLUE = new Color(232, 242, 250);  // pale tint for panels
    private static final Color DARK_BLUE  = new Color(9, 31, 64);      // WU Dark Blue (story text)
    private static final Color GOLD       = new Color(255, 229, 0);    // WU Yellow #FFE500 (accent)
    private static final Font  MAIN_FONT  = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    private static final Font  BOLD_FONT  = new Font(Font.SANS_SERIF, Font.BOLD, 16);

    private final JLabel roomLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel inventoryLabel = new JLabel();
    private final JTextPane storyPane = new JTextPane();
    private final JTextField inputField = new JTextField();

    // Command history (Up/Down arrows)
    private final List<String> history = new ArrayList<>();
    private int historyIndex = 0;

    // ----- Demo house world (mirrors World.buildWorld in the house lab) -----
    // Direction order matches Game.move: 0=east 1=west 2=north 3=south 4=up 5=down
    private static final String[] DIRS = { "east", "west", "north", "south", "up", "down" };
    private static final String[] ROOM_NAMES =
            { "Living Room", "Office", "Attic", "Garage", "Kitchen", "Bedroom" };
    private static final String[] ROOM_DESCS = {
            // Living Room
            "The small living room contains a couch, coffee table, and two armchairs. Pictures of various cousins adorn the walls along with two of your aunt's wretched attempts at oil painting. You can't help but notice that the carpet could use a good vacuuming.\n"
                    + "Doorways lead west to your father's office, north to your bedroom, and east to the kitchen.",
            // Office
            "Where you spend your time doing work. You wake up in the morning after making a shake, smoothie, or cup of coffee and look over what you have to do for the day.\n"
                    + "The office is west from the living room.",
            // Attic
            "a room at the top of your house that is quite literally filled with a bunch random things you have almost never seen before. To your right you see a couple of toys that you haven't seen in years. To your left a box of roirs that your father doddmoy have took.\n"
                    + "The only way in and out of the attic is from the entry way in the bedroom.",
            // Garage
            "Filled with a more things that you haven't seen in a long time along with a bunch of tools, and some freeweights which clearly you haven't been using lately. And obviously your car is in here as well.\n"
                    + "The entrance to the garage is from the kitchen.",
            // Kitchen
            "Where you will be getting your daily nutrients. This kitchen got remodeled a couple years ago along with some new appliances and was made with a little bit more space. You can now sit in the kitchen and stay and big enough to have a conversation with your familia.\n"
                    + "There are doorways leading south to the garage and west to the living room.",
            // Bedroom
            "This is your domain and has been since your family moved here when you were six. A nightstand with a Superman lamp sits next to your twin bed. Posters of various anime characters cover one wall. A pile of laundry, slowly but surely attaining sentience, occupies the far corner.\n"
                    + "The door to the attic is in the ceiling above the pile of clothes. You can return south to the living room." };
    // EXITS[room][direction] = index of the room you reach, or -1 for no exit
    private static final int[][] EXITS = {
            {  4,  1,  5, -1, -1, -1 },   // Living Room
            {  0, -1, -1, -1, -1, -1 },   // Office
            { -1, -1, -1, -1, -1,  5 },   // Attic
            { -1, -1,  4, -1, -1, -1 },   // Garage
            { -1,  0, -1,  3, -1, -1 },   // Kitchen
            { -1, -1, -1,  0,  2, -1 } }; // Bedroom
    private final List<List<String>> roomItems = new ArrayList<>();
    private final List<String> inventory = new ArrayList<>();
    private int currentRoom = 0;

    public AIGUI() {
        super("Text Adventure - 1821");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 600);
        setMinimumSize(new Dimension(650, 450));
        setLocationRelativeTo(null);

        getContentPane().setBackground(WHITE);
        setLayout(new BorderLayout(0, 8));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 12, 10, 12));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildStoryArea(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        // Starter content: the house from the lab
        for (int r = 0; r < ROOM_NAMES.length; r++) {
            roomItems.add(new ArrayList<>());
        }
        roomItems.get(0).add("tool");
        roomItems.get(0).add("musicbox");
        roomItems.get(4).add("knife");
        roomItems.get(4).add("flashlight");
        roomItems.get(4).add("orb");
        roomItems.get(5).add("officekey");

        appendText("Welcome to the house! Try: east, north, take knife, i, save, load.");
        showRoom();

        inputField.requestFocusInWindow();
    }

    // ------------------------------------------------------------------
    // GUI construction
    // ------------------------------------------------------------------

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 6));
        header.setBackground(WHITE);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(WHITE);
        titleRow.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 3, 0, GOLD),
                new EmptyBorder(0, 0, 4, 0)));

        JLabel year = new JLabel("1821");
        year.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        year.setForeground(BLUE);
        titleRow.add(year, BorderLayout.WEST);

        roomLabel.setFont(new Font(Font.SERIF, Font.BOLD, 22));
        roomLabel.setForeground(BLUE);
        titleRow.add(roomLabel, BorderLayout.CENTER);

        // invisible twin of the year label so the room name stays centered
        JLabel balance = new JLabel("1821");
        balance.setFont(year.getFont());
        balance.setForeground(WHITE);
        titleRow.add(balance, BorderLayout.EAST);

        header.add(titleRow, BorderLayout.NORTH);

        inventoryLabel.setFont(MAIN_FONT);
        inventoryLabel.setForeground(BLUE);
        inventoryLabel.setOpaque(true);
        inventoryLabel.setBackground(LIGHT_BLUE);
        inventoryLabel.setBorder(new CompoundBorder(
                new LineBorder(MID_BLUE, 1),
                new EmptyBorder(6, 10, 6, 10)));
        header.add(inventoryLabel, BorderLayout.SOUTH);

        return header;
    }

    private JComponent buildStoryArea() {
        storyPane.setEditable(false);
        storyPane.setBackground(WHITE);
        storyPane.setForeground(DARK_BLUE);
        storyPane.setFont(MAIN_FONT);
        storyPane.setMargin(new Insets(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(storyPane);
        scroll.setBorder(new LineBorder(MID_BLUE, 2));
        scroll.getViewport().setBackground(WHITE);
        return scroll;
    }

    private JComponent buildBottom() {
        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setBackground(WHITE);

        // Command input row
        JPanel inputRow = new JPanel(new BorderLayout(8, 0));
        inputRow.setBackground(WHITE);

        JLabel prompt = new JLabel("Command:");
        prompt.setFont(BOLD_FONT);
        prompt.setForeground(BLUE);
        inputRow.add(prompt, BorderLayout.WEST);

        inputField.setFont(MAIN_FONT);
        inputField.setForeground(BLUE);
        inputField.setBackground(LIGHT_BLUE);
        inputField.setCaretColor(BLUE);
        inputField.setBorder(new CompoundBorder(
                new LineBorder(GOLD, 2),
                new EmptyBorder(6, 8, 6, 8)));
        inputField.addActionListener(e -> {
            String text = inputField.getText().trim();
            inputField.setText("");
            if (!text.isEmpty()) {
                submitCommand(text);
            }
        });
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (history.isEmpty()) return;
                if (e.getKeyCode() == KeyEvent.VK_UP && historyIndex > 0) {
                    historyIndex--;
                    inputField.setText(history.get(historyIndex));
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (historyIndex < history.size() - 1) {
                        historyIndex++;
                        inputField.setText(history.get(historyIndex));
                    } else {
                        historyIndex = history.size();
                        inputField.setText("");
                    }
                }
            }
        });
        inputRow.add(inputField, BorderLayout.CENTER);

        JButton enter = new JButton("Enter");
        styleButton(enter);
        enter.addActionListener(e -> inputField.postActionEvent());
        inputRow.add(enter, BorderLayout.EAST);

        bottom.add(inputRow, BorderLayout.SOUTH);
        return bottom;
    }

    private void styleButton(JButton b) {
        b.setFont(BOLD_FONT);
        b.setBackground(BLUE);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 3, 0, GOLD),
                new EmptyBorder(8, 12, 5, 12)));
    }

    // ------------------------------------------------------------------
    // Public methods your game can call
    // ------------------------------------------------------------------

    /** Adds text to the story area. */
    public void appendText(String text) {
        appendStyled(text.endsWith("\n") ? text : text + "\n", DARK_BLUE, false);
    }

    /** Updates the room name at the top. */
    public void setRoomName(String name) {
        roomLabel.setText(name);
    }

    /** Updates the inventory strip at the top. */
    public void setInventory(String[] items) {
        if (items.length == 0) {
            inventoryLabel.setText("Inventory: (empty)");
        } else {
            inventoryLabel.setText("Inventory: " + String.join(", ", items));
        }
    }

    // ------------------------------------------------------------------
    // Command handling: connect your game here
    // ------------------------------------------------------------------

    private void submitCommand(String command) {
        history.add(command);
        historyIndex = history.size();
        appendStyled("\n> " + command + "\n", BLUE, true);
        handleCommand(command);
        inputField.requestFocusInWindow();
    }

    /** Shows the current room's name and description, and refreshes the inventory panel. */
    private void showRoom() {
        setRoomName(ROOM_NAMES[currentRoom]);
        appendText(ROOM_DESCS[currentRoom]);
        setInventory(inventory.toArray(new String[0]));
    }

    /**
     * Demo version of the house game, using the same commands as Game.java.
     * TODO: replace this with calls into your real Game class.
     */
    private void handleCommand(String command) {
        String[] words = command.toLowerCase().trim().split(" ");
        String com = words[0];

        int dir = -1;
        for (int d = 0; d < DIRS.length; d++) {
            if (DIRS[d].equals(com)) {
                dir = d;
            }
        }

        if (dir != -1) {
            int next = EXITS[currentRoom][dir];
            if (next == -1) {
                appendText("You can't go that way.");
            } else {
                currentRoom = next;
                showRoom();
            }
        } else if (com.equals("take")) {
            if (words.length < 2) {
                appendText("Take what?");
            } else if (roomItems.get(currentRoom).remove(words[1])) {
                inventory.add(words[1]);
                setInventory(inventory.toArray(new String[0]));
                appendText("You take the " + words[1] + " and carry it with you");
            } else {
                appendText("The " + words[1] + " is not here");
            }
        } else if (com.equals("i")) {
            if (inventory.isEmpty()) {
                appendText("You are carrying nothing.");
            } else {
                appendText("You are carrying:\n" + String.join("\n", inventory));
            }
        } else if (com.equals("save") || com.equals("load")) {
            appendText("(demo) '" + com + "' will work once this window is connected to your Game class.");
        } else {
            appendText("I don't understand \"" + command + "\".");
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private void appendStyled(String text, Color color, boolean bold) {
        StyledDocument doc = storyPane.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);
        StyleConstants.setBold(attrs, bold);
        try {
            doc.insertString(doc.getLength(), text, attrs);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        storyPane.setCaretPosition(doc.getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AIGUI().setVisible(true));
    }
}