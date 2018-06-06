/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Minesweeper;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author JeffreyWang
 */

public class MinesLeft extends JFrame implements Runnable {
    private JLabel disp;
    private int numOfMines;
    private boolean initialized;
    
    public MinesLeft(Board b) {
        disp = new JLabel();
        disp.setFont(new Font("Helvetica", Font.PLAIN, 30));
        disp.setBackground(Color.cyan);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(disp);
        numOfMines = b.getNumOfMines();
        initialized = false;
    }
    public void start() {
        initialized = true;
        Thread t = new Thread(this);
        t.start();
    }
    public boolean hasBegun() {
        return initialized;
    }
    public void run() {
        disp.setText("" + numOfMines);
    }
    public void decrement() {
        numOfMines--;
        disp.setText("" + numOfMines);
    }
    public void increment() {
        numOfMines++;
        disp.setText("" + numOfMines);
    }
    public int minesDisplayed() {
        return numOfMines;
    }
}
