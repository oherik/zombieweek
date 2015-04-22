package edu.chalmers.zombie.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.utils.Constants;


import edu.chalmers.zombie.utils.Direction;

/**
 * Created by neda on 2015-03-31.
 * Modified by Tobias
 */
public class Player extends Entity implements CreatureInterface {

    private int killCount;
    private int lives;
    private boolean isAttacked;
    private Body playerBody;
    private World world;
    private int width;
    private int height;
    private float x;
    private float y;
    private Vector2 force;
    //Sets the player's starting direction to east so that a thrown book will have a direction.
    private Direction direction = Direction.EAST;
    //Holds the players speed.
    private int speed = 7;


    protected Player(Sprite sprite, World world, float x, float y) {
        super(sprite, world, x, y);
        killCount = 0;
        force = new Vector2(0,0);
        getBody().setFixedRotation(true);   //Så att spelaren inte roterar
    }

    private int getKillCount() {

        return killCount;
    }

    private void incKillCount() {

        killCount = killCount + 1;
    }

    @Override
    public void move(Direction direction) {
        Body body = getBody();
        this.speed = 7;
        this.direction = direction;
        switch (direction){
            case NORTH:
                body.setTransform(body.getPosition(), 0);
                force.y = speed;
                stopX();
                break;
            case SOUTH:
                body.setTransform(body.getPosition(), Constants.PI);
                force.y = -speed;
                stopX();
                break;
            case WEST:
                body.setTransform(body.getPosition(), Constants.PI/2);
                force.x = -speed;
                stopY();
                break;
            case EAST:
                body.setTransform(body.getPosition(), -Constants.PI/2);
                force.x = speed;
                stopY();
                break;
            default:
                break;
        }
        setBodyVelocity(force);
    }

    public void stopX() {

        force.x = 0;
        if (force.y == 0) { this.speed = 0;}
        setBodyVelocity(force);
    }

    public void stopY(){

        force.y = 0;
        if (force.x == 0) { this.speed = 0;}
        setBodyVelocity(force);
    }

    @Override
    protected void setBodyVelocity(Vector2 velocity){
        super.setBodyVelocity(velocity);
    }

    private void updatePosition(){
        setY((float)y);
        setX((float) x);
    }

    private void updateLocation(float deltaTime){
        setX(getX() + deltaTime * force.x);
        setY(getY() + deltaTime * force.y);
    }

    public void attack(Zombie zombie) {

        // TODO: fill in with attack of zombie instance
    }

    @Override
    public void KnockOut() {

        // TODO: game over
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }

    @Override
    public void setBody(Body body) {

       super.setBody(body);
    }

    @Override
    public Body getBody() {

        return super.getBody();
    }

    public void throwBook(){
        Book book = new Book(direction, getX()-0.5f, getY()-0.5f, getWorld());
    }



    public void setX(float x){
        this.x = x;
    }

     public void setY(float y){
         this.y = y;
     }

    public int getSpeed(){
        return this.speed;
    }

}
