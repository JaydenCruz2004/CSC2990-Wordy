package com.example.csc2990_wordy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

public class CreateWordActivity extends AppCompatActivity {

    private DatabaseReference wordsRef;

    private EditText wordInput;
    private TextView wordLabel;
    private Button addButton;
    private Button cancelButton;
    private Button clearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_word);

        wordsRef = FirebaseDatabase
                .getInstance()
                .getReference("words");

        wordInput = findViewById(R.id.word_input);
        wordLabel = findViewById(R.id.word_label);
        addButton = findViewById(R.id.add_button);
        cancelButton = findViewById(R.id.cancel_button);
        clearButton = findViewById(R.id.clear_database_button);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddWord();
            }
        });

        clearButton = findViewById(R.id.clear_database_button);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDatabase();
            }
        });

    }

    private void attemptAddWord() {

        wordLabel.setTextColor(Color.BLACK);

        String input = wordInput.getText().toString().trim();

        if (input.isEmpty()) {
            showError("Word cannot be empty");
            return;
        }
        if (input.length() != 5) {
            showError("Word must be exactly 5 letters");
            return;
        }
        if (!input.matches("[a-zA-Z]+")) {
            showError("Letters only");
            return;
        }

        final String newWord = input.toLowerCase();

        wordsRef.orderByChild("text")
                .equalTo(newWord)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            showError("That word is already in the word bank");
                        } else {
                            addWordToFirebase(newWord);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showError("Error checking duplicates: " + error.getMessage());
                    }
                });
    }

    private void addWordToFirebase(String newWord) {
        wordsRef.push()
                .setValue(new Word(newWord))
                .addOnSuccessListener(unused -> {
                    Toast.makeText(CreateWordActivity.this,
                            "Word stored in database!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> showError("Write failed: " + e.getMessage()));
    }

    private void showError(String message) {
        wordLabel.setTextColor(Color.parseColor("#800080"));
        Toast.makeText(CreateWordActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void clearDatabase() {
        wordsRef.removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(CreateWordActivity.this,
                            "Word bank cleared!",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateWordActivity.this,
                            "Failed to clear database: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

}
