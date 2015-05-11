package edu.chalmers.zombie.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.utils.Constants;

/**
 * The main menu screen of the game
 *
 * Created by Tobias on 15-05-10.
 */
public class MainMenuScreen implements Screen {

    Stage stage;
    TextButton newGameButton;
    TextButton exitGameButton;

    @Override
    public void show() {
        System.out.println("show");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = createMenuSkin();

        newGameButton = new TextButton("New game", skin);
        exitGameButton = new TextButton("Exit game", skin);
        newGameButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/2 + 30);
        exitGameButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/2 - 30);
        stage.addActor(newGameButton);
        stage.addActor(exitGameButton);


        newGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MapController mapController = new MapController();
                World currentWorld = mapController.getWorld(); //gets world from singleton gameModel
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(currentWorld, Constants.TILE_SIZE));
            }
        });

        exitGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); //white background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(); //draws the buttons
        stage.draw();
    }


    /**
     * Creates a skin for the menu buttons
     * @return Skin The skin created
     */
    private Skin createMenuSkin(){

        Skin skin = new Skin();

        BitmapFont font = new BitmapFont(); //sets font to 15pt Arial, if we want custom font -> via constructor
        skin.add("default", font);

        //Creating a button texture
        Pixmap pixmap = new Pixmap((int)(Gdx.graphics.getWidth()/4),(int)(Gdx.graphics.getHeight()/10), Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE); //button color
        pixmap.fill();

        //Adding background to button
        skin.add("background",new Texture(pixmap));

        //Create buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        return skin;
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
