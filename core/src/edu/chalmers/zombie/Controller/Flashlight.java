package edu.chalmers.zombie.controller;

/**
 * Created by daniel on 5/19/2015.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import edu.chalmers.zombie.model.GameModel;

import java.util.ArrayList;

public class Flashlight {
    private Vector2 playerPosition = new Vector2();
    private Vector2 windowSize = new Vector2();
    private ArrayList<Float> vertices = new ArrayList<Float>();

    private Texture tex = new Texture("core/assets/darkness.png");
    private Texture light = new Texture("core/assets/light.png");


    public Flashlight() {


    }

    public void draw(){
        fetchWindowSize();
        fetchStandardVertices();
    }

    private void fetchStandardVertices(){
        fetchPlayerPosition();
        fetchCorners();
    }

    private void fetchPlayerPosition(){
        GameModel gameModel = GameModel.getInstance();
        playerPosition =  new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
    }


    private void fetchWindowSize(){
        windowSize.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void fetchCorners(){
        vertices.add(playerPosition.x - windowSize.x/64);
        vertices.add(windowSize.y/32 + playerPosition.y - windowSize.y/64);        //Top left
        vertices.add(playerPosition.x - windowSize.x/64);
        vertices.add(playerPosition.y - windowSize.y/64);                           //Bottom left
        vertices.add(windowSize.x/32+playerPosition.x - windowSize.x/64);
        vertices.add(playerPosition.y - windowSize.y/64);           //Bottom right
        vertices.add(windowSize.x/32 +playerPosition.x - windowSize.x/64);
        vertices.add(windowSize.y/32 + playerPosition.y - windowSize.y/64); //Top right
    }
}
