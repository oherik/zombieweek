package edu.chalmers.zombie.model;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Tobias on 15-05-28.
 */
public class ScreenModel {

    private Stage pauseStage;
    private Stage gameOverStage;
    private Stage soundAndSettingStage;
    private Stage nextLevelStage;
    public ScreenModel(){}

    public Stage getSoundAndSettingStage() {
        return soundAndSettingStage;
    }

    public void setSoundAndSettingStage(Stage soundAndSettingStage) {
        this.soundAndSettingStage = soundAndSettingStage;
    }

    public Stage getPauseStage() {
        return pauseStage;
    }

    public void setPauseStage(Stage pauseStage) {
        this.pauseStage = pauseStage;
    }

    public Stage getGameOverStage() {
        return gameOverStage;
    }

    public void setGameOverStage(Stage gameOverStage) {
        this.gameOverStage = gameOverStage;
    }

    public Stage getNextLevelStage() {
        return nextLevelStage;
    }

    public void setNextLevelStage(Stage nextLevelStage) {
        this.nextLevelStage = nextLevelStage;
    }



}
