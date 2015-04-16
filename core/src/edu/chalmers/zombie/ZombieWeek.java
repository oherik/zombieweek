package edu.chalmers.zombie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import edu.chalmers.zombie.controller.InputController;
import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.testing.PlayerTest;

public class ZombieWeek extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	private float tileSize = 32;

	private PlayerTest playerTest;
    private Player player;

    private InputController inputController;

	@Override
	public void create () {
		//Ladda kartan
		tiledMap =new TmxMapLoader().load("core/assets/Map/Test_v2.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);

		//Fixa kameran
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(width, height);

		batch = new SpriteBatch();
		img = new Texture("core/assets/zombie.png");

		//Skapa spelare
		playerTest = new PlayerTest(new Sprite(new Texture("core/assets/player_professional_final_version.png")));



        //Set input controller
        inputController = new InputController();
        Gdx.input.setInputProcessor(inputController);

        player = inputController.getPlayer();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.getX() + tileSize/2, player.getY() + tileSize/2, 0); //player is tileSize/2 from origin
        camera.update();


//Rita kartan
		mapRenderer.setView(camera);
		mapRenderer.render();

	//	batch.begin();
	//	batch.draw(img, 0, 0);
	//	batch.end();

		//Rita spelare
		//playerTest.setScale(1 / tileSize);
        player.setScale(1/tileSize);
		mapRenderer.getBatch().begin();
		//playerTest.draw(mapRenderer.getBatch());
        player.draw(mapRenderer.getBatch());
        mapRenderer.getBatch().end();



	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

		//Koden nedan kan man ha om man vill att spelv�rlden ut�kas n�r man resizar
		camera.setToOrtho(false, width / tileSize, height / tileSize);
		//Koden nedan om man vill ha statiskt.
		//camera.setToOrtho(false, 400 / 32, 400 / 32);




	}

    @Override
    public void dispose(){

        playerTest.getTexture().dispose();

    }
}
