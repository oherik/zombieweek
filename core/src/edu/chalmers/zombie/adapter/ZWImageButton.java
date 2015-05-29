package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.view.MainMenuScreen;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWImageButton {
    private ImageButton imageButton;
    ImageButton.ImageButtonStyle buttonStyle;

    public ZWImageButton(){
        buttonStyle = new ImageButton.ImageButtonStyle();
        imageButton = new ImageButton(buttonStyle);
    }

    public void setImageUp(ZWTexture zwTexture){
        buttonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(zwTexture.getTexture()));
    }

    public void setSize(float width, float height){ imageButton.setSize(width,height);}

    public void setPosition(float x, float y){imageButton.setPosition(x,y);}

    public ImageButton getButton(){return this.imageButton;}

    public void addListener(final ZWClickAction zwClickAction){
        this.imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zwClickAction.clicked();
            }
        });
    }



}
