package edu.chalmers.zombie.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import edu.chalmers.zombie.adapter.ZWTiledMap;

import java.util.HashMap;

/**
 * A recource manager to load and store assets.
 *
 * Created by Tobias on 15-05-11.
 */
public class ResourceManager {

    //Sprites?
    private HashMap<String, Texture> textureMap;
    private HashMap<String, ZWTiledMap> tiledmapMap;
    private HashMap<String, Sound> soundMap;


    /**
     * Initializes the hashmaps
     */
    public ResourceManager(){
        textureMap = new HashMap<String, Texture>();
        tiledmapMap = new HashMap<String, ZWTiledMap>();
        soundMap = new HashMap<String, Sound>();
    }

    /**
     * Save sound using path and key
     * @param key The key
     * @param path The path to the sound
     * @throws NullPointerException if the path is incorrect or not found
     */
    public void loadSound(String key, String path){
        if (path == null) {
            throw new NullPointerException("Sound: no path recieved");
        }

        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));

        if (sound == null) {
            throw new NullPointerException("Sound: incorrect path name");
        }

        soundMap.put(key,sound);
    }

    /**
     * Get sound
     * @param key The sound
     * @return sound
     */
    public Sound getSound(String key){ return soundMap.get(key);}

    /**
     * Dispose sound
     * @param key The sound
     */
    public void disposeSound(String key){
        Sound sound = soundMap.get(key);
        if (sound!=null){sound.dispose();}
    }


    /**
     * Save tiled map using path and key
     * @param key The key
     * @param path The path to the tiled map
     * @throws NullPointerException if the path is incorrect or not found
     */
    public void loadTiledMap(String key, String path){
        if (path == null) {
            throw new NullPointerException("Tiled map: no path recieved");
        }

        ZWTiledMap tiledMap = new ZWTiledMap(path);

        if (tiledMap == null) {
            throw new NullPointerException("Tiled map: incorrect path name");
        }

        tiledmapMap.put(key,tiledMap);
    }

    /**
     * Get tiled map
     * @param key The tiled map
     * @return tiled map
     */
    public ZWTiledMap getTiledMap(String key){ return tiledmapMap.get(key);}

    /**
     * Dispose tiled map
     * @param key The tiled map key
     */
    public void disposeTiledMap(String key){
        ZWTiledMap tiledMap = tiledmapMap.get(key);
        if (tiledMap!=null){tiledMap.dispose();}
    }

    /**
     * Save texture using path and key
     * @param key The key
     * @param path The path to the texture
     * @throws NullPointerException if the path is incorrect or not found
     */
    public void loadTexture(String key, String path){
        if (path == null) {
            throw new NullPointerException("Texture: no path recieved");
        }

        //Texture texture = new Texture(Gdx.files.internal(path));
        Texture texture = new Texture(path);

        if (texture == null) {
            throw new NullPointerException("Texture: incorrect path name");
        }

        textureMap.put(key,texture);
    }

    /**
     * Get texture
     * @param key The texture key
     * @return texture
     */
    public Texture getTexture(String key){
        return textureMap.get(key);
    }

    /**
     * Dispose texture
     * @param key The texture key
     */
    public void disposeTexture(String key){
        Texture texture = textureMap.get(key);
        if (texture!=null){texture.dispose();}
    }







}
