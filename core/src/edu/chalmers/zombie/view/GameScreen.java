package edu.chalmers.zombie.view;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.*;
import edu.chalmers.zombie.model.*;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.model.actors.Zombie;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.GameState;

/**
 * The applications game screen
 *
 * Created by Tobias on 15-04-02.
 * Modified by Erik and Daniel
 */
public class GameScreen extends ZWScreen{
    //HUD variables
    private ZWBitmapFont bitmapFont;
    private ZWSpriteBatch batchHUD;
    private ZWSpriteBatch sb = new ZWSpriteBatch();
    private ZWPolygonSpriteBatch psb = new ZWPolygonSpriteBatch();
    private ZWSprite darkness = GameModel.getInstance().getDarknessOverlay();
    private ZWBatch batch;
    private FlashlightController flashlightController = new FlashlightController();
    private ZWShapeRenderer grenadeShapeRenderer = new ZWShapeRenderer();

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
            /* ------ Initialize room  ------ */
        MapController.setWorldNeedsUpdate(true);
        MapController.updateRoomIfNeeded();

       /* ------- Create HUD--------*/
        batchHUD = new ZWSpriteBatch();
        bitmapFont = new ZWBitmapFont();

        /* ------- Set game as running --------*/
        GameModel.getInstance().setGameState(GameState.GAME_RUNNING);

       MenuController.initializeMenus(); //TODO: should be done in factory

         /* ------- Set input --------*/
        ZWInputMultiplexer inputMultiplexer = new ZWInputMultiplexer();
        inputMultiplexer.addInputProcessor(GameModel.getInstance().getScreenModel().getPauseStage());
        inputMultiplexer.addInputProcessor(GameModel.getInstance().getScreenModel().getGameOverStage());
        //TODO: add NextLevelStage as inputprocessor here
        inputMultiplexer.addInputProcessor(GameModel.getInstance().getScreenModel().getSoundAndSettingStage());
        inputMultiplexer.addInputProcessor(new InputController());

        ZWGameEngine.setInputProcessor(inputMultiplexer);

        GameModel.getInstance().getAimingSystem().setPlayer(GameModel.getInstance().getPlayer());
    }

    /* ------ Setters and getters ------ */

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
        ZWRenderer renderer = gameModel.getZWRenderer();
        Player player = gameModel.getPlayer();

        /* ------ Render the background color ------ */
        ZWGameEngine.clearColor(0, 0, 0, 1);
        ZWGameEngine.clearBufferBit();

        if(!GameModel.getInstance().worldNeedsUpdate()) {
            /* ------ Update the camera position ------ */
            MapController.setCameraPosition(player.getX(), player.getY());

            /* ------ Draw the background map layer ------ */
            int[] backgroundLayers = {0};
            renderer.renderMapLayer(backgroundLayers);
            renderer.setCameraView();

            /* ------ Start rendering the different sprites------ */
          batch = renderer.getBatch();
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
            AimingController.drawAimer(batch);
            batch.end();

            grenadeShapeRenderer.setAutoShapeType(true);
            grenadeShapeRenderer.begin();
            AimingController.drawGrenadeAimer(grenadeShapeRenderer);
            grenadeShapeRenderer.end();
             /* ------Draw the middle layer ------ */
            if (gameModel.getPlayer().isHidden() && gameModel.isFlashlightEnabled()) {
                int[] middleLayers = {2};
                renderer.renderMapLayer(middleLayers);

            } else {
                int[] middleLayers = {1};
                renderer.renderMapLayer(middleLayers);

            }
            batch = renderer.getBatch();
            batch.begin();
            renderer.setCombinedCameraBatch();

            /* ------ Draw books ------ */
            for (Book b : gameModel.getBooks()) {
                b.draw(batch);
            }
            for (Grenade g : gameModel.getGrenades()) {
                g.draw(batch);
            }

            /* ------ Finished drawing sprites ------ */
            batch.end();

        }

                        /* ----------------- TEST FLASHLIGHT -----------------*/

            if (gameModel.isFearOfTheDark()) {
                if (gameModel.isFlashlightEnabled()){
                    renderer.setCombinedCameraBatch();
                    flashlightController.draw(gameModel.getFlashlightModel(),psb);
                } else{
                    darkness.setSize(ZWGameEngine.getWindowWidth(), ZWGameEngine.getWindowHeight());
                    sb.begin();
                    darkness.draw(sb);
                    sb.end();
                }
            }


            drawBlood();

            int[] foregroundLayers = {3};
            renderer.renderMapLayer(foregroundLayers);

            renderer.renderBox2DDebug(gameModel.getRoom().getWorld());  //TODO debug

            /* ------ Render HUD ------ */
        String playerPos = "X: " + player.getX() + ", Y: " + player.getY();
        String playerHealth = "Health: " + player.getLives();
        String playerAmmo = "Ammo: " + player.getAmmunition();
        String level= "Level: " + (gameModel.getCurrentLevelIndex()+1);
        batchHUD.begin();
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFont.draw(batchHUD, playerHealth, 10, ZWGameEngine.getWindowHeight() - 10);
        bitmapFont.draw(batchHUD, playerAmmo, 10, ZWGameEngine.getWindowHeight() - 25);
        bitmapFont.draw(batchHUD, playerPos, 10, ZWGameEngine.getWindowHeight() - 40);
        bitmapFont.draw(batchHUD, level, 10, ZWGameEngine.getWindowHeight() - 55);
        batchHUD.end();

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
     * Dispose the world, player and the renderers.
     */
    public void dispose(){
        GameModel.getInstance().getPlayer().dispose();
        sb.dispose();
        psb.dispose();
    }
    private BloodController bloodController = new BloodController();

    private void drawBlood(){
        sb.begin();
        bloodController.draw(sb);
        sb.end();
    }

}
