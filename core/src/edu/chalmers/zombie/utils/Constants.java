package edu.chalmers.zombie.utils;

/**
 * Created by Tobias on 15-04-02.
 * Modified by Erik
 */
public class Constants {

    public static final int TILE_SIZE = 32;
    public static final float PIXELS_PER_METER = 32;
    public static final int PLAYER_SIZE = 32;

    //Room
    public static final String META_LAYER = "meta";
    public static final String COLLISION_PROPERTY_DOOR = "door";
    public static final String COLLISION_PROPERTY_LEVEL = "level";
    public static final String COLLISION_PROPERTY_ALL = "collision_all";
    public static final String COLLISION_PROPERTY_ZOMBIE = "collision_zombie";
    public static final String COLLISION_PROPERTY_WATER = "water";
    public static final String COLLISION_PROPERTY_SNEAK = "sneak";
    public static final String COLLISION_PROPERTY_PLAYER = "collision_player";

    public static final String POTION_SPAWN = "potion";
    public static final String BOOK_SPAWN = "book";
    public static final String ZOMBIE_SPAWN = "zombie_spawn";
    public static final String PLAYER_SPAWN = "player_spawn";
    public static final String PLAYER_RETURN = "player_return";


    //Kollisionshantering
    public static final short COLLISION_PLAYER= 2;
    public static final short COLLISION_ZOMBIE = 4;
    public static final short COLLISION_PROJECTILE = 8;
    public static final short COLLISION_ENTITY = 14;
    public static final short COLLISION_OBSTACLE  = 16;
    public static final short COLLISION_WATER  = 32;
    public static final short COLLISION_DOOR  = 64;
    public static final short COLLISION_SNEAK = 128;
    public static final short COLLISION_ACTOR_OBSTACLE = 256;
    public static final short COLLISION_POTION = 512;
    public static final short COLLISION_LEVEL = 1024;

    //Fysikhantering
    public static final float TIMESTEP = 1/60f;

    //Rörelse
    public static final float PLAYER_FRICTION_DEFAULT = 30f;
    public static final float PLAYER_FRICTION_WATER = 80f;
    public static final float PLAYER_FRICTION_SNEAK = 120f;
    public static final int MAX_PATH_COST = 200;
    public static final float PATH_UPDATE_MILLIS = 300f;

    //Matematik
    public static final float PI = 3.14159265358979323846f; //Oklart om vi behöver så mycket
    public static final float SQRT_2 = 1.4142f;

    //Potions
    public static final int POTION_HEALTH_AMOUNT = 50;
    public static final float POTION_SPEED_SCALE = 3f;
    public static final int POTION_SPEED_TIME_MILLIS = 5000;


}
