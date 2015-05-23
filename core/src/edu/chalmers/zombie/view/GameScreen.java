package edu.chalmers.zombie.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ShortArray;
import edu.chalmers.zombie.adapter.Entity;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.controller.*;
import edu.chalmers.zombie.adapter.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.MenuBuilder;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private TiledMapImageLayer tiledMapBottomLayer, tiledMapTopLayer;

    //För testa av path finding //TODO debug
    private PathAlgorithm pathFinding;
    private Iterator<Point> path;
    private Pixmap pixmap;
    private Texture pathTexture;
    private Sprite pathSprite;
    private int steps;

    private Stage pauseStage;

    private Texture tex = new Texture("core/assets/darkness.png");
    private Texture light = new Texture("core/assets/light.png");
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Vector2 playerPosition = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    Vector2 coll = new Vector2();
    private float currentFraction = 1337;
    private boolean foundFixture;
    private ArrayList<Float> collisionPoints = new ArrayList<Float>();
    private ArrayList<Float> corners = new ArrayList<Float>();



    public GameScreen(World world, float tileSize){

        this.currentWorld = world;
        this.tileSize = tileSize;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);
        mapController = new MapController();

        //Scale the level images
        float scale= 1f/tileSize;
//        mapController.scaleImages(scale);

        //Lägg till kollisionsobjekt
        mapController.initializeCollisionObjects();
        updateLevelIfNeeded();

        //Spelaren med
        mapController.setPlayerBufferPosition(GameModel.getInstance().getLevel().getPlayerSpawn());

        //Starta rendrerare
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);
        boxDebug = new Box2DDebugRenderer();



        //HUD
        batchHUD = new SpriteBatch();
        bitmapFont = new BitmapFont();

        /*---TEST--*/
        steps = 0;
        TiledMapTileLayer meta = (TiledMapTileLayer) GameModel.getInstance().getLevel().getMetaLayer();
        pathFinding = new PathAlgorithm(meta, Constants.COLLISION_PROPERTY_ZOMBIE);
        /*---SLUTTEST---*/

        GameModel.getInstance().setGameState(GameState.GAME_RUNNING);

        //Pause menu
        pauseStage = new Stage();
        setUpPauseMenu();

        //Set input
        InputProcessor inputProcessorOne = new InputController();
        InputProcessor inputProcessorTwo = pauseStage;
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputProcessorOne);
        inputMultiplexer.addProcessor(inputProcessorTwo);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    /**
     * If the level has changed the map and renderer need to change as well
     */
    public void updateLevelIfNeeded() {
        if (mapController.worldNeedsUpdate()) {
            this.currentWorld = mapController.getWorld();
            tiledMap = mapController.getMap();
        /*--- test ---*/
            this.tiledMapBottomLayer = mapController.getMapBottomLayer(); //TODO test
            this.tiledMapTopLayer = mapController.getMapTopLayer(); //TODO test
            mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / tileSize);
            mapController.createBodiesIfNeeded();
            if(GameModel.getInstance().getPlayer().getBody() == null){
                GameModel.getInstance().getPlayer().createDefaultBody(currentWorld, mapController.getPlayerBufferPosition());
            }
         //   mapController.updatePlayerPosition(mapController.getPlayerBufferPosition());
            mapController.setPlayerBufferPosition(null);
            TiledMapTileLayer meta = mapController.getMapMetaLayer();
            pathFinding = new PathAlgorithm(meta, Constants.COLLISION_PROPERTY_ZOMBIE);

            mapController.setWorldNeedsUpdate(false);

            //Save game
            SaveLoadController saveLoadController = new SaveLoadController();
            saveLoadController.saveGame();
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
        GameState gameState = GameModel.getInstance().getGameState();
        switch (gameState){
            case GAME_RUNNING:
                updateRunning(f);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
        }

        GameModel gameModel = GameModel.getInstance();
        for (Zombie z : gameModel.getZombies()) {

            z.moveToPlayer(pathFinding);
        }
    }

    /**
     * Render game when game is running
     * @param f
     */
    private void updateRunning(float f){

        updateLevelIfNeeded();

        GameModel gameModel = GameModel.getInstance();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Uppdatera fysik
        //Tells GameModel when step is happening
        gameModel.setStepping(true);
        currentWorld.step(Constants.TIMESTEP, 6, 2);
        gameModel.setStepping(false);
        removeEntities();
        if(!gameModel.worldNeedsUpdate()) {

            camera.position.set(gameModel.getPlayer().getX(), gameModel.getPlayer().getY(), 0); //player is tileSize/2 from origin //TODO kosntig mätning men får inte rätt position annars
            camera.update();


//Rita kartan
            int[] backgroundLayers = {0};
            int[] foregroundLayers = {1};
            mapRenderer.render(backgroundLayers);
            mapRenderer.setView(camera);

            //mapRenderer.render();
            steps++;
            mapRenderer.getBatch().begin();

            mapRenderer.getBatch().setProjectionMatrix(camera.combined);

            ArrayList<Book> books = gameModel.getBooks();
            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                long airTime = 500;
                long lifeTime = 5000; //life time for book in millisec
                if (System.currentTimeMillis() - b.getTimeCreated() > airTime && b.getBody()!=null)
                    EntityController.hitGround(b);
                if (System.currentTimeMillis() - b.getTimeCreated() > lifeTime) {
                    gameModel.addEntityToRemove(b);
                    b.markForRemoval();
                }
                if (b.toRemove())
                    books.remove(i); //Förenklad forsats skulle göra detta svårt
                else
                    b.draw(mapRenderer.getBatch());
            }


            for (Zombie z : gameModel.getZombies()) {

                z.draw(mapRenderer.getBatch());
                //    z.moveToPlayer(pathFinding);
            }

            gameModel.getPlayer().moveIfNeeded();
            gameModel.getPlayer().draw(mapRenderer.getBatch());
            gameModel.getPlayer().getHand().drawAimer(mapRenderer.getBatch());

            mapRenderer.getBatch().end();
            if (tiledMapTopLayer != null) {
                mapRenderer.render(foregroundLayers);

            }


            //rita box2d debug
            boxDebug.render(mapController.getWorld(), camera.combined);
            //render HUD
            String playerPos = "X: " + gameModel.getPlayer().getX() + ", Y: " + gameModel.getPlayer().getY();
            String playerHealth = "Health: " + gameModel.getPlayer().getLives();
            String playerAmmo = "Ammo: " + gameModel.getPlayer().getAmmunition();
            batchHUD.begin();
            bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            bitmapFont.draw(batchHUD, playerHealth, 10, Gdx.graphics.getHeight() - 10);
            bitmapFont.draw(batchHUD, playerAmmo, 10, Gdx.graphics.getHeight() - 25);
            bitmapFont.draw(batchHUD, playerPos, 10, Gdx.graphics.getHeight() - 40);
            batchHUD.end();




                        /* ----------------- TEST FLASHLIGHT -----------------*/


            //------------------------------------------------------------------------
            PolygonSpriteBatch psb = new PolygonSpriteBatch();
            SpriteBatch sb = new SpriteBatch();
            sb.begin();
            psb.setProjectionMatrix(camera.combined);
            psb.begin();
            Flashlight flashlight = new Flashlight(currentWorld);
            flashlight.draw(psb, sb);
            psb.end();
            sb.end();
            /*
            float direction = gameModel.getPlayer().getHand().getDirection() + Constants.PI/2;
            playerPosition.set(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
            RayCastCallback callback = new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    if (fixture.getFilterData().categoryBits ==
                            Constants.COLLISION_OBSTACLE){
                        if (fraction < currentFraction) {
                            currentFraction = fraction;
                            coll.set(point);
                        }
                        foundFixture =  true;
                    }
                        return 1;
                }
            };
            /* ----------------- TEST FLASHLIGHT -----------------*/
            /*
            float coneWidth = Constants.PI/4;
            int numberOfRays = 100;
            int coneLength = 8;
            Vector2[] rays = new Vector2[numberOfRays];
            Vector2 playerPosition =  new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
            ArrayList<Vector2> endPoints = new ArrayList<Vector2>();
            for(int i = 0; i<numberOfRays; i++){
                rays[i] = new Vector2(1,1);
                rays[i].setLength(coneLength);
                rays[i].setAngleRad(direction - coneWidth / 2 + i * coneWidth / numberOfRays);
                Vector2 end = new Vector2(rays[i]);
                end.add(playerPosition);
                endPoints.add(end);

            }
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.WHITE);
            for (Vector2 line: rays){
                currentFraction = 1337;
                foundFixture = false;

                currentWorld.rayCast(callback, new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY()), new Vector2(line.x + gameModel.getPlayer().getX(), line.y + gameModel.getPlayer().getY()));
                if (foundFixture){
                    Vector2 temp = new Vector2(line);
                    temp.add(playerPosition);
                    int tempIndex = endPoints.indexOf(temp);
                    endPoints.remove(temp);
                    endPoints.add(tempIndex,new Vector2(coll));
                }


            }

            collisionPoints.clear();
            //addCorners(collisionPoints);


                /*-----------------ERIK TESTAR--------------------*/
            /*
            endPoints.add(new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY()));
            float maxY = 0;
            int maxYIndex = -1;
            for(int i = 0; i<endPoints.size(); i++){
                float currentY = endPoints.get(i).y;
                if(currentY>maxY){
                    maxY = currentY;
                    maxYIndex = i;
                }
            }

            float windowWidth = Gdx.graphics.getWidth();
            float windowHeight = Gdx.graphics.getHeight();

            float[] corners = new float[]{
                    playerPosition.x - windowWidth/64, windowHeight/32 + playerPosition.y - windowHeight/64,        //Top left
                    playerPosition.x - windowWidth/64, playerPosition.y - windowHeight/64,                           //Bottom left
                    windowWidth/32+playerPosition.x - windowWidth/64, playerPosition.y - windowHeight/64,           //Bottom right
                    windowWidth/32 +playerPosition.x - windowWidth/64, windowHeight/32 + playerPosition.y - windowHeight / 64 //Top right
            };

            collisionPoints.add(corners[0]);
            collisionPoints.add(corners[1]);
            for(int i = maxYIndex; i >= 0; i--) {
                collisionPoints.add(endPoints.get(i).x);
                collisionPoints.add(endPoints.get(i).y);
            }
            for(int i =endPoints.size()-1; i >= maxYIndex; i--) {
                collisionPoints.add(endPoints.get(i).x);
                collisionPoints.add(endPoints.get(i).y);
            }
           // collisionPoints.add(endPoints.get(maxYIndex).x);
            //collisionPoints.add(endPoints.get(maxYIndex).y);
            for(float fl: corners)
                collisionPoints.add(fl);
            float[] collisionPointsArray = convertToArray(collisionPoints);


            //---------------------------------------------------------------------

            shapeRenderer.setProjectionMatrix(camera.combined);


            EarClippingTriangulator ecp = new EarClippingTriangulator();


            //ShortArray s = ecp.computeTriangles(region1);

            float[] region1 = new float[collisionPointsArray.length];

            for(int i = 0; i < region1.length; i++)
                region1[i] = collisionPointsArray[i];


           ShortArray s = ecp.computeTriangles(region1);
            PolygonRegion darkness = new PolygonRegion(new TextureRegion(tex), region1, s.toArray());
            shapeRenderer.setAutoShapeType(true);
            PolygonSpriteBatch psb = new PolygonSpriteBatch();

            psb.setProjectionMatrix(camera.combined);
            batchHUD.begin();
            Sprite lightSprite = new Sprite(light);
            lightSprite.setAlpha(0.15f);
            lightSprite.draw(batchHUD);
            batchHUD.end();
            psb.begin();
            //psb.draw(darkness1, 0, 0);
            psb.draw(darkness,0,0);
            psb.end();

            /*---------------- END TEST -------------------------*/
         /*--------------------------TESTA PATH FINDING------------------------------------*/

            //Skapa path finding        //TODO debug

            if (steps % 60 == 0) {   //uppdaterar varje sekund
                updateZombiePaths();
            }
        /*-----------------SLUTTESTAT---------------------*/

            //rita box2d debug
            boxDebug.render(mapController.getWorld(), camera.combined);
        //render HUD
        playerPos = "X: " + gameModel.getPlayer().getX() + ", Y: " + gameModel.getPlayer().getY();
        playerHealth = "Health: " + gameModel.getPlayer().getLives();
        playerAmmo = "Ammo: " + gameModel.getPlayer().getAmmunition();
            batchHUD.begin();
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFont.draw(batchHUD, playerHealth, 10, Gdx.graphics.getHeight()-10);
        bitmapFont.draw(batchHUD, playerAmmo, 10, Gdx.graphics.getHeight()-25);
        bitmapFont.draw(batchHUD, playerPos, 10, Gdx.graphics.getHeight()-40);
        batchHUD.end();
        }

    }

    /**
     * Render game when game is paused
     */
    private void updatePaused(){

        pauseStage.act();
        pauseStage.draw();

    }

    private void setUpPauseMenu(){

        MenuBuilder menuBuilder = new MenuBuilder();
        Skin skin = menuBuilder.createMenuSkin();

        Table table = new Table();

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        TextButton quitGameButton = new TextButton("Quit game", skin);

        table.add(mainMenuButton).size(250,50).padBottom(15).row();
        table.add(quitGameButton).size(250,50).padBottom(15).row();

        table.setFillParent(true);
        pauseStage.addActor(table);

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });

        quitGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

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
        for(Zombie z : gameModel.getZombies()) {
           if(!z.isKnockedOut()) {
               Point start = new Point(Math.round(z.getX()), Math.round(z.getY()));
               Point end = new Point(Math.round(gameModel.getPlayer().getX()), Math.round(gameModel.getPlayer().getY()));
               path = pathFinding.getPath(start, end, 15);                 //TODO gör nåt vettigt här istälelt för att abra printa.
               if (path == null) {
                   // System.out.println("Ingen path hittad");
               } else {
                   //System.out.println("\nPath från: " + start.x + " " + start.y + " till " + end.x + " " + end.y + ":");
                   int i = 0;
                   while (path.hasNext()) {
                       Point tile = path.next();
                       //System.out.println(tile.x + " " + tile.y);
                       i++;
                   }
                   //System.out.println("Antal steg: " + i);
               }

           }
        }

    }
    public void dispose(){
        GameModel.getInstance().getPlayer().dispose();
        currentWorld.dispose();
        batchHUD.dispose();
        shapeRenderer.dispose();
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

    private void movePlayerToBufferIfNeeded() {
        if (mapController.getPlayerBufferPosition() != null) {

//<<<<<<< HEAD
        mapController.updatePlayerPosition(mapController.getPlayerBufferPosition());
        mapController.setPlayerBufferPosition(null);
            updateZombiePaths();
//=======

//>>>>>>> 1ffaac9a2ec5c13ece91b3a017e86c6c456154c3
    }
    }
    private float[] convertToArray(ArrayList<Float> floatList){
        float[] floatArray = new float[floatList.size()];
        int i = 0;
        for (float f: floatList){
            floatArray[i] = f;
            i++;
        }
        return floatArray;
    }
}
