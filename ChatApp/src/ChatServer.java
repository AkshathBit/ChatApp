


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatServer {
    private ServerSocket server;
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

    // Constructor to initialize the server and GUI
    public ChatServer() {
        try {
            // Create the server socket with SO_REUSEADDR option enabled
            server = new ServerSocket(9999);
            System.out.println("Server is ready to accept connections on port 9999...");
            socket = server.accept(); // Accepting the client connection
            System.out.println("Client connected.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Setup the GUI
            createGUI();

            // Start reading messages from the client
            startReading();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Server Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);  // Exit on error
        }
    }

    // Method to create the GUI
    private void createGUI() {
        frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());

        heading = new JLabel("Server Chat", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 20));
        heading.setOpaque(true);
        heading.setBackground(new Color(0, 100, 0)); // Dark green color
        heading.setForeground(Color.WHITE);
        heading.setPreferredSize(new Dimension(100, 50));
        frame.add(heading, BorderLayout.NORTH);

        chatArea = new JTextPane();
        chatArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(230, 230, 250));
        doc = chatArea.getStyledDocument();
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        frame.add(chatScrollPane, BorderLayout.CENTER);

        footer = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setBackground(new Color(34, 139, 34));
        sendButton.setForeground(Color.WHITE);

        emojiButton = new JButton("😊");
        emojiButton.setFont(new Font("Arial", Font.PLAIN, 18));

        footer.add(emojiButton, BorderLayout.WEST);
        footer.add(messageField, BorderLayout.CENTER);
        footer.add(sendButton, BorderLayout.EAST);

        frame.add(footer, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        emojiButton.addActionListener(e -> showEmojiPopup());

        frame.setVisible(true);
    }

    private void showEmojiPopup() {
        if (emojiPopup == null) {
            emojiPopup = new JPopupMenu();
            String[] emojis = {"😀", "😁", "😂", "😊", "😍", "👍", "❤️", "🔥", "✨"};
            for (String emoji : emojis) {
                JMenuItem emojiItem = new JMenuItem(emoji);
                emojiItem.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                emojiItem.addActionListener(e -> messageField.setText(messageField.getText() + emoji));
                emojiPopup.add(emojiItem);
            }
        }
        emojiPopup.show(emojiButton, emojiButton.getWidth() / 2, emojiButton.getHeight() / 2);
    }

    private void sendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty()) {
            appendToChatArea("Me: " + content, Color.BLUE);
            out.println(content);
            messageField.setText("");
        }
    }

    private void appendToChatArea(String message, Color color) {
        try {
            Style style = chatArea.addStyle("Color Style", null);
            StyleConstants.setForeground(style, color);
            doc.insertString(doc.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        Runnable r1 = () -> {
            try {
                String msg;
                while (socket.isConnected() && (msg = br.readLine()) != null) {
                    appendToChatArea("Client: " + msg, Color.MAGENTA);
                    if (msg.equalsIgnoreCase("Bye")) {
                        JOptionPane.showMessageDialog(frame, "Client disconnected.");
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                appendToChatArea("Connection closed", Color.RED);
            }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
