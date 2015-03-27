package edu.chalmers.zombie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class ZombieWeek extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;

	@Override
	public void create () {
		//Ladda kartan
		tiledMap =new TmxMapLoader().load("core/assets/Map/Test_v1.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/32f);

		//Fixa kameran
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(width, height);

		batch = new SpriteBatch();
		img = new Texture("core/assets/zombie.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//Rita kartan
		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

		//Koden nedan kan man ha om man vill att spelvärlden utökas när man resizar
		camera.setToOrtho(false, width / 32, height / 32);
		//Koden nedan om man vill ha statiskt.
		//camera.setToOrtho(false, 400 / 32, 400 / 32);




	}
}
