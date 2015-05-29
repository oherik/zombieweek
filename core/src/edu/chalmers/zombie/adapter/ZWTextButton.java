package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWTextButton {
    private TextButton textButton;

    public ZWTextButton(String name, ZWSkin zwSkin){textButton = new TextButton(name,zwSkin.getSkin());}

    public TextButton getButton(){return this.textButton;}
}
