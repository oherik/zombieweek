package edu.chalmers.zombie.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
    private int overlayTime;
    private boolean overlay = false;
    private TextureRegion overlayFrame;
    private long systemTime; //stores system time


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


    public void setOverlayFrame(TextureRegion overlayFrame){
        this.overlayFrame = overlayFrame;

    }

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
       if (overlay){
           return createOverlay(textureFrames[currentFrame].getTexture(),currentFrame*32,0);
       }

        return textureFrames[currentFrame];
    }

    /**
     * Get the still frame of the animation
     * @return TextureRegion still frame
     */
    public TextureRegion getStillFrame() {
        if (overlay){
            return createOverlay(stillFrame.getTexture(),0,0);
        }

        return  stillFrame;
    }

    /**
     * Merges the frame with texture
     * @param texture The texture to be merged with the still frame
     * @param x The x pos of the frame
     * @param y The y pos of the frame
     * @return Merged texture as a TextureRegion
     */
    private TextureRegion createOverlay(Texture texture, int x, int y){

        //prepare texturedata
        overlayFrame.getTexture().getTextureData().prepare();
        texture.getTextureData().prepare();

        //turn into pixmaps
        Pixmap pixmap = overlayFrame.getTexture().getTextureData().consumePixmap();
        Pixmap pixmap1 = texture.getTextureData().consumePixmap();

        //merge pixmaps
        pixmap.drawPixmap(pixmap1,-x,y);

        TextureRegion combinedTextureRegion = new TextureRegion(new Texture(pixmap));

        pixmap.dispose();
        pixmap1.dispose();

        return  combinedTextureRegion;
    }

    /**
     * @param timeDelay The time delay
     */
    public void setTimeDelay(float timeDelay){this.timeDelay = timeDelay;}


}
