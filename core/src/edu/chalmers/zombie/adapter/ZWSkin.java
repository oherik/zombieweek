package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Wrapper to create a skin
 *
 * Created by Tobias on 15-05-29.
 */
public class ZWSkin {
    private Skin skin;

    public ZWSkin(){skin = new Skin();}

    public void add(String key, ZWTexture texture){
        skin.add(key,texture.getTexture());
    }

    public void add(String key, ZWBitmapFont font){this.skin.add(key,font);}

    public void createButtons(){
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }

    public Skin getSkin(){return this.skin;}



}
