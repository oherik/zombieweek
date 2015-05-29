package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by daniel on 5/29/2015.
 */
public class ZWGameEngine {
    private static Game game;
    public static float getWindowWidth(){
        return Gdx.graphics.getWidth();
    }
    public static float getWindowHeight(){
        return Gdx.graphics.getHeight();
    }
    public static float getDeltaTime(){
        return Gdx.graphics.getDeltaTime();
    }
    public static void setScreen(Screen screen){
        game = (Game)Gdx.app.getApplicationListener();
        game.setScreen(screen);
    }
    public static void exit(){
        Gdx.app.exit();
    }
    public static void addSound(String path){
        Gdx.audio.newSound(Gdx.files.internal(path));
    }
    public static void setInputProcessor(InputProcessor processor){
        Gdx.input.setInputProcessor(processor);
    }
    public static void setInputProcessor(ZWInputMultiplexer inputMultiplexer){Gdx.input.setInputProcessor(inputMultiplexer.getInputMultiplexer());}

    public static void setInputProcessor(ZWStage stage){Gdx.input.setInputProcessor(stage.getStage());}
    public static void clearColor(float red, float green, float blue, float alpha){
        Gdx.gl.glClearColor(red, green, blue, alpha);
    }
    public static void clearBufferBit(){Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);}

    public static Sound newSound(String path){
        return Gdx.audio.newSound(Gdx.files.internal(path));
    }
}
