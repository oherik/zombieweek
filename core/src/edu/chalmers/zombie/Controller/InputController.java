package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import edu.chalmers.zombie.adapter.ZWVector;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;
import edu.chalmers.zombie.utils.GameState;

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
     * Decides what to do when the player presses a key.
     * @param keycode   The key pressed
     * @return  True if successful, false if not
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                //move north
                PlayerController.move(Direction.NORTH);
                //gameModel.movePlayer(Direction.NORTH);
                break;
            case Input.Keys.S:
                //move south
                PlayerController.move(Direction.SOUTH);
                //gameModel.movePlayer(Direction.SOUTH);
                break;
            case Input.Keys.D:
                //move east
                PlayerController.move(Direction.EAST);
                //gameModel.movePlayer(Direction.EAST);
                break;
            case Input.Keys.A:
                //move west
                PlayerController.move(Direction.WEST);
                //gameModel.movePlayer(Direction.WEST);
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
            case Input.Keys.F:
                gameModel.toggleFlashlight();
                break;
            case Input.Keys.G:
                gameModel.getPlayer().getHand().toggleGrenadeThrowing();
                break;
            case Input.Keys.ESCAPE:
                switch (gameModel.getGameState()) {
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

        if(!GameModel.getInstance().worldNeedsUpdate() && player.getAmmunition()>0 && !MapController.pathObstructed(new ZWVector(getPlayer().getBody().getPosition()), mapController.getRoom(),distance,angle) ) {
            player.decreaseAmmunition();
            if (player.getHand().isThrowingGrenade()){
                player.getHand().throwGrenade();
            } else{
                throwBook();
            }

        }
    }



    /**
     * Throws a book
     */
    private void throwBook(){
        Player player = gameModel.getPlayer();
        player.throwBook();

        AudioController.playSound(gameModel.res.getSound("throw"));
    }
    /**
     * Decides what to do when the player releases a key
     * @param keycode   The key released
     * @return True if successful, false if not
     */
    @Override
    public boolean keyUp(int keycode) {
        if(!gameModel.isStepping()) {   //Don't do any movements while the game world is stepping
            if(keycode == Input.Keys.D || keycode == Input.Keys.A){
                //getPlayer().stopX();
                PlayerController.stopX();
            }

            if (keycode == Input.Keys.W || keycode == Input.Keys.S){
                //getPlayer().stopY();
                PlayerController.stopY();
            }

            if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN){
                //set aiming force to zero
                getPlayer().getHand().stopAiming();
            } else {return false;}
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (gameModel.getGameState()) {
            case GAME_RUNNING:
                tryThrowingBook();
                break;
        }
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
        switch (gameModel.getGameState()) {
            case GAME_RUNNING:
                getPlayer().getHand().setMousePosition(screenX, screenY);
                break;
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
