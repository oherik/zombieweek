package edu.chalmers.zombie.utils;

import edu.chalmers.zombie.adapter.ZWPixmap;
import edu.chalmers.zombie.adapter.ZWTexture;
import edu.chalmers.zombie.adapter.ZWTextureRegion;

import java.util.ArrayList;

/**
 * An animator to animate images using texture frames
 *
 * Created by Tobias on 15-05-21.
 */
public class Animator {

    private ZWTextureRegion[] textureFrames;
    private float time;
    private float timeDelay;
    private int currentFrame;
    private ArrayList<ZWTextureRegion> stillFrames; //still frame for animation, e.g. when player/zombie is standing still
    private int overlayTime;
    private boolean overlay = false;
    private ZWTextureRegion overlayFrame; //frame that will overlay
    private long systemTime; //stores system time
    private int currentStillFrame;

    /**
     * Creates an empty Animator
     */
    public Animator(){
        stillFrames = new ArrayList<ZWTextureRegion>();
    }

    /**
     * Created an Animator with frames and a time delay.
     * @param textureFrames The frames that will be animated
     * @param timeDelay The time delay
     */
    public Animator(ZWTextureRegion textureFrames[], float timeDelay){
        this();
        setFrames(textureFrames, timeDelay);
    }

    /**
     * Sets the frames of the animator
     * @param textureFrames The frames
     * @param timeDelay The time delay
     */
    public void setFrames(ZWTextureRegion[] textureFrames, float timeDelay){
        this.timeDelay = timeDelay;
        this.textureFrames = textureFrames;
        time = 0;
        currentFrame = 0;
    }

    /**
     * Sets a still image for animation.
     * @param stillFrames The still frames
     */
    public void setStillFrame(ArrayList<ZWTextureRegion> stillFrames){
        this.stillFrames = stillFrames;
    }

    /**
     * Adds still frame
     * @param stillFrame The frame
     */
    public void addStillFrame(ZWTextureRegion stillFrame){
        stillFrames.add(stillFrame);
    }

    /**
     * Sets overlay frame
     * @param overlayFrame The frame that will overlay
     */
    public void setOverlayFrame(ZWTextureRegion overlayFrame){
        this.overlayFrame = overlayFrame;

    }

    /**
     * Set the time to overlay starting now
     * @param overlayTime The time in milliseconds
     */
    public void setOverlay(int overlayTime){
        this.overlayTime = overlayTime;
        overlay = true;
        systemTime = System.currentTimeMillis();
    }

    /**
     * Updates the animation
     * @param deltaTime The delta time
     */
    public void update(float deltaTime){

        //Checks if image should overlay
        if(overlay==true && System.currentTimeMillis()-systemTime>overlayTime){
            overlay = false;
        }

        if(timeDelay <=0){
            return;
        }
        time +=deltaTime;

        while (time>= timeDelay){
            step();
        }
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
    public ZWTextureRegion getFrame() {
       if (overlay){
           return createOverlay(textureFrames[currentFrame].getZWTexture(),currentFrame*32,0);
       }

        return textureFrames[currentFrame];
    }

    /**
     * Get the still frame of the animation
     * @return TextureRegion still frame
     */
    public ZWTextureRegion getStillFrame() {
        if (overlay){
            return createOverlay(stillFrames.get(currentStillFrame).getZWTexture(), 0, 0);
        }

        return  stillFrames.get(currentStillFrame);
    }

    /**
     * Sets the current still frame
     * @param index
     */
    public void setCurrentStillFrame(int index){
        this.currentStillFrame = index;
    }

    /**
     * Merges the frame with texture
     * @param texture The texture to be merged with the still frame
     * @param x The x pos of the frame
     * @param y The y pos of the frame
     * @return Merged texture as a TextureRegion
     */
    private ZWTextureRegion createOverlay(ZWTexture texture, int x, int y){

        //prepare texturedata
        overlayFrame.getZWTexture().prepare();
        texture.prepare();

        //turn into pixmaps
        ZWPixmap pixmap = overlayFrame.getZWTexture().consumeZWPixmap();
        ZWPixmap pixmap1 = texture.consumeZWPixmap();

        //merge pixmaps
        pixmap.drawZWPixmap(pixmap1,-x,y);


        ZWTextureRegion combinedTextureRegion = new ZWTextureRegion(new ZWTexture(pixmap));

        pixmap.dispose();
        pixmap1.dispose();

        return  combinedTextureRegion;
    }
}
