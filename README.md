# Wordy – Android Wordle Clone

Android app for **CSC 2990 – Assignment 4**.  
Wordy is a simple Wordle-style game where the user has six tries to guess a secret **five-letter word** that is stored in **Firebase Realtime Database**.

---

## Overview

- Target API: **Android 13 (API 33)**  
- Language: **Java**  
- Architecture: Simple two-activity app  
  - `MainActivity` – play Wordy  
  - `CreateWordActivity` – add new words to the Firebase word bank  
- Cloud backend: **Firebase Realtime Database**

---

## Features

### 1. Word Bank (Firebase)

- All playable words are stored in a **Firebase Realtime Database** under a “words” node.
- A word is a simple 5-letter string (e.g., `cream`, `greed`, `color`).
- The app retrieves a **random word** from this bank at the start of each game or when restarting.

---

### 2. Wordy Activity (Main Game Screen)

- **Six rows for user guesses**
  - Each row represents one attempt to guess the 5-letter word.
- **Color feedback per letter**
  - **Green** – correct letter in the correct position.
  - **Yellow** – letter exists in the word but in a different position.
  - **Gray** – letter is not in the target word.
  - Handles repeated letters using standard Wordle rules.
- **Buttons**
  - **Add Word** – opens the **Create Word** activity to add a new word to the bank.
  - **Submit** – checks the current guess against the target word and locks that row.
  - **Restart** – starts a new game with a new random word from Firebase and clears all rows.
  - **Clear** – clears all current guesses while keeping the same target word.
- **Game Result Feedback**
  - Notifies the user of a **win** (correct guess) or **loss** (all six attempts used) through the UI (Toasts / on-screen message).

---

### 3. Create Word Activity (Add Word Screen)

Allows the user to add a new word to the Firebase word bank.

- **Inputs**
  - Single text field for the 5-letter word.
- **Buttons**
  - **Add** – validates the word and, if valid, stores it in Firebase.
  - **Cancel** – returns to the Wordy activity without saving.
- **Validation Rules**
  - Word is **not empty**.
  - Word is **exactly 5 characters long**.
  - Word contains **only alphabetic characters** (`[A–Z][a–z]`).
  - Word is **not already** in the word bank (case-insensitive, so `dog` and `DoG` are considered the same).
- **Feedback**
  - On success: shows a **Toast** confirming the word was added to the database.
  - On error:
    - The label for the input is changed to **purple**.
    - A **Toast** displays a message describing the validation errors.

---

### 4. Extra Credit (Implemented)

- **Clear Database Button (Create Word Activity)**
  - An extra button that, when tapped, **clears all words** in the Firebase word bank.
  - Uses Firebase to remove all entries under the words node.
  - Shows a Toast confirming that the database has been cleared.

---

## Project Structure (Key Files)

- `app/src/main/java/com/example/csc2990_wordy/`
  - `MainActivity.java` – main Wordy game logic and UI.
  - `CreateWordActivity.java` – add-word screen and validation.
  - `Word.java` – model class representing a single word.
- `app/src/main/res/layout/`
  - `activity_main.xml` – layout for the Wordy game.
  - `activity_create_word.xml` – layout for the Add Word screen.
- `app/google-services.json` *(ignored from Git)* – Firebase config (must be added locally).

---

## How to Run the App

1. **Clone the Repository**
   ```bash
   git clone <https://github.com/JaydenCruz2004/CSC2990-Wordy>
   cd Wordy
