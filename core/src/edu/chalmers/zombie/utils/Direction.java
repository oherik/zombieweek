package edu.chalmers.zombie.utils;

/**
 * Created by Tobias on 15-04-02.
 */
public enum Direction {
    SOUTH(Constants.PI),
    NORTH(0),
    WEST(Constants.PI/2),
    EAST(-Constants.PI/2),
    SOUTH_WEST(Constants.PI*3/4),
    NORTH_WEST(Constants.PI/4),
    SOUTH_EAST(-Constants.PI*3/4),
    NORTH_EAST(-Constants.PI/4)
    ;

    private float rotation;

    Direction(float rotation){
        this.rotation = rotation;
    }

    public float getRotation(){
        return rotation;
    }
}
