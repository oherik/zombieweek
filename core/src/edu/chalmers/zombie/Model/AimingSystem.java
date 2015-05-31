package edu.chalmers.zombie.model;

import edu.chalmers.zombie.adapter.ZWGameEngine;
import edu.chalmers.zombie.adapter.ZWSprite;
import edu.chalmers.zombie.adapter.ZWTexture;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.utils.Constants;

/**
 * Created by daniel on 5/31/2015.
 */
public class AimingSystem {
    private float direction = 0;
    private ZWSprite aimer = new ZWSprite(new ZWTexture("core/assets/Images/aimer.png"));
    private Player player;
    private boolean mouseAiming = false;
    private boolean throwingGrenade = false;
    private int mouseX;
    private int mouseY;
    private Thread aimRight;
    private Thread aimLeft ;
    public AimingSystem(Player player){
        this.player = player;
        aimer.setSize(1f, 1f);
        aimRight= new Thread() {
            public void run() {
                while (true) {
                    setDirection((float) (getDirection() - 0.1));
                    try {
                        this.sleep(50);
                    } catch (InterruptedException e) {

                    }

                }
            }
        };
        aimLeft = new Thread() {
            public void run() {
                while (true) {
                    setDirection((float) (getDirection() + 0.1));
                    try {
                        this.sleep(50);
                    } catch (InterruptedException e) {

                    }

                }
            }
        };
        aimLeft.start();
        aimLeft.suspend();
        aimRight.start();
        aimRight.suspend();
    }
    public void setPlayer(Player player){
        this.player=player;
    }
    public Thread getAimRight(){
        return aimRight;
    }
    public Thread getAimLeft(){
        return aimLeft;
    }
    public float getDirection(){
        return direction;
    }
    public ZWSprite getSprite(){
        return aimer;
    }
    public int getMouseX(){
        return mouseX;
    }
    public int getMouseY(){
        return mouseY;
    }
    public Player getPlayer(){
        return player;
    }
    public boolean isMouseAiming(){
        return mouseAiming;
    }
    public boolean isThrowingGrenade(){
        return throwingGrenade;
    }
    public void setDirection(float direction){
        this.direction = direction;
    }
    public void toggleMouseAiming(){
        mouseAiming = !mouseAiming;
    }
    public void setMousePosition(int x, int y){
        mouseX = x;
        mouseY = (int) ZWGameEngine.getWindowHeight() - y;
        if (mouseAiming) {
            float deltaX = ZWGameEngine.getWindowWidth() / 2 - x;
            float deltaY = y - ZWGameEngine.getWindowHeight() / 2;
            direction = (float) Math.atan2((double) deltaY, (double) deltaX) + Constants.PI / 2;
        }
    }
    public void toggleGrenadeThrowing(){
        throwingGrenade = !throwingGrenade;
    }
}
