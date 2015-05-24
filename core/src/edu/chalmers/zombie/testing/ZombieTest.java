package edu.chalmers.zombie.testing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.utils.ZombieType;
//import sun.awt.motif.X11CNS11643;

import java.awt.*;

/**
 * Created by Tobias on 15-04-21.
 */
public class ZombieTest extends Zombie {

    private ZombieType type;

    public ZombieTest(World world, float x, float y){
        super(new Sprite(new Texture("core/assets/Images/zombie.png")),world,x,y);
        setZombiePosition(new Point((int)x, (int)y));
    }


    @Override
    public ZombieType getType() {
        return type;
    }

    @Override
    public void setType(ZombieType type) {
        this.type = type;
    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }

    @Override
    public void attack() {

    }

    @Override
    public Zombie spawn(World world, int x, int y) {

        return new ZombieTest(world, x, y);
    }

    @Override
    public void setBody(Body body) {

    }

    @Override
    public Body getBody() {
        return null;
    }


}
