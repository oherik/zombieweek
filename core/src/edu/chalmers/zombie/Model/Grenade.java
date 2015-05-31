package edu.chalmers.zombie.model;

import com.badlogic.gdx.math.Vector2;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.EntityController;
import edu.chalmers.zombie.controller.ProjectileController;
import edu.chalmers.zombie.controller.ZombieController;
import edu.chalmers.zombie.model.Entity;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.model.actors.Zombie;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;

/**
 * Created by daniel on 5/28/2015.
 */
public class Grenade extends Entity {
    private float targetX;
    private float targetY;
    private ZWVector originalPlayerPosition;
    private float width;
    private float height;
    private ZWTexture grenadeTexture = new ZWTexture("core/assets/grenadeBook.png");
    private ZWVector force;
    private float speed = 7;
    private float direction;
    private float explosionRadius = 3;
    private ArrayList<ZWFixture> foundFixtures = new ArrayList<ZWFixture>();
    private ZWWorld world;

    private int damage = 0;
    public Grenade(int targetX, int targetY, float x, float y, ZWWorld world){
        super(world);
        this.world = world;
        GameModel gameModel = GameModel.getInstance();
        Player player = gameModel.getPlayer();
        originalPlayerPosition = new ZWVector(player.getX(), player.getY());
        height = Constants.TILE_SIZE/2f;
        width = Constants.TILE_SIZE/3f;
        force = new ZWVector(1,1);
        this.targetX = targetX;
        this.targetY = targetY;
        ZWSprite grenadeSprite = new ZWSprite(grenadeTexture);
        ZWBody body = new ZWBody();
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ZOMBIE;
        body.createBodyDef(true, x+0.5f, y+0.5f, 0, 0, true);
        body.setFixtureDef(0, 0, (width/2/ Constants.PIXELS_PER_METER), (height/2/Constants.PIXELS_PER_METER), Constants.COLLISION_PROJECTILE, maskBits, false);
        super.setBody(body);
        super.setSprite(grenadeSprite);
        super.scaleSprite(1f / Constants.TILE_SIZE);
        getBody().setUserData(this);
        calculateDirection();
        force.setLength(speed);
    }
    public ZWWorld getWorld(){
        return world;
    }
    public int getDamage(){
        return damage;
    }
    private void calculateDirection(){
        float deltaX = ZWGameEngine.getWindowWidth() / 2 - targetX;
        float deltaY = ZWGameEngine.getWindowHeight() / 2 - targetY;
        direction = (float) Math.atan2((double) deltaY, (double) deltaX) + Constants.PI / 2;
    }
     public float getDirection(){
        return direction;
    }
    public ZWVector getForce(){
        return force;
    }

    @Override
    public void setBodyVelocity(ZWVector velocity){
        super.setBodyVelocity(velocity);
    }
    public ZWVector getVelocity(){
        return getBody().getLinearVelocity();
    }

    public float getTargetX(){
        return targetX;
    }
    public void setTargetX(float targetX){
        this.targetX = targetX;
    }
    public float getTargetY(){
        return targetY;
    }
    public void setTargetY(float targetY){
        this.targetY = targetY;
    }
    public ZWVector getOriginalPlayerPosition(){
        return originalPlayerPosition;
    }

    public float getExplosionRadius(){
        return  explosionRadius;
    }
    @Override
    public void draw(ZWBatch batch) {
        super.draw(batch);
    }

    public ArrayList<ZWFixture> getFoundFixtures(){
        return foundFixtures;
    }






}
