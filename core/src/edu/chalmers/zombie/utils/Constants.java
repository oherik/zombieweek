package edu.chalmers.zombie.utils;

/**
 * Created by Tobias on 15-04-02.
 */
public class Constants {

    public static final int TILE_SIZE = 32;
    public static final float PIXELS_PER_METER = 32;

    //Kollisionshantering
    public static final short COLLISION_PLAYER= 2;
    public static final short COLLISION_ZOMBIE = 4;
    public static final short COLLISION_PROJECTILE = 8;
    public static final short COLLISION_ENTITY = 14;
    public static final short COLLISION_OBSTACLE  = 16;
    public static final short COLLISION_WATER  = 32;
    public static final short COLLISION_DOOR_NEXT  = 64;
    public static final short COLLISION_DOOOR_PREVIOUS  = 128;


    //Fysikhantering
    public static final float TIMESTEP = 1/60f;

    //Matematik
    public static final float PI = 3.14159265358979323846f; //Oklart om vi behöver så mycket
    public static final float SQRT_2 = 1.4142f;

}
