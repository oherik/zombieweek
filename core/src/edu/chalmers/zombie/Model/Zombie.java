package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie extends Entity implements CreatureInterface {

    public Zombie(Sprite sprite, World world, float x, float y){
        super(sprite,world,x,y, Constants.COLLISION_ZOMBIE);
    }



    private boolean isAttacked;

    public abstract int getType();

    public abstract int getSpeed();

    public abstract void attack(Player player);

    public void remove(Zombie zombie) {

        //TODO: remove zombie
    }

    @Override
    public void move(Direction direction) {

    }

    @Override
    public void KnockOut() {

        // TODO: remove zombie
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }
}
