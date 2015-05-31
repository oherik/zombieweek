package edu.chalmers.zombie;

import com.badlogic.gdx.Game;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.view.GameScreen;
import edu.chalmers.zombie.view.MainMenuScreen;

public class ZombieWeek extends Game {
<<<<<<< HEAD
    private MapController mapController;

	public void create () {
        mapController = new MapController(); //TODO: gör denna något?
        //setScreen(new MainMenuScreen()); //Main menu screen
=======

    public void create () {
>>>>>>> Erik_dev
        setScreen(new GameScreen()); //Game screen
	}

	@Override
	public void render () {
        super.render();
    }
}
