package edu.chalmers.zombie.utils;

/**
 * Created by Tobias on 15-04-02.
 */
public class Constants {

    public static final int TILE_SIZE = 32;
    public static final float PIXELS_PER_METER = 32;

    //Kollisionshantering
    public static final short COLLISION_PLAYER = 2;
    public static final short COLLISION_OBSTACLE  = 4;
    public static final short COLLISION_ZOMBIE  = 8;

    //Fysikrhantering
    public static final float TIMESTEP = 1/60;

    //Matematik
    public static final float PI = 3.14159265358979323846f; //Oklart om vi behöver så mycket

}
