package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private int x;
    private int y;


    public Player(Sprite sprite, World world, int x, int y) {
        super(sprite);

        this.world = world;
        this.x = x;
        this.y = y;

        updatePosition();

        width = Constants.TILE_SIZE;
        height = Constants.TILE_SIZE;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width, height);
        fixDef.restitution = .1f;
        fixDef.friction = .5f;

        playerBody = world.createBody(bodyDef);
        playerBody.createFixture(fixDef);



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


}
