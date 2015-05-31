package edu.chalmers.zombie.model.actors;

import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.ZombieType;

/**
 * An electro zombie has an intermediate resilience and a high speed. It causes a damage of 80 percent.
 * It has a detection radius of 10, which is intermediate.
 *
 * Created by neda on 2015-05-19.
 */
public class ElectroZombie extends Zombie {

    public ElectroZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie"),
                GameModel.getInstance().res.getTexture("zombie-still"),
                GameModel.getInstance().res.getTexture("zombie-dead"), world, x, y,32);
        setType(ZombieType.ELECTRO);
        setDetectionRadius(10);
        setStartingHp(50);
        setSpeed(200);
        setAngularSpeed(50);
        setDamage(80);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
