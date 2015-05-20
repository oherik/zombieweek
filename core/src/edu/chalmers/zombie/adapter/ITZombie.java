package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-20.
 */
public class ITZombie extends Zombie {

    private Sprite sprite;
    //private int hp;
    private World world;
    private Point position;

    public ITZombie(Sprite sprite, World world, int x, int y) {

        super(sprite, world, x, y);
        setType(ZombieType.MACHINE);
        this.world = world;
        this.sprite = sprite;
        position = new Point(x, y);
        setDetectionRadius(5);
        setStartingHp(100);
        setSpeed(30);
    }

    @Override
    public void attack() {

    }

    @Override
    public Zombie spawn(World world, int x, int y) {

        return new ITZombie(sprite, world, x, y);
    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }
}
