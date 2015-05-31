package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.FlashlightModel;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;

/**
 * Created by daniel on 5/31/2015.
 */
public class FlashlightController {
    private FlashlightModel flashlightModel;
    private float currentFraction;
    private boolean foundFixture;
     /*
    *draws a black polygon that is shaped like everything that is not inside the flashlight circle segment.
     */
    public void draw(FlashlightModel flashlightModel, ZWPolygonSpriteBatch polygonSpriteBatch){
        this.flashlightModel = flashlightModel;
        flashlightModel.setWorld(GameModel.getInstance().getRoom().getWorld());
        flashlightModel.clearAll();
        calculateLength();
        fetchDirection();
        fetchPlayerPosition();
        calculateEndPoints();
        calculateCollisionPoints();
        calculateMaxYIndex();
        calculateCorners();
        ZWPolygonRegion darkness = createDarkRegion();
        polygonSpriteBatch.begin();
        polygonSpriteBatch.drawPolygonRegion(darkness, 0, 0);
        polygonSpriteBatch.end();
    }
    private void calculateLength(){
        float lengthFraction = flashlightModel.getLengthFraction();
        float tileSize = Constants.TILE_SIZE;
        float windowHeight = ZWGameEngine.getWindowHeight();
        float windowWidth = ZWGameEngine.getWindowWidth();
        float height =  (windowHeight/tileSize - windowHeight/tileSize/2)*lengthFraction;
        float width = (windowWidth/tileSize - windowWidth/tileSize/2)*lengthFraction;
        if (height > width){
            flashlightModel.setLength(width);
        } else{
            flashlightModel.setLength(height);
        }
    }
    private void fetchDirection(){
        GameModel gameModel = GameModel.getInstance();
        float direction = gameModel.getPlayer().getRadDirection() + Constants.PI/2;
        flashlightModel.setDirection(direction);
    }
    private void fetchPlayerPosition(){
        GameModel gameModel = GameModel.getInstance();
        ZWVector playerPosition = new ZWVector();
        playerPosition.set(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
        flashlightModel.setPlayerPosition(playerPosition);
    }
    private void calculateEndPoints(){
        int numberOfRays = flashlightModel.getNumberOfRays();
        ZWVector[] rays = flashlightModel.getRays();
        float length = flashlightModel.getLength();
        float direction = flashlightModel.getDirection();
        float width = flashlightModel.getWidth();
        ZWVector playerPosition = flashlightModel.getPlayerPosition();
        ArrayList<ZWVector> endPoints = flashlightModel.getEndPoints();
        for(int i = 0; i<numberOfRays; i++) {
            rays[i] = new ZWVector(1, 1);
            rays[i].setLength(length);
            rays[i].setAngleRad(direction - width / 2 + i * width / numberOfRays);
            ZWVector end = new ZWVector(rays[i]);
            end.add(playerPosition);
            endPoints.add(lengthenRay(playerPosition,end,0.4f));
        }
    }
    private ZWVector lengthenRay(ZWVector origin, ZWVector end,float dist){
        float dy = origin.getY()-end.getY();
        float dx = origin.getX()-end.getX();
        float dl= (float)(Math.sqrt(dy*dy+dx*dx));
        float y = end.getY() - dy*dist/dl;
        float x = end.getX() - dx*dist/dl;
        return new ZWVector(x,y);
    }
    private void calculateCollisionPoints(){
        ZWVector[] rays = flashlightModel.getRays();
        ZWVector playerPosition = flashlightModel.getPlayerPosition();
        ArrayList<ZWVector> endPoints = flashlightModel.getEndPoints();
        ZWVector collisionPoint;
        ArrayList<Float> collisionPoints = flashlightModel.getCollisionPoints();
        for (ZWVector ray : rays) {
           currentFraction = 1337;
            foundFixture = false;
            rayCast(ray);
            collisionPoint = flashlightModel.getCollisionPoint();
            if (foundFixture) {
                ZWVector temp = new ZWVector(ray);
                temp.add(playerPosition);
                int tempIndex = endPoints.indexOf(temp);
                for(int i = 0; i < endPoints.size(); i++) {
                    if(endPoints.get(i).equals(lengthenRay(playerPosition,temp, 0.4f))) {
                        tempIndex = i;
                    }
                }
                endPoints.remove(tempIndex);
                endPoints.add(tempIndex, lengthenRay(playerPosition,collisionPoint,0.4f));
            }

        }
        endPoints.add(playerPosition);
        collisionPoints.clear();
    }
    private void rayCast(ZWVector ray) {
        ZWWorld world = flashlightModel.getWorld();
        ZWRayCastCallback callback = new ZWRayCastCallback() {
            @Override
            public float reportRayFixture(ZWFixture fixture, ZWVector point, ZWVector normal, float fraction) {
                if (fixture.getCategoryBits()== Constants.COLLISION_OBSTACLE ||
                        fixture.getCategoryBits() == Constants.COLLISION_ZOMBIE){
                    if (fraction < currentFraction) {
                        currentFraction = fraction;
                        flashlightModel.setCollisionPoint(point);
                    }
                    foundFixture = true;
                }
                return 1;
            }
        };
        ZWVector playerPosition = flashlightModel.getPlayerPosition();
        world.rayCast(callback, playerPosition, sum(ray, playerPosition));
    }
    private ZWVector sum(ZWVector v1, ZWVector v2){
        ZWVector tmpVector1 = new ZWVector();
        ZWVector tmpVector2 = new ZWVector();
        tmpVector1.set(v1);
        tmpVector2.set(v2);
        ZWVector returnVector = tmpVector1.add(tmpVector2);
        return returnVector;
    }
    private void calculateMaxYIndex(){
        float maxY = 0;
        flashlightModel.setMaxYIndex(-1);
        ArrayList<ZWVector> endPoints =flashlightModel.getEndPoints();
        for(int i = 0; i<endPoints.size(); i++){
            float currentY = endPoints.get(i).getY();
            if(currentY>maxY){
                maxY = currentY;
                flashlightModel.setMaxYIndex(i);
            }
        }
    }
    private void calculateCorners(){
        float windowWidth = ZWGameEngine.getWindowWidth();
        float windowHeight = ZWGameEngine.getWindowHeight();
        int tileSize = Constants.TILE_SIZE;
        ZWVector playerPosition = flashlightModel.getPlayerPosition();
        float[] corners = new float[8];
        corners[0] = playerPosition.getX() - windowWidth/(tileSize*2);
        corners[1] = windowHeight/tileSize + playerPosition.getY() - windowHeight/(tileSize*2);       //Top left
        corners[2] = playerPosition.getX() - windowWidth/(tileSize*2);
        corners[3] = playerPosition.getY() - windowHeight/(tileSize*2);                          //Bottom left
        corners[4] = windowWidth/tileSize+playerPosition.getX() - windowWidth/(tileSize*2);
        corners[5] = playerPosition.getY() - windowHeight/(tileSize*2);           //Bottom right
        corners[6] = windowWidth/tileSize +playerPosition.getX() - windowWidth/(tileSize*2);
        corners[7] = windowHeight/tileSize + playerPosition.getY() - windowHeight / (tileSize*2); //Top right
        flashlightModel.setCorners(corners);
    }
    private ZWPolygonRegion createDarkRegion(){
        float[] vertices = createArrayOfVertices();
        vertices = scaleVertices(vertices);
        short[] triangles = calculateTriangles(vertices);
        ZWTexture darkTexture = flashlightModel.getDarkTexture();
        ZWPolygonRegion darkness = new ZWPolygonRegion(new ZWTextureRegion(darkTexture), vertices, triangles);
        return darkness;
    }
    private float[] createArrayOfVertices(){
        ArrayList<Float> collisionPoints = flashlightModel.getCollisionPoints();
        float[] corners = flashlightModel.getCorners();
        int maxYIndex = flashlightModel.getMaxYIndex();
        ArrayList<ZWVector> endPoints = flashlightModel.getEndPoints();
        collisionPoints.add(corners[0]);
        collisionPoints.add(corners[1]);
        for(int i = maxYIndex; i >= 0; i--) {
            collisionPoints.add(endPoints.get(i).getX());
            collisionPoints.add(endPoints.get(i).getY());
        }
        for(int i =endPoints.size()-1; i >= maxYIndex; i--) {
            collisionPoints.add(endPoints.get(i).getX());
            collisionPoints.add(endPoints.get(i).getY());
        }

        for(float fl: corners)
            collisionPoints.add(fl);
        float[] vertices = convertToArray(collisionPoints);
        return vertices;
    }
    private float[] scaleVertices(float[] vertices){
        ZWVector playerPosition = flashlightModel.getPlayerPosition();
        for(int i = 0; i < vertices.length; i= i+ 2) {
            vertices[i] = vertices[i] * Constants.TILE_SIZE - playerPosition.getX() * Constants.TILE_SIZE + ZWGameEngine.getWindowWidth()/2;
            vertices[i+1] = vertices[i+1] * Constants.TILE_SIZE - playerPosition.getY() * Constants.TILE_SIZE + ZWGameEngine.getWindowHeight()/2;
        }
        return vertices;
    }
    private float[] convertToArray(ArrayList<Float> floatList){
        float[] floatArray = new float[floatList.size()];
        int i = 0;
        for (float f: floatList){
            floatArray[i] = f;
            i++;
        }
        return floatArray;
    }
    private short[] calculateTriangles(float[] vertices) {
        ZWEarClippingTriangulator ect = new ZWEarClippingTriangulator();
        short[] triangles = ect.computeTriangles(vertices);
        return triangles;
    }
}
