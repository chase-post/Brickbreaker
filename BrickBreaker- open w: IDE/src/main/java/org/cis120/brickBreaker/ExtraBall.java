package org.cis120.brickBreaker;

import java.awt.*;

/**
 * This class extends ball to provide a type of ball used for the extra balls generated when
 * pressing the space-bar during the game. They are green or red, never black. This assurance is
 * made within the override of the setIsPoison method.
 */
public class ExtraBall extends OriginalBall implements Poison {
    private Color color;
    private boolean isPoison = false;

    public ExtraBall(int vX, int vY, int pX, int pY, int courtWidth, int courtHeight, Color color) {
        super(vX, vY, pX, pY, courtWidth, courtHeight, color);
    }

    //Override setIsPoison Method so that ball is green when poison is false, not black
    @Override
    public void setIsPoison(boolean b) {
        this.isPoison = b;
        if (b) {
            this.color = Color.GREEN;
        } else {
            this.color = Color.RED;
        }
    }

}