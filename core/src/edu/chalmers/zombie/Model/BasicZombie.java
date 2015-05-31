package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class BasicZombie extends Zombie {

    public BasicZombie(/*Sprite sprite,*/ ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"),world,x,y,32);
        setType(ZombieType.BASIC);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(50);
        setAngularSpeed(100);
        setDamage(20);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
