package edu.chalmers.zombie.model;

/**
 * Created by daniel on 5/19/2015.
 * Modified by Erik
 */
import com.sun.istack.internal.NotNull;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.controller.AimingController;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import java.util.ArrayList;

public class FlashlightModel {
    private ZWWorld world;
    private ZWTexture darkTexture = new ZWTexture("core/assets/darkness.png");
    private ZWVector playerPosition = new ZWVector();
    private ZWVector collisionPoint = new ZWVector();
    private float currentFraction = 1337;
    private ArrayList<Float> collisionPoints = new ArrayList<Float>();
    private float direction;
    private float width;
    private int numberOfRays;
    private float length;
    private ZWVector[] rays;
    private ArrayList<ZWVector> endPoints = new ArrayList<ZWVector>();
    private int maxYIndex;
    private float[] corners = new float[8];
    private float lengthFraction;

    public FlashlightModel(@NotNull ZWWorld world) throws NullPointerException{
        if (world == null){
            throw new NullPointerException("The world is null");
        }
        this.world = world;
        width = Constants.PI/4;
        numberOfRays = 100;
        lengthFraction = 0.75f;
        initializeRays();
    }
    public FlashlightModel(@NotNull ZWWorld world, float width, int numberOfRays, float lengthFraction)throws NullPointerException, IllegalArgumentException{
        if (world == null){
            throw new NullPointerException("The world is null");
        }
        this.world = world;
        this.width = width;
        this.numberOfRays = numberOfRays;
        if (lengthFraction < 0.05f || 0.95f < lengthFraction){
            throw new IllegalArgumentException("The lengthFraction has to be between 0.05 and 0.95");
        }
        this.lengthFraction = lengthFraction;
        initializeRays();
    }

    public void clearAll(){
        endPoints.clear();
    }
    private void initializeRays(){
        rays = new ZWVector[numberOfRays];
    }


    public void setWorld(ZWWorld world){
        this.world = world;
    }
    public float getLengthFraction(){
        return lengthFraction;
    }

    public void setLength(float length) {
        this.length = length;
    }
    public void setDirection(float direction){
        this.direction = direction;
    }
    public void setPlayerPosition(ZWVector playerPosition){
        this.playerPosition = playerPosition;
    }
    public ZWVector[] getRays(){
        return rays;
    }
    public ArrayList<ZWVector> getEndPoints() {
        return endPoints;
    }
    public float getLength() {
        return length;
    }

    public int getNumberOfRays() {
        return numberOfRays;
    }

    public ZWWorld getWorld() {
        return world;
    }

    public ZWTexture getDarkTexture() {
        return darkTexture;
    }


    public ZWVector getPlayerPosition() {
        return playerPosition;
    }

    public ZWVector getCollisionPoint() {
        return collisionPoint;
    }

    public void setCurrentFraction(float currentFraction) {
        this.currentFraction = currentFraction;
    }

    public ArrayList<Float> getCollisionPoints() {
        return collisionPoints;
    }

    public float getDirection() {
        return direction;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setMaxYIndex(int maxYIndex) {
        this.maxYIndex = maxYIndex;
    }

    public void setCorners(float[] corners) {
        this.corners = corners;
    }
    public float[] getCorners(){
        return corners;
    }
    public int getMaxYIndex(){
        return maxYIndex;
    }

    public void setCollisionPoint(ZWVector collisionPoint) {
        this.collisionPoint = collisionPoint;
    }
}
