package edu.chalmers.zombie.utils;

import com.badlogic.gdx.Game;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.AudioController;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.controller.MenuController;
import edu.chalmers.zombie.controller.PlayerController;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.ScreenModel;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.view.GameScreen;
import edu.chalmers.zombie.view.MainMenuScreen;
import sun.jvm.hotspot.memory.PlaceholderEntry;

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

        GameModel.getInstance().res.loadTexture("audio-off","core/assets/Images/audioOff.png");//TODO: should use resource manager
        GameModel.getInstance().res.loadTexture("audio-on","core/assets/Images/audioOn.png");
        GameModel.getInstance().res.loadTexture("settings","core/assets/Images/settings.png");

        ZWImageButton settingsButton = new ZWImageButton();
        final ZWImageButton soundButton = new ZWImageButton();
        settingsButton.setImageUp(GameModel.getInstance().res.getTexture("settings"));
        soundButton.setImageUp(GameModel.getInstance().res.getTexture("audio-on"));

        settingsButton.setSize(40, 40);
        soundButton.setSize(40, 40);

        settingsButton.setPosition(ZWGameEngine.getWindowWidth() - 50, ZWGameEngine.getWindowHeight() - 50);
        soundButton.setPosition(ZWGameEngine.getWindowWidth() - 50, ZWGameEngine.getWindowHeight() - 50);

        //soundAndSettingStage.addActor(settingsButton);
        soundAndSettingStage.addActor(soundButton);

        soundButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                AudioController.toggleSound();
                MenuController.updateSoundButton(soundButton);
            }
        });


        return soundAndSettingStage;
    }


    /**
     * Sets up the pause menu
     * @return The ZWStage
     */
    public static ZWStage createPauseMenu(){

        ZWStage pauseStage = new ZWStage();

        ZWSkin skin = createMenuSkin();

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
                    ZWGameEngine.setScreen(new MainMenuScreen());
            }
        });

        quitGameButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                ZWGameEngine.exit();
            }
        });


        return pauseStage;
    }

    /**
     * Created the game over menu
     * @return
     */
    public static ZWStage createGameOverMenu(){

        ZWStage gameOverStage = new ZWStage();

        ZWSkin skin = createMenuSkin();

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

        startOverButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                Player player = GameModel.getInstance().getPlayer();
                player.lifeRefill();
                MapController.loadLevel(GameModel.getInstance().getCurrentLevelIndex());
                ZWGameEngine.setScreen(new GameScreen());
            }
        });

        quitGameButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                ZWGameEngine.exit();
            }
        });


        return gameOverStage;

    }

    public static ZWStage createNextLevelStage(){
        ZWStage nextLevelStage = new ZWStage();

        ZWTable table = new ZWTable();

        ZWLabel label = new ZWLabel("You made it!");
        label.scale(2.5f);

        table.add(label,15);


        //TODO: do we want buttons in the next level screen?


        table.setFillParent(true);
        nextLevelStage.addActor(table);


        return nextLevelStage;
    }


    /**
     * Sets up the stage for the main menu
     * @return The ZWStage
     */
    public static ZWStage createMainStage(){

        ZWStage mainStage = new ZWStage();

        ZWSkin skin = createMenuSkin();

        ZWTable table = new ZWTable();

        ZWTextButton newGameButton = new ZWTextButton("New game", skin);
        final ZWTextButton levelButton = new ZWTextButton("Choose level", skin);
        ZWTextButton exitGameButton = new ZWTextButton("Exit game", skin);

        table.add(newGameButton,250,50,15);
        table.add(levelButton,250,50,15);
        table.add(exitGameButton, 250, 50, 15);

        table.setFillParent(true);
        mainStage.addActor(table);

        newGameButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                ScreenModel screenModel = GameModel.getInstance().getScreenModel();
                screenModel.setMenuState(ScreenModel.MenuState.CHARACTER_MENU);
                ZWGameEngine.setInputProcessor(screenModel.getCharacterStage());
                //ZWGameEngine.setScreen(new GameScreen());
            }
        });

        levelButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                ScreenModel screenModel = GameModel.getInstance().getScreenModel();
                screenModel.setMenuState(ScreenModel.MenuState.LEVEL_MENU);
                ZWGameEngine.setInputProcessor(screenModel.getLevelChooserStage());
                levelButton.toggle();
            }
        });

        exitGameButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                ZWGameEngine.exit();
            }
        });

        return mainStage;

    }


    /**
     * Sets up the stage for the level chooser menu
     */
    public static ZWStage createLevelChooserStage(){
        ZWStage levelStage = new ZWStage();
        ZWSkin skin = createMenuSkin();
        ZWTable table = new ZWTable();
        table.setFillParent(true);
        levelStage.addActor(table);

        int levelsCompleted = GameModel.getInstance().getHighestCompletedLevel();

        int amountOfLevels = GameModel.getInstance().getAmountOfLevelsInGame();

        for (int i = 0;i <= amountOfLevels-1;i++){
            String buttonName = "Level " + (i+1);
            final int level = i;
            ZWTextButton levelButton;

            if (i>levelsCompleted){ //not completed levels
                ZWSkin disabledSkin = createMenuSkin();
                disabledSkin.createDisabledButtons();
                levelButton = new ZWTextButton(buttonName, disabledSkin);
            } else { //completed levels
                levelButton = new ZWTextButton(buttonName, skin);
                levelButton.addListener(new ZWClickAction(){
                    @Override
                    public void clicked(){
                        GameModel.getInstance().setCurrentRoomIndex(0);
                        GameModel.getInstance().setCurrentLevelIndex(level);

                        ZWGameEngine.setScreen(new GameScreen());
                        MapController.loadLevel(level);
                    }
                });
            }
            
            table.add(levelButton,250,50,15);
        }


        /*--- Back button ---*/
        ZWImageButton backIconButton = createBackButton();

        levelStage.addActor(backIconButton);

        return levelStage;
    }

    /**
     * Creates a back button in the top left of the screen
     * @return The image button
     */
    private static ZWImageButton createBackButton(){
        /*--- Back button ---*/
        ZWImageButton backIconButton = new ZWImageButton();
        backIconButton.setImageUp(new ZWTexture("core/assets/arrowLeft_grey.png"));//TODO: should use resource manager
        backIconButton.setImageDown(new ZWTexture("core/assets/arrowLeft.png"));
        backIconButton.setImageOver(new ZWTexture("core/assets/arrowLeft_lightgrey.png"));

        backIconButton.setSize(30,30);
        backIconButton.setPosition(10, ZWGameEngine.getWindowHeight()-40);

        backIconButton.addListener(new ZWClickAction() {
            @Override
            public void clicked() {
                ScreenModel screenModel = GameModel.getInstance().getScreenModel();
                screenModel.setMenuState(ScreenModel.MenuState.MAIN_MENU);
                ZWGameEngine.setInputProcessor(screenModel.getMainStage());
            }
        });

        return backIconButton;
    }

    /**
     * Creates the character menu
     * @return The ZWStage
     */
    public static ZWStage createCharacterStage(){

        ZWStage characterStage = new ZWStage();

        ZWTable table = new ZWTable();

        ZWLabel label = new ZWLabel("Choose a character");
        label.setColor(1f,1f,1f,1f);
        label.scale(2f);
        table.add(label,ZWGameEngine.getWindowHeight()-60);
        table.setFillParent(true);

        ZWImageButton characterButtonLeft = new ZWImageButton();
        ZWImageButton characterButtonRight = new ZWImageButton();

        characterButtonLeft.setImageUp(new ZWTexture("core/assets/Images/emilia-300400.png")); //TODO: should use resource manager
        characterButtonRight.setImageUp(new ZWTexture("core/assets/Images/emil-300400.png"));//TODO: should use resource manager

        int width = 300;
        int height = 400;
        int paddingBottom = 50;
        int paddingLeft = 15;

        characterButtonLeft.setSize(width,height);
        characterButtonLeft.setPosition(paddingLeft, paddingBottom);

        characterButtonRight.setSize(width,height);
        characterButtonRight.setPosition(paddingLeft+width, paddingBottom);

        characterStage.addActor(characterButtonLeft);
        characterStage.addActor(characterButtonRight);


        characterButtonLeft.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                GameModel.getInstance().setPlayerType(PlayerType.EMILIA);
                ScreenModel screenModel = GameModel.getInstance().getScreenModel();
                screenModel.setMenuState(ScreenModel.MenuState.GAMEMODE_STATE);
                ZWGameEngine.setInputProcessor(screenModel.getGameModeStage());
            }
        });



        characterButtonRight.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                GameModel.getInstance().setPlayerType(PlayerType.EMIL);
                ScreenModel screenModel = GameModel.getInstance().getScreenModel();
                screenModel.setMenuState(ScreenModel.MenuState.GAMEMODE_STATE);
                ZWGameEngine.setInputProcessor(screenModel.getGameModeStage());
            }
        });


        //Back button
        ZWImageButton backButton = createBackButton();
        characterStage.addActor(backButton);


        characterStage.addActor(table);

        return characterStage;
    }

    /**
     * Sets up the stage for the settings menu
     * @return The ZWStage
     */
    public static ZWStage createSettingsStage(){
        ZWStage settingsStage = new ZWStage();
        ZWTable table = new ZWTable();

        //TODO: Add settings stuff, eg switches for controls/sound etc

        table.setFillParent(true);
        settingsStage.addActor(table);

        return settingsStage;

    }

    public static ZWStage createGameModeStage(){
        ZWStage gameModeStage = new ZWStage();

        ZWSkin skin = createMenuSkin();

        ZWTable table = new ZWTable();

        ZWLabel label = new ZWLabel("Choose Game Mode");
        label.scale(2f);
        table.add(label,15);
        table.setFillParent(true);

        ZWTextButton wussModeButton = new ZWTextButton("Wuss Mode", skin);
        table.add(wussModeButton,250,50,15);

        ZWTextButton fearModeButton = new ZWTextButton("Fear of the Dark Mode", skin);
        table.add(fearModeButton,250,50,15);

        gameModeStage.addActor(table);

        wussModeButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                GameModel.getInstance().setFearOfTheDark(false);
                GameModel.getInstance().setCurrentLevelIndex(0);
                GameModel.getInstance().setCurrentRoomIndex(0);
                GameModel.getInstance().setWorldNeedsUpdate(true);

                PlayerController.genderSwopIfNeeded();
                ZWGameEngine.setScreen(new GameScreen());
            }
        });

        fearModeButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                GameModel.getInstance().setFearOfTheDark(true);
                GameModel.getInstance().setCurrentLevelIndex(0);
                GameModel.getInstance().setCurrentRoomIndex(0);
                GameModel.getInstance().setWorldNeedsUpdate(true);

                PlayerController.genderSwopIfNeeded();
                ZWGameEngine.setScreen(new GameScreen());
            }
        });

        return gameModeStage;

    }
}
