package edu.chalmers.zombie.view;

import com.badlogic.gdx.*;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.maps.tiled.TiledMap;
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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.ShortArray;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import edu.chalmers.zombie.adapter.Renderer;

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
    //HUD variables
    private BitmapFont bitmapFont;
    private SpriteBatch batchHUD;
    private PathAlgorithm pathFinding; //TODO debug

    private int steps;

    private Stage pauseStage;


    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Stage soundAndSettingStage;
    private ImageButton soundButton;

    private Flashlight flashlight;
    private Sprite sprite = new Sprite(new Texture("core/assets/darkness.png"));

    /**
     * Creates a game screen with the default tile size
     */
    public GameScreen(){
        this(Constants.TILE_SIZE);
    }


    /**
     * Creates a new screen based on a set tile size, i.e. pixels per meters.
     */
    public GameScreen(float tileSize){
        this.tileSize = tileSize;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);
        mapController = new MapController();

            /* ------ Initialize room  ------ */
        mapController.initializeRooms();
        mapController.setWorldNeedsUpdate(true);
        mapController.updateRoomIfNeeded();

         /* ------- Create box 2d renderer --------*/
        boxDebug = new Box2DDebugRenderer();

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

    /**
     * Resizes the camer view
     * @param width The width in pixels
     * @param height    The height in pixels
     */
    public void resize(int width, int height){
        camera.setToOrtho(false, width / tileSize, height / tileSize);
    }

   /* ------ Not currently used function ------ */
    public void resume(){

    }

    /* ------ Not currently used function ------ */
    public void pause(){

    }

    /**
     * The game screen render method, which draws the appropriate elements.
     * @param f The time step //TODO eller?
     */
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
     * Render game when game is running. The rendering includes updating the game world and map, drawing the different sprites,
     * move zombies toward the player and everything else that happens during the small time period in which the game updates.
     * @param f The time step
     */
    private void updateRunning(float f){
        GameModel gameModel = GameModel.getInstance();
        mapController.updateRoomIfNeeded();
        setMapRenderer(gameModel.getRenderer().getMapRenderer());
        setDisplayedWorld(gameModel.getRoom().getWorld());
        Player player = gameModel.getPlayer();

        /* ------ Render the background color ------ */
        Gdx.gl.glClearColor(0, 0, 0, 1);       //Black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //Uppdatera fysik
        //Tells GameModel when step is happening
        gameModel.setStepping(true);
        currentWorld.step(Constants.TIMESTEP, 6, 2);
        gameModel.setStepping(false);

        //TODO vad göra om spelaren är död?
        
        if(!GameModel.getInstance().worldNeedsUpdate()) {
            /* ------ Update the camera position ------ */
                camera.position.set(player.getX(), player.getY(), 0); //player is tileSize/2 from origin //TODO kosntig mätning men får inte rätt position annars
                camera.update();
            /* ------ Draw the background map layer ------ */
            int[] backgroundLayers = {0};
            mapRenderer.render(backgroundLayers);
            mapRenderer.setView(camera);


            //mapRenderer.render();
            steps++; //TODO debug

            /* ------ Start rendering the different sprites------ */
            mapRenderer.getBatch().begin();
            mapRenderer.getBatch().setProjectionMatrix(camera.combined);

            /* ------ Draw books ------ */
            for (Book b: gameModel.getBooks()){
                    b.draw(mapRenderer.getBatch());
            }

            /* ------ Draw the zombies ------ */
            for (Zombie z : gameModel.getZombies()) {
                z.draw(mapRenderer.getBatch());
            }

            /* ------ Draw the player ------ */
            player.draw(mapRenderer.getBatch());

           /* ------ Draw the aimer ------ */
            player.getHand().drawAimer(mapRenderer.getBatch());

            /* ------ Finished drawing sprites ------ */
            mapRenderer.getBatch().end();

            /* ------Draw the foreground layer ------ */
            int[] foregroundLayers = {1};
            if (mapController.getMap().getLayers().get("top") != null) {
                mapRenderer.render(foregroundLayers);
            }

            /* ------ Draw the box2d debug ------ */
            boxDebug.render(mapController.getWorld(), camera.combined); //TODO debug

            /* ------ Render HUD ------ */
            String playerPos = "X: " + player.getX() + ", Y: " + player.getY();
            String playerHealth = "Health: " + player.getLives();
            String playerAmmo = "Ammo: " + player.getAmmunition();
            batchHUD.begin();
            bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            bitmapFont.draw(batchHUD, playerHealth, 10, Gdx.graphics.getHeight() - 10);
            bitmapFont.draw(batchHUD, playerAmmo, 10, Gdx.graphics.getHeight() - 25);
            bitmapFont.draw(batchHUD, playerPos, 10, Gdx.graphics.getHeight() - 40);
            batchHUD.end();




                        /* ----------------- TEST FLASHLIGHT -----------------*/
            /*
            if (gameModel.isFlashlightEnabled()){
                PolygonSpriteBatch psb = new PolygonSpriteBatch();
                SpriteBatch sb = new SpriteBatch();
                sb.begin();
                psb.setProjectionMatrix(camera.combined);
                psb.begin();
                if (flashlight==null){
                    flashlight = new Flashlight(currentWorld);
                }
                flashlight.draw(psb,sb);
                psb.end();
                sb.end();
                psb.dispose();
                sb.dispose();
            } else{
                SpriteBatch sb = new SpriteBatch();
                sprite.setSize(650,480);
                sb.begin();
                sprite.draw(sb);
                player.draw(sb);
                sb.end();
            }
            */
            //------------------------------------------------------------------------


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
            /* ------ Test path finding ------ */
            if (steps % 60 == 0) {   //uppdaterar varje sekund  //TODO debug
              // updateZombiePaths();
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
    private void updateZombiePaths() {   //TODO gör ingenting nu. Kanske ha en path-variabel i Zombie.java?dddddd
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
    /**
     * Dispose the world, player and the renderers.
     */
    public void dispose(){
        GameModel.getInstance().getPlayer().dispose();
        currentWorld.dispose();
        batchHUD.dispose();
        shapeRenderer.dispose();
    }






//>>>>>>> 1ffaac9a2ec5c13ece91b3a017e86c6c456154c3




}
