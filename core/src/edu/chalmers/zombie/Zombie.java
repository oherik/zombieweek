package edu.chalmers.zombie;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie implements CreatureInterface {

    public abstract int getType();

    public abstract int getSpeed();

    public abstract void attack(Player player);

    @Override
    public void move(int keyID) {

    }

    @Override
    public void KnockOut() {

        // TODO: remove zombie
    }

    @Override
    public boolean hasBeenAttacked() {
        return false;
    }
}
