package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.*;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWLabel {
    private Label label;
    private com.badlogic.gdx.utils.StringBuilder stringBuilder;
    private ZWBitmapFont font;
    private com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle labelStyle;

    public ZWLabel(String text){


        com.badlogic.gdx.utils.StringBuilder stringBuilder = new com.badlogic.gdx.utils.StringBuilder();
        stringBuilder.append(text);

        labelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        font = new ZWBitmapFont(); //sets font to 15pt Arial, if we want custom font -> via constructor

        labelStyle.font = font.getBitmapFont();
    }

    public void scale(float scale){this.font.getBitmapFont().scale(scale);}

    public Label getLabel(){return this.label;}

    public void setLabel(String str){
        label = new Label(str, labelStyle);
    }
}
