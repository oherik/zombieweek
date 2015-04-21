package edu.chalmers.zombie.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by daniel on 4/21/2015.
 */
public class Book extends Sprite{
    private float velocity = 100;
    private Direction direction;
    private Body bookBody;
    private float x;
    private float y;

    public Book(Direction d, float x, float y, World world){
        super(new Sprite(new Texture("core/assets/bookSprite.png")));
        this.x = x;
        this.y = y;
        setX(x);
        setY(y);
        GameModel gameModel = GameModel.getInstance();
        this.direction = d;

        BodyDef bookBodyDef = new BodyDef();
        bookBodyDef.type = BodyDef.BodyType.DynamicBody;
        bookBodyDef.position.set(x, y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(gameModel.getPlayer().getHeight() / 2, gameModel.getPlayer().getWidth() / 2);

        FixtureDef bookFixDef = new FixtureDef();
        bookFixDef.shape = shape;
        bookFixDef.density = 50;
        bookFixDef.restitution = 0;
        bookFixDef.friction = 1;

        bookBody = world.createBody(bookBodyDef);
        bookBody.createFixture(bookFixDef);
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

}
