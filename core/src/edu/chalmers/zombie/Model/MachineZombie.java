package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class MachineZombie extends Zombie {

    private Sprite sprite;
    private ZWWorld world;
    private Point position;

    public MachineZombie(ZWWorld world, int x, int y) {

        super(null,null,null, world, x, y);
        setType(ZombieType.MACHINE);
        this.world = world;
        this.sprite = sprite;
        position = new Point(x, y);
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
