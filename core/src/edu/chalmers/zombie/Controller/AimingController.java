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

    public static void startAimingRight() {
        final AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        if (!aimingSystem.isMouseAiming()) {
            Thread aimLeft = aimingSystem.getAimLeft();
            Thread aimRight = aimingSystem.getAimRight();
            aimLeft.suspend();
            aimRight.resume();

        }
    }
    public static void startAimingLeft(){
        final AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        if (!aimingSystem.isMouseAiming()) {
            Thread aimRight = aimingSystem.getAimRight();
            Thread aimLeft = aimingSystem.getAimLeft();
            aimRight.suspend();
            aimLeft.resume();
        }
    }
    public static void stopAiming(){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        Thread aimRight = aimingSystem.getAimRight();
        Thread aimLeft = aimingSystem.getAimLeft();
        aimRight.suspend();
        aimLeft.suspend();
    }
    public static void drawAimer(ZWBatch batch){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        ZWSprite aimer = aimingSystem.getSprite();
        float direction = aimingSystem.getDirection();
        Player player = aimingSystem.getPlayer();
        if (!aimingSystem.isThrowingGrenade()){
            aimer.setPosition(player.getX() - 0.5f + (float) (Math.cos(direction + Constants.PI / 2)), player.getY() - 0.5f
                    + (float) (Math.sin(direction + Constants.PI / 2)));
            aimer.setOriginCenter();
            aimer.setRotation((float) (direction * 57.2957795));
            aimer.draw(batch);
        }

    }
    public static void drawGrenadeAimer(ZWShapeRenderer shapeRenderer){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        if (aimingSystem.isThrowingGrenade()){
            float mouseX = aimingSystem.getMouseX();
            float mouseY = aimingSystem.getMouseY();
            shapeRenderer.setColor(ZWShapeRenderer.Color.GREEN);
            shapeRenderer.line(mouseX - 10, mouseY -10, mouseX +10, mouseY+10);
            shapeRenderer.line(mouseX - 10, mouseY + 10, mouseX + 10, mouseY -10);
            shapeRenderer.circle(mouseX, mouseY, 20);
        }
    }
    public static void toggleMouseAiming(){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        stopAiming();
        aimingSystem.toggleMouseAiming();
    }

    public static void setMousePosition(int x, int y){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        aimingSystem.setMousePosition(x, y);
    }
    public static void toggleGrenadeThrowing(){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        aimingSystem.toggleGrenadeThrowing();
    }
    public static float getDirection(){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        return aimingSystem.getDirection();
    }
    public static boolean isThrowingGrenade(){
        AimingSystem aimingSystem = GameModel.getInstance().getAimingSystem();
        return aimingSystem.isThrowingGrenade();
    }
    public static AimingSystem getAimingSystem(){
       return GameModel.getInstance().getAimingSystem();
    }
}
