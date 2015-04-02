package edu.chalmers.zombie.testing;

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
    private float tileSize, mapHeight, mapWidth;
    private String collisionString;

    public CollisionTestLevel(){
        tiledMap =new TmxMapLoader().load("core/assets/Map/Test_collision.tmx");    //hämta testkartan
        meta = (TiledMapTileLayer) tiledMap.getLayers().get("meta");                //hämta data för kollision etc
        meta.setVisible(false);                                                     //ska ju inte ritas ut
        tileSize = meta.getTileWidth();
        mapHeight = meta.getHeight();
        mapWidth = meta.getWidth();
        collisionString = "collision";
    }

    public Point coordinatesToTile(Point coordinates){
        int x = Math.round(coordinates.x / tileSize);   //Vill ha som int istället för float.
        int y = Math.round((mapHeight * tileSize - coordinates.y) / tileSize); //Måste flippa, 0,0 är längst ner till vänster i libGDX men högst upp till vänster i Tiled
        return new Point(x,y);
    }

    public Point tileToCoordinates(Point tile){
        int x = Math.round(tile.x * tileSize);
        int y = Math.round(tileSize*(mapHeight-tile.y));
        return new Point(x,y);
    }

    public TiledMapTileLayer getMeta(){ return this.meta; }

    public TiledMap getMap(){ return this.tiledMap; }

    public float getTileSize(){ return this.tileSize; }

    public String getCollisionString(){ return this.collisionString;}
}
