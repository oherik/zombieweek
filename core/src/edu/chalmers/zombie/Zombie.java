package edu.chalmers.zombie;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie implements CreatureInterface {

    public abstract int getType();

    public abstract int getSpeed();

    @Override
    public abstract void attack();

    @Override
    public void move(int keyID) {

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
