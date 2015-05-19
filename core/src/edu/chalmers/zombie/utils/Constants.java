package edu.chalmers.zombie.utils;

/**
 * Created by Tobias on 15-04-02.
 */
public class Constants {

    public static final int TILE_SIZE = 32;
    public static final float PIXELS_PER_METER = 32;
    public static final int PLAYER_SIZE = 28;

    //Level
    public static final String META_LAYER = "meta";
    public static final String TOP_LAYER = "top";
    public static final String BOTTOM_LAYER = "bottom";
    public static final String DOOR_PROPERTY = "door"; //TODO för resten med?
    public static final String COLLISION_PROPERTY_ALL = "collision_all"; //TODO för resten med?
    public static final String COLLISION_PROPERTY_ZOMBIE = "collision_zombie"; //TODO för resten med?
    public static final String COLLISION_PROPERTY_WATER = "water";
    public static final String COLLISION_PROPERTY_SNEAK = "sneak";

    //Kollisionshantering
    public static final short COLLISION_PLAYER= 2;
    public static final short COLLISION_ZOMBIE = 4;
    public static final short COLLISION_PROJECTILE = 8;
    public static final short COLLISION_ENTITY = 14;
    public static final short COLLISION_OBSTACLE  = 16;
    public static final short COLLISION_WATER  = 32;
    public static final short COLLISION_DOOR  = 64;
    public static final short COLLISION_SNEAK = 256;

    //Fysikhantering
    public static final float TIMESTEP = 1/60f;

    //Rörelse
    public static final float PLAYER_FRICTION_DEFAULT = 30f;
    public static final float PLAYER_FRICTION_WATER = 80f;


    //Matematik
    public static final float PI = 3.14159265358979323846f; //Oklart om vi behöver så mycket
    public static final float SQRT_2 = 1.4142f;

}
