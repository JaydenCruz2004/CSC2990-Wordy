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

import com.example.csc2990_wordy.Word;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateWordActivity extends AppCompatActivity {

    private DatabaseReference wordsRef;

    private EditText wordInput;
    private TextView wordLabel;
    private Button addButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_word);

        // Correct database reference
        wordsRef = FirebaseDatabase
                .getInstance()
                .getReference("words");

        wordInput = findViewById(R.id.word_input);
        wordLabel = findViewById(R.id.word_label);
        addButton = findViewById(R.id.add_button);
        cancelButton = findViewById(R.id.cancel_button);

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
    }

    private void attemptAddWord() {

        wordLabel.setTextColor(Color.BLACK);
        String input = wordInput.getText().toString().trim();

        // validation
        if (input.isEmpty()) {
            Toast.makeText(this, "Word cannot be empty", Toast.LENGTH_LONG).show();
            wordLabel.setTextColor(Color.parseColor("#800080"));
            return;
        }
        if (input.length() != 5) {
            Toast.makeText(this, "Word must be exactly 5 letters", Toast.LENGTH_LONG).show();
            wordLabel.setTextColor(Color.parseColor("#800080"));
            return;
        }
        if (!input.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "Letters only", Toast.LENGTH_LONG).show();
            wordLabel.setTextColor(Color.parseColor("#800080"));
            return;
        }

        final String newWord = input.toLowerCase();

        // WRITE CORRECT WAY — MUST USE Word OBJECT
        wordsRef.push()
                .setValue(new Word(newWord))
                .addOnSuccessListener(unused -> {
                    Toast.makeText(CreateWordActivity.this,
                            "Word added!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(CreateWordActivity.this,
                                "Write failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
    }
}
