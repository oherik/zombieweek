package edu.chalmers.zombie.model.actors;

import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.ZombieType;

/**
 * An IT zombie has a low speed and an intermediate resilience. It does a damage of 65 percent,
 * which is fairly high. It has a detection radius of 5, which is intermediate, borderline low.
 * Created by neda on 2015-05-20.
 */
public class ITZombie extends Zombie {

    public ITZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie-it"),
                GameModel.getInstance().res.getTexture("zombie-it-still"),
                GameModel.getInstance().res.getTexture("zombie-it-dead"), world, x, y,32);
        setType(ZombieType.IT);
        setDetectionRadius(5);
        setStartingHp(100);
        setSpeed(30);
        setAngularSpeed(50);
        setDamage(65);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
