package edu.chalmers.zombie.view;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.utils.MenuBuilder;
import edu.chalmers.zombie.controller.SaveLoadController;
import edu.chalmers.zombie.model.GameModel;

/**
 * The main menu screen of the game
 *
 * Created by Tobias on 15-05-10.
 */
public class MainMenuScreen extends ZWScreen {

    private ZWStage mainStage;
    private ZWStage levelStage;
    private ZWStage settingsStage;
    private ZWSkin skin;
    private enum MenuState{MAIN_MENU, LEVEL_MENU, SETTINGS_MENU}
    private MenuState menuState;

    @Override
    public void show() {
        SaveLoadController saveLoadController = new SaveLoadController(); //TODO: should not be instantiated here, but loads saved game to gameModel and therefore needed

        MenuBuilder menuBuilder = new MenuBuilder();
        //Look of buttons
        skin = menuBuilder.createMenuSkin();

        mainStage = new ZWStage();
        levelStage = new ZWStage();
        settingsStage = new ZWStage();

        //Sets up the different stages for the menus
        setUpMainStage();
        setUpLevelStage();
        setUpSettingsStage();

        System.out.println("--- MAIN MENU ---");
        menuState = MenuState.MAIN_MENU;
        ZWGameEngine.setInputProcessor(mainStage);
    }

    @Override
    public void render(float delta) {
        ZWGameEngine.clearColor(1,1,1,1);
        ZWGameEngine.clearBufferBit();

        switch (menuState){
            case MAIN_MENU:
                mainStage.act(); //draws main menu
                mainStage.draw();
                break;
            case LEVEL_MENU:
                levelStage.act(); //draws level menu
                levelStage.draw();
                break;
            case SETTINGS_MENU:
                settingsStage.act(); //draws settings menu
                settingsStage.draw();
                break;
        }
    }





    /**
     * Sets up the stage for the main menu
     */
    private void setUpMainStage(){

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
               ZWGameEngine.setScreen(new GameScreen());
            }
        });

        levelButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                menuState = MenuState.LEVEL_MENU;
                ZWGameEngine.setInputProcessor(levelStage);
                levelButton.toggle();
            }
        });

        exitGameButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                ZWGameEngine.exit();
            }
        });

    }

    /**
     * Sets up the stage for the level menu
     */
    private void setUpLevelStage(){
        ZWTable table = new ZWTable();
        table.setFillParent(true);
        levelStage.addActor(table);

        int levelsCompleted = GameModel.getInstance().getHighestCompletedRoom();

        for (int i = 0;i <= levelsCompleted;i++){
            String buttonName = "Room " + (i+1);
            final int level = i;
            ZWTextButton levelButton = new ZWTextButton(buttonName, skin);
            table.add(levelButton,250,50,15);


            levelButton.addListener(new ZWClickAction(){
                @Override
                public void clicked(){
                    MapController mapController = new MapController();
                    ZWWorld world = mapController.getRoom(level).getWorld();
                    ZWGameEngine.setScreen(new GameScreen());
                }
            });


        }


        /*--- Back button ---*/

        ZWImageButton backIconButton = new ZWImageButton();
        backIconButton.setImageUp(new ZWTexture("core/assets/arrowLeft_grey.png"));
        backIconButton.setImageDown(new ZWTexture("core/assets/arrowLeft.png"));
        backIconButton.setImageOver(new ZWTexture("core/assets/arrowLeft_lightgrey.png"));

        backIconButton.setSize(30,30);
        backIconButton.setPosition(10, ZWGameEngine.getWindowHeight()-40);

        levelStage.addActor(backIconButton);

        backIconButton.addListener(new ZWClickAction(){
            @Override
            public void clicked(){
                menuState = MenuState.MAIN_MENU;
                ZWGameEngine.setInputProcessor(mainStage);
            }
        });


    }

    /**
     * Sets up the stage for the settings menu
     */
    private void setUpSettingsStage(){
        ZWTable table = new ZWTable();

        //TODO: Add settings stuff, eg switches for controls/sound etc

        table.setFillParent(true);
        settingsStage.addActor(table);

    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
