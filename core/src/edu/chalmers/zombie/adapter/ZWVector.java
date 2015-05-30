package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;

/** An adapter class for libGDX's Vector2
 * Modified by Neda
 */
public class ZWVector {
    private Vector2 vector;

    public ZWVector(){
        vector = new Vector2();
    }

    public ZWVector(Vector2 libVector){
        vector = libVector;
    }

    public ZWVector(ZWVector vector){
        this.vector = vector.getLibVector();
    }

    public ZWVector(float x, float y){

        vector = new Vector2(x,y);
    }

    public void setLength(float l){
        vector.setLength(l);
    }

    public void set(float x, float y){
        vector.set(x, y);
    }
    public void setX(float x){vector.x=x;}
    public void setY(float y){vector.y=y;}

    public void set(ZWVector vector){
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

    public ZWVector add(float x, float y){
        vector.add(x,y);
        return this;
    }

    public ZWVector add(ZWVector vector){
        this.vector.add(vector.getLibVector());
        return this;
    }

    public void scl(float scale) {
        this.vector.scl(scale);
    }

    public boolean equals(ZWVector other){
        return this.getLibVector().equals(other.getLibVector());
    }

}