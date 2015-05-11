package edu.chalmers.zombie.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;

/**
 * A recource manager to load and store assets.
 *
 * Created by Tobias on 15-05-11.
 */
public class ResourceManager {

    //Sprites?
    //Maps?
    private HashMap<String, Texture> textureMap;

    public ResourceManager(){
        textureMap = new HashMap<String, Texture>();
    }

    /**
     * Save texture with path and key
     * @param key The key
     * @param path The path to the texture
     */
    public void loadTexture(String key, String path){
        //Texture texture = new Texture(Gdx.files.internal(path));
        Texture texture = new Texture(path);
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
