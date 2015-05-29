package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.audio.Sound;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWSound {
    private Sound sound;

    public ZWSound(String path){
        sound = ZWGameEngine.newSound(path);
    }

    public void dispose(){
        sound.dispose();
    }
}
