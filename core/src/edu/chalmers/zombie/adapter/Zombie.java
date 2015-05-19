package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.model.CreatureInterface;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.PathAlgorithm;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * Created by neda on 2015-03-31.
 */
public abstract class Zombie extends Entity implements CreatureInterface {

    private int speed;
    private ZombieType type;
    private boolean isKnockedOut;
    private Vector2 force;
    private Vector2 point;
    private Sprite sprite;
    private Point position;
    private MapController mapController;
    private int hp;

    /**
     * Creates a new zombie
     * @param sprite    Which sprite to use
     * @param world     In which world to create it
     * @param x     The zombie's x coordinate
     * @param y     The zombie's y coordinate
     */
    public Zombie(Sprite sprite, World world, float x, float y){

        super(sprite,world,x,y);
        this.sprite = sprite;
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
        fixDef.filter.maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY;

        //Set vectors
        force = new Vector2(0,0);
        point = new Vector2(0,0);

        position = new Point((int)x,(int)y);
        
        //Set body
        super.setBody(bodyDef, fixDef);
        super.getBody().setUserData(this);

        super.scaleSprite(1f / Constants.TILE_SIZE);

        mapController = new MapController();
        mapController.initializeCollisionObjects();
        mapController.setPlayerBufferPosition(GameModel.getInstance().getLevel().getPlayerSpawn());

        super.getBody().setAngularDamping(10000);

        isKnockedOut = false;

    }


    private boolean isAttacked;

    /**
     * A method which sets the zombie's speed to a new speed.
     * @param newSpeed
     */
    public void setSpeed(int newSpeed) {

        speed = newSpeed;
    }

    /**
     * A method which decreases the zombie's hp (life measured in points).
     */
    public  void decHp() {

        hp = hp--;
    }

    public abstract ZombieType getType();

    public abstract void setType(ZombieType type);

    public abstract Vector2 getVelocity();

    public abstract void attack(Player player);

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

    //@Override
    public void moveToPlayer(PathAlgorithm path) {

        Point playerPos = mapController.getPlayerPosition();

        //System.out.println(playerPos.x + " " + playerPos.y); <-- for debugging

        Point p = playerPos;
        point = new Vector2(p.x, p.y);

        // Boop
        Point ps = getZombiePosition();
        Vector2 start = new Vector2(ps.x, ps.y);

        setSpeed(200);

        float time = 0.01f;

        float distance = start.dst2(point);

        Vector2 direction = new Vector2(p.x - ps.x, p.y - ps.y);
        direction = direction.nor();

        /*if (p.y > ps.y) {

            setForceY(50);
            setForceX(50);
        } else {

            setForceY(-50);
            setForceX(50);
        } */

        if (p.x == ps.x && p.y == ps.y) {

            // TODO: attack
        } else if (p.y > ps.y && p.x == ps.x) {

            setForceY(50);
            setForceX(0);
        } else if (p.x > ps.x && p.y == ps.y) {

            setForceY(0);
            setForceX(50);
        } else if (p.x < ps.x && p.y == ps.y) {

            setForceY(0);
            setForceX(-50);
        } else if (ps.y < p.y && p.x == ps.x) {

            setForceY(-50);
            setForceX(0);
        } else if (p.y > ps.y && p.x > ps.x) {

            setForceY(50);
            setForceX(50);
        } else if (p.y < ps.y && p.x > ps.x) {

            setForceY(-50);
            setForceX(-50);
        } else if (p.y > ps.y && p.x < ps.x) {

            setForceY(50);
            setForceX(-50);
        } else if (p.y < ps.y && p.x < ps.x) {

            setForceY(-50);
            setForceX(-50);
        } else {
            // TODO: some exception management
        }

        Circle zcircle = new Circle(ps.x, ps.y, 10);
        Circle pcircle = new Circle(p.x, p.y, 5);

        if (super.getBody() != null) {

            if (zcircle.overlaps(pcircle)) {

                super.getBody().applyForce(force, point, !isKnockedOut);
            }
        }
        

        //super.getBody().applyForce(force, point, !isKnockedOut);
        //super.getBody().setLinearVelocity(-speed, speed);
        //super.getBody().applyForceToCenter(force.nor(), !isKnockedOut);

        /*while(path.getPath(position, playerPos).hasNext()){

            Point po = path.getPath(position, playerPos).next();
            point = new Vector2(po.x, po.y);
            setForceY(500);
            force.x = 0;
            super.getBody().applyForce(force, point, !isKnockedOut);
        }*/

    }

    /**
     * Changes the sprite to a sleeping one
     */
    @Override
    public void knockOut() {
        isKnockedOut = true;
    }

    public boolean isKnockedOut(){
        return isKnockedOut;
    }
    @Override
    public boolean hasBeenAttacked() {

        return isAttacked;
    }

    public abstract Zombie spawn(World world, ZombieType type, int x, int y);

}
