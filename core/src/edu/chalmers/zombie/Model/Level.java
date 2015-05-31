package edu.chalmers.zombie.model;

import java.util.ArrayList;

/**
 * A level
 *
 * Created by Tobias on 15-05-20.
 * Modified by Erik and Neda
 */
public class Level {

    private int currentRoomIndex;

    private ArrayList<Room> roomList; //keeps list of rooms

    public Level(){
        roomList = new ArrayList<Room>();
        setCurrentRoomIndex(0);
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

    /**
     * Sets a complete set of rooms
     * @param roomList  The rooms to set
     */
    public void setRooms(ArrayList<Room> roomList){
        this.roomList = roomList;
    }

    public int numberOfRooms(){
        return roomList.size();
    }

    public ArrayList<Room> getRooms(){
        return roomList;
    }

    /**
     * A method which sets which room the player last visited in a level.
     * @param currentRoomIndex int room number.
     */
    public void setCurrentRoomIndex(int currentRoomIndex) {

        this.currentRoomIndex = currentRoomIndex;
    }

    /**
     * A method which returns which room the player last visited in a level.
     * @return currentRoomIndex, room number (int).
     */
    public int getCurrentRoomIndex() {

        return currentRoomIndex;
    }
}
