package edu.chalmers.zombie.model.actors;

import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.ZombieType;

/**
 * A data zombie cannot be defeated - it has a super-high resilience and causes a damage of 1000 percent.
 * It has a detection radius of 1, which is very low, and will only attack if approached or provoked.
 *
 * Created by neda on 2015-05-19.
 * Modified by Erik
 */
public class DataZombie extends Zombie {

    public DataZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie-data"),
                GameModel.getInstance().res.getTexture("zombie-data-still"),
                GameModel.getInstance().res.getTexture("zombie-data-dead"),
                world, x, y,32);

        setType(ZombieType.DATA);
        setDetectionRadius(1);
        setStartingHp(90000);
        setSpeed(800);
        setAngularSpeed(1000);
        setDamage(1000);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
