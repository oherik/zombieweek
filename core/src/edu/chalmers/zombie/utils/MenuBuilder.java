package edu.chalmers.zombie.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import edu.chalmers.zombie.controller.AudioController;
import edu.chalmers.zombie.controller.MenuController;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.view.MainMenuScreen;

/**
 * Controller to take care of Menus
 * Created by Tobias on 15-05-18.
 */
public class MenuBuilder {

    /**
     * Creates a skin for the menu buttons
     * @return Skin The skin created
     */
    public static Skin createMenuSkin(){

        Skin skin = new Skin();

        BitmapFont font = new BitmapFont(); //sets font to 15pt Arial, if we want custom font -> via constructor
        skin.add("default", font);

        //Creating a button texture
        Pixmap pixmap = new Pixmap((int)(Gdx.graphics.getWidth()/4),(int)(Gdx.graphics.getHeight()/10), Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE); //button color
        pixmap.fill();

        //Adding background to button
        skin.add("background",new Texture(pixmap));

        //Create buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        return skin;
    }

    /**
     * Sets up sound and settings icon button
     */
    public static Stage createSoundAndSettingsMenu(){

        Stage soundAndSettingStage = new Stage();

        ImageButton.ImageButtonStyle settingsButtonStyle = new ImageButton.ImageButtonStyle();
        ImageButton.ImageButtonStyle soundButtonStyle = new ImageButton.ImageButtonStyle();

        GameModel.getInstance().res.loadTexture("audio-off","core/assets/Images/audioOff.png");
        GameModel.getInstance().res.loadTexture("audio-on","core/assets/Images/audioOn.png");
        GameModel.getInstance().res.loadTexture("settings","core/assets/Images/settings.png");

        settingsButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(GameModel.getInstance().res.getTexture("settings")));
        soundButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(GameModel.getInstance().res.getTexture("audio-on")));

        ImageButton settingsButton = new ImageButton(settingsButtonStyle);
        final ImageButton soundButton = new ImageButton(soundButtonStyle);

        settingsButton.setSize(40, 40);
        soundButton.setSize(40, 40);

        settingsButton.setPosition(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50);
        soundButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        soundAndSettingStage.addActor(settingsButton);
        soundAndSettingStage.addActor(soundButton);

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioController audioController = new AudioController();
                audioController.toggleSound();
                MenuController.updateSoundButton(soundButton);


                //updateSoundButton();
            }
        });

        return soundAndSettingStage;
    }

    /**
     * Sets up the pause menu
     */
    public static Stage createPauseMenu(){

        Stage pauseStage = new Stage();

        MenuBuilder menuBuilder = new MenuBuilder();
        Skin skin = menuBuilder.createMenuSkin();

        Table table = new Table();

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        TextButton quitGameButton = new TextButton("Quit game", skin);

        table.add(mainMenuButton).size(250,50).padBottom(15).row();
        table.add(quitGameButton).size(250,50).padBottom(15).row();

        table.setFillParent(true);
        pauseStage.addActor(table);

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });

        quitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        return pauseStage;
    }

    public static Stage createGameOverMenu(){

        Stage gameOverStage = new Stage();

        Skin skin = (new MenuBuilder()).createMenuSkin();

        Table table = new Table();
        com.badlogic.gdx.utils.StringBuilder stringBuilder = new com.badlogic.gdx.utils.StringBuilder();
        stringBuilder.append("Game Over");

        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle labelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        BitmapFont font = new BitmapFont(); //sets font to 15pt Arial, if we want custom font -> via constructor
        font.scale(3);
        labelStyle.font = font;

        com.badlogic.gdx.scenes.scene2d.ui.Label label = new com.badlogic.gdx.scenes.scene2d.ui.Label(stringBuilder,labelStyle);
        table.add(label).padBottom(15).row();

        TextButton startOverButton = new TextButton("Start over", skin);
        table.add(startOverButton).size(250,50).padBottom(15).row();

        TextButton quitGameButton = new TextButton("Quit game", skin);
        table.add(quitGameButton).size(250,50).padBottom(15).row();

        table.setFillParent(true);
        gameOverStage.addActor(table);

        return gameOverStage;

    }

    public static Stage createNextLevelStage(){
        Stage nextLevelStage = new Stage();

        //TODO: create the stage!

        return nextLevelStage;
    }
}
