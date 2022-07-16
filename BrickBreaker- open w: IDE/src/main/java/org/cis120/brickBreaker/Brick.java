package org.cis120.brickBreaker;

import java.awt.*;

/**
 * This class defines the standard brick object present in the game.
 */
public class Brick extends GameObj {
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private int health = 100;
    private boolean containsPoison = false;

    public Brick(int courtWidth, int courtHeight, int posX, int posY, int w, int h) {
        super(
                INIT_VEL_X, INIT_VEL_Y, posX, posY, w, h, courtWidth,
                courtHeight
        );
    }

    //getters
    public boolean getContainsPoison() {
        return this.containsPoison;
    }

    public int getHealth() {
        return this.health;
    }

    //setters
    public void setContainsPoison(boolean b) {
        this.containsPoison = b;
    }

    public void setHealth(int newHealth) {
        this.health = newHealth;
    }

    //draw with a color that depends on health of the brick
    @Override
    public void draw(Graphics g) {
        if (this.getHealth() == 100) {
            g.setColor(Color.GRAY);
        } else if (this.getHealth() == 50) {
            g.setColor(Color.LIGHT_GRAY);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

}
