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
public class Player extends Sprite implements CreatureInterface {

    private Direction direction;
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


    protected Player(Sprite sprite, World world, float x, float y) {
        super(sprite);

        force = new Vector2(0,0);

        this.world = world;
        this.x = x;
        this.y = y;

        updatePosition();

        width = Constants.TILE_SIZE;
        height = Constants.TILE_SIZE;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x+0.5f,y+0.5f);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = .1f;
        fixDef.friction = .5f;
        fixDef.filter.categoryBits = Constants.COLLISION_PLAYER;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ZOMBIE;

        playerBody = world.createBody(bodyDef);
        playerBody.createFixture(fixDef);



    }
    public void scale(float scale) {
        setSize(getWidth() * scale, getHeight() * scale);
        updatePosition();
    }


    private int getKillCount() {

        return killCount;
    }

    private void incKillCount() {

        killCount = killCount + 1;
    }

    @Override
    public void move(Direction direction) {

        int speed = 7;

        switch (direction){
            case NORTH:
                force.y = speed;
                stopX();
                break;
            case SOUTH:
                force.y = -speed;
                stopX();
                break;
            case WEST:
                force.x = -speed;
                stopY();
                break;
            case EAST:
                force.x = speed;
                stopY();
                break;
            default:
                break;
        }

    }

    public void stopX() {

        force.x = 0;

    }

    public void stopY(){

        force.y = 0;
    }


    private void updatePosition(){
        setY((float)y);
        setX((float)x);
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

        playerBody = body;
    }

    @Override
    public Body getBody() {

        return playerBody;
    }
    @Override
    public void draw(Batch batch){
        updateLocation(Gdx.graphics.getDeltaTime());
        //updateLocation((float)0.15);

        super.draw(batch);
        playerBody.setTransform(x+0.5f,y+0.5f,0);

    }
    public void throwBook(){
        Book book = new Book(direction, getX(), getY(), world);
    }

}
