package edu.chalmers.zombie.model;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by neda on 2015-03-31.
 */
public class Player implements CreatureInterface {

    private int keyID;
    private int killCount;
    private int lives;
    private boolean isAttacked;
    private Body playerBody;

    protected Player() {

    }

    private int getKillCount() {

        return killCount;
    }

    private void incKillCount() {

        killCount = killCount + 1;
    }

    @Override
    public void move(int keyID) {

        // TODO: use Enum and controller here
    }

    public void attack(Zombie zombie) {

        // TODO: fill in with attack of zombie instance
    }

    @Override
    public void KnockOut() {

        // TODO: game over
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }

    @Override
    public void setGraphic(Body body) {

        playerBody = body;
    }

    @Override
    public Body getGraphic() {

        return playerBody;
    }


}
