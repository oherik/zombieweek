package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.ScreenModel;
import edu.chalmers.zombie.utils.MenuBuilder;

/**
 * Menu Controller
 * Created by Tobias on 15-05-28.
 */
public class MenuController {

    public MenuController(){}

    /**
     * Creates new instances for the menus
     */
    public static void initializeMenus(){

        ScreenModel screenModel = GameModel.getInstance().getScreenModel();

        screenModel.setGameOverStage(MenuBuilder.createGameOverMenu());
        screenModel.setSoundAndSettingStage(MenuBuilder.createSoundAndSettingsMenu());
        screenModel.setPauseStage(MenuBuilder.createPauseMenu());

    }

    /**
     * Checks whether sound is on or not and adjust image for audio on/off icon thereafter
     */
    public static void updateSoundButton(ImageButton soundButton){
        boolean soundOn = GameModel.getInstance().isSoundOn();
        ImageButton.ImageButtonStyle soundButtonStyle = soundButton.getStyle();
        Texture newTexture;
        if (soundOn){
            newTexture = GameModel.getInstance().res.getTexture("audio-on");
        } else {
            newTexture = GameModel.getInstance().res.getTexture("audio-off");
        }
        soundButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(newTexture));
    }
}
