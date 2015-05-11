package edu.chalmers.zombie.testing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.utils.ZombieType;

/**
 * Created by Tobias on 15-04-21.
 */
public class ZombieTest extends Zombie {

    public ZombieTest(World world, float x, float y){
        super(new Sprite(new Texture("core/assets/zombie_test.png")),world,x,y);
    }


    @Override
    public int getType() {
        return 0;
    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }

    @Override
    public void attack(Player player) {

    }

    @Override
    public Zombie spawn(World world, ZombieType type, int x, int y) {

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
