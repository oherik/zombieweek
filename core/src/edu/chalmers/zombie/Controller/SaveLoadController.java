package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.model.GameModel;
import java.io.*;
import java.util.Properties;

/**
 * Save and load game
 *
 * Created by Tobias on 15-05-12.
 */
public class SaveLoadController {

    private String fileName;
    private GameModel gameModel;
    private OutputStream output;
    private InputStream input;
    private Properties properties;

    public SaveLoadController(){
        fileName = "savedGame.properties";
        gameModel = GameModel.getInstance();
        properties = new Properties();
    }

    /**
     * Saves current level, player health and player ammo to file. Will be used at checkpoints.
     */
    public void saveGame(){
        int level = gameModel.getCurrentLevelIndex();
        int health = gameModel.getPlayer().getLives();
        int ammo = gameModel.getPlayer().getAmmunition();
        properties.setProperty("level", Integer.toString(level));
        properties.setProperty("health", Integer.toString(health));
        properties.setProperty("ammo", Integer.toString(ammo));

        try {
            output = new FileOutputStream(fileName);
            properties.store(output, null); //saving properties to file
        } catch (IOException e) {
            System.out.println("--- FAILED TO SAVE GAME ---");
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                    System.out.println("--- GAME SAVED ---");
                } catch (IOException e) {
                    System.out.println("--- FAILED TO CLOSE OUTPUT ---");
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Loads game if a game has been saved.
     */
    public void loadGame(){

        try {
            input = new FileInputStream(fileName);
            properties.load(input);
            System.out.println("--- LOADING GAME ---");
            System.out.println("Current level: " + properties.getProperty("level"));
            System.out.println("Player health: " + properties.getProperty("health"));
            System.out.println("Player ammo: " + properties.getProperty("ammo"));

            //TODO: Save properties to gameModel

        } catch (IOException e) {
            System.out.println("--- FAILED TO LOAD GAME ---");
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("--- FAILED TO CLOSE INTPUT ---");
                    e.printStackTrace();
                }
            }
        }

    }




}
