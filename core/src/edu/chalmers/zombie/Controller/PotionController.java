package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.Potion;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.utils.PotionType;

/**
 * Created by Erik on 2015-05-29.
 */
public class PotionController {

    public static void spawnPotion(String typeName, Room room, int x, int y){
        spawnPotion(PotionType.valueOf(typeName), room, x, y);
    }

    public static void spawnPotion(PotionType type, Room room, int x, int y){
        switch(type){
            case HEALTH:
                room.addPotion(new Potion(type, new Sprite(GameModel.getInstance().res.getTexture("potion-health")), room.getWorld().getWorld(), x, y));
                break;
            case SPEED:
                room.addPotion(new Potion(type, new Sprite(GameModel.getInstance().res.getTexture("potion-speed")), room.getWorld().getWorld(), x, y));
                break;
            default:
                //TODO randomize?
                break;
        }


    }
}
