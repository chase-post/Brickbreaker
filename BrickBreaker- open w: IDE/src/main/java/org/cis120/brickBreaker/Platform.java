package org.cis120.brickBreaker;

import java.awt.*;

/**
 * The platform object type is controlled by the user along the bottom of the screen.
 */
public class Platform extends GameObj {
    private static int width = 50;
    private static int height = 6;
    public static final int INIT_POS_X = GameData.COURT_WIDTH / 2;
    public static final int INIT_POS_Y = GameData.COURT_HEIGHT - 20;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private static Color color;

    //constructor
    public Platform(int courtWidth, int courtHeight, Color color) {
        super(
                INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, width, height, courtWidth,
                courtHeight
        );
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
