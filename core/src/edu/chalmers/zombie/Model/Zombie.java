package edu.chalmers.zombie.model;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie implements CreatureInterface {

    private boolean isAttacked;

    public abstract int getType();

    public abstract int getSpeed();

    public abstract void attack(Player player);

    public void remove(Zombie zombie) {

        //TODO: remove zombie
    }

    @Override
    public void move(int keyID) {

    }

    @Override
    public void KnockOut() {

        // TODO: remove zombie
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }
}
