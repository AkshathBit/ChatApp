


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    // GUI Components
    private JFrame frame;
    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton emojiButton;
    private JLabel heading;
    private JPanel footer;
    private JPopupMenu emojiPopup;
    private StyledDocument doc;

    // Constructor to initialize the client and GUI
    public ChatClient() {
        try {
            System.out.println("Sending Request to server...");
            socket = new Socket("127.0.0.1", 9999);  // Connect to server on port 9999
            System.out.println("Connection established.");

            // Initializing input and output streams
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // 'true' for auto-flush

            // Setup the GUI
            createGUI();

            // Start reading from server
            startReading();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to create the GUI
    private void createGUI() {
        // Frame initialization
        frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());

        // Heading of the chat window
        heading = new JLabel("Client Chat", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 20));
        heading.setOpaque(true);
        heading.setBackground(new Color(75, 0, 130)); // Dark purple color
        heading.setForeground(Color.WHITE);
        heading.setPreferredSize(new Dimension(100, 50));
        frame.add(heading, BorderLayout.NORTH);

        // Chat area (to display chat messages)
        chatArea = new JTextPane();
        chatArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16)); // Emoji supported font
        chatArea.setEditable(false); // Make chat area non-editable
        chatArea.setBackground(new Color(230, 230, 250)); // Lavender color
        doc = chatArea.getStyledDocument();
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        frame.add(chatScrollPane, BorderLayout.CENTER);

        // Footer panel for message input, emoji button, and send button
        footer = new JPanel();
        footer.setLayout(new BorderLayout());

        // Message field
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Emoji button
        emojiButton = new JButton("😊");
        emojiButton.setFont(new Font("Arial", Font.PLAIN, 18));
        emojiButton.setBackground(new Color(255, 215, 0)); // Gold color
        emojiButton.setFocusable(false);

        // Send button
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setBackground(new Color(34, 139, 34)); // Green color
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusable(false);

        // Adding components to footer
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(emojiButton, BorderLayout.WEST);
        inputPanel.add(sendButton, BorderLayout.EAST);
        footer.add(inputPanel, BorderLayout.SOUTH);

        frame.add(footer, BorderLayout.SOUTH);

        // Event handling for send button
        sendButton.addActionListener(e -> sendMessage());

        // Event handling for Enter key in message field
        messageField.addActionListener(e -> sendMessage());

        // Event handling for emoji button
        emojiButton.addActionListener(e -> showEmojiPopup());

        frame.setVisible(true);
    }

    // Method to show emoji popup
    private void showEmojiPopup() {
        if (emojiPopup == null) {
            emojiPopup = new JPopupMenu();
            String[] emojis = {
                "😀", "😁", "😂", "🤣", "😃", "😄", "😊", "😍", "😎", "😢",
                "😡", "👍", "👎", "🙏", "👏", "🎉", "❤️", "🔥", "✨", "😜",
                "🤔", "🙄", "🥳", "😇", "😱", "🤗", "😷", "💪", "🤝", "🤩"
            };
            for (String emoji : emojis) {
                JMenuItem emojiItem = new JMenuItem(emoji);
                emojiItem.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                emojiItem.addActionListener(e -> messageField.setText(messageField.getText() + emoji));
                emojiPopup.add(emojiItem);
            }
        }
        emojiPopup.show(emojiButton, emojiButton.getWidth() / 2, emojiButton.getHeight() / 2);
    }

    // Method to send a message
    private void sendMessage() {
        String contentToSend = messageField.getText().trim();
        if (!contentToSend.isEmpty()) {
            appendToChatArea("Me: " + contentToSend, Color.BLUE);
            out.println(contentToSend);
            out.flush();
            messageField.setText(""); // Clear the message field
            if (contentToSend.equalsIgnoreCase("Bye")) {
                closeConnection();
            }
        }
    }

    // Method to append text to the chat area with color support
    private void appendToChatArea(String message, Color color) {
        try {
            Style style = chatArea.addStyle("Color Style", null);
            StyleConstants.setForeground(style, color);
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Method to handle reading from the server
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg == null || msg.equalsIgnoreCase("Bye")) {
                        appendToChatArea("Server has terminated the chat", Color.RED);
                        socket.close();
                        break;
                    }
                    appendToChatArea("Server: " + msg, Color.MAGENTA);
                }
            } catch (Exception e) {
                appendToChatArea("Connection closed by server.", Color.RED);
            }
        };
        new Thread(r1).start();
    }

    // Method to close the connection gracefully
    private void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method to run the client
    public static void main(String[] args) {
        System.out.println("This is the client...going to start client");
        new ChatClient();
    }
}


 