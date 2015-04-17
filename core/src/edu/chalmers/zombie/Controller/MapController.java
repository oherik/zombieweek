package edu.chalmers.zombie.controller;

import com.badlogic.gdx.maps.tiled.TiledMap;
import edu.chalmers.zombie.model.GameModel;

/**
 * Created by Erik on 2015-04-17.
 */
public class MapController {
    GameModel gameModel;

    public MapController(GameModel gameModel){

        this.gameModel = gameModel;

    }

    public TiledMap getMap(int levelIndex){return gameModel.getMap(levelIndex);}
}
