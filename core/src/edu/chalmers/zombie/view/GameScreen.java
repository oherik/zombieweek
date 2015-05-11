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
import edu.chalmers.zombie.adapter.Zombie;
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
    private Sprite tiledMapPainting, tiledMapPaintingTopLayer;

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

        //Scale the level images
        float scale= 1f/tileSize;
        mapController.scaleImages(scale);

        //Lägg till kollisionsobjekt
        mapController.initializeCollisionObjects();
        updateLevelIfNeeded();

        //Starta rendrerare
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);
        boxDebug = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(new InputController());


        //HUD
        batchHUD = new SpriteBatch();
        bitmapFont = new BitmapFont();

        /*---TEST--*/
        steps = 0;
        TiledMapTileLayer meta = (TiledMapTileLayer) GameModel.getInstance().getLevel().getMap().getLayers().get("meta");
        pathFinding = new PathAlgorithm(meta, "collision");
        /*---SLUTTEST---*/


    }

    /**
     * If the level has changed the map and renderer need to change as well
     */
    public void updateLevelIfNeeded() {
        if (mapController.worldNeedsUpdate()) {
            this.currentWorld = mapController.getWorld();
            tiledMap = mapController.getMap();
        /*--- test ---*/
            this.tiledMapPainting = mapController.getMapPainting(); //TODO test
            this.tiledMapPaintingTopLayer = mapController.getMapPaintingTopLayer(); //TODO test
            mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / tileSize);
            mapController.createBodies();
            mapController.setWorldNeedsUpdate(false);
        }
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
        updateLevelIfNeeded();
        GameModel gameModel = GameModel.getInstance();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Uppdatera fysik
        currentWorld.step(Constants.TIMESTEP, 6, 2);



        camera.position.set(gameModel.getPlayer().getX(),gameModel.getPlayer().getY(),0); //player is tileSize/2 from origin //TODO kosntig mätning men får inte rätt position annars
        camera.update();


//Rita kartan
        mapRenderer.setView(camera);
        //mapRenderer.render();
        steps++;
        mapRenderer.getBatch().begin();

        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        //playerTest.draw(mapRenderer.getBatch());
        tiledMapPainting.draw(mapRenderer.getBatch());
        ArrayList<Book> books = gameModel.getBooks();
        for (int i = 0; i<books.size(); i++) {
            Book b = books.get(i);
            long airTime = 500;
            long lifeTime = 5000; //life time for book in millisec
            if(System.currentTimeMillis() - b.getTimeCreated() > airTime)
                b.applyFriction();
            if (System.currentTimeMillis() - b.getTimeCreated() > lifeTime){
                gameModel.addEntityToRemove(b);
                b.markForRemoval();
            }
            if(b.toRemove())
                books.remove(i); //Förenklad forsats skulle göra detta svårt
            else
                b.draw(mapRenderer.getBatch());
        }

        removeEntities();


        for(Zombie z : gameModel.getZombies())
            z.draw(mapRenderer.getBatch());
        gameModel.getPlayer().moveIfNeeded();
        gameModel.getPlayer().draw(mapRenderer.getBatch());

        /* --- TEST rita ut det som ska vara ovanför --- */
        if( tiledMapPaintingTopLayer!=null) {
            tiledMapPaintingTopLayer.draw(mapRenderer.getBatch());

        }
        /* --- END TEST --- */
        mapRenderer.getBatch().end();



        /*--------------------------TESTA PATH FINDING------------------------------------*/

        //Skapa path finding        //TODO debug

        if(steps%60==0) {   //uppdaterar varje sekund
           updateZombiePaths();
        }
        /*-----------------SLUTTESTAT---------------------*/

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

    /**
     * Updates the zombie paths to the player.
     */
    private void updateZombiePaths(){   //TODO gör ingenting nu. Kanske ha en path-variabel i Zombie.java?
        GameModel gameModel = GameModel.getInstance();
        for(Zombie z : gameModel.getZombies())
           if(!z.isKnockedOut())     {
            Point start = new Point(Math.round(z.getX()), Math.round(z.getY()));
            Point end = new Point(Math.round(gameModel.getPlayer().getX()), Math.round(gameModel.getPlayer().getY()));
            path = pathFinding.getPath(start, end);                 //TODO gör nåt vettigt här istälelt för att abra printa.
            if (path == null)
                System.out.println("Ingen path hittad");
            else {
                System.out.println("\nPath från: " + start.x + " " + start.y + " till " + end.x + " " + end.y + ":");
                int i = 0;
                while (path.hasNext()) {
                    Point tile = path.next();
                    System.out.println(tile.x + " " + tile.y);
                    i++;
                }
                System.out.println("Antal steg: " + i);
            }
        }
    }
    public void dispose(){
        GameModel.getInstance().getPlayer().dispose();
        currentWorld.dispose();
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
