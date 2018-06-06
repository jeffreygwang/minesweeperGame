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
import java.awt.event.*;
import java.util.Arrays;

public class SweeperGame  {
    public static void main(String[] args) {
        int a = gameMode();
        Board board = new Board(a, a);
        Stopwatch s = new Stopwatch();
        MinesLeft m = new MinesLeft(board);
        configureButtons(board, s, m);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.pack();
        board.setVisible(true);
        s.setSize(500, 100);
        s.setVisible(false);
        s.setTitle("StopWatch");
        m.setSize(500, 100);
        m.setVisible(false);
        m.setTitle("Mines Left");
        
    }
    public static int gameMode() { // select board size
        int dimensions = 0;
        JList list = new JList(new String[] {"Quick", "Casual", "Hardcore"});
        JOptionPane.showMessageDialog(
        null, list, "Game Type:", JOptionPane.PLAIN_MESSAGE);
        String select = Arrays.toString(list.getSelectedIndices());
        if (select.equals("[0]")) {
            dimensions = 10;
        }
        if (select.equals("[1]")) {
            dimensions = 15;
        }
        if (select.equals("[2]")) {
            dimensions = 20;
        }
        return dimensions;
    }
    /*
    Adds a mouse-listener to each JButton, and adds all necessary features.
    LEFT CLICK: If game has not begun, begins timer and "mines left" window.
    Oherwise, makes the move with setObscured() and (possibly) ends game based on result
    RIGHT CLICK: (Button 3) If tile is flagged, decrement "mines left" and vice versa
    If there was one more tile to flag and the user flags it, a win sequence happens. 
    */
    public static void configureButtons(Board b, Stopwatch s, MinesLeft m) {
        Tile[][] grid = b.getGrid();
        for (int i = 0; i < b.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                Tile TILE = grid[i][j];
                JButton tt = TILE.getButton();
                tt.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        int a = e.getButton();
                        if (a == MouseEvent.BUTTON1) {
                            if (!s.hasBegun()) {
                                s.setVisible(true);
                                s.beginTimer();
                            }   
                            if (!m.hasBegun()) {
                                m.setVisible(true);
                                m.start();
                            }
                            int game = TILE.setObscured(b);
                            if (game == 0) { // end game false
                                s.stop();
                                JOptionPane.showMessageDialog(null, "Game Over!");
                                b.revealAll(m);
                            }
                            if (game == 2) { // you win yayay
                                s.stop();
                                JOptionPane.showMessageDialog(null, "You Win!");
                                b.revealAll(m);
                            }
                        }
                        if (a == MouseEvent.BUTTON3) {
                            int result = TILE.setFlagged(b);
                            if (result == 0) {
                                m.decrement();
                            }
                            else if (result == 1) {
                                m.increment();
                            }
                            else if (result == 2) {
                                s.stop();
                                JOptionPane.showMessageDialog(null, "You Win!");
                                b.revealAll(m);
                            }
                        }
                    }
                });
            }
        }
    }
}
