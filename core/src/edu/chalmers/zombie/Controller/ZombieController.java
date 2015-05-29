package edu.chalmers.zombie.controller;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by neda on 2015-05-21.
 */
public class ZombieController {

    public static void move(Zombie z) {
        if (z.getBody() != null) {
        Point playerPosition = z.getThisMapController().getPlayerPosition();
        Point zombiePosition = z.getZombiePosition();

        Player player = GameModel.getInstance().getPlayer();
        Point playerTile = new Point(Math.round(player.getX() - 0.5f), Math.round(player.getY() - 0.5f));
        Point zombieTile = new Point(Math.round(z.getX() - 0.5f), Math.round(z.getY() - 0.5f));

        Circle zcircle = new Circle(zombiePosition.x, zombiePosition.y, z.getDetectionRadius());
        Circle pcircle = new Circle(playerPosition.x, playerPosition.y, z.getDetectionRadius());

        if (zcircle.overlaps(pcircle) || z.isAggressive()) {

            if (System.currentTimeMillis() - z.getTimeSinceLastPath() > Constants.PATH_UPDATE_MILLIS) {
                //Update path
                ArrayList<Point> pathToPlayer = MapController.getPath(zombieTile, playerTile, Constants.MAX_PATH_COST);
                z.setPath(pathToPlayer);
                z.setTimeSinceLastPath(System.currentTimeMillis());
            }
            ArrayList<Point> pathToPlayer = z.getPath();
            if (pathToPlayer != null) {
                if (!pathToPlayer.isEmpty() && pathToPlayer.get(0).equals(zombieTile)) {
                    pathToPlayer.remove(0); //Ta ej med tilen zombien st�r p�
                }
                Iterator<Point> iteratorToPlayer = pathToPlayer.iterator();
                if (iteratorToPlayer.hasNext()) {
                    Point temp = iteratorToPlayer.next();
                    if (z.getNextPathTile() == null || zombieTile.equals(z.getNextPathTile()) || !temp.equals(z.getNextPathTile())) {
                        z.setNextPathTile(temp);
                    }
                }
                    Vector2 direction = new Vector2(z.getNextPathTile().x - zombieTile.x, z.getNextPathTile().y - zombieTile.y);
                    direction.setLength(z.getSpeed());
                    //Rotate

                    float currentAngle = z.getBody().getAngle() % (Constants.PI * 2) - Constants.PI * 0.5f; //TODO fixa s� impuls funkar
                    float directionAngle = direction.angleRad();
                    float rotation = directionAngle - currentAngle + z.getBody().getAngularVelocity() * Constants.TIMESTEP;      //TODO fixa;

                    //Keep it within Pi och -Pi
                    if (rotation < -Constants.PI)
                        rotation += Constants.PI * 2f;
                    if (rotation > Constants.PI)
                        rotation -= Constants.PI * 2f;
                    int impulse = rotation > 0 ? -z.getAngularSpeed() : z.getAngularSpeed();

                    //Apply rotation
                    if(Math.abs(rotation-Constants.PI)>0.2) {
                        z.getBody().applyAngularImpulse(impulse, true);
                    }

                    z.getBody().applyForceToCenter(direction.x, direction.y, true);
                    z.setIsMoving(true);
                } else {
                    z.setIsAggressive(false);    //Lose aggression
                    z.setIsMoving(false);
                }

        }
        } else {
            // TODO: some exception management
        }

    }

    public static void spawnZombie(String zombieString, int x, int y){
        Room room = GameModel.getInstance().getRoom();
        Zombie zombie = null;
        ZombieType zombieType = ZombieType.valueOf(zombieString);   //TODO g�r det att g�ra snyggare?
        switch(zombieType){       //TODO g�r som en enum eller n�t?
            case DATA: zombie = new DataZombie(room.getWorld(),x,y);
                break;
            case IT: zombie = new ITZombie(room.getWorld(),x,y);
                break;
            case MACHINE:
                zombie = new MachineZombie(room.getWorld(),x,y);
                break;
            case ZETA:
                zombie = new ZetaZombie(room.getWorld(),x,y);
                break;
            case ECON:
                zombie = new EconZombie(room.getWorld(),x,y);
                break;
            case ELECTRO:
                zombie = new ElectroZombie(room.getWorld(),x,y);
                break;
            case BASIC:
                zombie = new BasicZombie(room.getWorld(),x,y);
                break;
            case ARCH:
                zombie = new ArchZombie(room.getWorld(),x,y);
                break;
            case BOSS:
                zombie = new BossZombie(room.getWorld(),x,y);
                break;
            default: zombie = new BasicZombie(room.getWorld(),x, y);
                break;

        }
        room.addZombie(zombie);
    }

    public static void attack(Zombie zombie, Player player){
        EntityController.knockBack(zombie, player, zombie.getDamage());
        zombie.setIsAggressive(true);
        if (player.getLives() - zombie.getDamage() > 0) {

            player.decLives(zombie.getDamage());
            player.setIsHit(true);
            //TODO: make the screen flash bright red
        } else {

            player.decLives(player.getLives());
            GameModel.getInstance().setGameState(GameState.GAME_GAMEOVER);
            //TODO: Game Over
        }

    }

    /**
     * A zombie gets knocked out
     * @param z The zombie
     */
    public static void knockOut(Zombie z){
        GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),z);
        z.getAnimator().setCurrentStillFrame(1);
        z.knockOut();                                                                   //TODO Zombie b�r f� en hit() eller n�t ist�llet
        AudioController.playSound(GameModel.getInstance().res.getSound("zombie_sleeping"));
    }


}
