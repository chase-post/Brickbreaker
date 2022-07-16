package org.cis120.brickBreaker;

public interface Poison {
    //used to manage poison state of ball and associated color
    public void setIsPoison(boolean b);

    //used to spread poison to bricks within radius of 1 brick
    public void spreadPoison(Brick[][] bricks, int row, int col);
}
