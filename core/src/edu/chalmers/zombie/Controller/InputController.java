package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.adapter.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;
import java.util.Iterator;

/**
 * controller to handle all inputs
 *
 * Created by Tobias on 15-04-01.
 */
public class InputController implements InputProcessor{

    GameModel gameModel;

    /**
     * Constructor. Initializes the game model
     */
    public InputController(){
        gameModel = GameModel.getInstance();
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
            case Input.Keys.UP:
                //aim left
                break;
            case Input.Keys.DOWN:
                //aim right
                break;
            case Input.Keys.SPACE:
                //throw book
                tryThrowingBook();
                break;
            default:
                return false;
        }
        return true;
    }

    private void tryThrowingBook(){
        Player player = gameModel.getPlayer();
        if(player.getAmmunition()>0) {
            player.decreaseAmmunition();
            throwBook();
        }
    }

    private void throwBook(){
        Player player = gameModel.getPlayer();
        Book book = new Book(player.getDirection(), player.getX() - 0.5f, player.getY() - 0.5f, player.getWorld(), player.getVelocity());
        gameModel.addBook(book);
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
        } else {return false;}
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
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
        World world = gameModel.getLevel().getWorld();
        TiledMap tiledMap =  gameModel.getLevel().getMap();
        BodyDef bodyDef = new BodyDef();
        PathAlgorithm pA = new PathAlgorithm((TiledMapTileLayer) tiledMap.getLayers().get(metaLayerName), collisionProperty);
        Iterator<Point> it = pA.getPath(start, end);
    }

}
