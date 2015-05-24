package edu.chalmers.zombie.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import edu.chalmers.zombie.adapter.Entity;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.controller.*;
import edu.chalmers.zombie.adapter.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.MenuBuilder;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;
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
    //HUD variables
    private BitmapFont bitmapFont;
    private SpriteBatch batchHUD;
    private TiledMapImageLayer tiledMapTopLayer;
    private PathAlgorithm pathFinding; //TODO debug

    private int steps;

    private Stage pauseStage;

    private Stage soundAndSettingStage;
    private ImageButton soundButton;

    public GameScreen(World world, float tileSize){

        this.currentWorld = world;
        this.tileSize = tileSize;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);
        mapController = new MapController();

            /* ------ Initialize room  ------ */
        mapController.initializeRooms();
        mapController.setWorldNeedsUpdate(true);
        mapController.updateRoomIfNeeded(this);

         /* ------- Create box 2d renderer --------*/
        boxDebug = new Box2DDebugRenderer();
        //mapRenderer = new OrthogonalTiledMapRenderer(mapController.getMap(), 1 / Constants.TILE_SIZE);

       /* ------- Create HUD--------*/
        batchHUD = new SpriteBatch();
        bitmapFont = new BitmapFont();

        /* ------- Set game as running --------*/
        GameModel.getInstance().setGameState(GameState.GAME_RUNNING);

        /* ------- Create pause menu --------*/
        pauseStage = new Stage();
        setUpPauseMenu();

         /* ------- Create sound and settings pause menu --------*/
        soundAndSettingStage = new Stage();
        setUpSoundAndSettingsMenu();

         /* ------- Set input --------*/
        InputProcessor inputProcessorTwo = pauseStage;
        InputProcessor inputProcessorOne = new InputController();
        InputProcessor inputProcessorThree = soundAndSettingStage;
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputProcessorTwo);
        inputMultiplexer.addProcessor(inputProcessorThree);
        inputMultiplexer.addProcessor(inputProcessorOne);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //TODO debug
        mapController.printCollisionTileGrid();



    }

    /* ------ Setters and getters ------ */

    /**
     * @return The screen's renderer
     */
    public OrthogonalTiledMapRenderer getMapRenderer(){
       return this.mapRenderer;
   }

    /**
     * Sets the screens renderer
     * @param renderer  A renderer based on a tile map
     */
    public void setMapRenderer(OrthogonalTiledMapRenderer renderer){
        this.mapRenderer = renderer;
    }

    /**
     * @return  The screen's currently displayed world
     */
    public World getDisplayedWorld(){
        return currentWorld;
    }

    /**
     * Sets the screens currently displayed world
     * @param displayedWorld    The world to be displayed
     */
    public void setDisplayedWorld(World displayedWorld){
        this.currentWorld = displayedWorld;
    }

    public void setCurrentTopLayer(TiledMapImageLayer topLayer){
        this.tiledMapTopLayer = topLayer;
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


    }

    /**
     * Render game when game is running
     * @param f
     */
    private void updateRunning(float f){

        mapController.updateRoomIfNeeded(this);

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

            for (Zombie z : gameModel.getZombies()) {
                z.moveToPlayer(pathFinding);
            }

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
            if (mapController.getMap().getLayers().get("top") != null) {
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

        /** Render settings and sound buttons **/

        soundAndSettingStage.act();
        soundAndSettingStage.draw();
    }

    /**
     * Render game when game is paused
     */
    private void updatePaused(){

        pauseStage.act();
        pauseStage.draw();

    }

    /**
     * Sets up sound and settings icon button
     */
    private void setUpSoundAndSettingsMenu(){

        ImageButton.ImageButtonStyle settingsButtonStyle = new ImageButton.ImageButtonStyle();
        ImageButton.ImageButtonStyle soundButtonStyle = new ImageButton.ImageButtonStyle();

        GameModel.getInstance().res.loadTexture("audio-off","core/assets/Images/audioOff.png");
        GameModel.getInstance().res.loadTexture("audio-on","core/assets/Images/audioOn.png");
        GameModel.getInstance().res.loadTexture("settings","core/assets/Images/settings.png");

        settingsButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(GameModel.getInstance().res.getTexture("settings")));
        soundButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(GameModel.getInstance().res.getTexture("audio-on")));

        ImageButton settingsButton = new ImageButton(settingsButtonStyle);
        soundButton = new ImageButton(soundButtonStyle);

        settingsButton.setSize(40, 40);
        soundButton.setSize(40, 40);

        settingsButton.setPosition(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50);
        soundButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        soundAndSettingStage.addActor(settingsButton);
        soundAndSettingStage.addActor(soundButton);

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioController audioController = new AudioController();
                audioController.toggleSound();
                updateSoundButton();
            }
        });
    }

    /**
     * Checks whether sound is on or not and adjust image for audio on/off icon thereafter
     */
    private void updateSoundButton(){
        boolean soundOn = GameModel.getInstance().isSoundOn();
        ImageButton.ImageButtonStyle soundButtonStyle = soundButton.getStyle();
        Texture newTexture;
        if (soundOn){
            newTexture = GameModel.getInstance().res.getTexture("audio-on");
        } else {
            newTexture = GameModel.getInstance().res.getTexture("audio-off");
        }
        soundButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(newTexture));
    }

    /**
     * Sets up the pause menu
     */
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

        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
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
    private void updateZombiePaths() {   //TODO gör ingenting nu. Kanske ha en path-variabel i Zombie.java?
        GameModel gameModel = GameModel.getInstance();
        for (Zombie z : gameModel.getZombies()) {
            if (!z.isKnockedOut()) {
                Player player = gameModel.getPlayer();
                Point end = new Point(Math.round(player.getX() - 0.5f), Math.round(player.getY() - 0.5f));
                Point start = new Point(Math.round(z.getX() - 0.5f), Math.round(z.getY() - 0.5f));
                mapController.printPath(mapController.getRoom(), start, end);                 //TODO gör nåt vettigt här istälelt för att bara printa.

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
            gameModel.getRoom().destroyBody(e);
        }
        gameModel.clearEntitiesToRemove();

    }

    private void movePlayerToBufferIfNeeded() {
        if (mapController.getPlayerBufferPosition() != null) {

        mapController.updatePlayerPosition(mapController.getPlayerBufferPosition());
        mapController.setPlayerBufferPosition(null);
           // updateZombiePaths();
    }
    }
}
