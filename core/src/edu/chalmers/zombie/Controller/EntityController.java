package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.Entity;
import edu.chalmers.zombie.model.GameModel;

/**
 * Handles the calculations that has to do with the zombies, the player, books, etc
 * Created by Erik on 2015-05-15.
 */
public class EntityController {

    public static void knockBack(Entity attacker, Entity victim, float amount){
        knockBack(new Vector(attacker.getX(),attacker.getY()),victim,amount);
    }

    public static void knockBack(Vector attackerPosition, Entity victim, float amount){
        float dx = victim.getX()-attackerPosition.getX();
        float dy = victim.getY()-attackerPosition.getY();
        Vector push = new Vector(dx,dy);
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
        if (entity.getBody() != null) {
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



}
