package edu.chalmers.zombie.model.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.awt.*;

/**
 * Created by Erik on 2015-04-02.
 */
public class CollisionTestLevel {

    private TiledMap tiledMap;
    private TiledMapTileLayer meta;
    private int tileSize, mapHeight, mapWidth;

    public CollisionTestLevel(){
        tiledMap =new TmxMapLoader().load("core/assets/Map/Test_collision.tmx");    //hämta testkartan
        meta = (TiledMapTileLayer) tiledMap.getLayers().get("meta");                //hämta data för kollision etc
        meta.setVisible(false);                                                     //ska ju inte ritas ut
        tileSize = Math.round(meta.getTileWidth());                 //Vill ha som int istället för float.
        mapHeight = Math.round(meta.getHeight());
        mapWidth = Math.round(meta.getWidth());
    }

    public Point coordinatesToTile(Point coordinates){
        int x = coordinates.x / tileSize;
        int y =((mapHeight * tileSize - coordinates.y) / tileSize); //Måste flippa, 0,0 är längst enr till vänster i libGDX men högst upp till vänster i Tiled
        return new Point(x,y);
    }

}
