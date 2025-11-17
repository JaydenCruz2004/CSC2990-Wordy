package com.example.csc2990_wordy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference wordsRef;

    private TextView statusText;
    private EditText guessInput;
    private Button submitButton, restartButton, clearButton, addWordButton;
    private TextView[][] grid = new TextView[6][5];

    private String targetWord = null;
    private int currentRow = 0;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Correct Firebase reference
        wordsRef = FirebaseDatabase.getInstance().getReference("words");

        statusText = findViewById(R.id.status_text);
        guessInput = findViewById(R.id.guess_input);
        submitButton = findViewById(R.id.submit_button);
        restartButton = findViewById(R.id.restart_button);
        clearButton = findViewById(R.id.clear_button);
        addWordButton = findViewById(R.id.add_word_button);

        setupGrid();
        setupButtons();
        resetBoard();
        loadRandomWord();
    }

    private void setupGrid() {
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {
                String idName = "cell_" + r + "_" + c;
                int resId = getResources().getIdentifier(idName, "id", getPackageName());
                grid[r][c] = findViewById(resId);
            }
        }
    }

    private void setupButtons() {
        submitButton.setOnClickListener(v -> submitGuess());

        restartButton.setOnClickListener(v -> {
            resetBoard();
            loadRandomWord();
        });

        clearButton.setOnClickListener(v -> {
            resetBoard();
            statusText.setText("Board cleared — try again!");
        });

        addWordButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateWordActivity.class);
            startActivity(intent);
        });
    }

    private void resetBoard() {
        currentRow = 0;
        gameOver = false;

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {
                grid[r][c].setText("");
                grid[r][c].setBackgroundResource(R.drawable.cell_background_neutral);
            }
        }

        guessInput.getText().clear();
    }

    private void loadRandomWord() {
        statusText.setText("Loading word...");
        submitButton.setEnabled(false);
        gameOver = true;

        wordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> wordList = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Word w = child.getValue(Word.class);

                    // Safety check
                    if (w != null && w.getText() != null && w.getText().length() == 5) {
                        wordList.add(w.getText());
                    }
                }

                if (wordList.isEmpty()) {
                    // Fallback word
                    targetWord = "apple";
                    statusText.setText("Using fallback: APPLE (DB empty)");
                } else {
                    // Random choice
                    targetWord = wordList.get(new Random().nextInt(wordList.size()));
                    statusText.setText("Guess the word!");
                }

                gameOver = false;
                submitButton.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                statusText.setText("Firebase error — using fallback word.");
                targetWord = "apple";
                submitButton.setEnabled(true);
                gameOver = false;
            }
        });
    }

    private void submitGuess() {
        if (gameOver || targetWord == null) return;

        String guess = guessInput.getText().toString().trim().toLowerCase();

        if (guess.length() != 5) {
            Toast.makeText(this, "Enter exactly 5 letters.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update grid display
        for (int i = 0; i < 5; i++) {
            grid[currentRow][i].setText(String.valueOf(guess.charAt(i)).toUpperCase());
        }

        // Win condition
        if (guess.equals(targetWord)) {
            statusText.setText("You WIN! Word was: " + targetWord.toUpperCase());
            gameOver = true;
            return;
        }

        // Loss condition
        if (currentRow == 5) {
            statusText.setText("You LOST! Word was: " + targetWord.toUpperCase());
            gameOver = true;
            return;
        }

        currentRow++;
        statusText.setText("Guess " + (currentRow + 1) + " of 6");
        guessInput.getText().clear();
    }
}
