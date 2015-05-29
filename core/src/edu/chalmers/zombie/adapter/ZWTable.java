package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWTable {
    private Table table;

    public ZWTable(){table = new Table();}

    public void add(ZWTextButton zwTextButton, float x, float y, float padBottom){
        table.add(zwTextButton.getButton()).size(x,y).padBottom(padBottom).row();
    }

    public void add(ZWLabel label, float padBottom){
        table.add(label.getLabel()).padBottom(padBottom).row();
    }

    public Table getTable(){return this.table;}

    public void setFillParent(boolean bool){this.table.setFillParent(bool);}

}
