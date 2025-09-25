/**
 * The Main class is the entry point for the Sudoku application.
 * It initializes the SudokuDisplay and starts the GUI at the difficulty
 * selection screen.
 */
public class Main 
{
    public static void main(String args[])
    {
        SudokuDisplay sd = new SudokuDisplay();
        sd.guiPageOne();
    }
}
