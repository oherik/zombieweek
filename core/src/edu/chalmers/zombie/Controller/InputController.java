package edu.chalmers.zombie.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Controller to handle all inputs
 *
 * Created by Tobias on 15-04-01.
 */
public class InputController implements InputProcessor{



    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.UP:
                //move north
                break;
            case Input.Keys.DOWN:
                //move south
                break;
            case Input.Keys.RIGHT:
                //move east
                break;
            case Input.Keys.LEFT:
                //move west
                break;
            case Input.Keys.W:
                //aim left
                break;
            case Input.Keys.S:
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
        if(keycode == Input.Keys.W || keycode == Input.Keys.S){
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
