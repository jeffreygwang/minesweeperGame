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
/*
"Tile" is an all-purpose wrapper class for the JButtons 
*/
public class Tile { 
    // FIELDS
    private boolean isFlagged; // true = tile is flagged, false = tile is not flagged
    private int number; // number on the tile; 0 is no mines around it, "9" is MINED(only used for debugging purposes)
    private boolean isMined; // true = mined, false = not mined
    private boolean isObscured;  // true = not clicked, false = clicked 
    private JButton tile; // the JButton that each 
    private int x; // this is the ROW NUMBER (technically y-coord) in the 2D Array "buttons"  in BOARD
    private int y; // this is the COL NUMBER (technically x-coord) in the 2D Array "buttons"  in BOARD

    public boolean getFlagged() {
        return isFlagged;
    }
    public void setFlaggedFalse() {
        isFlagged = false;
    }
    public JButton getButton() {
        return tile;
    }
    public int getNumber() {
        return number;
    }
    public boolean getMined() {
        return isMined;
    }
    public boolean getObscured() {
        return isObscured;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setNum(int n) {
        number = n;
    }
    public void setObscuredValue() {
        isObscured = false;
    }
    /*
    resizeAndReturn takes an ImageIcon and returns it so that it fits each 40x40 tile well
    This is done by changing the ImageIcon to an image, resizing the image, and changing it back
    to an ImageIcon
    */
    public static ImageIcon resizeAndReturn(ImageIcon i) {
        Image tempImage = i.getImage();
        Image newImage = tempImage.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
        ImageIcon returnIcon = new ImageIcon(newImage);
        return returnIcon;
    }
    /*
    setObscured is run when a tile is clicked. 
    RETURN VALUES:
    0 = game over (user loses)
    1 = default return; nothing happens
    2 = game over (user wins)
    These values are determined by the method isGameWon, which can be found in the Board class
    ALGORITHM:
    If tile is flagged, do nothing. If tile is mined, change tile icon and end game. If tile is numbered, change to number and end game.
    IF TILE IS EMPTY:
    Place 8 tiles around the tile in a queue.
    Look at the 8 tiles around each of those 8 tiles that is EMPTY.
    Continue until queue is empty.
    More information (esp. about special corner cases) can be found in the tilesAround method in Board class.
    */
    public int setObscured(Board b) { 
        if (isFlagged) {
        } else {
            if (isMined) { 
                ImageIcon i = new ImageIcon("Images/MinesweeperMine.png");
                i = resizeAndReturn(i);
                getButton().setIcon(i);
                this.setObscuredValue();
                return 0;
            } else {
                if (number != 0) { // OK since mine checked
                    String call = "Images/M" + number + ".png"; // "Images/M7.png"
                    ImageIcon h = new ImageIcon(call);
                    h = resizeAndReturn(h);
                    getButton().setIcon(h);
                    this.setObscuredValue();
                } else {
                    ImageIcon nothing = new ImageIcon("Images/MinesweeperClickedPNG.png");
                    nothing = resizeAndReturn(nothing);
                         
                    ArrayList<Tile> queue = new ArrayList<Tile>();
                    queue = b.tilesAround(this); 
                    this.setObscuredValue();
                    this.getButton().setIcon(nothing);
                    while (queue.size() > 0) {
                        queue.get(0).setObscuredValue();
                        if (queue.get(0).getNumber() > 0) { // if the num is greater than 0 reveal
                            String call = "Images/M" + queue.get(0).getNumber() + ".png";
                            ImageIcon h = new ImageIcon(call);
                            h = resizeAndReturn(h);
                            queue.get(0).getButton().setIcon(h);
                            queue.remove(0);
                        } else { // if the num is 0 reveal then add tiles around into queue
                            queue.get(0).getButton().setIcon(nothing);
                            queue.addAll(b.tilesAround(queue.get(0)));
                            queue.remove(0);
                        }
                    }

                }
                
            }
        }
        if (b.isGameWon()) {
            return 2;
        }
        return 1;
    }
    /*
    This method handles setting or un-setting flags. The return values are for the window that shows how many mines are left. 
    A return value of 0 indicates that a flag was set, a return value of 1 indicates a flag was unset. 2 shows game is won. 3 is default. 
    */
    public int setFlagged(Board b) {
        if ((!isFlagged) && isObscured) {
            isFlagged = true;
            ImageIcon i = new ImageIcon("Images/Flag.png");
            i = resizeAndReturn(i);
            getButton().setIcon(i);
            if (b.isGameWon()) {
                return 2;
            }
            return 0;
        }
        else if (isFlagged && isObscured) {
            isFlagged = false;
            ImageIcon i = new ImageIcon("Images/MinesweeperUnclickedPNG.png");
            i = resizeAndReturn(i);
            getButton().setIcon(i);
            return 1;
        }
        return 3;
    }
    
    public Tile(JButton j, boolean mined, int xC, int yC) { 
        isFlagged = false;
        isMined = mined;
        if (isMined) {
            number = 9;
        } else {
            number = 0;
        }
        isObscured = true;
        tile = j;
        x = xC;
        y = yC;
    }
}
