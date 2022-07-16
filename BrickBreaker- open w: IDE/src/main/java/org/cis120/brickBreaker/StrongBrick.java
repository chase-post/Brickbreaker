package org.cis120.brickBreaker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class extends brick to provide an indestructible type of brick. Serves as an obstacle during
 * the game and is drawn using an image rather than a solid color.
 */
public class StrongBrick extends Brick {

    //Constructor from super
    public StrongBrick(int courtWidth, int courtHeight, int posX, int posY, int w, int h) {
        super(courtWidth, courtHeight, posX, posY, w, h);
    }

    //Must override Draw from super Brick method because image is required
    @Override
    public void draw(Graphics g) {
        try {
            BufferedImage img = ImageIO.read(new File("files/StrongBrick.jpeg"));
            g.drawImage(img, this.getPx(), this.getPy(),
                     this.getWidth(), this.getHeight(), null);

        } catch (IOException e) {
            //if metalBrick file not found, present error message
            final JFrame frame = new JFrame("Error");
            frame.setLocation(300, 300);
            JLabel error = new JLabel("File files/StrongBrick.jpeg could not be located");
            frame.add(error);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }


}

