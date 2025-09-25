import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * The SudokuDisplay class manages the graphical user interface (GUI) for the Sudoku game.
 * It handles the creation of menus, game boards, controls, and interaction logic
 * (such as pausing, checking answers, resetting, solving, and starting new puzzles).
 * 
 * - Displaying the main menu with difficulty selection.
 * - Rendering the Sudoku board and cells.
 * - Handling user input and updating the board state.
 * - Providing utility buttons like Reset, Pause, Check, Back, Solve, and New Puzzle.
 */
public class SudokuDisplay
{
    private int[][] board;
    private int[][] solution;
    public JFrame gui = new JFrame("Sudoku");
    private JTextPane prevPane = null;
    private Color prevColor = null;
    private JPanel panel = new JPanel();
    private JPanel game = new JPanel();
    private JButton[] buttons = new JButton[9];
    private JTextPane[][] panes = new JTextPane[9][9];
    private JButton[] sideButtons = new JButton[6];
    private long startTime = 0;
    private long elaspedTime = 0;
    private long pausedTime = 0;
    private JLabel timer = null;

    /**
     * Displays the first page of the GUI where the user selects a difficulty level.
     * Initializes the main menu screen with "Easy", "Medium", and "Hard" buttons.
     */
    public void guiPageOne()
    {

        gui.setSize(1280, 720);
        gui.add(panel);
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setLocationRelativeTo(null);
        panel.setLayout(null);
        Color lblue = new Color(96, 179, 255);
        panel.setBackground(lblue);

        JLabel title = new JLabel("Sudoku");
        title.setForeground(Color.BLACK);
        title.setBounds(515,1,1000,100);
        title.setFont(new Font("Ariel", Font.BOLD, 60));
        panel.add(title);
        
        JLabel h1 = new JLabel("Please Select Your Difficulty");
        h1.setForeground(Color.BLACK);
        h1.setBounds(465,60,1000,100);
        h1.setFont(new Font("Ariel", Font.ITALIC, 25));
        panel.add(h1);

        Font diff = new Font("Ariel", Font.BOLD, 20);
        Border black = BorderFactory.createLineBorder(Color.BLACK);
        Color lightgreen = new Color(205, 255, 204);
        JButton easy = new JButton("Easy");
        easy.setForeground(Color.BLACK);
        easy.setBounds(540,225,175,50);
        easy.setFont(diff);
        easy.setBackground(lightgreen);
        easy.setFocusPainted(false);
        easy.setBorder(black);
        panel.add(easy);
        easy.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                panel.setVisible(false);
                SudokuGenerator sg = new SudokuGenerator();
                sg.createBoard("Easy");
                board = sg.getBoard();
                solution = sg.getSolution();
                guiGame("Easy");
                startTime = System.currentTimeMillis();
            }
        });

        Color lyellow = new Color(254, 255, 204);
        JButton med = new JButton("Medium");
        med.setBounds(540,325,175,50);
        med.setForeground(Color.BLACK);
        med.setFont(diff);
        med.setBackground(lyellow);
        med.setFocusPainted(false);
        med.setBorder(black);
        panel.add(med);
        med.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                panel.setVisible(false);
                SudokuGenerator sg = new SudokuGenerator();
                sg.createBoard("Medium");
                board = sg.getBoard();
                solution = sg.getSolution();
                guiGame("Medium");
                startTime = System.currentTimeMillis();
            }
        });

        Color lred = new Color(255, 175, 164);
        JButton hard = new JButton("Hard");
        hard.setForeground(Color.BLACK);
        hard.setBounds(540,425,175,50);
        hard.setFont(diff);
        hard.setBackground(lred);
        hard.setFocusPainted(false);
        hard.setBorder(black);
        panel.add(hard);
        hard.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                panel.setVisible(false);
                SudokuGenerator sg = new SudokuGenerator();
                sg.createBoard("Hard");
                board = sg.getBoard();
                solution = sg.getSolution();
                guiGame("Hard");
                startTime = System.currentTimeMillis();
            }
        });
        
        gui.setVisible(true);
    }

    /**
     * Initializes and displays the main Sudoku game GUI.
     *
     * @param diff The difficulty level chosen by the user ("Easy", "Medium", "Hard").
     */
    public void guiGame(String diff)
    {
        gui.add(game);
        Color lblue = new Color(96, 179, 255);
        game.setLayout(null);
        game.setBackground(lblue);
        JLabel title = new JLabel("Sudoku");
        title.setForeground(Color.BLACK);
        title.setBounds(515,1,1000,100);
        title.setFont(new Font("Ariel", Font.BOLD, 60));
        game.add(title);

        JLabel diffculty = new JLabel(diff);
        diffculty.setForeground(Color.BLACK);
        diffculty.setBounds(400,3,1000,100);
        diffculty.setFont(new Font("Ariel", Font.BOLD, 25));
        game.add(diffculty);
        
        // timerLabel();
        numGrid();
        allButtons(diff);

        int x = 358;
        int y = 40;
        for (int i = 0; i < 9; i++)
        {
            x = 358;
            y = y + 60;
            for (int j = 0; j < 9; j++)
            {
                JTextPane pane = new JTextPane();
                panes[i][j] = pane;
                if (board[i][j] != 0)
                {
                    pane.setText(Integer.toString(board[i][j]));
                    pane.setBackground(Color.lightGray);
                    pane.setEditable(false);
                }
                else
                {
                    pane.setText("");
                }

                if (i == 0 && j == 0)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(7, 7, 1, 1, Color.BLACK));
                }
                else if (i == 0 && (j == 2 || j == 5))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(7, 1, 1, 3, Color.BLACK));
                }
                else if (i == 0 && (j == 3 || j == 6))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(7, 3, 1, 1, Color.BLACK));
                }
                else if (i == 0 && j == 8)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(7, 1, 1, 7, Color.BLACK));
                }
                else if (i == 0)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(7, 1, 1, 1, Color.BLACK));
                }
                else if ((i == 1 || i == 4 || i == 7) && j == 0)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 7, 1, 1, Color.BLACK));
                }
                else if ((i == 1 || i == 4 || i == 7) && (j == 2 || j == 5))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
                }
                else if ((i == 1 || i == 4 || i == 7) && (j == 3 || j == 6))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.BLACK));
                }
                else if ((i == 1 || i == 4 || i == 7) && j == 8)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 7, Color.BLACK));
                }
                else if ((i == 2 || i == 5) && j == 0)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 7, 3, 1, Color.BLACK));
                }
                else if ((i == 2 || i == 5) && (j == 1 || j == 4 || j == 7))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
                }
                else if ((i == 2 || i == 5) && (j == 2 || j == 5))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
                }
                else if ((i == 2 || i == 5) && (j == 3 || j == 6))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.BLACK));
                }
                else if ((i == 2 || i == 5) && (j == 8))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 7, Color.BLACK));
                }
                else if ((i == 3 || i == 6) && (j == 0))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(3, 7, 1, 1, Color.BLACK));
                }
                else if ((i == 3 || i == 6) && (j == 1 || j == 4 || j == 7))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.BLACK));
                }
                else if ((i == 3 || i == 6) && (j == 2 || j == 5))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.BLACK));
                }
                else if ((i == 3 || i == 6) && (j == 3 || j == 6))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.BLACK));
                }
                else if ((i == 3 || i == 6) && (j == 8))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 7, Color.BLACK));
                }
                else if (i == 8 && j == 0)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 7, 7, 1, Color.BLACK));
                }
                else if (i == 8 && (j == 1 || j == 4 || j == 7))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 7, 1, Color.BLACK));
                }
                else if (i == 8 && (j == 2 || j == 5))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 7, 3, Color.BLACK));
                }
                else if (i == 8 && (j == 3 || j == 6))
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 3, 7, 1, Color.BLACK));
                }
                else if (i == 8 && j == 8)
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 7, 7, Color.BLACK));
                }
                else
                {
                    pane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                }  

                if (pane.getBackground() != Color.LIGHT_GRAY)
                {
                    pane.addKeyListener(new KeyAdapter() 
                    {
                        public void keyTyped(KeyEvent e) 
                        {   
                            char c = e.getKeyChar();
                            if (Character.isDigit(c) && c >= '1' && c <= '9' && !(checkNine(c - '0')) && pane.isEditable()) 
                            {
                                pane.setText(String.valueOf(c));
                                int num = c - '0';
                                int row = (((int)pane.getY() - 40) / 60) - 1;
                                int col = (((int)pane.getX() - 298) / 60) - 1;
                                pane.setForeground(Color.DARK_GRAY);
                                
                                board[row][col] = num;
                                // timerLabel();
                                numGrid();
                                errorChecker();
                                // displayBoard();
                                if (checkWin())
                                {
                                    finalScreen();
                                }
                            }
                            e.consume();
                            if (!(e.getKeyChar() >= '1' && e.getKeyChar() <= '9') && pane.isEditable())
                            {
                                int row = (((int)pane.getY() - 40) / 60) - 1;
                                int col = (((int)pane.getX() - 298) / 60) - 1;
                                board[row][col] = 0;
                                // timerLabel();
                                numGrid();
                                errorChecker();
                                // displayBoard();
                            }
                        }  
                    });
                }

                pane.addCaretListener(new CaretListener() {
                    @Override
                    public void caretUpdate(CaretEvent e) 
                    { 
                        if (prevPane != null && prevColor != null) 
                        {
                            prevPane.setBackground(prevColor);
                        }
                        prevPane = pane;
                        prevColor = prevPane.getBackground();
                        pane.setBackground(Color.YELLOW);
                    }
                });
                pane.setFont(new Font("Ariel", Font.BOLD, 40));
                pane.setCaretColor(Color.yellow);
                StyledDocument doc = pane.getStyledDocument();
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, doc.getLength(), center, false);

                pane.setBounds(x, y, 60, 60);
                game.add(pane);
                x = x + 60;
            }
        }
    }

    /**
     * Creates and initializes all side buttons in the game screen.
     *
     * @param diff The difficulty level for restarting a new puzzle.
     */
    public void allButtons(String diff)
    {
        pauseButton();
        checkerButton();
        resetButton();
        backButton();
        newButton(diff);
        solveButton();
    }

    /**
     * Creates the "Back" button that returns the user to the difficulty selection menu.
     */
    public void backButton()
    {
        JButton back = new JButton("Back");
        sideButtons[0] = back;
        Border black = BorderFactory.createLineBorder(Color.BLACK); 
        Color dblue = new Color(35, 115, 196);
        back.setBounds(85,550,190,50);
        back.setFont(new Font("Ariel", Font.BOLD, 25));
        back.setBackground(dblue);
        back.setForeground(Color.BLACK);
        back.setFocusPainted(false);
        back.setBorder(black);
        game.add(back);
        back.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                gui.remove(game);
                panel.setVisible(true);
                resetGUI();
            }
        });

    }

    /**
     * Creates the "Check" button which verifies the player's input against the solution.
     * Correct values turn green, incorrect ones turn red.
     */
    public void checkerButton()
    {
        JButton checker = new JButton("Check");
        sideButtons[1] = checker;
        Border black = BorderFactory.createLineBorder(Color.BLACK); 
        Color dblue = new Color(35, 115, 196);
        checker.setBounds(85,270,190,50);
        checker.setFont(new Font("Ariel", Font.BOLD, 25));
        checker.setBackground(dblue);
        checker.setForeground(Color.BLACK);
        checker.setFocusPainted(false);
        checker.setBorder(black);
        checker.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                for (int i = 0; i < 9; i++)
                {
                    for (int j = 0; j < 9; j++)
                    {
                        if (panes[i][j].getBackground() != Color.lightGray)
                        {
                            if (panes[i][j].getText().equals(Integer.toString(solution[i][j])))
                            {
                                panes[i][j].setForeground(new Color(71, 160, 18)); //TEMP
                            }
                            else
                            {
                                panes[i][j].setForeground(new Color(156, 0, 0 )); //TEMP
                            }
                        }
                    }
                }
            }
        });
        game.add(checker);
    }

    /**
     * Creates the "Pause" button, allowing the user to temporarily pause the game.
     * Displays a pause dialog and grays out the game board until resumed.
     */
    public void pauseButton()
    {
        JButton pause = new JButton("Pause");
        sideButtons[2] = pause;
        Border black = BorderFactory.createLineBorder(Color.BLACK); 
        Color dblue = new Color(35, 115, 196);
        Color lblack = new Color(31, 31, 31 );
        pause.setBounds(85,200,190,50);
        pause.setFont(new Font("Ariel", Font.BOLD, 25));
        pause.setBackground(dblue);
        pause.setForeground(Color.BLACK);
        pause.setFocusPainted(false);
        pause.setBorder(black);
        game.add(pause);

        String message = "<html><div width='500px' align='right'><div style='text-align: center;'>Paused<br></div></div></html>";
        JLabel paused = new JLabel(message);
        paused.setFont(new Font("Ariel", Font.BOLD, 40));
        paused.setForeground(Color.WHITE);
        JButton okButton = new JButton("Resume");
        okButton.setForeground(Color.BLACK);
        okButton.setBackground(dblue);
        okButton.setPreferredSize(new Dimension(100, 50));
        okButton.setFont(new Font("Ariel", Font.BOLD, 15));
        okButton.setFocusPainted(false);
        
        okButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                Window window = SwingUtilities.getWindowAncestor((Component)e.getSource());
                window.dispose();
                makeGrayScale();
                pausedTime = System.currentTimeMillis() - elaspedTime;
            }
        });

        pause.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                makeGrayScale();
                elaspedTime = System.currentTimeMillis();
                UIManager.put("OptionPane.minimumSize", new Dimension(500,500)); 
                UIManager.put("OptionPane.background", lblack);
                UIManager.put("Panel.background", lblack);

                JOptionPane optionPane = new JOptionPane(paused, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{okButton});
                JDialog dialog = optionPane.createDialog(game, "Paused");

                dialog.addWindowListener(new WindowAdapter() 
                {
                    @Override
                    public void windowClosing(WindowEvent e) 
                    {
                        okButton.doClick();
                    }
                });

                dialog.setVisible(true);
            }
        });
    }

     /**
     * Updates and displays the timer label in the game screen.
     * Tracks elapsed play time in HH:MM:SS format.
     */
    public void timerLabel()
    {
        String timeInHHMMSS = "error";
        if (timer == null)
        {
            timeInHHMMSS = "0:00:00";
        }
        else
        {
            game.remove(timer);
            timeInHHMMSS = timeFormat();
        }
        timer = new JLabel(timeInHHMMSS);
        timer.setForeground(Color.BLACK);
        timer.setBounds(770,7,1000,100);
        timer.setFont(new Font("Ariel", Font.BOLD, 25));
        game.add(timer);
        game.setVisible(false);
        game.setVisible(true);
    }

    /**
     * Creates the "Reset" button, which clears all user-entered values from the board.
     * Resets the timer and restores the initial puzzle state.
     */
    public void resetButton()
    {
        JButton reset = new JButton("Reset");
        sideButtons[3] = reset;
        Border black = BorderFactory.createLineBorder(Color.BLACK); 
        Color dblue = new Color(35, 115, 196);
        reset.setBounds(85,340,190,50);
        reset.setFont(new Font("Ariel", Font.BOLD, 25));
        reset.setBackground(dblue);
        reset.setForeground(Color.BLACK);
        reset.setFocusPainted(false);
        reset.setBorder(black);
        reset.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                for (int i = 0; i < 9; i++)
                {
                    for (int j = 0; j < 9; j++)
                    {
                        if (panes[i][j].getBackground() != Color.lightGray)
                        {
                            panes[i][j].setText("");
                            board[i][j] = 0;
                            panes[i][j].setEditable(true);
                        }
                    }
                }
                numGrid();
                errorChecker();
                startTime = System.currentTimeMillis();
                pausedTime = 0;
                // timerLabel();
            }
            
        });
        game.add(reset);
    }

    /**
     * Converts elapsed game time into a string formatted as HH:MM:SS.
     *
     * @return The formatted string representing elapsed time.
     */
    public String timeFormat()
    {
        Duration duration = Duration.ofMillis((System.currentTimeMillis() - startTime) - pausedTime);
        long seconds = duration.getSeconds();
        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        String timeInHHMMSS = String.format("%01d:%02d:%02d", HH, MM, SS);
        return timeInHHMMSS;
    }

    /**
     * Creates the "Solve" button, which fills the board with the solution
     * and ends the game.
     */
    public void solveButton()
    {
        JButton solve = new JButton("Solve");
        sideButtons[4] = solve;
        Border black = BorderFactory.createLineBorder(Color.BLACK); 
        Color dblue = new Color(35, 115, 196);
        solve.setBounds(988,550,190,50);
        solve.setFont(new Font("Ariel", Font.BOLD, 25));
        solve.setBackground(dblue);
        solve.setForeground(Color.BLACK);
        solve.setFocusPainted(false);
        solve.setBorder(black);
        solve.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                for (int i = 0; i < 9; i++)
                {
                    for (int j = 0; j < 9; j++)
                    {
                        if (panes[i][j].getBackground() != Color.lightGray)
                        {
                            panes[i][j].setText(Integer.toString(solution[i][j]));
                        }
                        board[i][j] = solution[i][j];
                    }
                }
                numGrid();
                errorChecker();
                finalScreen();
            }
        });
        game.add(solve);
    }

    /**
     * Creates the "New Puzzle" button, which generates a new puzzle of the same difficulty.
     *
     * @param diff The difficulty level for generating the new puzzle.
     */
    public void newButton(String diff)
    {
        JButton newP = new JButton("New Puzzle");
        sideButtons[5] = newP;
        Border black = BorderFactory.createLineBorder(Color.BLACK); 
        Color dblue = new Color(35, 115, 196);
        newP.setBounds(988,140,190,50);
        newP.setFont(new Font("Ariel", Font.BOLD, 25));
        newP.setBackground(dblue);
        newP.setForeground(Color.BLACK);
        newP.setFocusPainted(false);
        newP.setBorder(black);
        newP.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                gui.remove(game);
                resetGUI();
                SudokuGenerator sg = new SudokuGenerator();
                sg.createBoard(diff);
                board = sg.getBoard();
                solution = sg.getSolution();
                guiGame(diff);
            }
        });
        game.add(newP);
    }

    /**
     * Colors all user-entered numbers gold once the puzzle is solved.
     * Prevents further editing of the board.
     */
    public void makeGold()
    {
        Color gold = new Color(203, 188, 32);
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                panes[i][j].setEditable(false);
                if (panes[i][j].getBackground() != Color.lightGray)
                {
                    panes[i][j].setForeground(gold);
                }
            }
        }
    }

    /**
     * Resets the board's text colors to dark gray, unless a cell has been
     * explicitly marked as correct (green) or incorrect (red).
     */
    public void grayBoard()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                {
                    panes[i][j].setForeground(Color.darkGray);
                }
            }
        }
    }

    /**
     * Validates the board for errors by checking rows, columns, and 3x3 quadrants.
     * Marks invalid entries in red if duplicates are detected.
     */
    public void errorChecker()
    {
        grayBoard();
        SudokuGenerator sg = new SudokuGenerator();
        int num;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                num = board[i][j];
                int countROW = 0;
                int countCOL = 0;
                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                int count4 = 0;
                int count5 = 0;
                int count6 = 0;
                int count7 = 0;
                int count8 = 0;
                int count9 = 0;
                for (int k = 0; k < 9; k++)
                {
                    if (num == board[i][k])
                    {
                        countCOL++;
                        if (countCOL >= 2)
                        {
                            if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                            {
                                panes[i][j].setForeground(Color.RED);
                            }
                            if (!(panes[i][k].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][k].getForeground().equals(new Color(156, 0, 0))))
                            {
                                panes[i][k].setForeground(Color.RED);
                            }
                        }
                    }

                    if (num == board[k][j])
                    {
                        countROW++;
                        if (countROW >= 2)
                        {
                            if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                            {
                                panes[i][j].setForeground(Color.RED);
                            }
                            if (!(panes[k][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[k][j].getForeground().equals(new Color(156, 0, 0))))
                            {
                                panes[k][j].setForeground(Color.RED);
                            }
                        }
                    }
                }
                
                if (sg.quadrantSelector(i, j) == 1)
                {
                    for (int m = 0; m < 3; m++)
                    {
                        for (int n = 0; n < 3; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count1++;
                                if (count1 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    } 
                }
                
                if (sg.quadrantSelector(i, j) == 2)
                {
                    for (int m = 0; m < 3; m++)
                    {
                        for (int n = 3; n < 6; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count2++;
                                if (count2 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }

                if (sg.quadrantSelector(i, j) == 3)
                {
                    for (int m = 0; m < 3; m++)
                    {
                        for (int n = 6; n < 9; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count3++;
                                if (count3 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }

                if (sg.quadrantSelector(i, j) == 4)
                {
                    for (int m = 3; m < 6; m++)
                    {
                        for (int n = 0; n < 3; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count4++;
                                if (count4 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (sg.quadrantSelector(i, j) == 5)
                {
                    for (int m = 3; m < 6; m++)
                    {
                        for (int n = 3; n < 6; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count5++;
                                if (count5 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }
                    
                if (sg.quadrantSelector(i, j) == 6)
                {
                    for (int m = 3; m < 6; m++)
                    {
                        for (int n = 6; n < 9; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count6++;
                                if (count6 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }

                if (sg.quadrantSelector(i, j) == 7)
                {
                    for (int m = 6; m < 9; m++)
                    {
                        for (int n = 0; n < 3; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count7++;
                                if (count7 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }

                if (sg.quadrantSelector(i, j) == 8)
                {
                    for (int m = 6; m < 9; m++)
                    {
                        for (int n = 3; n < 6; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count8++;
                                if (count8 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }

                if (sg.quadrantSelector(i, j) == 9)
                {
                    for (int m = 6; m < 9; m++)
                    {
                        for (int n = 6; n < 9; n++)
                        {
                            if (board[m][n] == num)
                            {
                                count9++;
                                if (count9 >= 2)
                                {
                                    if (!(panes[i][j].getForeground().equals(new Color(71, 160, 18))) && !(panes[i][j].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[i][j].setForeground(Color.RED);
                                    }
                        
                                    if (!(panes[m][n].getForeground().equals(new Color(71, 160, 18))) && !(panes[m][n].getForeground().equals(new Color(156, 0, 0))))
                                    {
                                        panes[m][n].setForeground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Resets the GUI to the initial difficulty selection menu.
     * Clears panels and reinitializes the layout.
     */
    public void resetGUI()
    {
        board = new int[9][9];
        solution = new int[9][9];
        prevPane = null;
        prevColor = null;
        game = new JPanel();
        buttons = new JButton[9];
        panes = new JTextPane[9][9];
        sideButtons = new JButton[6];
        startTime = System.currentTimeMillis();
        pausedTime = 0;
    }

    /**
     * Toggles the game board and side buttons between two color themes:
     * - Light blue with dark blue buttons.
     * - Light gray with light blue buttons.
     */
    public void makeGrayScale()
    {
        if (game.getBackground().equals(new Color(96, 179, 255)))
        {
            game.setBackground(new Color(200, 228, 238));
            for (int i = 0; i < 6; i++)
            {
                sideButtons[i].setBackground(new Color(112, 181, 221));
            }
        }
        else
        {
            game.setBackground(new Color(96, 179, 255));
            for (int i = 0; i < 6; i++)
            {
                sideButtons[i].setBackground(new Color(35, 115, 196));
            }
        }
    }

    /**
     * Builds a 3x3 number grid (1–9) for user input.
     * Each button represents a number and is enabled/disabled
     * depending on whether that number already appears 9 times on the board.
     */
    public void numGrid()
    {
        clearButtons();
        int x = 955;
        int y = 160;
        int count = 1;
        UIManager.put("Button.disabledText", new ColorUIResource(Color.BLACK)); 
        for (int i = 0; i < 3; i++)
        {
            y = y + 85;
            x = 955;
            for (int j = 0; j < 3; j++)
            {
                JButton grid = new JButton();
                grid.setFont(new Font("Ariel", Font.BOLD, 55));
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                grid.setText(Integer.toString(count));
                grid.setFocusPainted(false);
                if (count == 1)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(6, 6, 2, 2, Color.BLACK));
                }
                else if (count == 2)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(6, 2, 2, 2, Color.BLACK));
                }
                else if (count == 3)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(6, 2, 2, 6, Color.BLACK));
                }
                else if (count == 4)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(2, 6, 2, 2, Color.BLACK));
                }
                else if (count == 5)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
                }
                else if (count == 6)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 6, Color.BLACK));
                }
                else if (count == 7)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(2, 6, 6, 2, Color.BLACK));
                }
                else if (count == 8)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 2, Color.BLACK));
                }
                else if (count == 9)
                {
                    grid.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, Color.BLACK));
                }

                if (checkNine(count))
                {
                    grid.setEnabled(false);
                    grid.setBackground(Color.darkGray);
                }
                else
                {
                    grid.setBackground(Color.lightGray);
                }

                
                buttons[count - 1] = grid;
                grid.setBounds(x, y, 85, 85);
                grid.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        
                    }
                });
                game.add(grid);
                x = x + 85;
                count++;
            }
        }
        game.revalidate(); 
        game.repaint(); 
    }

    /**
     * Displays the final win screen after the puzzle is solved.
     * Shows a "Solved!" message, elapsed time, and a Back button to return to the game.
     */
    public void finalScreen()
    {
        makeGold();
        game.setVisible(false);
        JPanel finalScreen = new JPanel();
        gui.add(finalScreen);
        Color lblue = new Color(96, 179, 255);
        finalScreen.setLayout(null);
        finalScreen.setBackground(lblue);
        JLabel title = new JLabel("Solved!");
        title.setForeground(Color.BLACK);
        title.setBounds(460,255,1000,100);
        title.setFont(new Font("Ariel", Font.ITALIC, 100));
        finalScreen.add(title);

        JLabel time = new JLabel(timeFormat());
        time.setForeground(Color.BLACK);
        time.setBounds(568 , 350, 1000,100);
        time.setFont(new Font("Ariel", Font.PLAIN, 40));
        finalScreen.add(time);


        JButton back = new JButton("Back");
        Border black = BorderFactory.createLineBorder(Color.BLACK); 
        Color dblue = new Color(35, 115, 196);
        back.setBounds(545,450,175,50);
        back.setFont(new Font("Ariel", Font.BOLD, 20));
        back.setBackground(dblue);
        back.setForeground(Color.BLACK);
        back.setFocusPainted(false);
        back.setBorder(black);
        back.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                finalScreen.setVisible(false);
                game.setVisible(true);
            }
        });
        finalScreen.add(back);

    }

    /**
     * Checks if a given number appears exactly 9 times on the board.
     * Used to disable number input when the number is fully placed.
     *
     * @param num The number to check (1–9).
     * @return true if the number appears 9 times, false otherwise.
     */
    public boolean checkNine(int num)
    {
        int count = 0;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (board[i][j] == num)
                {
                    count++;
                }
            }
        }

        return count == 9;
    }

    /**
     * Checks if the current board matches the solution.
     *
     * @return true if all cells are correct, false otherwise.
     */
    public boolean checkWin()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (board[i][j] != solution[i][j])
                {
                    return false; //EDIT
                }
            }
        }
        return true;
    }

    /**
     * Clears all number buttons (1–9) from the game screen.
     * Called before rebuilding the number grid.
     */
    public void clearButtons()
    {
        if (buttons[0] != null)
        {
            for (int i = 0; i < 9; i++)
            {
                game.remove(buttons[i]);
            }
        }
    }
}