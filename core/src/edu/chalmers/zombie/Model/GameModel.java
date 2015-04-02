package edu.chalmers.zombie.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by Tobias on 15-04-02.
 */
public class GameModel {

    Player player;
    World world;

    public GameModel(){
        world = new World(new Vector2(), true);
        player = new Player(world,5,5);
        world = new World(new Vector2(),true);

    }

    public Player getPlayer(){
        return player;
    }

    public World getWorld(){return world; }

    public void movePlayer(Direction direction){
        //player.move(direction);
    }

}
