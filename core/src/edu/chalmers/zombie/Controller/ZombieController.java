package edu.chalmers.zombie.controller;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import edu.chalmers.zombie.adapter.Zombie;

import java.awt.*;
import java.util.ArrayList;

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

        ArrayList<Point> pathToPlayer = MapController.getPath(zombiePosition, playerPosition);

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
