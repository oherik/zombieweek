package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class ArchZombie extends Zombie {

    private Sprite sprite;
    //private int hp;
    private ZWWorld world;
    private Point position;

    public ArchZombie(ZWWorld world, int x, int y) {

        super(null,null,null, world, x, y,32);
        setType(ZombieType.ARCH);
        this.world = world;
        this.sprite = sprite;
        position = new Point(x, y);
        setDetectionRadius(100);
        setStartingHp(500);
        setSpeed(800);
        setDamage(200);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
