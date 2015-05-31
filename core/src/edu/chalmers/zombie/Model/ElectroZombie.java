package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class ElectroZombie extends Zombie {

    private Sprite sprite;
    private ZWWorld world;
    private Point position;

    public ElectroZombie(ZWWorld world, int x, int y) {

        super(null,null,null, world, x, y,32);
        setType(ZombieType.ELECTRO);
        this.world = world;
        this.sprite = sprite;
        position = new Point(x, y);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(50);
        setAngularSpeed(50);
        setDamage(80);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
