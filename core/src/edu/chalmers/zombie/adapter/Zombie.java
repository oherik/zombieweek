package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.controller.PhysicsController;
import edu.chalmers.zombie.model.CreatureInterface;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.PathAlgorithm;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie extends Entity implements CreatureInterface {

    private int speed;
    private float radius;
    private ZombieType type;
    private boolean isKnockedOut;
    private boolean isAggressive;
    private boolean isMoving;
    private Vector2 force;
    private Vector2 point;
    //private Sprite sprite;
    private Point position;
    private MapController mapController;
    private int hp;
    private Point nextPathTile;
    private long timeSinceLastPath;
    private int angularSpeed;
    private ArrayList<Point> path;
    private int damage;

    /**
     * Creates a new zombie
     * @param walkingTexture    Which texture to use when the zombie is walking
     * @param stillTexture  Which texture to use when the zombie is standing still
     * @param deadTexture  Which texture to use when the zombie is dead
     * @param world     In which world to create it
     * @param x     The zombie's x coordinate
     * @param y     The zombie's y coordinate
     */
    public Zombie(Texture walkingTexture, Texture stillTexture, Texture deadTexture, World world, float x, float y){
        super(walkingTexture, world, x, y);

        if(walkingTexture == null) {
            walkingTexture = GameModel.getInstance().res.getTexture("zombie");
            setSprite(new Sprite(walkingTexture));
        }
        if(stillTexture == null){
            stillTexture = GameModel.getInstance().res.getTexture("zombie-still");
        }
        if(deadTexture == null)
            deadTexture = GameModel.getInstance().res.getTexture("zombie-dead");

        TextureRegion[] stillFrame = TextureRegion.split(stillTexture,32,32)[0];
        getAnimator().addStillFrame(stillFrame[0]);

        TextureRegion[] deadFrame = TextureRegion.split(deadTexture,32,32)[0];
        getAnimator().addStillFrame(deadFrame[0]);


        int width = Constants.TILE_SIZE;
        int height = Constants.TILE_SIZE;
        //Load body def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x+0.5f,y+0.5f);
        bodyDef.linearDamping = 20f;
        bodyDef.angularDamping = 20f;

        //Load shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/ Constants.PIXELS_PER_METER, height/2/Constants.PIXELS_PER_METER);

        //Load fixture def
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = 0;
        fixDef.friction = .8f;
        fixDef.filter.categoryBits = Constants.COLLISION_ZOMBIE;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_WATER | Constants.COLLISION_SNEAK | Constants.COLLISION_ACTOR_OBSTACLE;

        //Set vectors
        force = new Vector2(0,0);
        point = new Vector2(0,0);

        position = new Point((int)x,(int)y);
        
        //Set body
        super.setBody(bodyDef, fixDef);
        super.getBody().setUserData(this);

        super.scaleSprite(1f / Constants.TILE_SIZE);

        mapController = new MapController();
        PhysicsController.setCollisionObjects();
        mapController.setPlayerBufferPosition(GameModel.getInstance().getRoom().getPlayerSpawn());

        super.getBody().setAngularDamping(10000);

        isKnockedOut = false;

        //Set system time created
        timeSinceLastPath = System.currentTimeMillis();

        isAggressive = false;


    }

    /**
     * Set the zombie as attacked or not
     * @param isAggressive    true if attacked, false if not
     */
    public void setIsAggressive(boolean isAggressive){
        this.isAggressive = isAggressive;
    }

    /**
     * @return true if the zombie is attacked, false otherwise
     */
    public boolean isAggressive(){
        return isAggressive;
    }
    /**
     * @return System time when the last path finding was performed
     */
    public long getTimeSinceLastPath() {
        return timeSinceLastPath;
    }

    /**
     * @return Set the system time when the last path finding was performed
     */
    public void setTimeSinceLastPath(long timeSinceLastPath){
        this.timeSinceLastPath = timeSinceLastPath;
    }

    /**
     * @return The current path
     */
    public ArrayList<Point> getPath() {
        return path;
    }

    /**
     * @return Set the current path
     */
    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }

    /**
     * A method which sets the zombie's speed to a new speed.
     * @param newSpeed
     */
    public void setSpeed(int newSpeed) {

        this.speed = newSpeed;
    }

    public int getSpeed() {

        return speed;
    }

    /**
     * A method which sets the zombie's angular speed to a new speed.
     * @param angularSpeed The new angular speed
     */
    public void setAngularSpeed(int angularSpeed) {

        this.angularSpeed = angularSpeed;
    }

    public int getAngularSpeed() {

        return angularSpeed;
    }

    /**
     * A method which decreases the zombie's hp (life measured in points) by the amount stated.
     * @param decBy the amount hp will be decreased by. 
     */
    public  void decHp(int decBy) {

        hp -= decBy;
    }

    public void setStartingHp(int hp) {

        this.hp = hp;
    }

    public int getHP(){
        return hp;
    }

    public ZombieType getType() {

        return type;
    }

    public void setType(ZombieType type) {

        this.type = type;
    }

    //public abstract Vector2 getVelocity();

    public abstract void attack();

    public void setZombiePosition(Point pos) {

        position = pos;
    }

    public Point getZombiePosition() {

        return position;
    }

    /**
     * A method which sets force affording to chosen speed.
     * @param speed int.
     */
    public void setForceY(int speed) {

        force.y = speed;
    }

    public void setForceX(int speed) {

        force.x = speed;
    }

    public void remove(Zombie zombie) {

        //TODO: remove zombie
    }

    public void setDetectionRadius(float radius) {

        this.radius = radius;
    }

    public float getDetectionRadius() {

        return radius;
    }

    public Vector2 getForce() {

        return force;
    }

    public Vector2 getPoint() {

        return point;
    }

    //@Override
    public void moveToPlayer(PathAlgorithm path) {


        //point = new Vector2(playerPosition.x, playerPosition.y);

        Point zombiePosition = getZombiePosition();
        Point playerPosition = mapController.getPlayerPosition();

       // setSpeed(80);
        setDetectionRadius(10);


/*
        for (int i = 0; i < (MapController.getPath(zombiePosition, playerPosition).size() - 1); i++) {

        Vector2 direction = new Vector2(playerPosition.x - zombiePosition.x, playerPosition.y - zombiePosition.y);


        ArrayList<Point> pathToPlayer = MapController.getPath(zombiePosition, playerPosition);

        if(pathToPlayer!=null && super.getBody() != null) {

            zombiePosition = new Point(Math.round(super.getX()), Math.round(super.getY()));
        }

            if (playerPosition.x == zombiePosition.x && playerPosition.y == zombiePosition.y) {

                // TODO: attack
            } else if (playerPosition.y > zombiePosition.y && playerPosition.x == zombiePosition.x) {

                setForceY(speed);
                setForceX(0);
            } else if (playerPosition.x > zombiePosition.x && playerPosition.y == zombiePosition.y) {

                setForceY(0);
                setForceX(speed);
            } else if (playerPosition.x < zombiePosition.x && playerPosition.y == zombiePosition.y) {

                setForceY(0);
                setForceX(-speed);
            } else if (zombiePosition.y < playerPosition.y && playerPosition.x == zombiePosition.x) {

                setForceY(-speed);
                setForceX(0);
            } else if (playerPosition.y > zombiePosition.y && playerPosition.x > zombiePosition.x) {

                setForceY(speed);
                setForceX(speed);
            } else if (playerPosition.y < zombiePosition.y && playerPosition.x > zombiePosition.x) {

                setForceY(-speed);
                setForceX(speed);
            } else if (playerPosition.y > zombiePosition.y && playerPosition.x < zombiePosition.x) {

                setForceY(speed);
                setForceX(-speed);
            } else if (playerPosition.y < zombiePosition.y && playerPosition.x < zombiePosition.x) {

                setForceY(-speed);
                setForceX(-speed);
            } else {
                // TODO: some exception management
            }

            Circle zcircle = new Circle(zombiePosition.x, zombiePosition.y, radius);
            Circle pcircle = new Circle(playerPosition.x, playerPosition.y, radius);

            if (super.getBody() != null) {

                if (zcircle.overlaps(pcircle)) {

                    super.getBody().applyForce(force, point, !isKnockedOut);
                    isMoving = true;
                }
            }*/
    }

    /**
     * Changes the sprite to a sleeping one
     */
    @Override
    public void knockOut() {
        isKnockedOut = true;
        getAnimator().setCurrentStillFrame(1);
    }

    public boolean isKnockedOut(){
        return isKnockedOut;
    }
    @Override
    public boolean hasBeenAttacked() {

        return isAggressive;
    }

    /**
     * A method which returns whether a zombie is currently moving or not.
     * @return boolean isMoving; true if moving, false if not.
     */
    public boolean isMoving() {

        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {

        this.isMoving = isMoving;
    }

    public MapController getThisMapController() {

        return mapController;
    }

    public abstract Zombie spawn(World world, int x, int y);

    /**
     * @return  The next tile the zombie should traverse to. Add 0.5 to x and y for the center of the tile
     */
    public Point getNextPathTile(){
        return this.nextPathTile;
    }

    /**
     * Set the next tile the zombie should traverse to
     * @param nextPathTile  The next tile
     */
    public void setNextPathTile(Point nextPathTile){
        this.nextPathTile = nextPathTile;
    }

    /**
     * A method to set how much damage a zombie attack will do.
     * @param damage the amount of damage in decreased lives done.
     */
    public void setDamage(int damage) {

        this.damage = damage;
    }

    /**
     * A method to return the amount of damage a zombie attack will do.
     * @return int damage, the damage in decreased lives done.
     */
    public int getDamage() {

        return damage;
    }
}
