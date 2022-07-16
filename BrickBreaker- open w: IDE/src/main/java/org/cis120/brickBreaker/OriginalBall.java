package org.cis120.brickBreaker;

import java.awt.*;

/**
 * This class extends brick to provide an indestructible type of brick. Serves as an obstacle during
 * the game and is drawn using an image rather than a solid color.
 */
public class OriginalBall extends GameObj implements Poison {
    public static final int SIZE = 20;
    private Color color;
    private boolean isPoison = false;

    public OriginalBall(int vX, int vY, int pX, int pY, int courtWidth,
                        int courtHeight,
                        Color color) {
        super(vX, vY, pX, pY, SIZE, SIZE, courtWidth, courtHeight);
        this.color = color;
    }

    //Getters
    public boolean getIsPoison() {
        return this.isPoison;
    }

    public Color getColor() {
        return this.color;
    }

    //Setters
    public void setColor(Color c) {
        this.color = color;
    }

    @Override
    public void setIsPoison(boolean b) {
        this.isPoison = b;
        if (b) {
            this.color = Color.GREEN;
        } else {
            this.color = Color.BLACK;
        }
    }

    //hasHitFloor detector for losing the game
    public boolean hasHitFloor(int height) {
        return (this.getPy() + this.getVy() > height - this.getHeight());
    }

    @Override
    public void spreadPoison(Brick[][] bricks, int row, int col) {
        Brick curr = bricks[row][col];
        //if ball is poison
        if (this.getIsPoison()) {
            for (int y = row - 1; y <= row + 1; y++) {
                for (int x = col - 1; x <= col + 1; x++) {
                    if (y >= 0 && y < bricks.length
                            && x >= 0 && x < bricks[y].length && bricks[y][x] != null) {
                        Brick b = bricks[y][x];
                        int currHealth = b.getHealth();
                        int newHealth = currHealth - 50;
                        if (newHealth <= 0) {
                            b = null;
                        } else {
                            b.setHealth(newHealth);
                        }
                    }
                }
            }
            int currHealth = curr.getHealth();
            int newHealth = currHealth - 50;
            if (newHealth <= 0) {
                bricks[row][col] = null;
            } else {
                curr.setHealth(newHealth);
            }
        //if ball is not poison
        } else {
            int currHealth = curr.getHealth();
            int newHealth = currHealth - 50;
            if (newHealth <= 0) {
                bricks[row][col] = null;
            } else {
                curr.setHealth(newHealth);
            }
        }
    }

    //Draw the ball
    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());

    }

}