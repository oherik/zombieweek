package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.ZWSound;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.ResourceManager;

/**
 * Controller for audio and sound effects
 *
 * Created by Tobias on 15-05-21.
 */
public class AudioController {

    /**
     * Initializes all sound effects and music used in the application
     */
    public static void initializeSounds(){
        ResourceManager res = GameModel.getInstance().res;
        res.loadSound("throw", "core/assets/Audio/Sound_effects/throw_book.mp3");
        res.loadSound("menu_hover", "core/assets/Audio/Sound_effects/menu_hover.mp3");
        res.loadSound("zombie_hit","core/assets/Audio/Sound_effects/zombie_hit.mp3");
        res.loadSound("pick_up_book","core/assets/Audio/Sound_effects/pick_up_book.mp3");
        res.loadSound("zombie_sleeping","core/assets/Audio/Sound_effects/zombie_sleeping.mp3");
    }


    /**
     * Play sound
     * @param sound The sound that should be played
     */
    public static void playSound(ZWSound sound){
        GameModel gameModel = GameModel.getInstance();
        if (gameModel.isSoundOn()){
            sound.play();
        }
    }

    /**
     * Toggle sound ON/OFF
     */
    public static void toggleSound(){
        GameModel gameModel = GameModel.getInstance();
        if (gameModel.isSoundOn()){
            GameModel.getInstance().setSoundOn(false);
        } else {
            GameModel.getInstance().setSoundOn(true);
        }
    }
}
