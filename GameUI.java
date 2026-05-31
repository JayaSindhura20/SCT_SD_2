
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

// This is our "Frontend" class.
// It only manages the visual components.
// It holds an instance of GameLogic to do the "thinking".
public class GameUI {

    // UI Components 
    private JFrame frame;
    private JTextField guessInput;
    private JButton guessButton;
    private JLabel messageLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JButton newGameButton;

    //The "Backend" Logic
    private GameLogic game; //This UI class HAS A logic class

    public GameUI() {
        // 1. Create the instanzce of the logic
        this.game = new GameLogic();
        
        // 2. Build the UI
        createUI();
        
        // 3. Set the initial text
        updateUIForNewGame();
    }

    // Creates all the visual components
    private void createUI() {
        frame = new JFrame("Number Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 1, 10, 10));

        JLabel titleLabel = new JLabel("Guess a number between 1 and 100", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel);

        messageLabel = new JLabel("Good luck!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(messageLabel);

        JPanel inputPanel = new JPanel();
        guessInput = new JTextField(10);
        guessButton = new JButton("Guess");
        inputPanel.add(guessInput);
        inputPanel.add(guessButton);
        frame.add(inputPanel);
        
        attemptsLabel = new JLabel("", SwingConstants.CENTER);
        frame.add(attemptsLabel);
        
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        frame.add(scoreLabel);

        newGameButton = new JButton("Play Again");
        newGameButton.setVisible(false);
        frame.add(newGameButton);

        // --- Add Logic (Action Listeners) ---
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });
        
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Asks the logic to start a new game, then updates the UI
    private void startNewGame() {
        game.startNewGame();
        updateUIForNewGame();
    }

    // Helper method to set all labels
    private void updateUIForNewGame() {
        messageLabel.setText("New game started. Guess a number!");
        messageLabel.setForeground(Color.BLACK);
        
        // Get the starting values from the logic class
        attemptsLabel.setText("You have " + game.getAttemptsLeft() + " attempts left.");
        scoreLabel.setText("Total Score: " + game.getTotalScore());
        
        guessInput.setText("");
        guessInput.setEnabled(true);
        guessButton.setEnabled(true);
        newGameButton.setVisible(false);
    }

    // This is where the two classes talk!
    private void handleGuess() {
        // 1. Get text from the UI
        String guessText = guessInput.getText();
        
        // 2. Send the text to the logic class and get a result
        Map<String, Object> result = game.checkGuess(guessText);
        
        // 3. Update the UI based on the result
        messageLabel.setText((String) result.get("message"));
        messageLabel.setForeground((Color) result.get("color"));
        
        int attempts = (int) result.get("attemptsLeft");
        int score = (int) result.get("totalScore");
        boolean gameOver = (boolean) result.get("gameOver");

        attemptsLabel.setText("You have " + attempts + " attempts left.");
        scoreLabel.setText("Total Score: " + score);
        guessInput.setText(""); // Clear input

        if (gameOver) {
            guessInput.setEnabled(false);
            guessButton.setEnabled(false);
            newGameButton.setVisible(true);
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // This now starts the UI, which in turn starts the logic.
                new GameUI();
            }
        });
    }
}