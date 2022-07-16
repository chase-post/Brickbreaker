package org.cis120.brickBreaker;

// imports necessary libraries for Java swing
import java.awt.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI, implements Runnable to set up
 * the game's dashboard and initiate the GameChanges class, which then relies on GameData
 */
public class RunBrickBreaker implements Runnable {

    //finalAction method specifies which reset method to call at end of run
    public void finalAction(GameChanges gc) {
        gc.reset();
    }

    //initiate this label as public, so it can be accessed in GameChanges
    private static JLabel time = new JLabel("" + GameChanges.getTimeElapsed());

    //getter
    public static JLabel getTimeLabel() {
        return time;
    }

    //main run function
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        /* Top-level frame in which game components live.
         * Be sure to change "TOP LEVEL FRAME" to the name of your game
         */
        final JFrame frame = new JFrame("BrickBreaker");
        frame.setLocation(400, 400);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameChanges court = new GameChanges(status);
        frame.add(court, BorderLayout.CENTER);

        //Control Panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        //Display name
        final JLabel l = new JLabel("Your Name: " + StarterScreen.getPlayerName());
        control_panel.add(l);

        /* Note here that when we add an action listener to the reset button, we
         * define it as an anonymous inner class that is an instance of
         * ActionListener with its actionPerformed() method overridden. When the
         * button is pressed, actionPerformed() will be called.
         */
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        //Pause button
        final JButton pause = new JButton("Pause");
        pause.addActionListener(e -> court.pause());
        control_panel.add(pause);

        //Time Elapsed Display
        control_panel.add(time);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //resets game
        finalAction(court);
    }
}