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
import java.awt.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * StopWatch
 *
 * @author Santhosh Reddy Mandadi
 * @since 08-Jun-2009
 * @version 1.0
 */
public class Stopwatch extends JFrame implements Runnable { // ActionListener

    private JLabel disp;
    private boolean stop = false;
    private int a, b, c, d;
    private boolean begun = false;

    public Stopwatch() {
        disp = new JLabel();
        // btn = new JButton("Start");
        disp.setFont(new Font("Helvetica", Font.PLAIN, 30));
        disp.setBackground(Color.cyan);
        disp.setForeground(Color.red);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(disp);
    }
    public void stop() {
        stop = true;
    }
    public void run() {
        for (a = 0;; a++) {
            for (b = 0; b < 60; b++) {
                for (c = 0; c < 60; c++) {
                    for (d = 0; d < 100; d++) {
                        if (stop) {
                            break;
                        }
                        NumberFormat nf = new DecimalFormat("00");
                        disp.setText(nf.format(a) + ":" + nf.format(b) + ":" + nf.format(c) + ":" + nf.format(d));
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }
    public boolean hasBegun() {
        return begun;
    }
    public void beginTimer() {
        begun = true;
        Thread t = new Thread(this);
        t.start();
    }
}
