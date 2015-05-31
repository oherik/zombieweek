package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWBody;
import edu.chalmers.zombie.adapter.ZWSprite;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.PotionType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class for the potions in the game ZombieWeek.
 *
 * Potions are entities and contain a sprite, body and position along with
 * an ability to affect the player's properties such as increasing speed
 * and number of lives.
 *
 * Created by neda on 2015-05-19.
 * Modified by Neda
 */
public class Potion extends Entity {

    private ZWSprite sprite;
    private Point position;
    private ZWWorld world;
    private boolean hasBeenRemoved;
    private ZWVector velocity;
    private PotionType type;

    /**
     * Constructor for class Potion.
     *
     * Since no type is specified, a type will be randomized.
     * Remaining potion properties will be as specified by constructor call.
     * @param sprite
     * @param world
     * @param x
     * @param y
     */
    public Potion(ZWSprite sprite, ZWWorld world, int x, int y) {

        super(sprite, world, x, y);
        this.world = world;
        this.sprite = sprite;
        position = new Point(x, y);
        randomizePotion();
    }

    /**
     * Constructor for class Potion. Will create a potion as specified.
     * @param potionType
     * @param sprite
     * @param world
     * @param x
     * @param y
     */
    public Potion(PotionType potionType, ZWSprite sprite, ZWWorld world, int x, int y) {

        super(sprite, world, x, y);
        velocity = new ZWVector(0,0);
        this.sprite = sprite;
        this.world = world;
        position = new Point(x, y);

        setPotionType(potionType);

        ZWBody potionBody = new ZWBody();
        short categoryBits = Constants.COLLISION_POTION;
        short maskBits = Constants.COLLISION_PLAYER;
        potionBody.createBodyDef(true, x, y, 0, 0);
        potionBody.setFixtureDef(0, 0, 0.5f, 0.5f, categoryBits, maskBits, true);
        super.setBody(potionBody);
        super.scaleSprite(0.5f / Constants.TILE_SIZE);
        super.getBody().setUserData(this);

        hasBeenRemoved = false;

    }

    @Override
    public ZWVector getVelocity() {

        return velocity;
    }

    public Potion spawn(PotionType type, ZWWorld world, int x, int y) {

        return new Potion(type, sprite, world, x, y);
    }

    /**
     * A method which set's whether or not a potion has been removed.
     * @param hasBeenRemoved
     */
    public void setHasBeenRemoved(boolean hasBeenRemoved) {

        this.hasBeenRemoved = hasBeenRemoved;
    }

    /**
     * A method which removes a potion's body should it be set as removed.
     */
    public void removeIfNecessary() {

        if (hasBeenRemoved) {
            this.removeBody();
        }
    }

    /**
     * A method which returns a potion at random.
     */
    public void randomizePotion() {

        ArrayList<PotionType> types = new ArrayList<PotionType>();

        types.add(PotionType.HEALTH);
        types.add(PotionType.SPEED);
        types.add(PotionType.SUPER_STRENGTH);
        types.add(PotionType.IMMUNITY);

        int i = new Random().nextInt(4);

        PotionType pt = types.get(i);

        new Potion(pt, sprite, world, position.x, position.y);
    }

    /**
     * A method which returns the type of potion in question.
     * @return PotionType type (enum).
     */
    public PotionType getType() {
        return type;
    }

    /**
     * A method which sets the potion's type.
     * @param type desired PotionType.
     */
    public void setPotionType(PotionType type) {
        this.type = type;
    }
}
