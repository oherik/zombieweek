package edu.chalmers.zombie;

import com.badlogic.gdx.Game;
import edu.chalmers.zombie.utils.SaveLoadGame;
import edu.chalmers.zombie.view.GameScreen;

public class ZombieWeek extends Game {

	public void create () {
        //setScreen(new MainMenuScreen()); //Main menu screen
        SaveLoadGame saveLoadGame = new SaveLoadGame();
        saveLoadGame.loadGame();
        setScreen(new GameScreen()); //Game screen
	}

	@Override
	public void render () {
        super.render();
    }
}
