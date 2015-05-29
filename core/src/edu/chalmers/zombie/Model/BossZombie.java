package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-20.
 */
public class BossZombie extends Zombie {

    private Sprite sprite;
    private ZWWorld world;
    private Point position;

    public BossZombie(ZWWorld world, int x, int y) {

        super(null,null,null,world, x, y);
        setType(ZombieType.BOSS);
        this.world = world;
        this.sprite = sprite;
        position = new Point(x, y);
        setDetectionRadius(10);
        setStartingHp(200);
        setSpeed(100);
        setAngularSpeed(100);
        setDamage(100);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
