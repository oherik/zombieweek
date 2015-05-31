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

    /**
     * A method which sets a vector's length.
     * @param l the desired length.
     */
    public void setLength(float l){
        vector.setLength(l);
    }

    /**
     * A method which sets a vector's coordinates.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public void set(float x, float y){
        vector.set(x, y);
    }

    /**
     * A method which sets a vector's x-coordinate.
     * @param x desired coordinate.
     */
    public void setX(float x){vector.x=x;}

    /**
     * A method which sets a vector's y-coordinate.
     * @param y desired coordinate.
     */
    public void setY(float y){vector.y=y;}

    /**
     * A method which gives a vector the same properties as another vector.
     * @param vector the vector to be copied.
     */
    public void set(ZWVector vector){
        this.vector.set(vector.getLibVector());
    }

    /**
     * A method which returns a vector's angle in radians.
     * @return the angle in radians.
     */
    public float angleRad(){
        return vector.angleRad();
    }

    /**
     * A method which sets a vector's angle in radians.
     * @param angleRad the desired angle in radians.
     */
    public void setAngleRad( float angleRad){
        this.vector.setAngleRad(angleRad);
    }

    /**
     * A method which returns a vector's angle.
     * @return angle.
     */
    public float angle(){
        return vector.angle();
    }

    /**
     * A method which returns a vector's length.
     * @return vector length (float).
     */
    public float len(){
        return vector.len();
    }

    /**
     * A method which returns a vector's x-coordinate.
     * @return x-coordinate (float).
     */
    public float getX(){
        return vector.x;
    }

    /**
     * A method which returns a vector's y-coordinate.
     * @return y-coordinate (float).
     */
    public float getY(){
        return vector.y;
    }

    /**
     * A method which returns the gdx Vector2 instance of a ZWVector.
     * @return Vector2.
     */
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