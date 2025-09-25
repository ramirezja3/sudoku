/**
 * Generates and solves Sudoku puzzles with adjustable difficulty.
 */
public class SudokuGenerator 
{
    private int[][] board = new int[9][9];
    private int[][] solution = new int[9][9];

    /** 
     * @return current board 
     * */
    public int[][] getBoard()
    {
        return board;
    }

    /**
     * @return solved board
     * */
    public int[][] getSolution() 
    {
        return solution;
    }

    /**
     * Creates a puzzle with given difficulty.
     * @param diff the difficulty level (Easy, Medium, Hard)
     */
    public void createBoard(String diff)
    {
        int rand;
        int temp;
        for (int i = 0; i < 9; i++)
        {
            board[0][i] = i + 1;
        }
        for (int i = 0; i < 9; i++)
        {
            rand = (int)(Math.random()*9);
            temp = board[0][i];
            board[0][i] = board[0][rand];
            board[0][rand] = temp;
        }
        solveBoard(1, 0, 1);
        difficulty(diff);
    }    

    /**
     * Solves board using backtracking.
     * @param row current row
     * @param col current column
     * @param num number to fill
     */
    public void solveBoard(int row, int col, int num)
    {
        if (row == 9)
        {   
            for (int i = 0; i < 9; i++)
            {
                for (int j = 0; j < 9; j++)
                {
                    solution[i][j] = board[i][j];
                }
            }
            // displayBoard();
            return;
        }
        if (canPlace(row, col, num) == true)
        {
            board[row][col] = num;
            if (col < 8)
            {
                num = 1;
                solveBoard(row, col + 1, num);
            }
            else
            {
                num = 1;
                col = 0;
                solveBoard(row + 1, col, num);
            }
        }
        else if (num < 9)
        {
            solveBoard(row, col, num + 1);
        }
        else
        {
            if ((col == 0 || col == 1) && row >= 2)
            {
                board[row][col] = 0;
                num = board[row - 1][8] + 1;
                if (num == 10)
                {
                    num = board[row][7] + 1;
                    board[row][8] = 0;
                    col = 7;
                    solveBoard(row, col, num);
                }
                else
                {
                    col = 8;
                    solveBoard(row - 1, col, num);
                }
            }
            else
            {
                board[row][col] = 0;
                num = board[row][col - 1] + 1;
                if (num == 10)
                {
                    num = board[row][col - 2] + 1;
                    board[row][col - 1] = 0;
                    solveBoard(row, col - 2, num);
                }
                else
                {
                solveBoard(row, col - 1, num);
                }
            }
        }
    }

    /**
     * Checks if number can be placed.
     * @param row row index
     * @param col column index
     * @param num number to place
     * @return true if valid
     */
    public boolean canPlace(int row, int col, int num)
    {
        for (int i = 0; i < 9; i++)
        {
            if (board[row][i] == num)
            {
                return false;
            }
            if (board[i][col] == num)
            {
                return false;
            }
        }
        if (quadSolver(quadrantSelector(row, col), num) == false)
        {
            return false;
        }
        return true;
    }

    /**
     * Returns quadrant index for cell.
     * @param row row index
     * @param col column index
     * @return quadrant number (1–9)
     */
    public int quadrantSelector(int row, int col)
    {
        if (row < 3 && col < 3)
        {
            return 1;
        }
        else if (row < 3 && col < 6)
        {
            return 2;
        }
        else if (row < 3 && col < 9)
        {
            return 3;
        }
        else if (row < 6 && col < 3)
        {
            return 4;
        }
        else if (row < 6 && col < 6)
        {
            return 5;
        }
        else if (row < 6 && col < 9)
        {
            return 6;
        }
        else if (row < 9 && col < 3)
        {
            return 7;
        }
        else if (row < 9 && col < 6)
        {
            return 8;
        }
        else 
        {
            return 9;
        }
    }

    /**
     * Validates number placement in a quadrant.
     * @param quad quad quadrant index (1–9)
     * @param num number to check
     * @return true if valid
     */
    public boolean quadSolver(int quad, int num)
    {
        if (quad == 1)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 2)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 3)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 4)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 5)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 6)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 7)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 8)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        else if (quad == 9)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (board[i][j] == num)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Removes numbers to set difficulty.
     * @param diff difficulty ("Easy", "Medium", "Hard")
     */
    public void difficulty(String diff)
    {
        int randRow;
        int randCol;
        // remove 58
        if (diff == "Hard")
        {
            for (int i = 0; i < 58; i++)
            {
                randRow = (int)(Math.random()*9);
                randCol = (int)(Math.random()*9);
                if (board[randRow][randCol] != 0)
                {
                    board[randRow][randCol] = 0;
                }
                else
                {
                    i--;
                }
            }
        }

        // remove 50
        if (diff == "Medium")
        {
            for (int i = 0; i < 50; i++)
            {
                randRow = (int)(Math.random()*9);
                randCol = (int)(Math.random()*9);
                if (board[randRow][randCol] != 0)
                {
                    board[randRow][randCol] = 0;
                }
                else
                {
                    i--;
                }
            }
        }

        // remove 43
        if (diff == "Easy")
        {
            for (int i = 0; i < 43; i++)
            {
                randRow = (int)(Math.random()*9);
                randCol = (int)(Math.random()*9);
                if (board[randRow][randCol] != 0)
                {
                    board[randRow][randCol] = 0;
                }
                else
                {
                    i--;
                }
            }
        }
    }

    public void displayBoard()
    {
        String table = "";
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if ((i == 2 || i == 5) && j == 8)
                {
                    table += "[" + board[i][j] + "]\n" + 
                        "----------------------------------\n";
                }
                else if (j == 8)
                {
                    table += "[" + board[i][j] + "]\n";
                }
                else if (j == 2 || j == 5)
                {
                    table += "[" + board[i][j] + "] | ";
                }
                else
                {
                    table += "[" + board[i][j] + "]";
                }
            }
        } 
        System.out.println(table);
    }
}
