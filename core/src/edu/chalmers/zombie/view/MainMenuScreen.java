package edu.chalmers.zombie.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import edu.chalmers.zombie.controller.MapController;
import edu.chalmers.zombie.controller.SaveLoadController;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

import javax.xml.soap.Text;

/**
 * The main menu screen of the game
 *
 * Created by Tobias on 15-05-10.
 */
public class MainMenuScreen implements Screen {

    private Stage mainStage;
    private Stage levelStage;
    private Stage settingsStage;
    private Skin skin;
    private enum MenuState{MAIN_MENU, LEVEL_MENU, SETTINGS_MENU}
    private MenuState menuState;

    @Override
    public void show() {
        SaveLoadController saveLoadController = new SaveLoadController(); //TODO: should not be instantiated here, but loads saved game to gameModel and therefore needed

        //Look of buttons
        skin = createMenuSkin();

        mainStage = new Stage();
        levelStage = new Stage();
        settingsStage = new Stage();

        //Sets up the different stages for the menus
        setUpMainStage();
        setUpLevelStage();
        setUpSettingsStage();

        System.out.println("--- MAIN MENU ---");
        menuState = MenuState.MAIN_MENU;
        Gdx.input.setInputProcessor(mainStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); //white background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (menuState){
            case MAIN_MENU:
                mainStage.act(); //draws main menu
                mainStage.draw();
                break;
            case LEVEL_MENU:
                levelStage.act(); //draws level menu
                levelStage.draw();
                break;
            case SETTINGS_MENU:
                settingsStage.act(); //draws settings menu
                settingsStage.draw();
                break;
        }
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
        textButtonStyle.checked = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        return skin;
    }


    /**
     * Sets up the stage for the main menu
     */
    private void setUpMainStage(){

        Table table = new Table();

        TextButton newGameButton = new TextButton("New game", skin);
        final TextButton levelButton = new TextButton("Choose level", skin);
        TextButton exitGameButton = new TextButton("Exit game", skin);

        table.add(newGameButton).size(250,50).padBottom(15).row();
        table.add(levelButton).size(250,50).padBottom(15).row();
        table.add(exitGameButton).size(250,50).padBottom(15).row();

        table.setFillParent(true);
        mainStage.addActor(table);


        newGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MapController mapController = new MapController();
                World currentWorld = mapController.getWorld(); //gets world from singleton gameModel
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(currentWorld, Constants.TILE_SIZE));
            }
        });

        levelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuState = MenuState.LEVEL_MENU;
                Gdx.input.setInputProcessor(levelStage); //level menu is input processor
                levelButton.toggle();
            }
        });

        exitGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

    }

    /**
     * Sets up the stage for the level menu
     */
    private void setUpLevelStage(){
        Table table = new Table();
        table.setFillParent(true);
        levelStage.addActor(table);

        int levelsCompleted = GameModel.getInstance().getHighestCompletedLevel();

        for (int i = 0;i <= levelsCompleted;i++){
            String buttonName = "Level " + (i+1);
            final int level = i;
            TextButton levelButton = new TextButton(buttonName, skin);
            table.add(levelButton).size(250,50).padBottom(15).row();

            levelButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    MapController mapController = new MapController();
                    World world = mapController.getLevel(level).getWorld();
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(world, Constants.TILE_SIZE));
                }
            });
        }


        /*--- Back button ---*/
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();

        imageButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("core/assets/arrowLeft_grey.png")));
        imageButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("core/assets/arrowLeft.png")));
        imageButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture("core/assets/arrowLeft_lightgrey.png")));

        ImageButton backIconButton = new ImageButton(imageButtonStyle);
        backIconButton.setSize(30,30);
        backIconButton.setPosition(10, Gdx.graphics.getHeight()-40);

        levelStage.addActor(backIconButton);

        backIconButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuState = MenuState.MAIN_MENU;
                Gdx.input.setInputProcessor(mainStage);
            }
        });

    }

    /**
     * Sets up the stage for the settings menu
     */
    private void setUpSettingsStage(){
        Table table = new Table();

        //TODO: Add settings stuff, eg switches for controls/sound etc

        table.setFillParent(true);
        settingsStage.addActor(table);

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
