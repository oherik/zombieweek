package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Player;

/**
 * Created by daniel on 5/27/2015.
 */
public class Blood {
    private ZWSprite blood;
    private ZWTimer t = new ZWTimer();
    private boolean drawing = false;
    private ZWTask task;
    public Blood(){
        ZWTexture bloodTexture = new ZWTexture("core/assets/blood.png");
        blood = new ZWSprite(bloodTexture);
        blood.setAlpha(0.4f);
        task = new ZWTask() {
            @Override
            public void run() {
                GameModel gameModel = GameModel.getInstance();
                Player player = gameModel.getPlayer();
                player.setIsHit(false);
                drawing = false;
            }
        };
    }

    public void draw(ZWSpriteBatch spriteBatch){
        GameModel gameModel = GameModel.getInstance();
        Player player = gameModel.getPlayer();
        if (drawing && player.isHit()){
            blood.draw(spriteBatch);
        } else if (!drawing && player.isHit()){
            t.scheduleTask(task, 1);
            t.start();
            drawing = true;
        }
    }
}
