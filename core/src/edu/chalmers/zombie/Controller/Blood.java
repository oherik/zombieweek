package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.model.GameModel;

/**
 * Created by daniel on 5/27/2015.
 */
public class Blood {
    private Sprite blood;
    private Timer t = new Timer();
    private boolean drawing = false;
    private Timer.Task task;
    public Blood(){
        Texture bloodTexture = new Texture("core/assets/blood.png");
        blood = new Sprite(bloodTexture);
        blood.setAlpha(0.4f);
        task = new Timer.Task() {
            @Override
            public void run() {
                GameModel gameModel = GameModel.getInstance();
                Player player = gameModel.getPlayer();
                player.setIsHit(false);
                drawing = false;
            }
        };
    }

    public void draw(SpriteBatch spriteBatch){
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
