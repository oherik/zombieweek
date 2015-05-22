package edu.chalmers.zombie.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An animator to animate images using texture frames
 *
 * Created by Tobias on 15-05-21.
 */
public class Animator {

    private TextureRegion[] textureFrames;
    private float time;
    private float timeDelay;
    private int currentFrame;
    private TextureRegion stillFrame; //still frame for animation, e.g. when player/zombie is standing still


    /**
     * Creates an empty Animator
     */
    public Animator(){}


    /**
     * Created an Animator with frames and a time delay.
     * @param textureFrames The frames that will be animated
     * @param timeDelay The time delay
     */
    public Animator(TextureRegion textureFrames[], float timeDelay){
        setFrames(textureFrames, timeDelay);
    }

    /**
     * Sets the frames of the animator
     * @param textureFrames The frames
     * @param timeDelay The time delay
     */
    public void setFrames(TextureRegion[] textureFrames, float timeDelay){
        this.timeDelay = timeDelay;
        this.textureFrames = textureFrames;
        time = 0;
        currentFrame = 0;
    }

    /**
     * Sets a still image for animation.
     * @param stillFrame The still frame
     */
    public void setStillFrame(TextureRegion stillFrame){
        this.stillFrame = stillFrame;
    }



    /**
     * Updates the animation
     * @param deltaTime The delta time
     */
    public void update(float deltaTime){
        if(timeDelay <=0){
            return;
        }
        time +=deltaTime;

        while (time>= timeDelay){
            step();
        }
    }

    /**
     * Makes animation step once
     */
    public void stepOnce(){
        step();
    }

    /**
     * Makes animation step
     */
    private void step(){
        time -= timeDelay;
        currentFrame++;
        if(currentFrame == textureFrames.length){ //if end of frames array, start from beginning
            currentFrame = 0;
        }
    }

    /**
     * Get all frames of the animation
     * @return TextureRegion the frames of the animation
     */
    public TextureRegion getFrame() {
        return textureFrames[currentFrame];
    }

    /**
     * Get the still frame of the animation
     * @return TextureRegion still frame
     */
    public TextureRegion getStillFrame() {return  stillFrame;}

    /**
     * @param timeDelay The time delay
     */
    public void setTimeDelay(float timeDelay){this.timeDelay = timeDelay;}


}
