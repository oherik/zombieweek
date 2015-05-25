package edu.chalmers.zombie.controller;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.model.GameModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by neda on 2015-05-21.
 */
public class ZombieController {

    public void move(Zombie z) {

        Point playerPosition = z.getThisMapController().getPlayerPosition();

        //point = new Vector2(playerPosition.x, playerPosition.y);

        Point zombiePosition = z.getZombiePosition();

        z.setSpeed(80);
        z.setDetectionRadius(10);

        Vector2 direction = new Vector2(playerPosition.x - zombiePosition.x, playerPosition.y - zombiePosition.y);


             /* --------------------- BÖRJAN ERIKS ÄNDRINGAR -------------------*/
        Player player = GameModel.getInstance().getPlayer();
        Point playerTile = new Point(Math.round(player.getX()-0.5f), Math.round(player.getY()-0.5f));
        Point zombieTile = new Point(Math.round(z.getX()-0.5f), Math.round(z.getY()-0.5f));
        ArrayList<Point> pathToPlayer = MapController.getPath(zombieTile, playerTile);

        if(pathToPlayer!=null){
            if(pathToPlayer.size()>1){
                pathToPlayer.remove(0); //Ta ej med tilen zombien står på
            }
            Iterator<Point> iteratorToPlayer = pathToPlayer.iterator();
            if(iteratorToPlayer.hasNext()) {
                Point temp = iteratorToPlayer.next();
                if (z.getNextPathTile() == null || zombieTile.equals(z.getNextPathTile()) || !temp.equals(z.getNextPathTile())) {
                    z.setNextPathTile(temp);
                }
            }
        }

        //För att få dit bodyn ska röra sig, ta nextPathTile med +0.5 på x och y. Det ger då mitten av tilen.

        /* --------------------- SLUT ERIKS ÄNDRINGAR ---------------------*/



        if(pathToPlayer!=null && z.getBody() != null) {

            zombiePosition = new Point(Math.round(z.getX()), Math.round(z.getY()));
        }

        if (playerPosition.x == zombiePosition.x && playerPosition.y == zombiePosition.y) {

            // TODO: attack
        } else if (playerPosition.y > zombiePosition.y && playerPosition.x == zombiePosition.x) {

            z.setForceY(z.getSpeed());
            z.setForceX(0);
        } else if (playerPosition.x > zombiePosition.x && playerPosition.y == zombiePosition.y) {

            z.setForceY(0);
            z.setForceX(z.getSpeed());
        } else if (playerPosition.x < zombiePosition.x && playerPosition.y == zombiePosition.y) {

            z.setForceY(0);
            z.setForceX(-z.getSpeed());
        } else if (zombiePosition.y < playerPosition.y && playerPosition.x == zombiePosition.x) {

            z.setForceY(-z.getSpeed());
            z.setForceX(0);
        } else if (playerPosition.y > zombiePosition.y && playerPosition.x > zombiePosition.x) {

            z.setForceY(z.getSpeed());
            z.setForceX(z.getSpeed());
        } else if (playerPosition.y < zombiePosition.y && playerPosition.x > zombiePosition.x) {

            z.setForceY(-z.getSpeed());
            z.setForceX(z.getSpeed());
        } else if (playerPosition.y > zombiePosition.y && playerPosition.x < zombiePosition.x) {

            z.setForceY(z.getSpeed());
            z.setForceX(-z.getSpeed());
        } else if (playerPosition.y < zombiePosition.y && playerPosition.x < zombiePosition.x) {

            z.setForceY(-z.getSpeed());
            z.setForceX(-z.getSpeed());
        } else {
            // TODO: some exception management
        }

        Circle zcircle = new Circle(zombiePosition.x, zombiePosition.y, z.getDetectionRadius());
        Circle pcircle = new Circle(playerPosition.x, playerPosition.y, z.getDetectionRadius());

        if (z.getBody() != null) {

            if (zcircle.overlaps(pcircle)) {

                z.getBody().applyForce(z.getForce(), z.getPoint(), !z.isKnockedOut());
                z.setIsMoving(true);
            }
        }
    }
}
