package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

/**
 * //TODO borde kanske vara en n�stlad klass inne i Player? /Erik
 * Created by daniel on 5/12/2015.
 */
public class Hand {
    private float direction = 0;
    private Thread aimRight = new Thread();
    private Thread aimLeft = new Thread();
    private Sprite aimer = new Sprite(new Texture("core/assets/aimer.png"));
    private Player player;
    private boolean mouseAiming = false;
    private boolean throwingGrenade = false;
    private int mouseX;
    private int mouseY;

    public Hand(Player player){
        aimer.setSize(0.5f,0.5f);
        this.player = player;
    }
    public void throwBook(){
        GameModel gameModel = GameModel.getInstance();
        Player player = gameModel.getPlayer();
        Book book = new Book(direction, player.getX() - 0.5f, player.getY() - 0.5f, player.getWorld(), player.getVelocity());
        gameModel.addBook(book);
    }
    public void throwGrenade(){
        GameModel gameModel = GameModel.getInstance();
        Player player = gameModel.getPlayer();
        Grenade grenade = new Grenade(mouseX, mouseY, player.getX() - 0.5f, player.getY() - 0.5f, player.getWorld());
        gameModel.addGrenade(grenade);
    }
    public void startAimingRight() {
        if (!mouseAiming) {
            aimLeft.stop();
            aimRight = new Thread() {
                public void run() {
                    while (true) {
                        direction = (float) (direction - 0.1);
                        try {
                            this.sleep(50);
                        } catch (InterruptedException e) {

                        }

                    }
                }
            };
            aimRight.start();

        }
    }
    public void startAimingLeft(){
        if (!mouseAiming) {
            aimRight.stop();
            aimLeft = new Thread() {
                public void run() {
                    while (true) {
                        direction = (float) (direction + 0.1);
                        try {
                            this.sleep(50);
                        } catch (InterruptedException e) {

                        }

                    }
                }
            };
            aimLeft.start();
        }
    }
    public void stopAiming(){
        aimRight.stop();
        aimLeft.stop();
    }
    public void drawAimer(Batch batch){
        if (!throwingGrenade){
            aimer.setPosition(player.getX() - 0.25f + (float)(Math.cos(direction + Constants.PI/2)), player.getY() - 0.25f
                    + (float)(Math.sin(direction + Constants.PI/2)));
            aimer.setOriginCenter();
            aimer.setRotation((float) (direction * 57.2957795));
            aimer.draw(batch);
        }

    }
    public void drawGrenadeAimer(ShapeRenderer shapeRenderer){
        if (throwingGrenade){
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.line(mouseX - 10, mouseY -10, mouseX +10, mouseY+10);
            shapeRenderer.line(mouseX - 10, mouseY + 10, mouseX + 10, mouseY -10);
            shapeRenderer.circle(mouseX, mouseY, 20);
        }
    }
    public Sprite getAimer(){
        return aimer;
    }

    public float getDirection(){
        return direction;
    }

    public void toggleMouseAiming(){
        stopAiming();
        mouseAiming = !mouseAiming;
    }

    public void setMousePosition(int x, int y){
        mouseX = x;
        mouseY = Gdx.graphics.getHeight() - y;
        if (mouseAiming) {
            float deltaX = Gdx.graphics.getWidth() / 2 - x;
            float deltaY = y - Gdx.graphics.getHeight() / 2;
            direction = (float) Math.atan2((double) deltaY, (double) deltaX) + Constants.PI / 2;
        }
    }

    public void toggleGrenadeThrowing(){
        throwingGrenade = !throwingGrenade;
    }

    public boolean isThrowingGrenade(){
        return throwingGrenade;
    }
}
