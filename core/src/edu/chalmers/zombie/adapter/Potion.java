package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.utils.PotionType;

import java.awt.*;

/**
 * Created by neda on 2015-05-19.
 */
public class Potion extends Entity {

    private Sprite sprite;
    private Point position;
    private boolean hasBeenRemoved;
    private Vector2 velocity;

    public Potion(PotionType potionType, Sprite sprite, World world, int x, int y) {

        super(sprite, world, x, y);
        velocity = new Vector2(0,0);

        switch (potionType) {
            case HEALTH:

                break;
            case SPEED:

                break;
            case IMMUNITY:

                break;
            case SUPER_STRENGTH:

                break;
            default:

                break;
        }

        hasBeenRemoved = false;

    }

    @Override
    public Vector2 getVelocity() {

        return velocity;
    }

    public Potion spawn(PotionType type, World world, int x, int y) {

        return new Potion(type, sprite, world, x, y);
    }

    public void setHasBeenRemoved(boolean hasBeenRemoved) {

        this.hasBeenRemoved = hasBeenRemoved;
    }
}
