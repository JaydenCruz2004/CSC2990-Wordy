package com.example.csc2990_wordy.model;

public class Word {
        private String text;

        public Word() { }

        public Word(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
