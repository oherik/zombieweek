package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class BasicZombie extends Zombie {

   // private Sprite sprite;
    //private int hp;
    private World world;
    private Point position;

    public BasicZombie(/*Sprite sprite,*/ World world, int x, int y) {

        super(new Sprite(new Texture("core/assets/Images/zombie.png")),world,x,y);
        setType(ZombieType.BASIC);
        this.world = world;
        //this.sprite = sprite;
        position = new Point(x, y);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(50);
    }

    @Override
    public void attack() {

    }

    @Override
    public Zombie spawn(World world, int x, int y) {

        return new BasicZombie(world, x, y);
    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }
}
