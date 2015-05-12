package edu.chalmers.zombie.adapter;

import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.Direction;

/**
 * Created by daniel on 5/12/2015.
 */
public class Hand {
    private float direction = 0;
    private Thread aimRight = new Thread();
    private Thread aimLeft = new Thread();

    public Hand(){}
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

}
