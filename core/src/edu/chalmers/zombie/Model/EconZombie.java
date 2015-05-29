package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.adapter.Vector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.model.Zombie;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class EconZombie extends Zombie {

    private Sprite sprite;
    private ZWWorld world;
    private Point position;

    public EconZombie(ZWWorld world, int x, int y) {

        super(null,null,null, world, x, y);
        setType(ZombieType.ECON);
        this.world = world;
        this.sprite = sprite;
        position = new Point(x, y);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(200);
        setAngularSpeed(200);
        super.setAngularVelocity(10);
        setDamage(75);
    }

    @Override
    public Vector getVelocity() {
        return null;
    }
}
