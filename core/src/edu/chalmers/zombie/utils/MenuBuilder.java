package edu.chalmers.zombie.utils;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.AudioController;
import edu.chalmers.zombie.controller.MenuController;
import edu.chalmers.zombie.model.GameModel;

/**
 * Builder to create menus
 *
 * Created by Tobias on 15-05-18.
 */
public class MenuBuilder {

    /**
     * Creates a skin for the menu buttons
     * @return Skin The skin created
     */
    public static ZWSkin createMenuSkin(){

        ZWSkin skin = new ZWSkin();

        ZWBitmapFont font = new ZWBitmapFont(); //sets font to 15pt Arial, if we want custom font -> via constructor
        skin.add("default", font);

        ZWPixmap pixmap = new ZWPixmap((int)(ZWGameEngine.getWindowWidth()/4),(int)(ZWGameEngine.getWindowWidth()/10));


        //Adding background to button
        skin.add("background",new ZWTexture(pixmap));

        //Create buttons
        skin.createButtons();

        return skin;
    }

    /**
     * Sets up sound and settings icon button
     */
    public static ZWStage createSoundAndSettingsMenu(){

        ZWStage soundAndSettingStage = new ZWStage();

        GameModel.getInstance().res.loadTexture("audio-off","core/assets/Images/audioOff.png");
        GameModel.getInstance().res.loadTexture("audio-on","core/assets/Images/audioOn.png");
        GameModel.getInstance().res.loadTexture("settings","core/assets/Images/settings.png");

        ZWImageButton settingsButton = new ZWImageButton();
        final ZWImageButton soundButton = new ZWImageButton();
        settingsButton.setImageUp(GameModel.getInstance().res.getTexture("settings"));
        soundButton.setImageUp(GameModel.getInstance().res.getTexture("audio-on"));

        settingsButton.setSize(40, 40);
        soundButton.setSize(40, 40);

        settingsButton.setPosition(ZWGameEngine.getWindowWidth() - 50, ZWGameEngine.getWindowHeight() - 50);
        soundButton.setPosition(ZWGameEngine.getWindowWidth() - 100, ZWGameEngine.getWindowHeight() - 50);

        soundAndSettingStage.addActor(settingsButton);
        soundAndSettingStage.addActor(soundButton);

        soundButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                AudioController audioController = new AudioController();
                audioController.toggleSound();
                MenuController.updateSoundButton(soundButton);
            }
        });


        return soundAndSettingStage;
    }


    /**
     * Sets up the pause menu
     */
    public static ZWStage createPauseMenu(){

        ZWStage pauseStage = new ZWStage();

        MenuBuilder menuBuilder = new MenuBuilder();
        ZWSkin skin = menuBuilder.createMenuSkin();

        ZWTable table = new ZWTable();

        ZWTextButton mainMenuButton = new ZWTextButton("Main Menu", skin);
        ZWTextButton quitGameButton = new ZWTextButton("Quit game", skin);

        table.add(mainMenuButton,250,50,15);
        table.add(quitGameButton,250,50,15);

        table.setFillParent(true);
        pauseStage.addActor(table);


        mainMenuButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                    ZWGameEngine.exit();
            }
        });


        return pauseStage;
    }

    public static ZWStage createGameOverMenu(){

        ZWStage gameOverStage = new ZWStage();

        ZWSkin skin = (new MenuBuilder()).createMenuSkin();

        ZWTable table = new ZWTable();

        ZWLabel label = new ZWLabel("Game Over");
        label.scale(3f);

        table.add(label,15);

        ZWTextButton startOverButton = new ZWTextButton("Start over", skin);
        table.add(startOverButton,250,50,15);

        ZWTextButton quitGameButton = new ZWTextButton("Quit game", skin);
        table.add(quitGameButton,250,50,15);

        table.setFillParent(true);
        gameOverStage.addActor(table);

        return gameOverStage;

    }

    public static ZWStage createNextLevelStage(){
        ZWStage nextLevelStage = new ZWStage();

        //TODO: create the stage!

        return nextLevelStage;
    }
}
