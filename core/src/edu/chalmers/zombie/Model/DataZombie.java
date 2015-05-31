package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class DataZombie extends Zombie {

    public DataZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie-data"),
                GameModel.getInstance().res.getTexture("zombie-data-still"),
                GameModel.getInstance().res.getTexture("zombie-data-dead"),
                world, x, y,32);



        setType(ZombieType.DATA);
        setDetectionRadius(1);
        setStartingHp(90000);
        setSpeed(800);
        setAngularSpeed(1000);
        setDamage(1000);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
