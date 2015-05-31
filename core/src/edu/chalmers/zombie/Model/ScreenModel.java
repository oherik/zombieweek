package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.ZWStage;

/**
 * Model for the screens
 *
 * Created by Tobias on 15-05-28.
 */
public class ScreenModel {

    private ZWStage pauseStage;
    private ZWStage gameOverStage;
    private ZWStage soundAndSettingStage;
    private ZWStage nextLevelStage;
    private ZWStage mainStage;
    private ZWStage levelChooserStage;
    private ZWStage settingsStage;
    private ZWStage characterStage;
   private ZWStage gameModeStage;
    public enum MenuState{MAIN_MENU, LEVEL_MENU, SETTINGS_MENU,CHARACTER_MENU, GAMEMODE_STATE}
    private MenuState menuState;

    public ScreenModel(){}

    public ZWStage getSoundAndSettingStage() {
        return soundAndSettingStage;
    }

    public void setSoundAndSettingStage(ZWStage soundAndSettingStage) {
        this.soundAndSettingStage = soundAndSettingStage;
    }

    public ZWStage getPauseStage() {
        return pauseStage;
    }

    public void setPauseStage(ZWStage pauseStage) {
        this.pauseStage = pauseStage;
    }

    public ZWStage getGameOverStage() {
        return gameOverStage;
    }

    public void setGameOverStage(ZWStage gameOverStage) {
        this.gameOverStage = gameOverStage;
    }

    public ZWStage getNextLevelStage() {
        return nextLevelStage;
    }

    public void setNextLevelStage(ZWStage nextLevelStage) {
        this.nextLevelStage = nextLevelStage;
    }

    public ZWStage getMainStage() {
        return mainStage;
    }

    public void setMainStage(ZWStage mainStage) {
        this.mainStage = mainStage;
    }

    public ZWStage getLevelChooserStage() {
        return levelChooserStage;
    }

    public void setLevelChooserStage(ZWStage levelChooserStage) {
        this.levelChooserStage = levelChooserStage;
    }

    public ZWStage getSettingsStage() {
        return settingsStage;
    }

    public void setSettingsStage(ZWStage settingsStage) {
        this.settingsStage = settingsStage;
    }

    public MenuState getMenuState() {
        return menuState;
    }

    public void setMenuState(MenuState menuState) {
        this.menuState = menuState;
    }

    public ZWStage getCharacterStage() {
        return characterStage;
    }

    public void setCharacterStage(ZWStage characterStage) {
        this.characterStage = characterStage;
    }

    public ZWStage getGameModeStage() {
        return gameModeStage;
    }

    public void setGameModeStage(ZWStage gameModeStage) {
        this.gameModeStage = gameModeStage;
    }



}
