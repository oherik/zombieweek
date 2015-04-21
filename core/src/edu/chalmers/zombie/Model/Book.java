package edu.chalmers.zombie.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by daniel on 4/21/2015.
 */
public class Book extends Sprite{
    private final int VELOCITY = 10;
        private Direction direction;
        private Body bookBody;
        private float x;
        private float y;
        private Vector2 force = new Vector2(0,0);

        public Book(Direction d, float x, float y, World world) {
            super(new Sprite(new Texture("core/assets/bookSprite.png")));
            this.x = x;
            this.y = y;
            setX(x);
            setY(y);
            GameModel gameModel = GameModel.getInstance();
            //this.direction = d;

            BodyDef bookBodyDef = new BodyDef();
            bookBodyDef.type = BodyDef.BodyType.DynamicBody;
            bookBodyDef.position.set(x + 0.5f, y + 0.5f);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(gameModel.getPlayer().getHeight() / 2, gameModel.getPlayer().getWidth() / 2);
            FixtureDef bookFixDef = new FixtureDef();
            bookFixDef.shape = shape;
            bookFixDef.density = (float) Math.pow(gameModel.getPlayer().getWidth(), gameModel.getPlayer().getHeight());
            bookFixDef.restitution = 0;
            bookFixDef.friction = 1;
            bookBody = world.createBody(bookBodyDef);
            bookBody.createFixture(bookFixDef);
            gameModel.addBook(this);
            setSize(1, 1);
            this.direction = d;
            setInMotion();
        }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    private void updateLocation(float deltaTime){
        setX(getX() + deltaTime * force.x);
        setY(getY() + deltaTime * force.y);
        this.x = getX() + deltaTime * force.x;
        this.y = getY() + deltaTime * force.y;
    }
    public void setInMotion(){
        switch (direction) {
            case NORTH:
                force.y = VELOCITY;
                break;
            case SOUTH:
                force.y = -VELOCITY;
                break;
            case WEST:
                force.x = -VELOCITY;
                break;
            case EAST:
                force.x = VELOCITY;
                break;
            default:
                break;
        }
    }
    public void draw(Batch batch) {
        updateLocation(Gdx.graphics.getDeltaTime());
        //updateLocation((float)0.15);
        super.draw(batch);
        bookBody.setTransform(x+0.5f,y+0.5f,0);
    }
}