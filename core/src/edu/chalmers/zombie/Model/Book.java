package edu.chalmers.zombie.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by daniel on 4/21/2015.
 */
public class Book extends Entity{
    private float x;
    private float y;
    private Vector2 force;
    //Sets the player's starting direction to east so that a thrown book will have a direction.
    private Direction direction = Direction.EAST;
    private boolean remove = false;
    int speed, velocity, omega;
    float width, height;
    Sprite sprite;



    /*
    private final int VELOCITY = 10;
    private Direction direction;
    private Body bookBody;
    private float x;
    private float y;
    private Vector2 force = new Vector2(0,0);
    private boolean remove = false;*/
    public Book(Direction d, float x, float y, World world, int intialSpeed) {
        super(world);
        height = Constants.TILE_SIZE/2f;
        width = Constants.TILE_SIZE/3f;

        //Set variables
        this.direction=d;
        force = new Vector2(0,0);
        setPosition(x, y);

        //Load body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(this.x+0.5f,this.y+0.5f);

        //Load shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/ Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);
        //Load fixture def
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = 0;
        fixDef.friction = 8f;
        fixDef.filter.categoryBits = Constants.COLLISION_PROJECTILE;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY;
        //Set body
        super.setBody(bodyDef, fixDef);




        velocity = 7;
        omega= 10;

        setInMotion();

        speed = intialSpeed;

        //Load sprite
        sprite = new Sprite(new Texture("core/assets/bookSprite.png"));
        sprite.setSize(width, height);
        super.setSprite(sprite);
        super.scale(1f/Constants.TILE_SIZE);


      //  getBody().setTransform(this.x-0.5f, this.y-0.5f, getBody().getAngle());

        getBody().setUserData(this);


        /*
        super(new Sprite(new Texture("core/assets/bookSprite.png")));

        GameModel gameModel = GameModel.getInstance();

        this.direction = d;
        //Sets this book's position to be in front of the player.
        setPosition(x,y);

        BodyDef bookBodyDef = new BodyDef();
        bookBodyDef.type = BodyDef.BodyType.DynamicBody;
        System.out.println(x + " " + y + "  " + this.x + " " + this.y);
        bookBodyDef.position.set(this.x, this.y);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(gameModel.getPlayer().getHeight() / 2, gameModel.getPlayer().getWidth() / 2);
        FixtureDef bookFixDef = new FixtureDef();
        bookFixDef.shape = shape;
        bookFixDef.density = (float) Math.pow(gameModel.getPlayer().getWidth(), gameModel.getPlayer().getHeight());
        bookFixDef.restitution = 0;
        bookFixDef.friction = 0.1f;
        bookFixDef.filter.categoryBits = Constants.COLLISION_PROJECTILE;
        bookFixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY;
        bookFixDef.isSensor = false;
        bookBody = world.createBody(bookBodyDef);
        bookBody.createFixture(bookFixDef);

        bookBody.setSleepingAllowed(false);
        //Adds this book to the list of all books that exist for the renderer.
        gameModel.addBook(this);
        setSize(1, 1);
        setInMotion();

        bookBody.setUserData(this);
        bookBody.setSleepingAllowed(false);*/

    }
    public float getX(){
        return x;
    }

    @Override
    public void setY(float y) {

    }

    public float getY(){
        return y;
    }

    @Override
    public int getSpeed() {
        return 0;
    }
    /**
     * Updates velocity, direction and rotation of body
     */
    private void updateMovement(){
        setBodyVelocity(force);
    }


    private void updateLocation(float deltaTime){
//TODO beh�vs?



    }
    @Override
    protected void setBodyVelocity(Vector2 velocity){
        super.setBodyVelocity(velocity);
    }
    @Override
    protected void setAngularVelocity(float omega){
        super.setAngularVelocity(omega);
    }


    //Sets the books position to be next to the coordinates given.
    public void setPosition(float x, float y){
        float distance = 1.5f;
        switch (direction) {
            case NORTH:
                y = y + distance;
                break;
            case SOUTH:
                y = y - distance;
                break;
            case WEST:
                x = x - distance;
                break;
            case EAST:
                x = x + distance;
                break;
            case NORTH_EAST:
                x =x + distance;
                y = y + distance;
                break;
            case NORTH_WEST:
                y = y + distance;
                x = x - distance;
                break;
            case SOUTH_EAST:
                x = x + distance;
                y = y - distance;
                break;
            case SOUTH_WEST:
                y = y - distance;
                x = x - distance;
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
        switch(direction){
        case NORTH:
        force.y = speed +   velocity;
        break;
        case SOUTH:
        force.y = -speed - velocity;
        break;
        case WEST:
        force.x = -speed-velocity;
        break;
        case EAST:
        force.x = speed+velocity;
        break;
        case NORTH_EAST:
        force.x = Constants.SQRT_2*(speed+velocity);
        force.y = Constants.SQRT_2*(speed+velocity);
        break;
        case NORTH_WEST:
        force.x = Constants.SQRT_2*(-speed-velocity);
        force.y =  Constants.SQRT_2*(speed+velocity);
        break;
        case SOUTH_EAST:
        force.x = Constants.SQRT_2*(speed+velocity);
        force.y =Constants.SQRT_2*(-speed-velocity);
        break;
        case SOUTH_WEST:
        force.x = Constants.SQRT_2*(-speed-velocity);
        force.y =  Constants.SQRT_2*(-speed-velocity);
        break;
        default:
        break;
    }
    setBodyVelocity(force);
    setAngularVelocity(omega);
}
    /*public void setInMotion(){
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
        bookBody.setAwake(true);
        System.out.println(bookBody.isAwake());
        System.out.println(force);
        setPosition(0,0); //TODO test
        bookBody.setLinearVelocity(force);
        bookBody.setAngularVelocity(10);
        bookBody.setAwake(true);
    }
    public void draw(Batch batch) {
        updateLocation(Gdx.graphics.getDeltaTime());
        //updateLocation((float)0.15);
        super.draw(batch);
        bookBody.setTransform(x+0.5f,y+0.5f,0);
    }*/

    @Override
    public void setX(float x) {

    }

    public void markForRemoval(){
        this.remove = true;
    }

    public boolean toRemove(){
        return this.remove;
    }


}
