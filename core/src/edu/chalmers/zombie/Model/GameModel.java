package edu.chalmers.zombie.model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.utils.Direction;

import java.util.ArrayList;

/**
 * Created by Tobias on 15-04-02.
 */
public class GameModel {

    private static GameModel instance = new GameModel();
    private Player player;
    private ArrayList<Level> levels;
    private TiledMapTileLayer metaLayer;
    private int currentLevel;

    private GameModel(){
        currentLevel = 0;
        levels = new ArrayList<Level>();
        addTestLevel();                                 //TODO debug
        addTestPlayer();
    }

    private void addTestLevel(){
        levels.add(new Level("core/assets/Map/Test_v2.tmx"));
    }

    private void addTestPlayer(){
        player = new Player(new Sprite(new Texture("core/assets/player_professional_final_version.png")),levels.get(0).getWorld(),0,0);
    }

    public static GameModel getInstance( ) {
        return instance;
    }

    public Player getPlayer(){
        return player;
    }

    public World getWorld(){return levels.get(currentLevel).getWorld(); }

    public Level getLevel(){return levels.get(currentLevel); }

    public Level getLevel(int levelIndex){
        if(levelIndex >= levels.size())
            throw new IndexOutOfBoundsException("GameModel: the getLevel index exceeds array size");
        currentLevel = levelIndex;
        return levels.get(levelIndex);
    }

    public TiledMap getMap(int levelIndex){
        if(levelIndex >= levels.size())
            throw new IndexOutOfBoundsException("GameModel: getMap index exceeds array size");
        currentLevel = levelIndex;
        return levels.get(levelIndex).getMap();
    }

    public TiledMap getMap(){
        return levels.get(currentLevel).getMap();
    }

    public TiledMap getNextMap(){
        if(this.currentLevel ==this.levels.size()-1)
            throw new IndexOutOfBoundsException("GameModel: already at last indexed map");
        currentLevel+=1;
        return this.levels.get(currentLevel).getMap();
    }

    public TiledMap getPreviousMap(){
        if(this.currentLevel == 0)
            throw new IndexOutOfBoundsException("GameModel: already at first indexed map");
        currentLevel-=1;
        return this.levels.get(currentLevel).getMap();

    }

    public void movePlayer(Direction direction){
        player.move(direction);
    }
}
