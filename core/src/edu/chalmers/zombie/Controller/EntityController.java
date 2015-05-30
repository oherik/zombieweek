package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.Entity;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Animator;
import edu.chalmers.zombie.utils.Constants;

/**
 * Handles the calculations that has to do with the zombies, the player, books, etc
 * Created by Erik on 2015-05-15.
 */
public class EntityController {

    public static void knockBack(Entity attacker, Entity victim, float amount){
        knockBack(new ZWVector(attacker.getX(),attacker.getY()),victim,amount);
    }

    public static void knockBack(ZWVector attackerPosition, Entity victim, float amount){
        float dx = victim.getX()-attackerPosition.getX();
        float dy = victim.getY()-attackerPosition.getY();
        ZWVector push = new ZWVector(dx,dy);
        push.setLength(amount);
        victim.applyLinearImpulse(push);
    }

    public static void remove(Entity entity){
        GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),entity);
        entity.markForRemoval();
    }

    /**
     * Applies friction to an entity, for example if it hits the wall and falls to the ground. If the book's body is set to null no friction is applied since the body handles the physics.
     * @param entity  The book to apply friction on
     * @param linearDampening   How high the linear friction should be
     * @param angularDampening  How high the angular, or rotational, friciton should be
     */
    public static void setFriction(Entity entity, float linearDampening, float angularDampening) {
        if (entity.getBody().getBody() != null) {
            entity.getBody().setLinearDamping(linearDampening);
            entity.getBody().setAngularDamping(angularDampening);
        }
    }

    /**
     * Sets the entity's category bits, used for collision detection
     * @param entity    The entity
     * @param bits  The category bits
     * @throws NullPointerException if the entity is null
     */
    public static void setCategoryBits(Entity entity, short bits) throws NullPointerException{
        if(entity == null)
            throw new NullPointerException("setMaskBits: the entity can't be null");
        try {
            entity.setCategoryBits(bits);
        }catch(NullPointerException e){
            System.err.println("Tried to set mask bits, but the entity's body and/or fixture was null. No category bits set." +
                    "\nInternal error message: " + e.getMessage());
        }
    }

    /**
     * Sets the entity's mask bits, used for collision detection
     * @param entity    The entity
     * @param bits  The mask bits
     * @throws NullPointerException if the entity or is null
     */
    public static void setMaskBits(Entity entity, short bits) throws NullPointerException{
        if(entity == null)
            throw new NullPointerException("setMaskBits: the entity can't be null");
        try {
            entity.setMaskBits(bits);
        }catch(NullPointerException e){
            System.err.println("Tried to set mask bits, but the entity's body and/or fixture was null. No mask bits set." +
                    "\nInternal error message: " + e.getMessage());
        }
    }

    public static void updateRotation(Entity entity){
        System.out.println("updateRotation");
        ZWBody body = entity.getBody();
        ZWSprite sprite = entity.getSprite();
        float angle = body.getAngle();
        float angleDegrees = angle * 180.0f / Constants.PI;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setRotation(angleDegrees);
    }

    /**
     * Updates the sprite's position based on the position of the body.
     */
    private static void updatePosition(Entity entity){
        ZWSprite sprite = entity.getSprite();
        ZWBody body = entity.getBody();
        sprite.setY(body.getPosition().getY() - sprite.getWidth() / 2f);
        sprite.setX(body.getPosition().getX() - sprite.getHeight() / 2f);
    }

    /**
     * Updates the entities sprite
     * @param entity The entity
     */
    public static void updateSprite(Entity entity){

        ZWBody body = entity.getBody();

        if (body != null) {
            updateRotation(entity);
            updatePosition(entity);
        }

        if(entity.isAnimated()){ //only if Entity should be animated
            Animator animator = entity.getAnimator();

            ZWSprite sprite = entity.getSprite();

            if(body != null) {
                float bodySpeed = getBodySpeed(entity);
                if (bodySpeed>9)  bodySpeed=9;
                float deltaTime = 1 / (300f - bodySpeed * 28); //fix to get a realistic movement

                animator.update(deltaTime);

                if (getBodySpeed(entity) < 0.2f) { //not moving
                    ZWTextureRegion stillFrame = animator.getStillFrame();
                    if (stillFrame != null) {
                        sprite.setRegion(stillFrame);
                    } else {
                        sprite.setRegion(animator.getFrame());
                    }

                } else { //is moving
                    sprite.setRegion(animator.getFrame());
                }
            }
            else{
                ZWTextureRegion stillFrame = animator.getStillFrame();
                if (stillFrame != null) {
                    sprite.setRegion(stillFrame);
                } else {
                    sprite.setRegion(animator.getFrame());
                }
            }
        }
    }

    public static float getBodySpeed(Entity entity){
        try {
            return entity.getBody().getLinearVelocity().len();
        } catch (NullPointerException e) {
            System.err.println("Get body speed: no body found");
            return 0f;
        }
    }



}
