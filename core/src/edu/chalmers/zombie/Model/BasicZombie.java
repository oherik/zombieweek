package edu.chalmers.zombie.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.adapter.Vector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Zombie;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class BasicZombie extends Zombie {

   // private Sprite sprite;
    //private int hp;
    private ZWWorld world;
    private Point position;

    public BasicZombie(/*Sprite sprite,*/ ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"),world,x,y);
        setType(ZombieType.BASIC);
        this.world = world;
        //this.sprite = sprite;
        position = new Point(x, y);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(50);
        setAngularSpeed(100);
        setDamage(20);
    }

    @Override
    public Vector getVelocity() {
        return null;
    }
}
