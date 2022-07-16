package org.cis120.brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class implements Runnable in the same way as RunBrickBreaker, but its purpose is to
 * initiate the starting screen with instructions, a text field for the user to enter their name,
 * and
 * options to either start a new game or resume an old game from a file.
 */
public class StarterScreen implements Runnable {
    //String that stores player name for RunBrickBreaker Access
    private static String playerName;

    //getter
    public static String getPlayerName() {
        return playerName;
    }

    //main run function
    public void run() {
        final JFrame frame = new JFrame("BrickBreaker");
        frame.setLocation(300, 300);

        //allow user to type name
        JTextField f = new JTextField();
        f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                playerName = f.getText();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                playerName = f.getText();
            }
        });

        //buttons
        Runnable game = new RunBrickBreaker();
        Runnable gameFile = new RunBrickBreakerFromFile();
        JButton b1 = new JButton("Resume");
        b1.addActionListener(x -> SwingUtilities.invokeLater(gameFile));
        JButton b2 = new JButton("New Game");
        b2.addActionListener(x -> SwingUtilities.invokeLater(game));

        //Instructions
        JLabel l0 = new JLabel("---> *Type your name in the text field above*");
        JLabel empty0 = new JLabel("         ");
        JLabel l1 = new JLabel("---> This game is called BrickBreaker. You will use the " +
                "left and right");
        JLabel l2 =
                new JLabel("arrow keys to control a slider on the bottom of the screen.");
        JLabel empty1 = new JLabel("             ");
        JLabel l3 = new JLabel("---> There will be a bouncing ball that you must deflect " +
                "with the slider. In the");
        JLabel l4 = new JLabel("top half of the screen, there are many bricks, each which "
                + "must be hit twice by the ball");
        JLabel l5 = new JLabel("to be destroyed. Your objective is to destroy all of " +
                "the bricks in order to win. If ");
        JLabel l6 = new JLabel("the ball hits the bottom parts of the screen, " +
                "below the slider, you lose. ");
        JLabel empty2 = new JLabel("             ");
        JLabel l7 = new JLabel("---> Take Advantage of poison (ball turns green) and " +
                "beware of unbreakable bricks.");
        JLabel l8 = new JLabel("IMPORTANT: Press space-bar once per game to generate two " +
                "more disposable balls.");
        JLabel empty3 = new JLabel("             ");
        JLabel l9 = new JLabel("---> Press New Game to start a new game, " +
                "or press resume to continue a previously ");
        JLabel l10 = new JLabel("paused game. Good luck!");



        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Welcome!"));

        //set up panel with button choices and label
        panel.setLayout(new GridLayout(1, 3));
        panel.add(f);
        panel.add(b1);
        panel.add(b2);

        //add instructions to the main/center block of the frame
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(15, 1));
        panelCenter.add(l0);
        panelCenter.add(empty0);
        panelCenter.add(l1);
        panelCenter.add(l2);
        panelCenter.add(empty1);
        panelCenter.add(l3);
        panelCenter.add(l4);
        panelCenter.add(l5);
        panelCenter.add(l6);
        panelCenter.add(empty2);
        panelCenter.add(l7);
        panelCenter.add(l8);
        panelCenter.add(empty3);
        panelCenter.add(l9);
        panelCenter.add(l10);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(panelCenter, BorderLayout.CENTER);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
