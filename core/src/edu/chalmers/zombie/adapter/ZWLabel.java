package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.*;

/**
 * Created by Tobias on 15-05-29.
 * Modified by Erik
 */
public class ZWLabel {
    private Label label;
    private ZWBitmapFont font;
    private com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle labelStyle;

    public ZWLabel(String text){
        com.badlogic.gdx.utils.StringBuilder stringBuilder = new com.badlogic.gdx.utils.StringBuilder();
        stringBuilder.append(text);

        labelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        font = new ZWBitmapFont(); //sets font to 15pt Arial, if we want custom font -> via constructor

        labelStyle.font = font.getBitmapFont();

        setLabel(text);
    }

    public void setColor(float r, float g, float b, float a){
        font.setColor(r,g,b,a);
        label.getStyle().font = font.getBitmapFont();
    }

    public void scale(float scale){this.font.getBitmapFont().scale(scale);}

    public Label getLabel(){return this.label;}

    public void setLabel(String str){
        label = new Label(str, labelStyle);
    }
}
