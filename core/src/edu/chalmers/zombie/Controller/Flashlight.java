package edu.chalmers.zombie.controller;

/**
 * Created by daniel on 5/19/2015.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ShortArray;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import java.util.ArrayList;

public class Flashlight {
    private World world;
    private Texture darkTexture = new Texture("core/assets/darkness.png");
    private Texture lightTexture = new Texture("core/assets/light.png");
    private Vector2 playerPosition = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    Vector2 collisionPoint = new Vector2();
    private float currentFraction = 1337;
    private boolean foundFixture;
    private ArrayList<Float> collisionPoints = new ArrayList<Float>();
    private ArrayList<Float> corners = new ArrayList<Float>();
    private float direction;
    private RayCastCallback callback = createCallback();

    public Flashlight(World world){
        this.world = world;
    }

    private void fetchDirection(){
        GameModel gameModel = GameModel.getInstance();
        direction = gameModel.getPlayer().getHand().getDirection() + Constants.PI/2;
    }
    private void fetchPlayerPosition(){
        GameModel gameModel = GameModel.getInstance();
        playerPosition.set(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
    }

    public void draw(PolygonSpriteBatch psb, SpriteBatch sb){
        GameModel gameModel = GameModel.getInstance();
        fetchDirection();
        fetchPlayerPosition();
        float coneWidth = Constants.PI/4;
        int numberOfRays = 100;
        int coneLength = 8;
        Vector2[] rays = new Vector2[numberOfRays];
        Vector2 playerPosition =  new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY());
        ArrayList<Vector2> endPoints = new ArrayList<Vector2>();
        for(int i = 0; i<numberOfRays; i++) {
            rays[i] = new Vector2(1, 1);
            rays[i].setLength(coneLength);
            rays[i].setAngleRad(direction - coneWidth / 2 + i * coneWidth / numberOfRays);
            Vector2 end = new Vector2(rays[i]);
            end.add(playerPosition);
            endPoints.add(end);
            int x = 0;
        }
        for (Vector2 line : rays) {
            currentFraction = 1337;
            foundFixture = false;
            world.rayCast(callback, new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY()),
                    new Vector2(line.x + gameModel.getPlayer().getX(), line.y + gameModel.getPlayer().getY()));
            if (foundFixture) {
                Vector2 temp = new Vector2(line);
                temp.add(playerPosition);
                int tempIndex = endPoints.indexOf(temp);
                endPoints.remove(temp);
                endPoints.add(tempIndex, new Vector2(collisionPoint));
            }

        }
        collisionPoints.clear();
        endPoints.add(new Vector2(gameModel.getPlayer().getX(), gameModel.getPlayer().getY()));
        float maxY = 0;
        int maxYIndex = -1;
        for(int i = 0; i<endPoints.size(); i++){
            float currentY = endPoints.get(i).y;
            if(currentY>maxY){
                maxY = currentY;
                maxYIndex = i;
            }
        }

        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();

        float[] corners = new float[]{
                playerPosition.x - windowWidth/64, windowHeight/32 + playerPosition.y - windowHeight/64,        //Top left
                playerPosition.x - windowWidth/64, playerPosition.y - windowHeight/64,                           //Bottom left
                windowWidth/32+playerPosition.x - windowWidth/64, playerPosition.y - windowHeight/64,           //Bottom right
                windowWidth/32 +playerPosition.x - windowWidth/64, windowHeight/32 + playerPosition.y - windowHeight / 64 //Top right
        };

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
        float[] collisionPointsArray = convertToArray(collisionPoints);

        EarClippingTriangulator ecp = new EarClippingTriangulator();

        float[] region1 = new float[collisionPointsArray.length];

        for(int i = 0; i < region1.length; i++)
            region1[i] = collisionPointsArray[i];


        ShortArray s = ecp.computeTriangles(region1);
        PolygonRegion darkness = new PolygonRegion(new TextureRegion(darkTexture), region1, s.toArray());
        psb.draw(darkness,0,0);
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

    private RayCastCallback createCallback(){
        RayCastCallback returnCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits ==
                        Constants.COLLISION_OBSTACLE){
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
