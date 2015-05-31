package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.ZWBody;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.adapter.ZWWorld;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.ZombieType;

import java.awt.*;

/**
 * A boss zombie has a fairly high resilience and an intermediate speed. It causes a damage of 100 percent.
 * It has a detection radius of 10, which is intermediate.
 *
 * Created by neda on 2015-05-20.
 * Modified by Erik
 */
public class BossZombie extends Zombie {

    public BossZombie(ZWWorld world, int x, int y) {

        super(GameModel.getInstance().res.getTexture("zombie-boss"),
                GameModel.getInstance().res.getTexture("zombie-boss-still"),
                GameModel.getInstance().res.getTexture("zombie-boss-dead"), world, x, y,64);

        setType(ZombieType.BOSS);
        setDetectionRadius(10);
        setStartingHp(200);
        setSpeed(100);
        setAngularSpeed(1000);
        setDamage(100);


    }
    @Override
    protected void createBody(float x, float y){
        ZWBody body = new ZWBody();
        short categoryBits = Constants.COLLISION_ZOMBIE;
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_WATER |
                Constants.COLLISION_SNEAK | Constants.COLLISION_ACTOR_OBSTACLE | Constants.COLLISION_LEVEL;

        body.createBodyDef(true, x+0.5f, y+0.5f, 20f, 20f);
        body.setFixtureDef(0.8f, 0, 2, 2, categoryBits, maskBits, false);
        //Set body
        super.setBody(body);
        super.getBody().setUserData(this);
        super.getBody().setAngularDamping(10000);
    }

    @Override
    public ZWVector getVelocity() {
        return null;
    }
}
