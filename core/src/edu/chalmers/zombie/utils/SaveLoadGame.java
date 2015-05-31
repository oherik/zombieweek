package edu.chalmers.zombie.utils;

import edu.chalmers.zombie.model.GameModel;
import java.io.*;
import java.util.Properties;

/**
 * Save and load game using local file on device. If never played before, will create file.
 *
 * Created by Tobias on 15-05-12.
 */
public class SaveLoadGame {

    private String fileName;
    private OutputStream output;
    private InputStream input;
    private Properties properties;

    public SaveLoadGame(){
        fileName = "savedGame.properties";
        properties = new Properties();
    }


    /**
     * Saves current level, highest reached level, player health and player ammo to file. Will be used at checkpoints.
     */
    public void saveGame(){
        updateProperties();
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
     * Loads game if a game has been saved to file.
     */
    public void loadGame(){

        try {
            input = new FileInputStream(fileName);
            properties.load(input);
            System.out.println("--- LOADING GAMEFILE PROPERTIES ---");

            int highestReachedLevel = getHighestLevelFromProperties();
            GameModel.getInstance().setHighestCompletedLevel(highestReachedLevel);

            GameModel.getInstance().setfirstTimePlay(false);

            //TODO: Save properties to gameModel

        } catch (IOException e) {
            System.out.println("--- FAILED TO LOAD GAME ---");
            GameModel.getInstance().setfirstTimePlay(true);
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

    /**
     * Loads properties from file
     */
    private void loadProperties(){
        try {
            input = new FileInputStream(fileName);
            properties.load(input);
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

    /**
     * Updates properties from current game data
     */
    private void updateProperties(){
        loadProperties();
        GameModel gameModel = GameModel.getInstance();
        int level = gameModel.getCurrentLevelIndex();
        //int health = gameModel.getPlayer().getLives();
        //int ammo = gameModel.getPlayer().getAmmunition();
        int highestReachedLevel = getHighestLevelFromProperties();

        //checks if player has reached a higher level than before
        if (level>=highestReachedLevel){
            properties.setProperty("highestReachedLevel", Integer.toString(level));
            gameModel.setHighestCompletedLevel(level); //gives model the new highest completed level
        } else {
            properties.setProperty("highestReachedLevel", Integer.toString(highestReachedLevel));
            gameModel.setHighestCompletedLevel(highestReachedLevel);
        }

        System.out.println("Saved Highest reached levl: "+properties.getProperty("highestReachedLevel"));

        properties.setProperty("level", Integer.toString(level));
        //properties.setProperty("health", Integer.toString(health));
        //properties.setProperty("ammo", Integer.toString(ammo));
    }

    /**
     * Get the highest level from the already saved properties file. If no level is completed, current will be highest.
     * @return The highest level completed
     */
    private int getHighestLevelFromProperties(){
        GameModel gameModel = GameModel.getInstance();
        int highestReachedLevel;
        int level = gameModel.getCurrentLevelIndex();
        String highest = properties.getProperty("highestReachedLevel");
        if (highest!=null){ //if property not saved
            highestReachedLevel = Integer.parseInt(highest);
        } else {
            highestReachedLevel = level;
        }
        return highestReachedLevel;
    }

    /**
     * @return A copy of the properties stored in file
     */
    public Properties getProperties(){
        return (Properties)properties.clone();
    }
}
