package edu.chalmers.zombie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.controller.InputController;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.testing.PlayerTest;
import edu.chalmers.zombie.view.GameScreen;

public class ZombieWeek extends Game {
	SpriteBatch batch;
	Texture img;

	private TiledMap tiledMap;
    private TiledMapTileLayer metaLayer;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	private float tileSize = 32;
    private Box2DDebugRenderer boxDebug;
    private World currentWorld;


	private PlayerTest playerTest;
    private Player player;

    private Zombie zombie;

    private InputController inputController;
    private MapController mapController;

    //HUD variables
    private BitmapFont bitmapFont;
    private SpriteBatch batchHUD;


	public void create () {
        mapController = new MapController();
        currentWorld = mapController.getWorld();
        setScreen(new GameScreen(this.currentWorld, tileSize));
        //Set input controller
        /*
        inputController = new InputController();
        Gdx.input.setInputProcessor(inputController);

        //Set map controller
        mapController = new MapController();

		//Ladda första kartan
        currentWorld = mapController.getWorld();

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
        //Skapa en box debugger
        boxDebug = new Box2DDebugRenderer();

        zombie = inputController.getZombie();

<<<<<<< HEAD
    */
//=======
        //HUD
        batchHUD = new SpriteBatch();
        bitmapFont = new BitmapFont();
//>>>>>>> e9fd29e199e6157b4894b0ce33d3209fea933596
	}

	@Override
	public void render () {
        super.render();
        /*
        GameModel gameModel = GameModel.getInstance();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Uppdatera fysik
        currentWorld.step(Constants.TIMESTEP, 6, 2);

        //Ta bort gamla objekt
        for(Body b : gameModel.getEntitiesToRemove()){
            gameModel.getLevel().getWorld().destroyBody(b);
        }
        gameModel.clearEntitiesToRemove();

        camera.position.set(player.getX(), player.getY(),0); //player is tileSize/2 from origin //TODO kosntig mätning men får inte rätt position annars
        camera.update();


//Rita kartan
		mapRenderer.setView(camera);
		mapRenderer.render();

<<<<<<< HEAD
	//	batch.begin();
	//	batch.draw(img, 0, 0);
	//	batch        .end();
=======


		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();



>>>>>>> e9fd29e199e6157b4894b0ce33d3209fea933596

        //Rita spelare
        //playerTest.setScale(1 / tileSize);
        mapRenderer.getBatch().begin();
        //playerTest.draw(mapRenderer.getBatch());
        ArrayList<Book> books = gameModel.getBooks();
        if (books.size() != 0) {
            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                if(b.toRemove()) {
                    books.remove(i);
                    i--;
                }
                else {
                   b.draw(mapRenderer.getBatch());
                }
            }
        }
        player.draw(mapRenderer.getBatch());
        zombie.draw(mapRenderer.getBatch());



        mapRenderer.getBatch().end();

        //rita box2d debug
        boxDebug.render(mapController.getWorld(), camera.combined);

    */

//<<<<<<< HEAD
    }
//=======


        //render HUD
    //String playerPos = "X: " + player.getX() + ", Y: " + player.getY();
    //String playerHealth = "Health: " + player.getLives();
    //String playerAmmo = "Ammo: " + player.getAmmunition();
    //batchHUD.begin();
    //bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    //bitmapFont.draw(batchHUD, playerHealth, 10, Gdx.graphics.getHeight()-10);
    //bitmapFont.draw(batchHUD, playerAmmo, 10, Gdx.graphics.getHeight()-25);
    //bitmapFont.draw(batchHUD, playerPos, 10, Gdx.graphics.getHeight()-40);
    //batchHUD.end();




//>>>>>>> e9fd29e199e6157b4894b0ce33d3209fea933596

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
    /*
		//Koden nedan kan man ha om man vill att spelv�rlden ut�kas n�r man resizar
		camera.setToOrtho(false, width / tileSize, height / tileSize);
		//Koden nedan om man vill ha statiskt.
		//camera.setToOrtho(false, 400 / 32, 400 / 32);
    */



	}

    @Override
    public void dispose(){

        player.dispose();
        currentWorld.dispose();

    }
}
