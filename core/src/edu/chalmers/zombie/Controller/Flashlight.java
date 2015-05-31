package edu.chalmers.zombie.controller;

/**
 * Created by daniel on 5/19/2015.
 */
import com.sun.istack.internal.NotNull;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import java.util.ArrayList;

public class Flashlight {
    private ZWWorld world;
    private ZWTexture darkTexture = new ZWTexture("core/assets/darkness.png");
    private ZWTexture lightTexture = new ZWTexture("core/assets/light.png");
    private ZWVector playerPosition = new ZWVector();
    private ZWVector collisionPoint = new ZWVector();
    private float currentFraction = 1337;
    private boolean foundFixture;
    private ArrayList<Float> collisionPoints = new ArrayList<Float>();
    private float direction;
    private ZWRayCastCallback callback = createCallback();
    private float width;
    private int numberOfRays;
    private float length;
    private ZWVector[] rays;
    private ArrayList<ZWVector> endPoints = new ArrayList<ZWVector>();
    private int maxYIndex;
    private float[] corners = new float[8];
    private float lengthFraction;

    public Flashlight(@NotNull ZWWorld world) throws NullPointerException{
        if (world == null){
            throw new NullPointerException("The world is null");
        }
        this.world = world;
        width = Constants.PI/4;
        numberOfRays = 100;
        lengthFraction = 0.75f;
        initializeRays();
    }
    public Flashlight(@NotNull ZWWorld world, float width, int numberOfRays, float lengthFraction)throws NullPointerException, IllegalArgumentException{
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
    public void draw(ZWPolygonSpriteBatch polygonSpriteBatch){
        world = GameModel.getInstance().getRoom().getWorld();
        clearAll();
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
    private void clearAll(){
        endPoints.clear();
    }
    private void calculateLength(){
        float tileSize = Constants.TILE_SIZE;
        float windowHeight = ZWGameEngine.getWindowHeight();
        float windowWidth = ZWGameEngine.getWindowWidth();
        float height =  (windowHeight/tileSize - windowHeight/tileSize/2)*lengthFraction;
        float width = (windowWidth/tileSize - windowWidth/tileSize/2)*lengthFraction;
        if (height > width){
            length = width;
        } else{
            length = height;
        }
    }

    private void fetchDirection(){
        GameModel gameModel = GameModel.getInstance();
        direction = gameModel.getPlayer().getAimingController().getDirection() + Constants.PI/2;
    }
    private void fetchPlayerPosition(){
        GameModel gameModel = GameModel.getInstance();
        playerPosition.set(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
    }
    private void initializeRays(){
        rays = new ZWVector[numberOfRays];
    }
    private void calculateEndPoints(){
        for(int i = 0; i<numberOfRays; i++) {
            rays[i] = new ZWVector(1, 1);
            rays[i].setLength(length);
            rays[i].setAngleRad(direction - width / 2 + i * width / numberOfRays);
            ZWVector end = new ZWVector(rays[i]);
            end.add(playerPosition);
            endPoints.add(lengthenRay(playerPosition,end,0.4f));
            int x = 0;
        }
    }

    private void lengthenRays(){
        for(ZWVector v : rays) {
            v.set(lengthenRay(playerPosition, v, 0.4f));
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
        for (ZWVector ray : rays) {
            currentFraction = 1337;
            foundFixture = false;
            rayCast(ray);
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
               // endPoints.remove(lengthenRay(playerPosition,temp,0.4f));
                endPoints.add(tempIndex, lengthenRay(playerPosition,collisionPoint,0.4f));
            }

        }
        endPoints.add(playerPosition);
        collisionPoints.clear();
    }

    private void rayCast(ZWVector ray) {
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
        maxYIndex = -1;
        for(int i = 0; i<endPoints.size(); i++){
            float currentY = endPoints.get(i).getY();
            if(currentY>maxY){
                maxY = currentY;
                maxYIndex = i;
            }
        }
    }
    private void calculateCorners(){
        float windowWidth = ZWGameEngine.getWindowWidth();
        float windowHeight = ZWGameEngine.getWindowHeight();
        int tileSize = Constants.TILE_SIZE;
        corners[0] = playerPosition.getX() - windowWidth/(tileSize*2);
        corners[1] = windowHeight/tileSize + playerPosition.getY() - windowHeight/(tileSize*2);       //Top left
        corners[2] = playerPosition.getX() - windowWidth/(tileSize*2);
        corners[3] = playerPosition.getY() - windowHeight/(tileSize*2);                          //Bottom left
        corners[4] = windowWidth/tileSize+playerPosition.getX() - windowWidth/(tileSize*2);
                corners[5] = playerPosition.getY() - windowHeight/(tileSize*2);           //Bottom right
                corners[6] = windowWidth/tileSize +playerPosition.getX() - windowWidth/(tileSize*2);
                corners[7] = windowHeight/tileSize + playerPosition.getY() - windowHeight / (tileSize*2); //Top right

            }
            private float[] createArrayOfVertices(){
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
    private ZWPolygonRegion createDarkRegion(){
        float[] vertices = createArrayOfVertices();
        vertices = scaleVertices(vertices);
        short[] triangles = calculateTriangles(vertices);
        //Punkterna som har räknats ut är i spelvärldens format. Vet inte om det alltid har varit så. Men då
        //Trianglarna stämmer i alla fall.
        ZWPolygonRegion darkness = new ZWPolygonRegion(new ZWTextureRegion(darkTexture), vertices, triangles);
        return darkness;
    }

    private float[] scaleVertices(float[] vertices){
        for(int i = 0; i < vertices.length; i= i+ 2) {
            vertices[i] = vertices[i] * Constants.TILE_SIZE - playerPosition.getX() * Constants.TILE_SIZE + ZWGameEngine.getWindowWidth()/2;
            vertices[i+1] = vertices[i+1] * Constants.TILE_SIZE - playerPosition.getY() * Constants.TILE_SIZE + ZWGameEngine.getWindowHeight()/2;
        }
        return vertices;
    }
    private ZWRayCastCallback createCallback(){
        ZWRayCastCallback returnCallback = new ZWRayCastCallback() {
            @Override
            public float reportRayFixture(ZWFixture fixture, ZWVector point, ZWVector normal, float fraction) {
                if (fixture.getCategoryBits()== Constants.COLLISION_OBSTACLE ||
                        fixture.getCategoryBits() == Constants.COLLISION_ZOMBIE){
                    if (fraction < currentFraction) {
                        currentFraction = fraction;
                        collisionPoint.set(point);
                    }
                    foundFixture = true;
                }
                return 1;
            }
        };
        return returnCallback;
    }
}
