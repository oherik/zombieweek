package edu.chalmers.zombie.adapter;

import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Room;

import java.util.ArrayList;

/**
 * A level
 *
 * Created by Tobias on 15-05-20.
 */
public class Level {

    private ArrayList<Room> roomList; //keeps list of rooms

    public Level(){
        roomList = new ArrayList<Room>();

        GameModel gameModel = GameModel.getInstance();

        roomList.add(new Room(gameModel.res.getTiledMap("room0")));
        roomList.add(new Room(gameModel.res.getTiledMap("room1")));
        roomList.add(new Room(gameModel.res.getTiledMap("room2")));


    }

    /**
     * Add room to level
     * @param room The room
     */
    public void addRoom(Room room){
        roomList.add(room);
    }

    /**
     * Get room at index
     * @param index Index of the room
     * @return Room
     */
    public Room getRoom(int index){
        return roomList.get(index);
    }


}
