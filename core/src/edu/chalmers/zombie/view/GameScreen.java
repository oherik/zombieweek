package edu.chalmers.zombie.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.controller.InputController;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.model.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Tobias on 15-04-02.
 */
public class GameScreen implements Screen{
    private World currentWorld;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer boxDebug;
    private MapController mapController;
    private float tileSize;
    private TiledMap tiledMap;



    public GameScreen(World world, float tileSize){
        this.currentWorld = world;
        this.tileSize = tileSize;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);
        mapController = new MapController();
        tiledMap = mapController.getMap(0);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);
        boxDebug = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(new InputController());
    }

    public void resize(int width, int height){

        //Koden nedan kan man ha om man vill att spelv�rlden ut�kas n�r man resizar
        camera.setToOrtho(false, width / tileSize, height / tileSize);
        //Koden nedan om man vill ha statiskt.
        //camera.setToOrtho(false, 400 / 32, 400 / 32);

    }
    public void resume(){

    }
    public void pause(){

    }
    public void render(float f){
        GameModel gameModel = GameModel.getInstance();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Uppdatera fysik
        currentWorld.step(Constants.TIMESTEP, 6, 2);

        //Ta bort gamla objekt
        for(Body b : gameModel.getBodiesToRemove()){
            gameModel.getLevel().getWorld().destroyBody(b);
        }
        gameModel.clearBodiesToRemove();

        camera.position.set(gameModel.getPlayer().getX(),gameModel.getPlayer().getY(),0); //player is tileSize/2 from origin //TODO kosntig mätning men får inte rätt position annars
        camera.update();


//Rita kartan
        mapRenderer.setView(camera);
        mapRenderer.render();

        //	batch.begin();
        //	batch.draw(img, 0, 0);
        //	batch        .end();

        //Rita spelare
        //playerTest.setScale(1 / tileSize);
        mapRenderer.getBatch().begin();
        //playerTest.draw(mapRenderer.getBatch());
        ArrayList<Book> books = gameModel.getBooks();
        if (books.size() != 0) {
            for (Book b : books) {
                b.draw(mapRenderer.getBatch());
            }
        }
        gameModel.getPlayer().draw(mapRenderer.getBatch());
        gameModel.getZombie().draw(mapRenderer.getBatch());
        mapRenderer.getBatch().end();

        //rita box2d debug
        boxDebug.render(mapController.getWorld(), camera.combined);

    }
    public void show(){

    }
    public void hide(){

    }
    public void dispose(){

    }
}
