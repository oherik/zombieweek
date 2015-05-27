package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.model.CreatureInterface;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;


import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.PotionType;

import java.awt.*;

/**
 * Created by neda on 2015-03-31.
 * Modified by Tobias
 */
public class Player extends Entity implements CreatureInterface {

    private int killCount;
    private int lives;
    private int ammunition;
    private boolean isAttacked;
    private boolean isHidden;
    private Body playerBody;
    private int width;
    private int height;
    private Vector2 force;
    //Sets the player's starting direction to north so that a thrown book will have a direction.
    private Direction direction = Direction.NORTH;
    //Holds the players speed.
    private int speed = 7;
    private float dampening;
    private int legPower;
    private FixtureDef fixDef;
    private BodyDef bodyDef;
    private PotionType potion;
    private int waterTilesTouching = 0; //TODO måste göras på nåt snyggare sätt
    private int sneakTilesTouching = 0; //TODO måste göras på nåt snyggare sätt

    private Thread keyThread; //Keeps track of key releases
    //The hand is throwing the book and aiming.
    private Hand hand = new Hand(this);

    public Player(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);

        //Set still image frame
        GameModel.getInstance().res.loadTexture("emilia-still","core/assets/Images/emilia-still.png"); //TODO: shouldnt be done here
        Texture stillTexture = GameModel.getInstance().res.getTexture("emilia-still");
        TextureRegion[] stillFrame = TextureRegion.split(stillTexture,32,32)[0];
        getAnimator().addStillFrame(stillFrame[0]);

        //Set overlay image (Hand)
        GameModel.getInstance().res.loadTexture("emilia-hand","core/assets/Images/emilia-hand.png");//TODO: shouldnt be done here
        Texture overlayTexture = GameModel.getInstance().res.getTexture("emilia-hand");
        TextureRegion overlayFrame = new TextureRegion(overlayTexture);
        getAnimator().setOverlayFrame(overlayFrame);


        legPower =  75; //Styr maxhastigheten
        dampening = 30f; //Styr maxhastigheten samt hur snabb accelerationen är

        width = Constants.PLAYER_SIZE;
        height = Constants.PLAYER_SIZE;


        //Load body def
        this.bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x+0.5f,y+0.5f);
        bodyDef.linearDamping = dampening;
        bodyDef.angularDamping = dampening;


        Vector2[] vectors = new Vector2[8];

        vectors[0] = new Vector2(2f,-1.5f);
        vectors[1] = new Vector2(3f,-0.5f);
        vectors[2] = new Vector2(3f,0.5f);
        vectors[3] = new Vector2(2f,1.5f);
        vectors[4] = new Vector2(-2f,1.5f);
        vectors[5] = new Vector2(-3f,0.5f);
        vectors[6] = new Vector2(-3f,-0.5f);
        vectors[7] = new Vector2(-2f,-1.5f);

        for (Vector2 vector:vectors){
            vector.scl(1f/6.5f);
        }

        PolygonShape shape = new PolygonShape();
        shape.set(vectors);

        //Load fixture def
        this.fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = (float)Math.pow(width/Constants.PIXELS_PER_METER, height/Constants.PIXELS_PER_METER);
        fixDef.restitution = 0;
        fixDef.friction = .8f;
        fixDef.filter.categoryBits = Constants.COLLISION_PLAYER;
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_DOOR | Constants.COLLISION_WATER| Constants.COLLISION_SNEAK;

        //Set body
        super.setBody(bodyDef, fixDef);
        super.scaleSprite(1f / Constants.TILE_SIZE);
        killCount = 0;
        ammunition = 5;
        lives = 100;
        force = new Vector2(0,0);
        getBody().setFixedRotation(true);   //Så att spelaren inte roterar


    }

    public Player(Player p) {
        this(p.getSprite().getTexture(), p.getWorld(), p.getX(), p.getY());
    }

    /**
     * A method to get current players kill count.
     * @return int killCount.
     */
    private int getKillCount() {

        return killCount;
    }

    /**
     * A method that increases current player's kill count.
     */
    private void incKillCount() {

        killCount = killCount + 1;
    }


    /**
     * Moves player if needed.
     */
    public void moveIfNeeded(){
        getBody().applyForce(force, getBody().getLocalCenter(), true);
    }


    /**
     * Moves player
     * @param direction Direction to move in
     */
    public void move(Direction direction) {
        
        switch (direction){
            case NORTH:
                force.y = legPower;
                break;
            case SOUTH:
                force.y = -legPower;
                break;
            case WEST:
                force.x = -legPower;
                break;
            case EAST:
                force.x = legPower;
                break;
            default:
                break;
        }

        updateMovement();

    }

    /**
     * Updates Body rotation
     */
    private void updateRotation(){
        GameModel gameModel = GameModel.getInstance();
        Body body = getBody();
        float rotation =  direction.getRotation();
        //Checks if world.step() is running. If it is running it tries again and again until step isn't running.
        //This is the reason why sometimes the game lags and a StackOverflowError happens.
        if (!gameModel.isStepping()) {
            body.setTransform(body.getPosition(), rotation);    //TODO orsakar krash
        } else{
            updateRotation();
            // Commenting the section above causes no issues and fizes the StackOverflowError.
        }
    }

    /**
     * @return Direction of player
     */
    public Direction getDirection(){
        return direction;
    }



    /**
     * Updates velocity, direction and rotation of body
     */
    private void updateMovement(){

       // setBodyVelocity(force);
        updateSpeed();
        updateDirecton();
        updateRotation();

        moveIfNeeded();
    }

    /**
     * Updates player speed
     */
    private void updateSpeed(){force.setLength(speed);}

    /**
     * Sets Direction from variable force
     */
    private void updateDirecton(){

        if(force.y > 0){
            if (force.x > 0){
                direction = Direction.NORTH_EAST;
            } else if (force.x < 0){
                direction = Direction.NORTH_WEST;
            } else {
                direction = Direction.NORTH;
            }
        } else if (force.y < 0){
            if (force.x > 0){
                direction = Direction.SOUTH_EAST;
            } else if (force.x < 0){
                direction = Direction.SOUTH_WEST;
            } else {
                direction = Direction.SOUTH;
            }
        } else {
            if (force.x > 0){
                direction = Direction.EAST;
            } else if (force.x < 0){
                direction = Direction.WEST;
            }
        }

    }



    /**
     * Method that checks if keys are released simultaneously
     */
    private void checkSimultaneousRelease(){
        final int timeSensitiveness = 50; //release keys within x millisec and they are released simultaneously
        if (keyThread!=null && keyThread.getState() == Thread.State.TIMED_WAITING){

                //Keys were released at the same time (thread is sleeping/waiting)
            updateMovement();

        } else {

            keyThread = new Thread() {
                public void run() {
                    try {
                        keyThread.sleep(timeSensitiveness); //waiting for new key release
                        updateMovement();
                        //if(getWorld().isLocked())     //TODO hack för att inte krascha
                    } catch (InterruptedException e) {
                        System.out.println("------ Key thread interrupted -------\n" + e);
                    }
                    //keyThread.interrupt();
                }
            };

            keyThread.start();
        }


    }


    /**
     * Sets speed in x-axis to zero
     */
    public void stopX() {
        force.x = 0;
        if (force.y == 0) { this.speed = 0;}
        checkSimultaneousRelease();
    }

    /**
     * Sets speed in y-axis to zero
     */
    public void stopY(){
        force.y = 0;
        if (force.x == 0) { this.speed = 0;}
        checkSimultaneousRelease();
    }

    @Override
    protected void setBodyVelocity(Vector2 velocity){
        super.setBodyVelocity(velocity);
    }

    /**
     * Updates position of player
     */
    private void updatePosition(){
        //setY((float)y);
        //setX((float) x);
        //TODO: delete?
    }

    /**
     * Updates location of player
     */
    private void updateLocation(float deltaTime){
        //setX(getX() + deltaTime * force.x);
        //setY(getY() + deltaTime * force.y);
        //TODO: delete?
    }

    /**
     * A method used when player is attacking zombie.
     * @param zombie the zombie that is attacked.
     */
    public void attack(Zombie zombie) {

        // TODO: fill in with attack of zombie instance
    }

    @Override
    public void knockOut() {

        // TODO: game over
    }

    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }

    /*
    public Sprite getSprite() {

        return sprite;
    }*/

    @Override
    public void setBody(Body body) {

       super.setBody(body);
    }

    @Override
    public Body getBody() {

        return super.getBody();
    }

    /**
     * A method that increases ammuntion.
     */
    public void addBook(){
        ++ammunition;
    }

    /**
     * Since the ammunition count can't be negative it checks if it's >0 before decreasing it
     */
    public void decreaseAmmunition(){
        if(ammunition>0)
            ammunition--;
    }

    public void increaseAmmunition(){
        ammunition++;
    }

    /**
     * Get player ammunition
     * @return ammunition
     */
    public int getAmmunition(){
        return ammunition;
    }

    /**
     * Get player lives
     * @return lives
     */
    public int getLives(){
        return lives;
    }

    /**
     * A method which decreases the player's number of lives.
     */
    public void decLives() {

        lives = lives--;
    }

    /**
     * A method which increases the player's number of lives.
     */
    public void incLives() {

        lives = lives++;
    }


    public Vector2 getVelocity(){
        return getBody().getLinearVelocity(); //TODO måste fixas, borde skicka en vector2
    }

    /**
     * Creates a new default body at the given position
     * @param point The point where to create the new body
     * @return A new default body
     */
    public Body createDefaultBody(World world, Point point){
        if(this.getBody()!=null) {
            this.removeBody();
        }
        this.setWorld(world);
        this.setBody(bodyDef, fixDef);
        this.setPosition(point);
        getBody().setFixedRotation(true);   //Så att spelaren inte roterar
        return this.getBody();
    }

    /**
     * Spawns the current player at chosen point.
     * @param x coordinate.
     * @param y coordinate.
     * @return this player.
     */
    public Player spawn( int x, int y) {

        this.removeBody();
        Point p = new Point(x,y);
        this.setPosition(p);
        this.setBody(bodyDef, fixDef);
        return this;
    }

    /**
     * A method that checks whether Player is hidden or not.
     * @return true if hidden, false if not.
     */
    public boolean isHidden() {

        return isHidden;
    }

    public void setPosition(Point point){
        getBody().setTransform(point.x + 0.5f, point.y + 0.5f, getBody().getAngle()); //+0.5f because we want it in the middle
        updateRotation();
    }
    public Hand getHand(){
        return this.hand;
    }
    public void throwBook(){
        hand.throwBook();
        getAnimator().setOverlay(500); //time in millisec of Hand to be shown when trowing
    }

    public int getWaterTilesTouching(){
        return waterTilesTouching;
    }

    public void setWaterTilesTouching(int i){
        waterTilesTouching = i;
    }

    public int getSneakTilesTouching(){
        return sneakTilesTouching;
    }

    public void setSneakTilesTouching(int i){
        sneakTilesTouching = i;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
}
