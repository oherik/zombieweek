package edu.chalmers.zombie.controller;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
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
                    pathToPlayer.remove(0); //Ta ej med tilen zombien står på
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

                    float currentAngle = z.getBody().getAngle() % (Constants.PI * 2) - Constants.PI * 0.5f; //TODO fixa så impuls funkar
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
                    z.isAggressive(false);    //Lose aggression
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
        ZombieType zombieType = ZombieType.valueOf(zombieString);   //TODO går det att göra snyggare?
        switch(zombieType){       //TODO gör som en enum eller nåt?
            case DATA: zombie = new DataZombie(room.getWorld(),x,y);
                break;
            case IT: zombie = new ITZombie(room.getWorld(),x,y);
                break;
            default: zombie = new BasicZombie(room.getWorld(),x, y);
                break;

        }
        room.addZombie(zombie);
    }

    public static void attack(Zombie zombie, Player player){
        zombie.isAggressive(true);
        //TODO Puzzel fyll på här
    }


}
