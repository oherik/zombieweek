package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;

/** An adapter class for libGDX's Vector2
 */
public class Vector {
    private Vector2 vector;

    public Vector(){
        vector = new Vector2();
    }

    public Vector(Vector2 libVector){
        vector = libVector;
    }

    public Vector(Vector vector){
        this.vector = vector.getLibVector();
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

    public void set(Vector vector){
        this.vector.set(vector.getLibVector());
    }

    public float angleRad(){
        return vector.angleRad();
    }

    public void setAngleRad( float angleRad){
        this.vector.setAngleRad(angleRad);
    }

    public float angle(){
        return vector.angle();
    }

    public float len(){
        return vector.len();
    }

    public float getX(){
        return vector.x;
    }

    public float getY(){
        return vector.y;
    }

    public Vector2 getLibVector(){
        return vector;
    }

    public Vector add(float x, float y){
        vector.add(x,y);
        return this;
    }

    public Vector add(Vector vector){
        this.vector.add(vector.getLibVector());

    }
}