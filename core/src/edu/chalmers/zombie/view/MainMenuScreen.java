package edu.chalmers.zombie.view;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.MenuController;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.ScreenModel;
import edu.chalmers.zombie.utils.SaveLoadGame;

/**
 * The main menu screen of the game
 *
 * Created by Tobias on 15-05-10.
 */
public class MainMenuScreen extends ZWScreen {

    @Override
    public void show() {
        MenuController.initializeMenus();

        System.out.println("--- MAIN MENU ---");
        ScreenModel screenModel = GameModel.getInstance().getScreenModel();
        screenModel.setMenuState(ScreenModel.MenuState.MAIN_MENU);

        ZWGameEngine.setInputProcessor(screenModel.getMainStage());
    }

    @Override
    public void render(float delta) {
        ZWGameEngine.clearColor(1,1,1,1);
        ZWGameEngine.clearBufferBit();
        ScreenModel screenModel = GameModel.getInstance().getScreenModel();
        ScreenModel.MenuState menuState = screenModel.getMenuState();
        switch (menuState){
            case LEVEL_MENU:
                screenModel.getLevelChooserStage().act();
                screenModel.getLevelChooserStage().draw();
                break;
            case SETTINGS_MENU:
                screenModel.getSettingsStage().act();
                screenModel.getSettingsStage().draw();
                break;
            case CHARACTER_MENU:
                screenModel.getCharacterStage().act();
                screenModel.getCharacterStage().draw();
                break;
            case GAMEMODE_STATE:
                screenModel.getGameModeStage().act();
                screenModel.getGameModeStage().draw();
                break;
            default: //main menu
                screenModel.getMainStage().act();
                screenModel.getMainStage().draw();
                break;
        }
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
