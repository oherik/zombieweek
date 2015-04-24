package edu.chalmers.zombie.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by daniel on 4/21/2015.
 */
public class Book extends Sprite{
    private final int VELOCITY = 10;
    private Direction direction;
    private Body bookBody;
    private float x;
    private float y;
    private Vector2 force = new Vector2(0,0);
    private boolean remove = false;
    public Book(Direction d, float x, float y, World world) {
        super(new Sprite(new Texture("core/assets/bookSprite.png")));

        GameModel gameModel = GameModel.getInstance();

        this.direction = d;
        //Sets this book's position to be next to the player.
        setPosition(x, y);

        BodyDef bookBodyDef = new BodyDef();
        bookBodyDef.type = BodyDef.BodyType.DynamicBody;
        bookBodyDef.position.set(x + 2.5f, y + 2.5f);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(gameModel.getPlayer().getHeight() / 2, gameModel.getPlayer().getWidth() / 2);
        FixtureDef bookFixDef = new FixtureDef();
        bookFixDef.shape = shape;
        bookFixDef.density = (float) Math.pow(gameModel.getPlayer().getWidth(), gameModel.getPlayer().getHeight());
        bookFixDef.restitution = 0;
        bookFixDef.friction = 1;
        bookFixDef.filter.categoryBits = Constants.COLLISION_PROJECTILE;
        bookFixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY;
        bookBody = world.createBody(bookBodyDef);
        bookBody.createFixture(bookFixDef);
        //Adds this book to the list of all books that exist for the renderer.
        gameModel.addBook(this);
        setSize(1, 1);
        setInMotion();

        bookBody.setUserData(this);
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    private void updateLocation(float deltaTime){
        setX(getX() + deltaTime * force.x);
        setY(getY() + deltaTime * force.y);
        this.x = getX() + deltaTime * force.x;
        this.y = getY() + deltaTime * force.y;
    }

    //Sets the books position to be next to the coordinates given.
    public void setPosition(float x, float y){
        switch (direction) {
            case NORTH:
                y = y + 1f;
                break;
            case SOUTH:
                y = y - 1f;
                break;
            case WEST:
                x = x - 1f;
                break;
            case EAST:
                x = x + 1f;
                break;
            case NORTH_EAST:
                x = x + 1f;
                y = y + 1f;
                break;
            case NORTH_WEST:
                y = y + 1f;
                x = x - 1f;
                break;
            case SOUTH_EAST:
                x = x + 1f;
                y = y - 1f;
                break;
            case SOUTH_WEST:
                y = y - 1f;
                x = x - 1f;
                break;
            default:
                break;
        }
        this.x = x;
        this.y = y;
        setX(x);
        setY(y);
    }
    public void setInMotion(){
        GameModel gameModel = GameModel.getInstance();
        int speed = VELOCITY + gameModel.getPlayer().getSpeed();
        switch (direction) {
            case NORTH:
                force.y = speed;
                break;
            case SOUTH:
                force.y = -speed;
                break;
            case WEST:
                force.x = -speed;
                break;
            case EAST:
                force.x = speed;
                break;
            case NORTH_EAST:
                force.x = speed;
                force.y = speed;
                break;
            case NORTH_WEST:
                force.x = -speed;
                force.y = speed;
                break;
            case SOUTH_EAST:
                force.x = speed;
                force.y = -speed;
                break;
            case SOUTH_WEST:
                force.x = -speed;
                force.y = -speed;
                break;
            default:
                break;
        }
    }
    public void draw(Batch batch) {
        updateLocation(Gdx.graphics.getDeltaTime());
        //updateLocation((float)0.15);
        super.draw(batch);
        bookBody.setTransform(x+0.5f,y+0.5f,0);
    }

    public void markForRemoval(){
        this.remove = true;
    }

    public boolean toRemove(){
        return this.remove;
    }


}
