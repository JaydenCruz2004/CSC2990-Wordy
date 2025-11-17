package com.example.csc2990_wordy;

public class Word {

    public String text;  // must be PUBLIC for Firebase

    public Word() { }

    public Word(String text) {
        this.text = text.toLowerCase();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text.toLowerCase();
    }
}

