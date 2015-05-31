package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Erik on 2015-05-31.
 */
public class ZWClickListener extends ClickListener {
    private ZWClickAction action;

    public ZWClickListener(ZWClickAction action) {
        this.action = action;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        action.clicked();
    }
}