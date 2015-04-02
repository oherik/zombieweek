package edu.chalmers.zombie.model;

/**
 * Created by Erik on 2015-04-01.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import edu.chalmers.zombie.PlayerTest;
import com.badlogic.gdx.graphics.Color;
import edu.chalmers.zombie.utils.PathAlgorithm;

import java.awt.*;
import java.util.Iterator;

public class PathTest extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private float tileSize = 32;
    private PathAlgorithm algo;
    private PlayerTest player;
    private Sprite dot,pathSprite;
    private Iterator<Point> path;
    private ShapeRenderer shapeDebugger;
    private Point start, end;
    private Pixmap pixmap;
    private int map;
    private Texture pathTexture;


    public PathTest(int i){
       map = i;
    }



    @Override
    public void create () {
        if(map==1)
            tiledMap =new TmxMapLoader().load("core/assets/Map/Test_path.tmx");
        else  if(map==2)
            tiledMap =new TmxMapLoader().load("core/assets/Map/Test_path_2.tmx");
        else
            tiledMap =new TmxMapLoader().load("core/assets/Map/Test_path_3.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);

        //Fixa kameran
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(width, height);

        batch = new SpriteBatch();
        img = new Texture("core/assets/zombie.png");
        dot = new Sprite(new Texture("core/assets/dot.png"));


        //Skapa spelare
        player = new PlayerTest(new Sprite(new Texture("core/assets/player_professional_final_version.png")));

        algo = new PathAlgorithm( (TiledMapTileLayer) tiledMap.getLayers().get("Grass"));

        start = new Point(1,2);
        end = new Point(5, 2);
        path = algo.getPath(start, end);
        shapeDebugger=new ShapeRenderer();



        pixmap = new Pixmap(7*32 ,5*32, Pixmap.Format.RGBA8888);


        pixmap.setColor(Color.RED);
        Point oldP = start;
        Point p = oldP;
        while(path.hasNext()){
            p = path.next();
            pixmap.drawLine(32*oldP.x+16, 5*32-32*oldP.y-16, 32*p.x+16, 5*32-32*p.y-16);
            oldP = p;
        }
        pixmap.drawLine(32*oldP.x+16, 5*32-32*oldP.y-16, 32*end.x+16, 5*32-32*end.y-16);



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
        pathSprite.setPosition(0, 0);
        pathSprite.draw(batch);
        pathSprite.draw(batch);
        batch.draw(player,32,64);
        batch.draw(player,32*5,64);


        batch.end();

        //Rita spelare
       // player.setScale(1/tileSize);
     //   mapRenderer.getBatch().begin();
    //    player.draw(mapRenderer.getBatch());
    //    mapRenderer.getBatch().end();
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
