=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: cpost
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D array --> I used it to implement the array of bricks that need to be destroyed in order to
  win the game. The 2D array was appropriate because it is perfect for representing the data of any
  2-D, grid-like group of objects.

  2. Collections: LinkedList --> I used a LinkedList to implement the space bar feature where the
  user is able to split the ball into 3 balls at any point during the game. A LinkedList was perfect
  because it allows me to easily call the head of the list (the original ball) which I have to do
  at certain points in my code. The LinkedList also allows mutability of length which makes it
  easier to work with:  I can fluidly add (when user clicks space-bar) and remove (when balls hit
  bottom wall) objects without IndexOutOfBounds exceptions.

   The main point of feedback I received was that my original idea of storing bricks and their
   health in a map was unnecessary. I just stored health in my Brick class instead and added the
   ball splitting feature as a more suitable use of a Collection.

  3. Subtypes --> I used subtypes in many ways throughout my code. First, MetalBrick and
  RunBrickBreakerFromFile both extend other files and override certain methods.

   My main implementation of subtypes is with my balls. I have an overlying interface call Poison
   that defines basic methods for working with the poison feature in my game. This interface defines
   setIsPoison and spreadPoison. spreadPoison is implemented in OriginalBall and extended to
   ExtraBall while setIsPoison is implemented differently in OriginalBall and ExtraBall. This
   difference in implementation is necessary because I never wanted the extraBalls to be black, even
   when they gain and then lose poison. This was important because it needed to be easy for the user
   to keep track of the original ball (which they must bounce to avoid losing) and the extraBalls
   (which they do not necessarily need to bounce).


  4. File I/O --> Finally, I implemented File I/O as a way to pause the game, save all data in a
  storage file, and reload/resume the game after terminating the program. This feature was
  appropriate for pausing and resuming because it enables storage that doesn't rely on the fields of
  any classes in the program. This is important because all fields of the classes are only existent
  when the program is running, and we want to store game data even when the game stops running.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  (1) Poison is an interface that defines methods for working with the poison state of my ball
  objects.

  (2) OriginalBall is a class that defines the Ball object, extending GameObj and implementing
  Poison.Includes fields and methods, including two overrides of the Ball interface, to enable
  the poisonfeature and corresponding adjustments in the Ball's color.

  (3) ExtraBall is a class that extends OriginalBall and implements Poison. It overrides setIsPoison
  in a different way from how OriginalBall does it so that the ball is only red or green, never
  black.

  (4) Brick is a class the defines the Brick object, extending GameObj. Includes fields and methods
  to enable the poison feature, track health, and draw the brick appropriately.

  (5) MetalBrick is a class that extends Brick and overrides draw so that the brick is drawn as an
  image, not as a solid color. This type of brick is treated differently in GameData when updating
  health because it is indestructible.

  (6) Direction is an enumeration that defines basic directions for use with GameObj.

  (7) GameObj is a super class borrowed from Mushroom of Doom that defines basic position, velocity,
   court dimension, and object dimension information used for Ball, Brick, and Platform.

  (8) Platform is a class that defines the Platform object, extending GameObj. Includes fields and
  methods to draw it and track its location, velocity, and color.

  (9) GameChanges is the class where most of the game is ran. It includes methods to reset the
  game in different ways (including pause/resume using File I/O), listen for keyboard input and make
  appropriate adjustments, initiate and track each frame, and display winning/losing screens.

  (10) GameData tracks information related to the bricks and board. It includes a method to randomly
  populate the array of bricks, and it also includes a complex method to update the health of bricks
   depending on state and subtypes of the balls and bricks involved in the collision. It includes a
   method to search for ball-brick collisions in each frame, and it also includes a method to detect
    when there are no longer any destructible bricks left in the game (indicating a win).

  (11) RunBrickBreaker sets up the dashboard of the game, including displaying player name and
  elapsed time. At the end of the run method in this class (after the dashboard is set up),
  GameChanges.reset is called to initiate play.

  (12) RunBrickBreakerFromFile sets up the dashboard in the exact same way as RunBrickBreaker, but
   calls GameChanges.resetFromFile to initiate play using saved data from storage.txt rather than
   randomly generated data. The user is still allowed to enter a new name when resuming.

  (13) Finally, StarterScreen is a class that includes a run method similar to RunBrickBreaker, but
  it sets up a starter screen from which the user can enter their name, read the instructions, and
  choose to either start a new game or resume from file. The player's choice of doing a New Game or
  Resuming determines whether RunBrickBreaker or RunBrickBreakerFromFile is called.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

One big challenge for me was figuring out how to implement Collections. I spent a lot of time
thinking about it, and I am proud of the cool space-bar feature that I added. Not only does it
implement a Collection, but it also adds a lot more strategy to the game and allows for an
additional implementation of subtypes.

Also, implementing subtypes was harder than I thought it would be. I had to think hard about how I
could abstract certain parts of the ball object in a meaningful way to highlight similarities and
differences between the OriginalBall object I generate at the start of the game and the ExtraBall
objects I generate later during the game.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think my game is designed pretty nicely. My private state is always encapsulated and accessibility
of fields between files is handled by getters and setters unless the fields are constants like
COURT_WIDTH and COURT_HEIGHT.

The separation of functionality is pretty good. I think with timer-based games, it doesn't always
make sense to completely separate Model, View, and Controller. In my case, View is mostly split
between the initial dashboard, established in RunBrickBreaker, and the drawing of objects like
bricks and balls with each frame.

My Model is largely confined to GameChanges and GameData in terms of the bricks and their health.
For the most part, GameChanges creates data, and it feeds that data into the GameData class which
then interprets and modifies the data appropriately by calling its own methods and the methods of
the Ball interface and implemented OriginalBall and ExtraBall classes.

Finally, my Controller features are necessarily split between GameChanges, RunBrickBreaker, and
StarterScreen, as the user needs to be able to interact with each dashboard as well as the game
itself in different ways.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

I didn't use any external resources, but I did (1) borrow from MushroomOfDoom and (2) reference
javadocs to find keywords like instanceOf, which I use to differentiate between subtypes in my code.
 I also referenced javadocs in addition to lecture slides when figuring out how to draw an image on
 the screen and do things like multiline instructions (which I ended up pretty much hard coding).