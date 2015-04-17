package edu.chalmers.zombie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import edu.chalmers.zombie.controller.InputController;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.testing.PlayerTest;

public class ZombieWeek extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private TiledMap tiledMap;
    private TiledMapTileLayer metaLayer;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	private float tileSize = 32;
    private Box2DDebugRenderer boxDebug;


	private PlayerTest playerTest;
    private Player player;

    private InputController inputController;
    private MapController mapController;

	@Override
	public void create () {
        //Set input controller
        inputController = new InputController();
        Gdx.input.setInputProcessor(inputController);

        //Set map controller
        mapController = new MapController();

		//Ladda första kartan
		tiledMap = mapController.getMap(0);
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);

        //Lägg till kollisionsobjekt
        mapController.createObstacles("meta", "collision");

		//Fixa kameran
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(width, height);

		batch = new SpriteBatch();
		img = new Texture("core/assets/zombie.png");

        player = inputController.getPlayer();
        player.scale(1/tileSize);
        //Skapa en box debugger
        boxDebug = new Box2DDebugRenderer();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.getX() + tileSize/2 - tileSize/2+0.5f, player.getY() + tileSize/2- tileSize/2+0.5f, 0); //player is tileSize/2 from origin //TODO kosntig mätning men får inte rätt position annars
        camera.update();


//Rita kartan
		mapRenderer.setView(camera);
		mapRenderer.render();

	//	batch.begin();
	//	batch.draw(img, 0, 0);
	//	batch.end();

		//Rita spelare
		//playerTest.setScale(1 / tileSize);
		mapRenderer.getBatch().begin();
		//playerTest.draw(mapRenderer.getBatch());
        player.draw(mapRenderer.getBatch());
        mapRenderer.getBatch().end();

        //rita box2d debug
        boxDebug.render(mapController.getWorld(), camera.combined);



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

        player.getTexture().dispose();

    }
}
