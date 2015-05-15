package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * //TODO borde kanske vara en nästlad klass inne i Player? /Erik
 * Created by daniel on 5/12/2015.
 */
public class Hand {
    private float direction = 0;
    private Thread aimRight = new Thread();
    private Thread aimLeft = new Thread();
    private Sprite aimer = new Sprite(new Texture("core/assets/aimer.png"));
    private Player player;

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
    public void startAimingRight() {
        aimLeft.stop();
        aimRight = new Thread(){
            public void run()  {
                while (true){
                    direction = (float)(direction - 0.1);
                    try{
                        this.sleep(50);
                    } catch (InterruptedException e){

                    }

                }
            }
        };
        aimRight.start();
    }
    public void startAimingLeft(){
        aimRight.stop();
        aimLeft = new Thread(){
            public void run()  {
                while (true){
                    direction = (float)(direction + 0.1);
                    try{
                        this.sleep(50);
                    } catch (InterruptedException e){

                    }

                }
            }
        };
        aimLeft.start();
    }
    public void stopAiming(){
        aimRight.stop();
        aimLeft.stop();
    }
    public void drawAimer(Batch batch){
        aimer.setPosition(player.getX() - 0.25f + (float)(Math.cos(direction + Constants.PI/2)), player.getY() - 0.25f
                + (float)(Math.sin(direction + Constants.PI/2)));
        aimer.setOriginCenter();
        aimer.setRotation((float) (direction * 57.2957795));
        aimer.draw(batch);
    }
    public Sprite getAimer(){
        return aimer;
    }

    public float getDirection(){
        return direction;
    }
}
