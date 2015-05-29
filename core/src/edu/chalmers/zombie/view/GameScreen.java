package edu.chalmers.zombie.view;

import edu.chalmers.zombie.adapter.*;

import edu.chalmers.zombie.controller.*;
import edu.chalmers.zombie.model.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.model.Zombie;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;

/**
 * The applications game screen
 *
 * Created by Tobias on 15-04-02.
 * Modified by Erik and Daniel
 */
public class GameScreen extends ZWScreen{
    private ZWWorld currentWorld;
   // private OrthographicCamera camera;
  //  private OrthogonalTiledMapRenderer mapRenderer;
  //  private Box2DDebugRenderer boxDebug;
    //private MapController mapController;
    private float tileSize;
    //HUD variables
    private ZWBitmapFont bitmapFont;
    private ZWSpriteBatch batchHUD;
    private PathAlgorithm pathFinding; //TODO debug
    private int steps;




  //  private ShapeRenderer shapeRenderer = new ShapeRenderer();


    private Flashlight flashlight;
  //  private Sprite sprite = new Sprite(new Texture("core/assets/darkness.png"));
  //  private ShapeRenderer grenadeShapeRenderer = new ShapeRenderer();
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
        float width = ZWGameEngine.getWindowWidth();
        float height = ZWGameEngine.getWindowHeight();
        //camera = new OrthographicCamera(width, height);
       // mapController = new MapController();

            /* ------ Initialize room  ------ */
        MapController.initializeRooms();
        MapController.setWorldNeedsUpdate(true);
        MapController.updateRoomIfNeeded();

         /* ------- Create box 2d renderer --------*/
      //  boxDebug = new Box2DDebugRenderer();

       /* ------- Create HUD--------*/
        batchHUD = new ZWSpriteBatch();
        bitmapFont = new ZWBitmapFont();

        /* ------- Set game as running --------*/
        GameModel.getInstance().setGameState(GameState.GAME_RUNNING);

       MenuController.initializeMenus(); //TODO: should be done in factory

         /* ------- Set input --------*/
        ZWInputMultiplexer inputMultiplexer = new ZWInputMultiplexer();
        inputMultiplexer.addInputProcessor(GameModel.getInstance().getScreenModel().getPauseStage());
        inputMultiplexer.addInputProcessor(new InputController());
        inputMultiplexer.addInputProcessor(GameModel.getInstance().getScreenModel().getSoundAndSettingStage());
        
        ZWGameEngine.setInputProcessor(inputMultiplexer);

        //TODO debug
        MapController.printCollisionTileGrid();




    }

    /* ------ Setters and getters ------ */

    /**
     * @return The screen's renderer
     */
   /* public OrthogonalTiledMapRenderer getMapRenderer(){
       return this.mapRenderer;
   }
   */

    /**
     * Sets the screens renderer
     * @param renderer  A renderer based on a tile map
     *//*
    public void setMapRenderer(OrthogonalTiledMapRenderer renderer){
        this.mapRenderer = renderer;
    }
    */

    /**
     * @return  The screen's currently displayed world
     */
    /*
    public World getDisplayedWorld(){
        return currentWorld;
    }
    */

    /**
     * Sets the screens currently displayed world
     * @param displayedWorld    The world to be displayed
     */
    public void setDisplayedWorld(ZWWorld displayedWorld){
        this.currentWorld = displayedWorld;
    }

    /**
     * Resizes the camer view
     * @param width The width in pixels
     * @param height    The height in pixels
     */
    public void resize(int width, int height){
        MapController.resizeRenderer(width,height);
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
            case GAME_GAMEOVER:
                updateGameOver();
                break;
            case GAME_NEXTLEVEL:
                updateNextLevel();
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
        MapController.updateRoomIfNeeded();
        //setMapRenderer(gameModel.getRenderer().getMapRenderer());
        Renderer renderer = gameModel.getRenderer();
        setDisplayedWorld(gameModel.getRoom().getWorld());
        Player player = gameModel.getPlayer();

        /* ------ Render the background color ------ */
        ZWGameEngine.clearColor(0, 0, 0, 1);
        ZWGameEngine.clearBufferBit();


        //Uppdatera fysik
        //Tells GameModel when step is happening
        //gameModel.setStepping(true);
        //currentWorld.step(Constants.TIMESTEP, 6, 2);
        //gameModel.setStepping(false);

        //TODO vad göra om spelaren är död?

        if(!GameModel.getInstance().worldNeedsUpdate()) {
            /* ------ Update the camera position ------ */
            MapController.setCameraPosition(player.getX(), player.getY());
            /* ------ Draw the background map layer ------ */
            int[] backgroundLayers = {0};
            renderer.renderMapLayer(backgroundLayers);
            renderer.setCameraView();

                 //mapRenderer.render();
            steps++; //TODO debug

            /* ------ Start rendering the different sprites------ */
            ZWBatch batch = renderer.getBatch();
            batch.begin();
            renderer.setCombinedCameraBatch();

             /* ------ Draw the zombies ------ */
            for (Zombie z : gameModel.getZombies()) {
                z.draw(batch);
            }

            /* ------- Draw the potions --------*/
            for (Potion p : gameModel.getRoom().getPotions()) {
                p.draw(batch);
            }
            /* ------ Draw the player ------ */
            player.draw(batch);

           /* ------ Draw the aimer ------ */
            player.getHand().drawAimer(batch);
            batch.end();


           // grenadeShapeRenderer.setAutoShapeType(true);
           // grenadeShapeRenderer.begin();
        //    player.getHand().drawGrenadeAimer(grenadeShapeRenderer);
        //    grenadeShapeRenderer.end();
             /* ------Draw the middle layer ------ */
            if(gameModel.getPlayer().isHidden() && gameModel.isFlashlightEnabled()) {
                int[] middleLayers = {2};
                    renderer.renderMapLayer(middleLayers);

            }
            else{
                int[] middleLayers = {1};
                renderer.renderMapLayer(middleLayers);

            }
            batch = renderer.getBatch();
            batch.begin();
            renderer.setCombinedCameraBatch();
            /* ------ Draw books ------ */
            for (Book b: gameModel.getBooks()){
                b.draw(batch);
            }
            for (Grenade g: gameModel.getGrenades()){
                g.draw(batch);
            }

            /* ------ Finished drawing sprites ------ */
          batch.end();



            /* ------ Render HUD ------ */
            String playerPos = "X: " + player.getX() + ", Y: " + player.getY();
            String playerHealth = "Health: " + player.getLives();
            String playerAmmo = "Ammo: " + player.getAmmunition();
            batchHUD.begin();
            bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            bitmapFont.draw(batchHUD, playerHealth, 10, ZWGameEngine.getWindowHeight() - 10);
            bitmapFont.draw(batchHUD, playerAmmo, 10, ZWGameEngine.getWindowHeight() - 25);
            bitmapFont.draw(batchHUD, playerPos, 10, ZWGameEngine.getWindowHeight() - 40);
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
                    flashlight = new Flashlight(currentWorld,Constants.PI*0.25f,100,10);
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
            //-----------------------------------------------------------------------

            /*---------------- END TEST -------------------------*/
        /* ------Draw the foreground layer ------ */
            drawBlood();
            int[] foregroundLayers = {3};

                renderer.renderMapLayer(foregroundLayers);

            /* ------ Draw the box2d debug ------ */
            //gameModel.getRenderer().renderBox2DDebug(gameModel.getRoom()); //TODO debug

            renderer.renderBox2DDebug(gameModel.getRoom());
            //boxDebug.render(gameModel.getRoom().getWorld().getWorld(),camera.combined);

            /*---------------- END TEST -------------------------*/
         /*--------------------------TESTA PATH FINDING------------------------------------*/

            //Skapa path finding        //TODO debug

            if (steps % 60 == 0) {   //uppdaterar varje sekund
               // updateZombiePaths();
            }
        /*-----------------SLUTTESTAT---------------------*/




        }
            /* ------ Test path finding ------ */
            if (steps % 60 == 0) {   //uppdaterar varje sekund  //TODO debug
              // updateZombiePaths();
            }
        /** Render settings and sound buttons **/
        GameModel.getInstance().getScreenModel().getSoundAndSettingStage().act();
        GameModel.getInstance().getScreenModel().getSoundAndSettingStage().draw();

    }


    /**
     * Render game when game is paused
     */
    private void updatePaused(){
        GameModel.getInstance().getScreenModel().getPauseStage().act();
        GameModel.getInstance().getScreenModel().getPauseStage().draw();
    }

    private void updateGameOver(){
        /* ------ Render the background color ------ */
        ZWGameEngine.clearColor(0, 0, 0, 1);
        ZWGameEngine.clearBufferBit();
        GameModel.getInstance().getScreenModel().getGameOverStage().act();
        GameModel.getInstance().getScreenModel().getGameOverStage().draw();
    }

    private void updateNextLevel(){
        GameModel.getInstance().getScreenModel().getNextLevelStage().act();
        GameModel.getInstance().getScreenModel().getNextLevelStage().draw();
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
                //mapController.printPath(mapController.getRoom(), start, end);                 //TODO gör nåt vettigt här istälelt för att bara printa.
            }
        }
    }
    /**
     * Dispose the world, player and the renderers.
     */
    public void dispose(){
        GameModel.getInstance().getPlayer().dispose();
       // currentWorld.dispose();
     //   batchHUD.dispose();
    //    shapeRenderer.dispose();
    //    sb.dispose();
    }
    private Blood blood = new Blood();
 //   private SpriteBatch sb = new SpriteBatch();
    private void drawBlood(){
 //           sb.begin();
 //           blood.draw(sb);
 //           sb.end();
    }






//>>>>>>> 1ffaac9a2ec5c13ece91b3a017e86c6c456154c3




}
