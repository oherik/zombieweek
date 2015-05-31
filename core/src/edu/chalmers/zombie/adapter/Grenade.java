package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;
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
    private ZWTimer timer;
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
        unproject();
        initializeTimer();
        force.setLength(speed);
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
    private void unproject(){
        ZWRenderer ZWRenderer = new ZWRenderer();
        float unprojectedX = ZWRenderer.unprojectX(targetX);
        float unprojectedY = ZWRenderer.unprojextY(targetY);
        float width = ZWGameEngine.getWindowWidth()/Constants.TILE_SIZE;
        float height = ZWGameEngine.getWindowHeight()/Constants.TILE_SIZE;
        this.targetX = originalPlayerPosition.getX() + unprojectedX*width/2;
        this.targetY = originalPlayerPosition.getY() - unprojectedY*height/2;
    }
    @Override
    public void setBodyVelocity(ZWVector velocity){
        super.setBodyVelocity(velocity);
    }
    public ZWVector getVelocity(){
        return getBody().getLinearVelocity();
    }

    public void stopIfNeeded(){
        if ((targetX - 0.1 < this.getX() && this.getX() < targetX + 0.1) &&
                (targetY - 0.1 < this.getY() && this.getY() < targetY + 0.1)){
            stop();
        }
    }
    public void stop(){
        force.setLength(0);
        setBodyVelocity(force);
    }
    @Override
    public void draw(ZWBatch batch) {
        super.draw(batch);
        if (force.len() !=0){
            stopIfNeeded();
        }
    }
    private ZWVector[] rays;
    public void explode(){
        stop();
        ZWRayCastCallback callback = createCallback();
        ZWVector grenadePosition = new ZWVector(getX(), getY());
        rays = new ZWVector[100];
        for(int i = 0; i < 100; i++){
            rays[i] = new ZWVector(1,1);
            rays[i].setLength(explosionRadius);
            rays[i].setAngleRad(Constants.PI*2*i/100);
            rays[i].add(getX(), getY());
        }
        ArrayList<ZWFixture> fixturesInRadius = new ArrayList<ZWFixture>();
        for(ZWVector ray:rays){
            foundFixtures.clear();
            world.rayCast(callback, grenadePosition, ray);
            for (ZWFixture f: foundFixtures){
                if (checkIfInsideRadius(f, ray)){
                    fixturesInRadius.add(f);
                }
            }
            for (ZWFixture f: fixturesInRadius){
                if (f.getBodyUserData() instanceof Zombie){
                    Zombie z = (Zombie)f.getBodyUserData();
                    z.decHp(damage);
                    if (z.getHp() <= 0) {
                        ZombieController.knockOut(z);
                        GameModel.getInstance().getPlayer().incKillCount();
                    }
                    EntityController.knockBack(this, z, 3);
                }
            }
        }
        EntityController.remove(this);
        this.getSprite().setAlpha(0);
    }
    private boolean checkIfInsideRadius(ZWFixture fixture, ZWVector ray){
        ZWVector fixturePosition = fixture.getPosition();
        return (((getX() < fixturePosition.getX() && fixturePosition.getX() < ray.getX()) ||
                (ray.getX() < fixturePosition.getX() && fixturePosition.getX() < getX())) &&
                ((getY() < fixturePosition.getY() && fixturePosition.getY() < ray.getY()) ||
                        (ray.getY() < fixturePosition.getY() && fixturePosition.getY() < getY())));
    }
    private ZWRayCastCallback createCallback(){
        ZWRayCastCallback callback = new ZWRayCastCallback() {
            @Override
            public float reportRayFixture(ZWFixture fixture, ZWVector point, ZWVector normal, float fraction) {
                if (fixture.getCategoryBits() == Constants.COLLISION_ZOMBIE){
                    foundFixtures.add(fixture);
                }
                return 1;
            }
        };
        return callback;
    }
    private void initializeTimer(){
        timer = new ZWTimer();
        ZWTask task = createTask();
        timer.scheduleTask(task, 3);
        timer.start();
    }
    private ZWTask createTask(){
        ZWTask task = new ZWTask() {
            @Override
            public void run() {
                explode();
            }
        };
        return task;
    }

}
