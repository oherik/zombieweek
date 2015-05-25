package edu.chalmers.zombie.controller;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by neda on 2015-05-21.
 */
public class ZombieController {

    public static void move(Zombie z) {

        Point playerPosition = z.getThisMapController().getPlayerPosition();
        Point zombiePosition = z.getZombiePosition();

        z.setSpeed(80);
        z.setDetectionRadius(10);

        Player player = GameModel.getInstance().getPlayer();
        Point playerTile = new Point(Math.round(player.getX() - 0.5f), Math.round(player.getY() - 0.5f));
        Point zombieTile = new Point(Math.round(z.getX() - 0.5f), Math.round(z.getY() - 0.5f));

        Circle zcircle = new Circle(zombiePosition.x, zombiePosition.y, z.getDetectionRadius());
        Circle pcircle = new Circle(playerPosition.x, playerPosition.y, z.getDetectionRadius());

        if (zcircle.overlaps(pcircle)) {
            ArrayList<Point> pathToPlayer = null;
            if (System.currentTimeMillis() - z.getTimeCreated() > Constants.PATH_UPDATE_MILLIS) {
                //Update path
                pathToPlayer = MapController.getPath(zombieTile, playerTile, Constants.MAX_PATH_STEP);
            }
            if (pathToPlayer != null) {
                if (pathToPlayer.size() > 1) {
                    pathToPlayer.remove(0); //Ta ej med tilen zombien står på
                }
                Iterator<Point> iteratorToPlayer = pathToPlayer.iterator();
                if (iteratorToPlayer.hasNext()) {
                    Point temp = iteratorToPlayer.next();
                    if (z.getNextPathTile() == null || zombieTile.equals(z.getNextPathTile()) || !temp.equals(z.getNextPathTile())) {
                        z.setNextPathTile(temp);
                    }
                }
                if (z.getBody() != null) {
                    Vector2 direction = new Vector2(z.getNextPathTile().x - zombieTile.x, z.getNextPathTile().y - zombieTile.y);
                    direction.setLength(z.getSpeed());

                    //Rotate

                    float currentAngle = z.getBody().getAngle() % (Constants.PI * 2) - Constants.PI * 0.5f; //TODO fixa så impuls funkar
                    float directionAngle = direction.angleRad();
                    float rotation = directionAngle - currentAngle + z.getBody().getAngularVelocity() / 60.0f;      //TODO fixa;

                    //Keep it within Pi och -Pi
                    if (rotation < -Constants.PI)
                        rotation += Constants.PI * 2f;
                    if (rotation > Constants.PI)
                        rotation -= Constants.PI * 2f;
                    int impulse = rotation > 0 ? -100 : 100;

                    //Apply rotation
                    z.getBody().applyAngularImpulse(impulse, true);

                    z.getBody().applyForceToCenter(direction.x, direction.y, true);
                    z.setIsMoving(true);
                } else {
                    z.setIsMoving(false);
                }

        } else if (playerPosition.x == zombiePosition.x && playerPosition.y == zombiePosition.y) {

            // TODO: attack
        } else {
            // TODO: some exception management
        }

    }


    }
}
