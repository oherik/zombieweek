package edu.chalmers.zombie.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.adapter.Entity;
import edu.chalmers.zombie.controller.InputController;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.adapter.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

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
    //HUD variables
    private BitmapFont bitmapFont;
    private SpriteBatch batchHUD;

    //För testa av path finding //TODO debug
    private PathAlgorithm pathFinding;
    private Iterator<Point> path;
    private Pixmap pixmap;
    private Texture pathTexture;
    private Sprite pathSprite;
    private int steps;

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
        //Lägg till kollisionsobjekt
        mapController.createObstacles("meta", "collision");
        //HUD
        batchHUD = new SpriteBatch();
        bitmapFont = new BitmapFont();

        /*---TEST--*/
        steps = 0;
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



        camera.position.set(gameModel.getPlayer().getX(),gameModel.getPlayer().getY(),0); //player is tileSize/2 from origin //TODO kosntig mätning men får inte rätt position annars
        camera.update();


//Rita kartan
        mapRenderer.setView(camera);
        mapRenderer.render();
        steps++;
        //	batch.begin();
        //	batch.draw(img, 0, 0);
        //	batch        .end();

        //Rita spelare
        //playerTest.setScale(1 / tileSize);
        mapRenderer.getBatch().begin();
        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        //playerTest.draw(mapRenderer.getBatch());
        removeEntities();
        ArrayList<Book> books = gameModel.getBooks();
        for (int i = 0; i<books.size(); i++) {
            Book b = books.get(i);
            if(b.toRemove())
                books.remove(i); //Förenklad forsats skulle göra detta svårt
            else
                b.draw(mapRenderer.getBatch());
        }

        gameModel.getPlayer().draw(mapRenderer.getBatch());
        gameModel.getZombie().draw(mapRenderer.getBatch());



        /*--------------------------TESTA PATH FINDING------------------------------------*/

        //Skapa path finding        //TODO debug





        if(steps>=60) {
            TiledMapTileLayer meta = (TiledMapTileLayer) gameModel.getLevel().getMap().getLayers().get("meta");
            pathFinding = new PathAlgorithm(meta, "collision");
            Point start = new Point(Math.round(gameModel.getZombie().getX()), Math.round(gameModel.getZombie().getY()));
            Point end = new Point(Math.round(gameModel.getPlayer().getX()), Math.round(gameModel.getPlayer().getY()));

            Point startTile = start;
            Point endTile = end;
            System.out.println(startTile + " " + endTile);
            path = pathFinding.getPath(startTile, endTile);
            int layerHeight = meta.getHeight();
            int layerWidth = meta.getWidth();
            //Rita ut pathen
            pixmap = new Pixmap(Math.round(layerWidth * tileSize), Math.round(layerHeight * tileSize), Pixmap.Format.RGBA8888);
            pixmap.setColor(com.badlogic.gdx.graphics.Color.RED);
            Point oldP = start;
            Point p = oldP;
            if (path == null)
                System.out.println("Ingen path hittad");
            else {
                System.out.println("Path:");
                while (path.hasNext()) {
                    Point tile = path.next();
                    System.out.print(tile.x + " " + tile.y);
                    p = tile;
                    System.out.print("    = " + p.x + " " + p.y + "\n");
                    oldP = p;
                }

            }
            steps=0;
        }
        /*-----------------SLUTTESTAT---------------------*/


        mapRenderer.getBatch().end();

        //rita box2d debug
        boxDebug.render(mapController.getWorld(), camera.combined);
        //render HUD
        String playerPos = "X: " + gameModel.getPlayer().getX() + ", Y: " + gameModel.getPlayer().getY();
        String playerHealth = "Health: " + gameModel.getPlayer().getLives();
        String playerAmmo = "Ammo: " + gameModel.getPlayer().getAmmunition();
        batchHUD.begin();
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFont.draw(batchHUD, playerHealth, 10, Gdx.graphics.getHeight()-10);
        bitmapFont.draw(batchHUD, playerAmmo, 10, Gdx.graphics.getHeight()-25);
        bitmapFont.draw(batchHUD, playerPos, 10, Gdx.graphics.getHeight()-40);
        batchHUD.end();


    }
    public void show(){

    }
    public void hide(){

    }
    public void dispose(){

    }

    /**
     * Removes the entity bodies from the world if necessary
     */
    private void removeEntities(){
        GameModel gameModel = GameModel.getInstance();
        for(Entity e: gameModel.getEntitiesToRemove()){
            gameModel.getLevel().destroyBody(e);
        }
        gameModel.clearEntitiesToRemove();

    }
}
