package edu.chalmers.zombie.testing;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.model.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.ZombieType;
//import sun.awt.motif.X11CNS11643;

import java.awt.*;

/**
 * Created by Tobias on 15-04-21.
 */
public class ZombieTest extends Zombie {

    private ZombieType type;

    public ZombieTest(World world, float x, float y){
        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"),world,x,y);
        setZombiePosition(new Point((int) x, (int) y));
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



}
