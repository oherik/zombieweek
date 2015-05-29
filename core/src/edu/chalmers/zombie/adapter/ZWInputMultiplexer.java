package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import edu.chalmers.zombie.controller.InputController;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWInputMultiplexer {
    private InputMultiplexer inputMultiplexer;

    public ZWInputMultiplexer(){
        inputMultiplexer = new InputMultiplexer();
    }

    public void addInputProcessor(ZWStage stage){
        InputProcessor inputProcessor = stage.getStage();
        inputMultiplexer.addProcessor(inputProcessor);
    }

    public void addInputProcessor(InputController inputController){
        InputProcessor inputProcessor = inputController;
        inputMultiplexer.addProcessor(inputProcessor);
    }

    public InputMultiplexer getInputMultiplexer(){return this.inputMultiplexer;}
}
