
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GameUI {

    private JFrame frame;
    private JTextField guessInput;
    private JButton guessButton;
    private JLabel messageLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JButton newGameButton;
    private GameLogic game; 

    public GameUI() {
        this.game = new GameLogic();

        createUI();
        
        updateUIForNewGame();
    }
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
    private void startNewGame() {
        game.startNewGame();
        updateUIForNewGame();
    }
    private void updateUIForNewGame() {
        messageLabel.setText("New game started. Guess a number!");
        messageLabel.setForeground(Color.BLACK);
        
        attemptsLabel.setText("You have " + game.getAttemptsLeft() + " attempts left.");
        scoreLabel.setText("Total Score: " + game.getTotalScore());
        
        guessInput.setText("");
        guessInput.setEnabled(true);
        guessButton.setEnabled(true);
        newGameButton.setVisible(false);
    }

    private void handleGuess() {
        String guessText = guessInput.getText();
        
        Map<String, Object> result = game.checkGuess(guessText);
        
        messageLabel.setText((String) result.get("message"));
        messageLabel.setForeground((Color) result.get("color"));
        
        int attempts = (int) result.get("attemptsLeft");
        int score = (int) result.get("totalScore");
        boolean gameOver = (boolean) result.get("gameOver");

        attemptsLabel.setText("You have " + attempts + " attempts left.");
        scoreLabel.setText("Total Score: " + score);
        guessInput.setText("");

        if (gameOver) {
            guessInput.setEnabled(false);
            guessButton.setEnabled(false);
            newGameButton.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameUI();
            }
        });
    }
}
