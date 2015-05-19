package edu.chalmers.zombie.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Controller to take care of Menus
 * Created by Tobias on 15-05-18.
 */
public class MenuBuilder {

    /**
     * Creates a skin for the menu buttons
     * @return Skin The skin created
     */
    public Skin createMenuSkin(){

        Skin skin = new Skin();

        BitmapFont font = new BitmapFont(); //sets font to 15pt Arial, if we want custom font -> via constructor
        skin.add("default", font);

        //Creating a button texture
        Pixmap pixmap = new Pixmap((int)(Gdx.graphics.getWidth()/4),(int)(Gdx.graphics.getHeight()/10), Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE); //button color
        pixmap.fill();

        //Adding background to button
        skin.add("background",new Texture(pixmap));

        //Create buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        return skin;
    }
}
