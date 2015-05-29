package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Wrapper class for the stage
 *
 * Created by Tobias on 15-05-29.
 */
public class ZWStage {

    private Stage stage;

    public ZWStage(){stage = new Stage();}

    public void addActor(ZWImageButton zwImageButton){stage.addActor(zwImageButton.getButton());}

    public void addActor(ZWTable zwTable){stage.addActor(zwTable.getTable());}

    public void act(){stage.act();}

    public void draw(){stage.draw();}


    public Stage getStage() {
        return this.stage;
    }
}
