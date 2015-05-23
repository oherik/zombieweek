package edu.chalmers.zombie.controller;

/**
 * Created by daniel on 5/19/2015.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ShortArray;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

import java.util.ArrayList;

public class Flashlight {
    private Vector2 playerPosition = new Vector2();
    private Vector2 windowSize = new Vector2();
    private ArrayList<Float> vertices = new ArrayList<Float>();
    private World world;
    private Texture darkness = new Texture("core/assets/darkness.png");
    private Texture light = new Texture("core/assets/light.png");
    private boolean foundFixture;
    private float currentFraction = 1337;
    private Vector2 collisionPoint = new Vector2();
    private RayCastCallback callback = createCallback();
    private float width;
    private float length;
    private int precision;
    private Vector2[] rays;
    private ArrayList<Vector2> endPoints = new ArrayList<Vector2>();
    private Sprite lightRegion;
    private TextureRegion darkRegion;


    public Flashlight(World world) {
        this.world = world;
        width = Constants.PI/4;
        length = 8;
        precision = 20;
        initializeRays();
        initializeImages();
    }

    public void draw(PolygonSpriteBatch polygonSpriteBatch, SpriteBatch spriteBatch){
        clearAll();
        fetchAllInfo();
        lightRegion.setAlpha(0.15f);
        lightRegion.draw(spriteBatch);
        PolygonRegion darkPolygonRegion = createPolygonRegion();
        polygonSpriteBatch.draw(createPolygonRegion(), 0, 0);
    }

    private void clearAll(){
        vertices.clear();
        endPoints.clear();
    }

    private void fetchAllInfo(){
        fetchWindowSize();
        fetchStandardVertices();
        calculateEndPoints();
        calculateCollisionPoints();
    }

    private void fetchStandardVertices(){
        fetchPlayerPosition();
        fetchCorners();

    }

    private void fetchWindowSize(){
        windowSize.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void fetchPlayerPosition(){
        GameModel gameModel = GameModel.getInstance();
        playerPosition =  new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
        vertices.add(playerPosition.x);
        vertices.add(playerPosition.y);
    }

    private void fetchCorners(){
        vertices.add(playerPosition.x - windowSize.x/64);
        vertices.add(windowSize.y/32 + playerPosition.y - windowSize.y/64);        //Top left
        vertices.add(playerPosition.x - windowSize.x/64);
        vertices.add(playerPosition.y - windowSize.y/64);                           //Bottom left
        vertices.add(windowSize.x/32+playerPosition.x - windowSize.x/64);
        vertices.add(playerPosition.y - windowSize.y/64);           //Bottom right
        vertices.add(windowSize.x/32 + playerPosition.x - windowSize.x/64);
        vertices.add(windowSize.y/32 + playerPosition.y - windowSize.y/64); //Top right
    }

    private void calculateEndPoints(){
        for(int i = 0; i<precision; i++){
            rays[i] = new Vector2(1,1);
            rays[i].setLength(length);
            rays[i].setAngleRad(getDirection() - width / 2 + i * width / precision);
            Vector2 end = new Vector2(rays[i]);
            end.add(playerPosition);
            endPoints.add(end);
        }
    }

    private void calculateCollisionPoints(){
        rayCast();
    }

    private float getDirection(){
        GameModel gameModel = GameModel.getInstance();
        return gameModel.getPlayer().getHand().getDirection();
    }

    private void rayCast(){
        for (Vector2 line: rays){
            currentFraction = 1337;
            foundFixture = false;
            world.rayCast(callback, playerPosition, new Vector2(line.x + playerPosition.x, line.y + playerPosition.y));
            if (foundFixture){
                Vector2 temp = new Vector2(line);
                temp.add(playerPosition);
                int tempIndex = endPoints.indexOf(temp);
                endPoints.remove(temp);
                endPoints.add(tempIndex,new Vector2(collisionPoint));
            }
        }
        vertices.addAll(convert(endPoints));
    }

    private ArrayList<Float> convert(ArrayList<Vector2> vectorList){
        ArrayList<Float> returnList = new ArrayList<Float>();
        for(Vector2 v: vectorList){
            returnList.add(v.x);
            returnList.add(v.y);
        }
        return returnList;
    }

    private PolygonRegion createPolygonRegion(){
        EarClippingTriangulator ecp = new EarClippingTriangulator();
        float[] verticesArray = convertToArray(vertices);
        ShortArray s = ecp.computeTriangles(verticesArray);
        short[] triangles = s.toArray();
        PolygonRegion returnPolygonRegion = new PolygonRegion(darkRegion, verticesArray, triangles);
        return returnPolygonRegion;
    }

    private RayCastCallback createCallback(){
        return new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits ==
                        Constants.COLLISION_OBSTACLE){
                    if (fraction < currentFraction) {
                        currentFraction = fraction;
                        collisionPoint.set(point);
                        foundFixture =  true;
                    }
                }
                return 1;
            }
        };
    }

    private void initializeRays(){
        rays = new Vector2[precision];
        for(int i = 0; i<precision; i++){
            rays[i] = new Vector2(1,1);
            rays[i].setLength(length);
            rays[i].setAngleRad(getDirection() - width / 2 + i * width / precision);
            Vector2 end = new Vector2(rays[i]);
            end.add(playerPosition);
            endPoints.add(end);
        }
    }

    private void initializeImages(){
        darkRegion = new TextureRegion(darkness);
        lightRegion = new Sprite(light);
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
}
