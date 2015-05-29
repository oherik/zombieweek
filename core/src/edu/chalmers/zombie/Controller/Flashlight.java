package edu.chalmers.zombie.controller;

/**
 * Created by daniel on 5/19/2015.
 */
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ShortArray;
import com.sun.istack.internal.NotNull;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import java.util.ArrayList;

public class Flashlight {
    private World world;
    private ZWTexture darkTexture = new ZWTexture("core/assets/darkness.png");
    private ZWTexture lightTexture = new ZWTexture("core/assets/light.png");
    private Vector2 playerPosition = new Vector2();
    Vector2 collisionPoint = new Vector2();
    private float currentFraction = 1337;
    private boolean foundFixture;
    private ArrayList<Float> collisionPoints = new ArrayList<Float>();
    private float direction;
    private RayCastCallback callback = createCallback();
    private float width;
    private int numberOfRays;
    private float length;
    private Vector2[] rays;
    private ArrayList<Vector2> endPoints = new ArrayList<Vector2>();
    private int maxYIndex;
    private float[] corners = new float[8];
    private float lengthFraction;

    public Flashlight(@NotNull World world) throws NullPointerException{
        if (world == null){
            throw new NullPointerException("The world is null");
        }
        this.world = world;
        width = Constants.PI/4;
        numberOfRays = 100;
        lengthFraction = 0.75f;
        initializeRays();
    }
    public Flashlight(@NotNull World world, float width, int numberOfRays, float length, float lengthFraction)throws NullPointerException, IllegalArgumentException{
        if (world == null){
            throw new NullPointerException("The world is null");
        }
        this.world = world;
        this.width = width;
        this.length = length;
        this.numberOfRays = numberOfRays;
        if (lengthFraction < 0.05f || 0.95f > lengthFraction){
            throw new IllegalArgumentException("The lengthFraction has to be between 0.05 and 0.95");
        }
        this.lengthFraction = lengthFraction;
        initializeRays();
    }
    public void draw(PolygonSpriteBatch psb, SpriteBatch sb){
        world = GameModel.getInstance().getRoom().getWorld();
        clearAll();
        calculateLength();
        fetchDirection();
        fetchPlayerPosition();
        calculateEndPoints();
        calculateCollisionPoints();
       // lengthenRays();
        calculateMaxYIndex();
        calculateCorners();
        PolygonRegion darkness = createDarkRegion();
        Sprite light = createLight();
        light.draw(sb);
        psb.draw(darkness, 0, 0);
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
        direction = gameModel.getPlayer().getHand().getDirection() + Constants.PI/2;
    }
    private void fetchPlayerPosition(){
        GameModel gameModel = GameModel.getInstance();
        playerPosition.set(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
    }
    private void initializeRays(){
        rays = new Vector2[numberOfRays];
    }
    private void calculateEndPoints(){
        for(int i = 0; i<numberOfRays; i++) {
            rays[i] = new Vector2(1, 1);
            rays[i].setLength(length);
            rays[i].setAngleRad(direction - width / 2 + i * width / numberOfRays);
            Vector2 end = new Vector2(rays[i]);
            end.add(playerPosition);
            endPoints.add(lengthenRay(playerPosition,end,0.4f));
            int x = 0;
        }
    }

    private void lengthenRays(){
        for(Vector2 v : rays) {
            v.set(lengthenRay(playerPosition, v, 0.4f));
        }
    }

    private Vector2 lengthenRay(Vector2 origin, Vector2 end,float dist){
        float dy = origin.y-end.y;
        float dx = origin.x-end.x;
        float dl= (float)(Math.sqrt(dy*dy+dx*dx));
        float y = end.y - dy*dist/dl;
        float x = end.x - dx*dist/dl;
        return new Vector2(x,y);
    }
    private void calculateCollisionPoints(){
        for (Vector2 ray : rays) {
            currentFraction = 1337;
            foundFixture = false;
            rayCast(ray);
            if (foundFixture) {
                Vector2 temp = new Vector2(ray);
                temp.add(playerPosition);
                int tempIndex = endPoints.indexOf(lengthenRay(playerPosition,temp,0.4f));
                endPoints.remove(lengthenRay(playerPosition,temp,0.4f));
                endPoints.add(tempIndex, lengthenRay(playerPosition,collisionPoint,0.4f));
            }

        }
        endPoints.add(playerPosition);
        collisionPoints.clear();
    }

    private void rayCast(Vector2 ray) {
        world.rayCast(callback, playerPosition, sum(ray, playerPosition));
    }
    private Vector2 sum(Vector2 v1, Vector2 v2){
        Vector2 tmpVector1 = new Vector2();
        Vector2 tmpVector2 = new Vector2();
        tmpVector1.set(v1);
        tmpVector2.set(v2);
        Vector2 returnVector = tmpVector1.add(tmpVector2);
        return returnVector;
    }
    private void calculateMaxYIndex(){
        float maxY = 0;
        maxYIndex = -1;
        for(int i = 0; i<endPoints.size(); i++){
            float currentY = endPoints.get(i).y;
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
        corners[0] = playerPosition.x - windowWidth/(tileSize*2);
        corners[1] = windowHeight/tileSize + playerPosition.y - windowHeight/(tileSize*2);       //Top left
        corners[2] = playerPosition.x - windowWidth/(tileSize*2);
        corners[3] = playerPosition.y - windowHeight/(tileSize*2);                          //Bottom left
        corners[4] = windowWidth/tileSize+playerPosition.x - windowWidth/(tileSize*2);
        corners[5] = playerPosition.y - windowHeight/(tileSize*2);           //Bottom right
        corners[6] = windowWidth/tileSize +playerPosition.x - windowWidth/(tileSize*2);
        corners[7] = windowHeight/tileSize + playerPosition.y - windowHeight / (tileSize*2); //Top right

    }
    private float[] createArrayOfVertices(){
        collisionPoints.add(corners[0]);
        collisionPoints.add(corners[1]);
        for(int i = maxYIndex; i >= 0; i--) {
            collisionPoints.add(endPoints.get(i).x);
            collisionPoints.add(endPoints.get(i).y);
        }
        for(int i =endPoints.size()-1; i >= maxYIndex; i--) {
            collisionPoints.add(endPoints.get(i).x);
            collisionPoints.add(endPoints.get(i).y);
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
        EarClippingTriangulator ecp = new EarClippingTriangulator();
        short[] returnTriangles;
        ShortArray s = ecp.computeTriangles(vertices);
        returnTriangles = s.toArray();
        return returnTriangles;
    }
    private PolygonRegion createDarkRegion(){
        float[] vertices = createArrayOfVertices();
        short[] triangles = calculateTriangles(vertices);
        PolygonRegion darkness = new PolygonRegion(new TextureRegion(darkTexture), vertices, triangles);
        return darkness;
    }
    private Sprite createLight(){
        Sprite light = new Sprite(lightTexture);
        light.setAlpha(0.2f);
        return light;
    }
    private RayCastCallback createCallback(){
        RayCastCallback returnCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits ==
                        Constants.COLLISION_OBSTACLE || fixture.getFilterData().categoryBits == Constants.COLLISION_ZOMBIE){
                    if (fraction < currentFraction) {
                        currentFraction = fraction;
                        collisionPoint.set(point);
                    }
                    foundFixture =  true;
                }
                return 1;
            }
        };
        return returnCallback;
    }
}
