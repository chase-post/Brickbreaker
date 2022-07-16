package org.cis120.brickBreaker;

import java.awt.*;
import java.util.*;

/**
 * This class handles generating and updating the array of bricks. When the array is empty, there
 * are no more "living" bricks and the player has won. GameChanges feeds data into GameData via
 * constructor for processing with the methods contained in this class.
 */
public class GameData {
    private LinkedList<OriginalBall> balls;
    private Brick[][] bricks;
    public static final int COURT_WIDTH = 400;
    public static final int COURT_HEIGHT = 400;

    /**
     * Populates array of bricks with a 70% chance of bricks spawning in any given location. Helper
     * method for constructor.
     */
    public void populate() {
        //variable to ensure metal bricks are limited to 4
        int numMetal = 0;
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[row].length; col++) {
                //70% chance of brick at any given location
                if (Math.random() > 0.3) {
                    bricks[row][col] = new Brick(COURT_WIDTH, COURT_HEIGHT,
                            col * (COURT_WIDTH / bricks[row].length),
                            row * ((COURT_HEIGHT - 100) / bricks.length),
                            (COURT_WIDTH / bricks[row].length) - 3,
                            ((COURT_HEIGHT - 100) / bricks.length) - 3);
                    //20% chance any given brick is poison
                    if (Math.random() > 0.8) {
                        bricks[row][col].setContainsPoison(true);
                    //10% chance any non-poison bricks is metal
                    } else if (Math.random() > 0.90 && numMetal < 4) {
                        bricks[row][col] = new StrongBrick(COURT_WIDTH, COURT_HEIGHT,
                                col * (COURT_WIDTH / bricks[row].length),
                                row * ((COURT_HEIGHT - 100) / bricks.length),
                                (COURT_WIDTH / bricks[row].length) - 3,
                                ((COURT_HEIGHT - 100) / bricks.length) - 3);
                        numMetal++;
                    }
                } else {
                    bricks[row][col] = null;
                }
            }
        }
    }


    /**
     * Constructs GameData object by populating the data map with every brick in the inputted
     * 2D array, giving them a full health of 100.
     * @param brickArray
     */
    public GameData(Brick[][] brickArray, LinkedList<OriginalBall> balls) {
        this.bricks = brickArray;
        this.populate();
        this.balls = balls;
    }

    /**
     * Alternate Constructor, used when population of the brick array is not necessary
     */
    public GameData(Brick[][] brickArray, LinkedList<OriginalBall> balls, boolean b) {
        if (b) {
            this.bricks = brickArray;
            this.balls = balls;
        } else {
            this.bricks = null;
            this.balls = null;
        }
    }

    /**
     * Updates the health of any given brick, specified by the row and column at
     * which the brick is located. Calls methods from OriginalBall superclass and interface.
     * @param row
     * @param col
     * @return boolean representing whether the brick still exists after losing health
     */
    public void updateHealth(OriginalBall ball, int row, int col) {
        Brick curr = bricks[row][col];
        if (curr instanceof StrongBrick) {
            return;
        }
        if (curr.getContainsPoison()) {
            ball.setIsPoison(!ball.getIsPoison());
            //if ball not poison and not original ball, make it red again
            if (!ball.getIsPoison() && ball != balls.getFirst()) {
                ball.setColor(Color.RED);
            }
        }

        //check for poison and handle spreading of poison
        ball.spreadPoison(bricks, row, col);

    }

    /**
     * Enables bouncing of param ball against bricks in the 2D array, updating the health of the
     * bricks as necessary using the updateHealth method.
     * @param b
     */
    public void bricksBounce(OriginalBall b) {
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[row].length; col++) {
                Brick curr = bricks[row][col];
                if (curr != null) {
                    Direction d = b.hitObj(curr);
                    b.bounce(d);
                    if (d != null) {
                        this.updateHealth(b, row, col);
                    }
                }
            }
        }
    }

    /**
     * Determines if there are bricks left on the game board to be broken.
     * @return boolean representing whether the map is empty
     */
    public boolean boardIsEmpty() {
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[row].length; col++) {
                Brick curr = bricks[row][col];
                if (curr != null && !(curr instanceof StrongBrick)) {
                    return false;
                }
            }
        }
        return true;
    }

}
