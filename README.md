Sudoku Game (Java Swing)

A Sudoku game built in Java using Swing for the graphical user interface.  
The project generates solvable Sudoku boards, applies difficulty levels, and lets the player interact with a full GUI.

---

Project Structure

Main.java
- Entry point of the program.
- Starts the GUI by creating a SudokuDisplay instance and calling guiPageOne().

SudokuDisplay.java
- Handles all **GUI elements**:
  - Difficulty selection screen.
  - Sudoku board rendering.
  - Buttons for Reset, Pause, Check, Solve, New Puzzle, and Back.
  - Timer display and win screen.
- Manages **user input** and updates the board interactively.

SudokuGenerator.java
- Handles puzzle generation and logic:
  - Creates a full valid Sudoku solution.
  - Removes numbers from the solution based on chosen difficulty.
  - Provides helper methods for checking rows, columns, and 3x3 grids.
  - Stores both the playable board and the full solution.

---

Features
- Difficulty selection: Easy, Medium, Hard.
- Interactive 9x9 Sudoku board with number buttons.
- Game controls:
  - Reset – clears user input.
  - Pause – temporarily stops play and freezes the board.
  - Check – verifies player’s answers (green = correct, red = wrong).
  - Solve – fills the board with the solution.
  - New Puzzle – generates a new puzzle of the same difficulty.
  - Back – returns to the difficulty selection screen.
- Timer that tracks elapsed time in HH:MM:SS format.
- Win screen showing completion time.

---

How to Run
1. To compile at the command line:
   javac Main.java SudokuDisplay.java SudokuGenerator.java
   java Main
