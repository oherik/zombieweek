package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.AimingSystem;
import edu.chalmers.zombie.model.Book;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.actors.Player;
import edu.chalmers.zombie.utils.Constants;

/**
 * //TODO borde kanske vara en n�stlad klass inne i Player? /Erik
 * Created by daniel on 5/12/2015.
 */
public class AimingController {
    private AimingSystem aimingSystem;
    private Thread aimRight = new Thread();
    private Thread aimLeft = new Thread();

    public AimingController(Player player){
        aimingSystem = new AimingSystem(player);
    }


    public void startAimingRight() {
        if (!aimingSystem.isMouseAiming()) {
            aimLeft.stop();
            aimRight = new Thread() {
                public void run() {
                    while (true) {
                        aimingSystem.setDirection((float) (aimingSystem.getDirection() - 0.1));
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
        if (!aimingSystem.isMouseAiming()) {
            aimRight.stop();
            aimLeft = new Thread() {
                public void run() {
                    while (true) {
                        aimingSystem.setDirection((float) (aimingSystem.getDirection() + 0.1));
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
    public void drawAimer(ZWBatch batch){
        ZWSprite aimer = aimingSystem.getSprite();
        float direction = aimingSystem.getDirection();
        Player player = aimingSystem.getPlayer();
        if (!aimingSystem.isThrowingGrenade()){
            aimer.setPosition(player.getX() - 0.25f + (float) (Math.cos(direction + Constants.PI / 2)), player.getY() - 0.25f
                    + (float) (Math.sin(direction + Constants.PI / 2)));
            aimer.setOriginCenter();
            aimer.setRotation((float) (direction * 57.2957795));
            aimer.draw(batch);
        }

    }
    public void drawGrenadeAimer(ZWShapeRenderer shapeRenderer){
        if (aimingSystem.isThrowingGrenade()){
            float mouseX = aimingSystem.getMouseX();
            float mouseY = aimingSystem.getMouseY();
            shapeRenderer.setColor(ZWShapeRenderer.Color.GREEN);
            shapeRenderer.line(mouseX - 10, mouseY -10, mouseX +10, mouseY+10);
            shapeRenderer.line(mouseX - 10, mouseY + 10, mouseX + 10, mouseY -10);
            shapeRenderer.circle(mouseX, mouseY, 20);
        }
    }
    public void toggleMouseAiming(){
        stopAiming();
        aimingSystem.toggleMouseAiming();
    }

    public void setMousePosition(int x, int y){
        aimingSystem.setMousePosition(x, y);
    }
    public void toggleGrenadeThrowing(){
        aimingSystem.toggleGrenadeThrowing();
    }
    public float getDirection(){
        return aimingSystem.getDirection();
    }
    public boolean isThrowingGrenade(){
        return aimingSystem.isThrowingGrenade();
    }
    public AimingSystem getAimingSystem(){
        return aimingSystem;
    }
}
