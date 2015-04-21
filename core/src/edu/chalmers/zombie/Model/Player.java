package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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


    protected Player(Sprite sprite, World world, float x, float y) {
        super(sprite);

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

        switch (direction){
            case NORTH: y+=1;
                break;
            case SOUTH: y-=1;
                break;
            case WEST: x-=1;
                break;
            case EAST: x+=1;
                break;
            default:
                break;
        }

        updatePosition();
    }

    private void updatePosition(){
        setY((float)y);
        setX((float)x);
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
        super.draw(batch);
        playerBody.setTransform(x+0.5f,y+0.5f,0);

    }
    public void throwBook(){
        Book book = new Book(direction, x, y, world);
    }

}
