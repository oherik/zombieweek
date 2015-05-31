package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.Grenade;
import edu.chalmers.zombie.model.*;
import edu.chalmers.zombie.model.AimingSystem;
import edu.chalmers.zombie.model.Book;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.model.actors.Zombie;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-05-29.
 * Modified by Neda.
 */
public class ProjectileController {

    /**
     * Performs the calculations necessary for the book to hit the ground. It calls other methods to set the
     * collision detection to players and other books as well and to apply friction.
     * @param book  The book
     */
    public static void hitGround(Book book){
        book.setOnGround(true);
        //TODO g?ra boken mindre, l?gga till ljud etc
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_WATER
                | Constants.COLLISION_ACTOR_OBSTACLE | Constants.COLLISION_DOOR;
        EntityController.setMaskBits(book, maskBits);
        EntityController.setFriction(book, 4f, 3f);
    }

    public static void spawnStillBook(Room room, int x, int y){
        Book book = new Book(x,y,room);
        hitGround(book);
        GameModel.getInstance().addBook(book);
    }



    /**
     * A zombie gets hit by a book
     * @param z The zombie
     * @param b The book
     */
    public static void applyHit(Zombie z, Book b){

        z.setIsAggressive(true);
        if(!b.isOnGround()) {
            int damage = b.getDamage();
            z.decHp(damage);
            if (z.getHp() <= 0) {
                ZombieController.knockOut(z);
                GameModel.getInstance().getPlayer().incKillCount();
            }

            EntityController.knockBack(b, z, damage / 10f);

            hitGround(b);
            // GameModel.getInstance().addEntityToRemove(b);
            //b.markForRemoval();
            AudioController.playSound(GameModel.getInstance().res.getSound("zombie_hit"));
        }
    }

    /**
     * A player picks up a book
     * @param p The player
     * @param b The book
     */
    public static void pickUp(Player p, Book b){
        GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),b); //TODO beh?vs b?da dessa?
        b.markForRemoval();             //TODO beh?vs b?da dessa?
        p.increaseAmmunition();
        AudioController.playSound(GameModel.getInstance().res.getSound("pick_up_book"));
    }

    /**
     *  Starts moving the book using forces and angular rotation. The velocity of the book depends on if the player is moving and in which direction she's moving.
     */
    public static void throwBook(){
        GameModel gameModel = GameModel.getInstance();
        Player player = gameModel.getPlayer();
        AimingSystem aimingSystem = AimingController.getAimingSystem();
        Book book = new Book(aimingSystem.getDirection(), player.getX() - 0.5f, player.getY() - 0.5f, player.getWorld());
        ZWVector force = book.getForce();
        force.setLength(book.getThrowingSpeed());
        force.setAngleRad(book.getDirection() + Constants.PI*1/2);
        force.add(GameModel.getInstance().getPlayer().getVelocity()); // Add the player velocity
        setInMotion(book, force, book.getDirection(), book.getOmega());
        gameModel.addBook(book);
    }
    public static void throwGrenade(){
        GameModel gameModel = GameModel.getInstance();
        Player player = gameModel.getPlayer();
        AimingSystem aimingSystem = AimingController.getAimingSystem();
        Grenade grenade = new Grenade(aimingSystem.getMouseX(), aimingSystem.getMouseY(), player.getX() - 0.5f, player.getY() - 0.5f, player.getWorld());
        setInMotion(grenade, grenade.getForce(), grenade.getDirection(), 0);
        initializeGrenadeTimer(grenade);
        unproject(grenade);
        gameModel.addGrenade(grenade);
    }
    public static void setInMotion(Entity entity, ZWVector force, float direction, float omega){
        force.setAngleRad(direction + Constants.PI*1/2);
        entity.setBodyVelocity(force);
        entity.setAngularVelocity(omega);
    }
    private static void unproject(Grenade g){
        ZWRenderer ZWRenderer = new ZWRenderer();
        float unprojectedX = ZWRenderer.unprojectX(g.getTargetX());
        float unprojectedY = ZWRenderer.unprojextY(g.getTargetY());
        float width = ZWGameEngine.getWindowWidth()/Constants.TILE_SIZE;
        float height = ZWGameEngine.getWindowHeight()/Constants.TILE_SIZE;
        g.setTargetX(g.getOriginalPlayerPosition().getX() + unprojectedX*width/2);
        g.setTargetY(g.getOriginalPlayerPosition().getY() - unprojectedY*height/2);
    }

    private static void initializeGrenadeTimer(Grenade g){
        ZWTimer timer = new ZWTimer();
        ZWTask task = createGrenadeTask(g);
        timer.scheduleTask(task, 3);
        timer.start();
    }
    private static ZWTask createGrenadeTask(Grenade g){
        final Grenade grenade = g;
        ZWTask task = new ZWTask() {
            @Override
            public void run() {
                explode(grenade);
            }
        };
        return task;
    }

    private static void explode(Grenade g){
        stop(g);
        ZWRayCastCallback callback = createCallback(g);
        ZWVector grenadePosition = new ZWVector(g.getX(), g.getY());
        ZWVector[] rays= new ZWVector[100];
        for(int i = 0; i < 100; i++){
            rays[i] = new ZWVector(1,1);
            rays[i].setLength(g.getExplosionRadius());
            rays[i].setAngleRad(Constants.PI*2*i/100);
            rays[i].add(g.getX(), g.getY());
        }
        ArrayList<ZWFixture> fixturesInRadius = new ArrayList<ZWFixture>();
        for(ZWVector ray:rays){
            g.getFoundFixtures().clear();
            g.getWorld().rayCast(callback, grenadePosition, ray);
            for (ZWFixture f: g.getFoundFixtures()){
                if (checkIfInsideRadius(g, f, ray)){
                    fixturesInRadius.add(f);
                }
            }
            for (ZWFixture f: fixturesInRadius){
                if (f.getBodyUserData() instanceof Zombie){
                    Zombie z = (Zombie)f.getBodyUserData();
                    z.decHp(g.getDamage());
                    if (z.getHp() <= 0) {
                        ZombieController.knockOut(z);
                        GameModel.getInstance().getPlayer().incKillCount();
                    }
                    EntityController.knockBack(g, z, 3);
                }
            }
        }
        EntityController.remove(g);
        g.getSprite().setAlpha(0);
    }

    private static boolean checkIfInsideRadius(Grenade g, ZWFixture fixture, ZWVector ray){
        ZWVector fixturePosition = fixture.getPosition();
        return (((g.getX() < fixturePosition.getX() && fixturePosition.getX() < ray.getX()) ||
                (ray.getX() < fixturePosition.getX() && fixturePosition.getX() < g.getX())) &&
                ((g.getY() < fixturePosition.getY() && fixturePosition.getY() < ray.getY()) ||
                        (ray.getY() < fixturePosition.getY() && fixturePosition.getY() < g.getY())));
    }

    public static void stopIfNeeded(Grenade g){
        if ((g.getTargetX() - 0.1 < g.getX() && g.getX() < g.getTargetX() + 0.1) &&
                (g.getTargetY() - 0.1 < g.getY() && g.getY() < g.getTargetY() + 0.1)){
            stop(g);
        }
    }

    public static void stop(Grenade g){
        g.getForce().setLength(0);
        g.setBodyVelocity(g.getForce());
    }

    private static ZWRayCastCallback createCallback(Grenade grenade){
        final Grenade g = grenade;
        ZWRayCastCallback callback = new ZWRayCastCallback() {
            @Override
            public float reportRayFixture(ZWFixture fixture, ZWVector point, ZWVector normal, float fraction) {
                if (fixture.getCategoryBits() == Constants.COLLISION_ZOMBIE){
                    g.getFoundFixtures().add(fixture);
                }
                return 1;
            }
        };
        return callback;
    }
}
