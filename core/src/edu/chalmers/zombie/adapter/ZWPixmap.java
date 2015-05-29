package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * Created by Tobias on 15-05-29.
 */
public class ZWPixmap {
    private Pixmap pixmap;

    public ZWPixmap(int width, int height){
        pixmap = new Pixmap(width,height, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
    }

    public ZWPixmap(Pixmap pixmap){
        this.pixmap = pixmap;
    }

    public Pixmap getPixmap(){return this.pixmap;}

    public void dispose(){this.pixmap.dispose();}

    public void drawZWPixmap(ZWPixmap pixmap1, int x, int y){
        this.pixmap.drawPixmap(pixmap1.getPixmap(),x,y);
    }

}
