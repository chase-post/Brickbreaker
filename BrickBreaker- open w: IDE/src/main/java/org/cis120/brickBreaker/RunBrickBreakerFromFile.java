package org.cis120.brickBreaker;

/**
 * This class extends RunBrickBreaker, using the same run method to setup an identical dashboard,
 * but calling gc.restFromFile() instead of gc.reset() to resume the game from the storage.txt file
 * instead of randomly generating a new game
 */
public class RunBrickBreakerFromFile extends RunBrickBreaker {

    //override the finalAction method to call gc.resetFromFile instead of gc.reset
    @Override
    public void finalAction(GameChanges gc) {
        gc.resetFromFile();
    }
}
