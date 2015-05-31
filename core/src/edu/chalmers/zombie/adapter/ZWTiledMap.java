package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by Erik on 2015-05-29.
 */
public class ZWTiledMap {
    private TiledMap tiledMap;

    public ZWTiledMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
    }

    public int getTiledWidth() {
        return tiledMap.getProperties().get("width", Integer.class);
    }

    public int getTiledHeight() {
        return tiledMap.getProperties().get("height", Integer.class);
    }

    public boolean tileIsEmpty(String layer, int col, int row) {
        MapLayer mapLayer = tiledMap.getLayers().get(layer);
        if(mapLayer instanceof TiledMapTileLayer) {
            TiledMapTileLayer  tiledLayer = (TiledMapTileLayer) mapLayer;
            TiledMapTileLayer.Cell currentCell = tiledLayer.getCell(col, row);
            return(currentCell==null||currentCell.getTile()==null);
        }
        return true;

    }

    public boolean hasProperty(String layer, int col, int row, String property){
        MapLayer mapLayer = tiledMap.getLayers().get(layer);
        if(mapLayer instanceof TiledMapTileLayer) {
             TiledMapTileLayer  tiledLayer = (TiledMapTileLayer) mapLayer ;
             TiledMapTileLayer.Cell currentCell = tiledLayer.getCell(col, row);
             return (!tileIsEmpty(layer,col, row) && currentCell.getTile().getProperties().get(property) != null);
        }
        return false;
    }

    public Object getProperty(String layer, int col, int row, String propertyName){
        MapLayer mapLayer = tiledMap.getLayers().get(layer);
        if(mapLayer instanceof TiledMapTileLayer) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer) mapLayer;
            TiledMapTileLayer.Cell currentCell = tiledLayer.getCell(col, row);
            if (hasProperty(layer, col, row, propertyName))
                return currentCell.getTile().getProperties().get(propertyName);
        }
        return null;
    }

    public void setVisibible(String layer, boolean visible){
        MapLayer mapLayer =  tiledMap.getLayers().get(layer);
        mapLayer.setVisible(visible);
    }

    public void dispose(){
        tiledMap.dispose();
    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }

}
