package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Direction;

/**
 * Controller to handle all inputs
 *
 * Created by Tobias on 15-04-01.
 */
public class InputController implements InputProcessor{

    GameModel gameModel;

    public InputController(){

        gameModel = new GameModel();

    }



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
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
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
}
