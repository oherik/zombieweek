package edu.chalmers.zombie;

/**
 * Created by neda on 2015-03-31.
 */
public class Player implements CreatureInterface {

    private int keyID;

    protected Player() {

    }

    public void setLastKeyPressed(int keyID) {

        this.keyID = keyID;
    }

    public int getLastKeyPressed() {

        return keyID;
    }

    @Override
    public void move(int keyID) {

        switch (keyID) {
            case 1: // Move avatar UP; setLastKeyPressed;
                break;
            case 2: // Move avatar LEFT; setLastKeyPressed;
                break;
            case 3: // Move avatar DOWN; setLastKeyPressed;
                break;
            case 4: // Move avatar RIGHT; setLastKeyPressed;
                break;
            default: // Do absolutely nothing
                break;
        }
    }

    @Override
    public void attack() {

    }

    @Override
    public boolean isKnockedOut(boolean isKnockedOut) {
        return false;
    }

    @Override
    public boolean hasBeenAttacked(boolean isAttacked) {
        return false;
    }
}
