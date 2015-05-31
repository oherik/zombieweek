package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * A machine zombie has a low speed and an intermediate resilience. It causes a damage of 80 percent.
 * It has a detection radius of 100, which is very high.
 *
 * Created by neda on 2015-05-19.
 * Modified by Erik
 */
public class MachineZombie extends Zombie {

    public MachineZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"), world, x, y,32);
        setType(ZombieType.MACHINE);
        setDetectionRadius(100);
        setStartingHp(50);
        setSpeed(30);
        setAngularSpeed(30);
        setDamage(80);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
