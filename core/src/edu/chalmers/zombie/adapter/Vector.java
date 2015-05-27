package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;

/** An adapter class for libGDX's Vector2
 */
public class Vector {
    private Vector2 vector;

    public Vector(){
        vector = new Vector2();
    }

    public Vector(float x, float y){

        vector = new Vector2(x,y);
    }

    public void setLength(float l){
        vector.setLength(l);
    }

    public void set(float x, float y){
        vector.set(x, y);
    }

    public float angleRad(){
        return vector.angleRad();
    }

    public float angle(){
        return vector.angle();
    }

    public float len(){
        return vector.len();
    }
}