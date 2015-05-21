package edu.chalmers.zombie.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Tobias on 15-05-21.
 */
public class Animator {

    private TextureRegion[] frames;
    private float time;
    private float delay;
    private int currentFrame;
    private int timesPlayed;

    public Animator(){}

    public Animator(TextureRegion frames[]){
        this(frames, 1/12f);
    }

    public Animator(TextureRegion frames[], float delay){
        setFrames(frames, delay);
        System.out.println("Number of frames: " + frames.length);
    }

    public void setFrames(TextureRegion[] frames, float delay){
        this.delay = delay;
        this.frames = frames;
        time = 0;
        currentFrame = 0;
        timesPlayed = 0;


    }

    public void update(float deltaTime){
        if(delay<=0){
            return;
        }
        time +=deltaTime;
        while (time>=delay){
            step();
        }
    }

    public void stepOnce(){
        step();
    }

    private void step(){
        System.out.println("STEP");
        time -= delay;
        currentFrame++;
        if(currentFrame == frames.length){
            currentFrame = 0;
            timesPlayed++;
        }
    }

    public TextureRegion getFrame() {
        System.out.println("Return frame: " + currentFrame);
        return frames[currentFrame];
    }

    public int getTimesPlayed(){return timesPlayed;}





}
