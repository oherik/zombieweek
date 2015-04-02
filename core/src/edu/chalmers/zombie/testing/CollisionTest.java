package edu.chalmers.zombie.testing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import edu.chalmers.zombie.controller.InputController;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class CollisionTest extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private TiledMap tiledMap;
    private TiledMapTileLayer meta;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private CollisionTestLevel testLevel;
    private float tileSize;

    private PlayerTest player;

    private InputController inputController;

    private PathAlgorithm pathFinding;
    private Point start, end, startTile, endTile;
    private Iterator<Point> path;
    private Pixmap pixmap;
    private Texture pathTexture;
    private Sprite pathSprite;

    private String collisionString;

    private int layerWidth, layerHeight;

    @Override
    public void create () {
    //Ladda testkarta

        testLevel   = new CollisionTestLevel();
        tiledMap    = testLevel.getMap();
        meta        = testLevel.getMeta();
        tileSize    = testLevel.getTileSize();
        collisionString = testLevel.getCollisionString();
        layerHeight = meta.getHeight();
        layerWidth = meta.getWidth();
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);

        //Fixa kameran
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);

        batch = new SpriteBatch();
        img = new Texture("core/assets/zombie.png");

        //Skapa spelare
        player = new PlayerTest(new Sprite(new Texture("core/assets/player_professional_final_version.png")));

        //Set input controller
        inputController = new InputController();
        Gdx.input.setInputProcessor(inputController);

        //Skapa path finding
        pathFinding = new PathAlgorithm(meta, collisionString);
        start       = new Point(0,0);
        end         = new Point(288, 320);
        startTile   = testLevel.coordinatesToTile(start);
        endTile     = testLevel.coordinatesToTile(end);
        path = pathFinding.getPath(startTile, endTile);

        //Rita ut pathen
        pixmap = new Pixmap(Math.round(layerWidth*tileSize) ,Math.round(layerHeight*tileSize), Pixmap.Format.RGBA8888);
        pixmap.setColor(com.badlogic.gdx.graphics.Color.RED);
        Point oldP = start;
        Point p = oldP;
        if(path == null)
            System.out.println("Ingen path hittad");
        else {
            System.out.println("Path:");
            while (path.hasNext()) {
                Point tile = path.next();
                System.out.print(tile.x + " " + tile.y);
                p = testLevel.tileToCoordinates(tile);
                System.out.print("    = " + p.x + " " + p.y + "\n");
                pixmap.drawLine(oldP.x + Math.round(tileSize / 2), Math.round(layerHeight * tileSize - oldP.y) - Math.round(tileSize / 2),
                        p.x + Math.round(tileSize / 2), Math.round(layerHeight*tileSize-p.y) - Math.round(tileSize / 2));
                oldP = p;
            }
            pixmap.drawLine(oldP.x + Math.round(tileSize / 2), Math.round(layerHeight*tileSize-oldP.y) - Math.round(tileSize / 2),
                    end.x + Math.round(tileSize / 2), Math.round(layerHeight*tileSize-end.y) - Math.round(tileSize / 2));
        }
        pathTexture = new Texture(pixmap);
        pixmap.dispose();
        pathSprite = new Sprite(pathTexture);

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//Rita kartan
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        batch.draw(player,start.x,start.y);
        batch.draw(player,end.x,end.y);
        pathSprite.setPosition(0, 0);
        pathSprite.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

        //Koden nedan kan man ha om man vill att spelv�rlden ut�kas n�r man resizar
        camera.setToOrtho(false, width / tileSize, height / tileSize);
        //Koden nedan om man vill ha statiskt.
        //camera.setToOrtho(false, 400 / 32, 400 / 32);




    }
}
