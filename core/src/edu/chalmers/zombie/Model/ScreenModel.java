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



}
