package org.cis120.brickBreaker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;

/** Where updates to data and screen occur
 * GameChanges
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameChanges extends JPanel {

    // the state of the game logic
    private LinkedList<OriginalBall> balls = new LinkedList<>();
    private Platform platform;
    private Brick[][] bricks = new Brick[4][5];
    private GameData data;
    private static double timeElapsed;

    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = GameData.COURT_WIDTH;
    public static final int COURT_HEIGHT = GameData.COURT_HEIGHT;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    //getter
    public static double getTimeElapsed() {
        return timeElapsed;
    }

    public GameChanges(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        /* The timer is an object which triggers an action periodically with the
         * given INTERVAL. We register an ActionListener with this timer, whose
         * actionPerformed() method is called each time the timer triggers. We
         * define a helper method called tick() that actually does everything
         * that should be done in a single time step.
         */
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        /* This key listener allows the square to move as long as an arrow key
         * is pressed, by changing the square's velocity accordingly. (The tick
         * method below actually moves the square.)
         */
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    platform.setVx(-10);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    platform.setVx(10);
                }
            }
            public void keyReleased(KeyEvent e) {
                platform.setVx(0);
            }
        });

        /* This key listener allows the user to press space bar once per game and generate
         * two additional red balls that don't need to be bounced by the platform, they can only
         * help the player
         */
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (balls.size() < 3) {
                        OriginalBall b = balls.get(0);
                        OriginalBall newBall2 = new ExtraBall(- 2 * b.getVx(), b.getVy(),
                                b.getPx(), b.getPy(), COURT_WIDTH, COURT_HEIGHT, Color.red);
                        OriginalBall newBall3 = new ExtraBall(2 * b.getVx(), b.getVy(),
                                b.getPx(), b.getPy(), COURT_WIDTH, COURT_HEIGHT, Color.red);
                        balls.add(newBall2);
                        balls.add(newBall3);
                    }
                }
            }
        });
        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        balls = new LinkedList<>();
        OriginalBall ball = new OriginalBall(3, 7, 150,
                200, COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        ball.setIsPoison(false);
        balls.add(ball);
        platform = new Platform(COURT_WIDTH, COURT_HEIGHT, Color.BLUE);
        timeElapsed = 0;

        // instantiate our health tracking object, including population
        data = new GameData(bricks, balls);

        //set starting conditions of game
        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    public void resetFromFile() {
        balls = new LinkedList<>();

        platform = new Platform(COURT_WIDTH, COURT_HEIGHT, Color.BLUE);
        timeElapsed = 0;

        //read and populate array of bricks from file
        resume();

        //alternate GameData Constructor, does not populate the bricks array
        data = new GameData(bricks, balls, true);

        //set starting conditions of game
        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called whenever the game is paused using the pause button. This longPause
     * method works by ending the game and storing the ball's information as well
     * as each bricks information in a CSV file denoted "storage.txt". The user has an option
     * when they restart the game to select "resume" which uses longResume to retrieve
     * the information from storage.txt and start the game from that point.
     */
    public void pause() {
        playing = false;
        try {
            FileWriter writer = new FileWriter("files/storage.txt");
            writer.write((int) timeElapsed + "\n");
            writer.write("" + balls.size() + "\n");
            for (OriginalBall ball : balls) {
                String ballInfo = ball.getPx() + " " + ball.getPy() + " "
                        + ball.getVx() + " " + ball.getVy() + " " + ball.getColor() + "\n";
                writer.write(ballInfo);
            }
            for (int row = 0; row < bricks.length; row++) {
                for (int col = 0; col < bricks[0].length; col++) {
                    Brick curr = bricks[row][col];
                    if (curr != null && (curr instanceof StrongBrick)) {
                        String s = "m " + row + " " + col + " " + curr.getHealth() + "\n";
                        writer.write(s);
                    } else if (curr != null && !(curr instanceof StrongBrick)) {
                        String s = "b " + row + " " + col + " " + curr.getHealth() + "\n";
                        writer.write(s);
                    }
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            //if storage file not found, present error message
            final JFrame frame = new JFrame("Error");
            frame.setLocation(300, 300);
            JLabel error = new JLabel("File files/storage.txt could not be located");
            frame.add(error);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    /**
     * This method is used to resume a game that has been terminated midway through. When the user
     * selects resume in the starting screen, this method is called from resetFromFile and it reads
     * the information from the storage.txt file line by line, initializing the ball and brick
     * array objects appropriately given the stored information.
     */
    public void resume() {
        try {
            //initialize File and buffered readers
            FileReader reader = new FileReader("files/storage.txt");
            BufferedReader br = new BufferedReader(reader);
            //read timeElapsed from file
            String t = br.readLine();
            timeElapsed = Integer.parseInt(t);
            //read numBalls iterate through each ball, assigning info appropriately
            int numBalls = Integer.parseInt(br.readLine());
            for (int i = 0; i < numBalls; i++) {
                String s = br.readLine();
                String[] coordinates = s.split(" ");
                int ballPx = Integer.parseInt(coordinates[0]);
                int ballPy = Integer.parseInt(coordinates[1]);
                int ballVx = Integer.parseInt(coordinates[2]);
                int ballVy = Integer.parseInt(coordinates[3]);
                String ballColor = coordinates[4];
                //initialize ball type based on color and position in LinkedList
                if (ballColor.equals("java.awt.Color[r=0,g=0,b=0]")) {
                    balls.add(new OriginalBall(ballVx, ballVy, ballPx, ballPy,
                            COURT_WIDTH, COURT_HEIGHT, Color.BLACK));
                } else if (ballColor.equals("java.awt.Color[r=255,g=0,b=0]")) {
                    balls.add(new ExtraBall(ballVx, ballVy, ballPx, ballPy,
                            COURT_WIDTH, COURT_HEIGHT, Color.RED));
                } else if (ballColor.equals("java.awt.Color[r=0,g=255,b=0]") && i == 0) {
                    OriginalBall b = new OriginalBall(ballVx, ballVy, ballPx, ballPy,
                            COURT_WIDTH, COURT_HEIGHT, Color.GREEN);
                    b.setIsPoison(true);
                    balls.add(b);
                } else if (ballColor.equals("java.awt.Color[r=0,g=255,b=0]") && i > 0) {
                    OriginalBall b = new ExtraBall(ballVx, ballVy, ballPx, ballPy,
                            COURT_WIDTH, COURT_HEIGHT, Color.GREEN);
                    b.setIsPoison(true);
                    balls.add(b);
                }
            }
            //iterate through the rest of the lines and initialize each brick
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] words = line.split(" ");
                String metal = words[0];
                int row = Integer.parseInt(words[1]);
                int col = Integer.parseInt(words[2]);
                int health = Integer.parseInt(words[3]);
                if (metal.equals("b")) {
                    bricks[row][col] = new Brick(COURT_WIDTH, COURT_HEIGHT,
                            col * (COURT_WIDTH / bricks[row].length),
                            row * ((COURT_HEIGHT - 100) / bricks.length),
                            (COURT_WIDTH / bricks[row].length) - 3,
                            ((COURT_HEIGHT - 100) / bricks.length) - 3);
                    bricks[row][col].setHealth(health);
                } else {
                    bricks[row][col] = new StrongBrick(COURT_WIDTH, COURT_HEIGHT,
                            col * (COURT_WIDTH / bricks[row].length),
                            row * ((COURT_HEIGHT - 100) / bricks.length),
                            (COURT_WIDTH / bricks[row].length) - 3,
                            ((COURT_HEIGHT - 100) / bricks.length) - 3);
                    bricks[row][col].setHealth(health);
                }

            }
        //
        } catch (IOException e) {
            //if storage file could not be found, present error message
            final JFrame frame = new JFrame("Error");
            frame.setLocation(300, 300);
            JLabel error = new JLabel("File files/storage.txt could not be located" +
                    " or is mal-typed. Please start new game.");
            frame.add(error);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            timeElapsed += 0.035;
            RunBrickBreaker.getTimeLabel().setText("Time Elapsed: " + (int) (timeElapsed));

            //move platform
            platform.move();

            //iterate through list of balls and account for each
            for (OriginalBall ball : balls) {
                //move ball
                ball.move();
                // make the ball bounce off walls...
                ball.bounce(ball.hitWall());

                //make ball bounce off platform
                ball.bounce(ball.hitObj(platform));

                // make the ball bounce off any bricks... update brick health
                data.bricksBounce(ball);
            }

            //lose message if intersects with lower wall
            OriginalBall original = balls.get(0);
            if (original.hasHitFloor(COURT_HEIGHT)) {
                playing = false;
                System.out.println("You lose!");
                // Put lose frame on the screen
                JFrame loseScreen = new JFrame("You Lose!");
                loseScreen.setLocation(300, 300);
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(3, 1));
                JLabel extra = new JLabel("                  ");
                JLabel message = new JLabel("Close this screen and click reset to try again.");
                JLabel extra2 = new JLabel("             ");
                panel.add(extra);
                panel.add(message);
                panel.add(extra2);
                loseScreen.add(panel);
                loseScreen.pack();
                loseScreen.setVisible(true);
            }

            //remove extraBalls from ball collection if intersect lower wall
            if (balls.size() > 1) {
                OriginalBall x = balls.getLast();
                if (x.hasHitFloor(COURT_HEIGHT)) {
                    balls.remove(x);
                    OriginalBall y = balls.getLast();
                    if (y.hasHitFloor(COURT_HEIGHT)) {
                        balls.remove(x);
                    }
                } else if (balls.get(1).hasHitFloor(COURT_HEIGHT)) {
                    balls.remove(balls.get(1));
                }
            }

            //win message if all bricks are gone
            if (data.boardIsEmpty()) {
                playing = false;
                System.out.println("You win!");
                // Put win frame on the screen
                JFrame winScreen = new JFrame("You Win!");
                winScreen.setLocation(300, 300);
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(3, 1));
                JLabel extra = new JLabel("                  ");
                JLabel message = new JLabel("Close this screen and click " +
                        "reset to play again!");
                JLabel extra2 = new JLabel("             ");
                panel.add(extra);
                panel.add(message);
                panel.add(extra2);
                winScreen.add(panel);
                winScreen.pack();
                winScreen.setVisible(true);
            }

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (OriginalBall ball : balls) {
            ball.draw(g);
        }
        platform.draw(g);
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[row].length; col++) {
                if (bricks[row][col] != null) {
                    bricks[row][col].draw(g);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}