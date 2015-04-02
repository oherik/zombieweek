package edu.chalmers.zombie.model;

import edu.chalmers.zombie.Player;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by Tobias on 15-04-02.
 */
public class GameModel {

    Player player;

    public GameModel(){

        player = new Player();

    }

    public Player getPlayer(){
        return player;
    }

    public void movePlayer(Direction direction){
        //player.move(direction);
    }

}
