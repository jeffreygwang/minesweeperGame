/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Minesweeper;

/**
 *
 * @author JeffreyWang
 */


import javax.swing.*;
import java.awt.*;
import java.util.*;


public class Board extends JFrame {
    // Fields; Field methods at bottom of program
    private Tile[][] buttons;
    private int rows;
    private int cols;
    private int numOfMines;

    public Board(int row, int col) {
        double difficulty = difficultySelect();
        buttons = new Tile[row][col];
        rows = row;
        cols = col;
        numOfMines = 0;
        if (row == 10)
            setPreferredSize(new Dimension(400, 400));
        else if (row == 15)
            setPreferredSize(new Dimension(600, 600));
        else
            setPreferredSize(new Dimension(800, 800));
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(rows, cols, 1, 1));
        for (int i = 0; i < rows * cols; i++) {
            JButton button = new JButton();
            int xR = i / rows; // ex: 3 x 3; tile num 4 should be (1,1) in 0 indexed system. 4/3= 1. 
            int yR = i % rows; // 0-indexed
            if (isMine(difficulty)) { // exact values can be configured later
                Tile t = new Tile(button, true, xR, yR); 
                buttons[xR][yR] = t;
                numOfMines++;
            }
            else {
                Tile t = new Tile(button, false, xR, yR);
                buttons[xR][yR] = t;
            }
            pane.add(button);
        }
        printBoardNums(); // solely for ease-of-use and debugging, not necessary 
        initializeMines();
        System.out.println();
        System.out.println();
        printBoardNums(); // solely for ease-of-use and debugging, not necessary 
        setIcons();
    }
    public void initializeMines() { // sets initial values of nums
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!buttons[r][c].getMined())
                    buttons[r][c].setNum(minesAround(buttons[r][c]));
            }
        }
    }
    public void setIcons() { // sets unclicked icon pics
        ImageIcon a = new ImageIcon("Images/MinesweeperUnclickedPNG.png");
        Image tempImage = a.getImage();
        Image newImage = tempImage.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH); // this is to scale the image
        ImageIcon i = new ImageIcon(newImage);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                buttons[r][c].getButton().setIcon(i);
            }
        }
    }
    public void printBoardNums() { // prints board numbers into console for ease-of-use and debugging purposes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(buttons[i][j].getNumber());
            }
            System.out.println();
        }
    }
    /*
    Checks if game is over with two conditions:
    1. All non-mines must be opened
    2. All mines must be flagged
    */
    public boolean isGameWon() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!buttons[r][c].getMined()) {
                    if (buttons[r][c].getObscured()) {
                        return false;
                    }
                }
                else {
                    if (!buttons[r][c].getFlagged()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /*
    End Game Method: Opens up all tiles when user wins or loses
    */
    public void revealAll(MinesLeft m) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (buttons[r][c].getObscured()) {
                    if (buttons[r][c].getFlagged()) {
                        buttons[r][c].setFlaggedFalse();
                    }
                    buttons[r][c].setObscured(this);
                }
                
            }
        }
    }
    /*
    Selects the difficulty (mine spawn rate) based on what the user chooses
    */
    public static double difficultySelect() {
        double ratio = 0;
        JList list = new JList(new String[] {"Easy", "Medium", "Hard"});
        JOptionPane.showMessageDialog(null, list, "Difficulty Select (Number of mines):", JOptionPane.PLAIN_MESSAGE);
        String select = Arrays.toString(list.getSelectedIndices());
        if (select.equals("[0]")) {
            return 0.1;
        }
        if (select.equals("[1]")) {
            return 0.125;
        }
        if (select.equals("[2]")) {
            return 0.15;
        }
        return 0.0;
    }
    public int minesAround(Tile t) { // finds number of mines around tile
        int xC = t.getX();
        int yC = t.getY();
        int counter = 0;
        if (!isOutOfBounds(xC - 1, yC)) {
            if (buttons[xC - 1][yC].getMined())
                counter++;
        }
        if (!isOutOfBounds(xC - 1, yC + 1)) {
             if (buttons[xC - 1][yC + 1].getMined())
                 counter++;
        }
        if ((!isOutOfBounds(xC, yC + 1))) {
            if (buttons[xC][yC + 1].getMined())
                counter++;
        }
        if (!isOutOfBounds(xC + 1, yC + 1)) {
            if (buttons[xC + 1][yC + 1].getMined())
                counter++;
        }
        if (!isOutOfBounds(xC + 1, yC)) {
            if (buttons[xC + 1][yC].getMined())
                counter++;
        }
        if (!isOutOfBounds(xC + 1, yC - 1)) {
            if (buttons[xC + 1][yC - 1].getMined())
                counter++;
        }
        if (!isOutOfBounds(xC, yC - 1)) {
            if (buttons[xC][yC - 1].getMined())
                counter++;
        }
        if (!isOutOfBounds(xC - 1, yC - 1))  {
            if (buttons[xC - 1][yC - 1].getMined())
                counter++;
        }
        return counter;         
    }
    /*
    Finds number of tiles around a tile that are not out of bounds with special algorithm for corners
    */
    public ArrayList<Tile> tilesAround(Tile t) {
        int xC = t.getX();
        int yC = t.getY();
        ArrayList<Tile> tileArray = new ArrayList<Tile>();
        if (!isOutOfBounds(xC - 1, yC) && IndexObscured(xC - 1, yC)) 
            tileArray.add(buttons[xC - 1][yC]);
        if (!isOutOfBounds(xC - 1, yC + 1) && IndexObscured(xC - 1, yC + 1) && (IndexObscured(xC, yC + 1) ||
                IndexObscured(xC - 1, yC))) 
            tileArray.add(buttons[xC - 1][yC + 1]);
        if (!isOutOfBounds(xC, yC + 1) && IndexObscured(xC, yC + 1)) 
            tileArray.add(buttons[xC][yC + 1]);
        if (!isOutOfBounds(xC + 1, yC + 1) && IndexObscured(xC + 1, yC + 1) && (IndexObscured(xC, yC + 1) ||
                IndexObscured(xC + 1, yC))) 
            tileArray.add(buttons[xC + 1][yC + 1]);
        if (!isOutOfBounds(xC + 1, yC) && IndexObscured(xC + 1, yC)) 
            tileArray.add(buttons[xC + 1][yC]);
        if (!isOutOfBounds(xC + 1, yC - 1) && IndexObscured(xC + 1, yC - 1) && (IndexObscured(xC, yC - 1) ||
                 IndexObscured(xC + 1, yC))) 
            tileArray.add(buttons[xC + 1][yC - 1]);
        if (!isOutOfBounds(xC, yC - 1) && IndexObscured(xC, yC - 1)) 
            tileArray.add(buttons[xC][yC - 1]);
        if (!isOutOfBounds(xC - 1, yC - 1) && IndexObscured(xC - 1, yC - 1) && (IndexObscured(xC - 1, yC) || 
                IndexObscured(xC, yC - 1))) 
            tileArray.add(buttons[xC - 1][yC - 1]);
        return tileArray;
    }
    public boolean IndexObscured(int r, int c) {
        return (buttons[r][c].getObscured());
    }
    public static boolean isMine(double percentage) {
        double d = Math.random();
        return d < percentage;
    }
    public int getNumOfMines() {
        return numOfMines;
    }
    public boolean isOutOfBounds(int x, int y) {
        return ((x < 0) || (x >= cols) || (y < 0) || (y >= rows));    // gud
    }
    public Tile[][] getGrid() {
        return buttons;
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
}


