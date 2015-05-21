package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.GameState;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * controller to handle all inputs
 *
 * Created by Tobias on 15-04-01.
 */
public class InputController implements InputProcessor{

    GameModel gameModel;
    MapController mapController;

    /**
     * Constructor. Initializes the game model
     */
    public InputController(){
        gameModel = GameModel.getInstance();
        mapController = new MapController();
    }

    /**
     * Temporary method to get player
     * @return player
     */
    public Player getPlayer(){
        return gameModel.getPlayer();
    }

    /**
     * Temporary method to get player
     * @return zombie
     */
    public Zombie getZombie(){
        return gameModel.getZombie();
    }

    /**
     * Decides what to do when the player presses a key.
     * @param keycode   The key pressed
     * @return  True if successful, false if not
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.W:
                //move north
                gameModel.movePlayer(Direction.NORTH);
                break;
            case Input.Keys.S:
                //move south
                gameModel.movePlayer(Direction.SOUTH);
                break;
            case Input.Keys.D:
                //move east
                gameModel.movePlayer(Direction.EAST);
                break;
            case Input.Keys.A:
                //move west
                gameModel.movePlayer(Direction.WEST);
                break;
            case Input.Keys.SPACE:
                //throw book
                tryThrowingBook();
                break;
            case Input.Keys.UP:
                //aim left
                getPlayer().getHand().startAimingLeft();
                break;
            case Input.Keys.DOWN:
                //aim right
                getPlayer().getHand().startAimingRight();
                break;
            case Input.Keys.C:
                //change aiming type
                getPlayer().getHand().toggleMouseAiming();
                break;
            case Input.Keys.ESCAPE:

                switch (gameModel.getGameState()){
                    case GAME_RUNNING:
                        System.out.println("GAME PAUSED");
                        gameModel.setGameState(GameState.GAME_PAUSED);
                        break;
                    case GAME_PAUSED:
                        System.out.println("GAME STARTED");
                        gameModel.setGameState(GameState.GAME_RUNNING);
                        break;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Throws a book if all conditions are sactisfactory
     */
    private void tryThrowingBook(){
        Player player = gameModel.getPlayer();
        float distance = 1f;
        float angle = player.getHand().getDirection()+Constants.PI*0.5f;


        if(!GameModel.getInstance().worldNeedsUpdate() && player.getAmmunition()>0 && !MapController.pathObstructed(player.getBody().getPosition(), mapController.getMapMetaLayer(),distance,angle) ) {
            player.decreaseAmmunition();
            throwBook();
        }
    }



    /**
     * Throws a book
     */
    private void throwBook(){
        Player player = gameModel.getPlayer();
        player.throwBook();

        gameModel.res.getSound("throw").play();
    }
    /**
     * Decides what to do when the player releases a key
     * @param keycode   The key released
     * @return True if successful, false if not
     */
    @Override
    public boolean keyUp(int keycode) {

        if(keycode == Input.Keys.D || keycode == Input.Keys.A){
            getPlayer().stopX();
        }

        if (keycode == Input.Keys.W || keycode == Input.Keys.S){
            getPlayer().stopY();
        }

        if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN){
            //set aiming force to zero
            getPlayer().getHand().stopAiming();
        } else {return false;}
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tryThrowingBook();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        getPlayer().getHand().setMouseDirection(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * For debug only
     * @param start
     * @param end
     * @param metaLayerName
     * @param collisionProperty
     */
   private void drawPath(Point start, Point end ,String metaLayerName, String collisionProperty){       //TODO ej klar
        World world = gameModel.getRoom().getWorld();
        TiledMap tiledMap =  gameModel.getRoom().getMap();
        BodyDef bodyDef = new BodyDef();
        PathAlgorithm pA = new PathAlgorithm((TiledMapTileLayer) tiledMap.getLayers().get(metaLayerName), collisionProperty);
        ArrayList<Point> it = pA.getPath(start, end);
    }
}
