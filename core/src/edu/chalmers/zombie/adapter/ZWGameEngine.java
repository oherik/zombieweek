package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

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
    public static void clearColor(float red, float green, float blue, float alpha){
        Gdx.gl.glClearColor(red, green, blue, alpha);
    }
    public static void clear(int mask){
        Gdx.gl.glClear(mask);
    }
}
