package edu.chalmers.zombie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.InputController;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.view.GameScreen;
import edu.chalmers.zombie.view.MainMenuScreen;

public class ZombieWeek extends Game {
    private World currentWorld;
    private InputController inputController;
    private MapController mapController;


	public void create () {
        mapController = new MapController();
        mapController.initializeRooms();
        currentWorld = mapController.getWorld();
        //setScreen(new MainMenuScreen()); //Main menu screen
        setScreen(new GameScreen(this.currentWorld, Constants.TILE_SIZE)); //Game screen
	}

	@Override
	public void render () {
        super.render();
    }
}
