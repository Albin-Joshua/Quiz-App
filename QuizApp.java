import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Question {
    private String questionText;
    private String[] choices;
    private char correctAnswer;

    public Question(String questionText, String[] choices, char correctAnswer) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getChoices() {
        return choices;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }
}

public class QuizApp extends JFrame {
    private ArrayList<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField questionField;
    private JTextField[] choiceFields;
    private JTextField correctAnswerField;
    private JLabel questionLabel;
    private JRadioButton[] choiceButtons;
    private ButtonGroup buttonGroup;

    public QuizApp() {
        setTitle("Quiz App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Set up the input panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Enter Question:"));
        questionField = new JTextField();
        inputPanel.add(questionField);

        choiceFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            inputPanel.add(new JLabel("Choice " + (char) ('A' + i) + ":"));
            choiceFields[i] = new JTextField();
            inputPanel.add(choiceFields[i]);
        }

        inputPanel.add(new JLabel("Correct Answer (A, B, C, or D):"));
        correctAnswerField = new JTextField();
        inputPanel.add(correctAnswerField);

        JButton addButton = new JButton("Add Question");
        inputPanel.add(addButton);
        JButton startQuizButton = new JButton("Start Quiz");
        inputPanel.add(startQuizButton);

        // Set up the quiz panel
        JPanel quizPanel = new JPanel(new BorderLayout(10, 10));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        questionLabel = new JLabel("Question:");
        quizPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel choicesPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        choiceButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            choiceButtons[i] = new JRadioButton();
            buttonGroup.add(choiceButtons[i]);
            choicesPanel.add(choiceButtons[i]);
        }
        quizPanel.add(choicesPanel, BorderLayout.CENTER);

        JButton submitAnswerButton = new JButton("Submit Answer");
        quizPanel.add(submitAnswerButton, BorderLayout.SOUTH);

        cardPanel.add(inputPanel, "Input");
        cardPanel.add(quizPanel, "Quiz");

        add(cardPanel);
        cardLayout.show(cardPanel, "Input");

        addButton.addActionListener(e -> addQuestion());
        startQuizButton.addActionListener(e -> startQuiz());
        submitAnswerButton.addActionListener(e -> submitAnswer());

        setLocationRelativeTo(null);
    }

    private void addQuestion() {
        String questionText = questionField.getText();
        String[] choices = new String[4];
        for (int i = 0; i < 4; i++) {
            choices[i] = choiceFields[i].getText();
        }
        char correctAnswer = correctAnswerField.getText().trim().toUpperCase().charAt(0);

        if (questionText.isEmpty() || choices[0].isEmpty() || choices[1].isEmpty() || choices[2].isEmpty() || choices[3].isEmpty() || (correctAnswer != 'A' && correctAnswer != 'B' && correctAnswer != 'C' && correctAnswer != 'D')) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        questions.add(new Question(questionText, choices, correctAnswer));
        questionField.setText("");
        for (JTextField choiceField : choiceFields) {
            choiceField.setText("");
        }
        correctAnswerField.setText("");
    }

    private void startQuiz() {
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions added.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentQuestionIndex = 0;
        score = 0;
        showQuestion();
        cardLayout.show(cardPanel, "Quiz");
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText("Question: " + q.getQuestionText());
            String[] choices = q.getChoices();
            for (int i = 0; i < 4; i++) {
                choiceButtons[i].setText(choices[i]);
                choiceButtons[i].setSelected(false);
            }
        } else {
            endQuiz();
        }
    }

    private void submitAnswer() {
        char selectedAnswer = ' ';
        for (int i = 0; i < 4; i++) {
            if (choiceButtons[i].isSelected()) {
                selectedAnswer = (char) ('A' + i);
                break;
            }
        }

        if (selectedAnswer == ' ') {
            JOptionPane.showMessageDialog(this, "Please select an answer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Question q = questions.get(currentQuestionIndex);
        if (selectedAnswer == q.getCorrectAnswer()) {
            score++;
        }
        currentQuestionIndex++;
        showQuestion();
    }

    private void endQuiz() {
        JOptionPane.showMessageDialog(this, "Quiz Completed! You scored " + score + " out of " + questions.size());
        cardLayout.show(cardPanel, "Input");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizApp().setVisible(true));
    }
}