package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.Book;
import edu.chalmers.zombie.adapter.Entity;
import edu.chalmers.zombie.adapter.Player;
import edu.chalmers.zombie.adapter.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;

/**
 * Handles the calculations that has to do with the zombies, the player, books, etc
 * Created by Erik on 2015-05-15.
 */
public class EntityController {

    /* ----------------  ZOMBIE -------------------*/

    /**
     * A zombie gets knocked out
     * @param z The zombie
     */
    public static void knockOut(Zombie z){
        z.setSprite("core/assets/zombie_test_sleep.png");      //TODO temp, borde vara z.getDeadSprite() eller n�t
        z.scaleSprite(1f / Constants.TILE_SIZE);
        GameModel.getInstance().addEntityToRemove(z);
        z.knockOut();                                                                   //TODO Zombie b�r f� en hit() eller n�t ist�llet
        GameModel.getInstance().res.getSound("zombie_sleeping").play();
    }

    /* ---------------- PLAYER --------------------*/


    /*----------------- BOOK -----------------------*/

    /**
     * Performs the calculations necessary for the book to hit the ground. It calls other methods to set the collision detection to players and other books as well and to apply friction.
     * @param book  The book
     */
    public static void hitGround(Book book){
        //TODO g�ra boken mindre, l�gga till ljud etc
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_WATER;
        setMaskBits(book, maskBits);
        setFriction(book, 4f, 3f);
    }



    /**
     * A zombie gets hit by a book
     * @param z The zombie
     * @param b The book
     */
    public static void applyHit(Zombie z, Book b){
        double damage = getDamage(b);
        //TODO f�rst kolla om zombien ska knockas ut
        knockOut(z);
        GameModel.getInstance().addEntityToRemove(b);
        b.markForRemoval();
        GameModel.getInstance().res.getSound("zombie_hit").play();
    }

    /**
     * Calculates the damage caused by the book
     * @param b The book in question
     * @return  The damage caused by the book
     */
    public static double getDamage(Book b){
        return b.getSpeed()*b.getMass();    //TODO ta med massan h�r och n�n konstant
    }

    /**
     * A player picks up a book
     * @param p The player
     * @param b The book
     */
    public static void pickUp(Player p, Book b){
            GameModel.getInstance().addEntityToRemove(b); //TODO beh�vs b�da dessa?
            b.markForRemoval();             //TODO beh�vs b�da dessa?
            p.increaseAmmunition();
            GameModel.getInstance().res.getSound("pick_up_book").play();
    }


    /* ------------------------ ENTITY ----------------------------*/
    public static void remove(Entity entity){
        GameModel.getInstance().addEntityToRemove(entity);
        entity.markForRemoval();
    }

    /** The player attacks the zombie or vice versa
    * @param e1    The first entity
    * @param e2    The second entity
    */
    public static void attack(Entity e1, Entity e2){
        //TODO skriv denna
    }

    /**
     * Applies friction to the book, for example if it hits the wall and falls to the ground. If the book's body is set to null no friction is applied since the body handles the physics.
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
     */
    public static void setCategoryBits(Entity entity, short bits) {
        if(entity == null)
            throw new NullPointerException("setMaskBits: the entity can't be null");
        if(entity.getBody() == null)
            throw new NullPointerException("setMaskBits: the entity's body must be initialized");
        entity.setCategoryBits(bits);
    }

    /**
     * Sets the entity's mask bits, used for collision detection
     * @param entity    The entity
     * @param bits  The mask bits
     */
    public static void setMaskBits(Entity entity, short bits){
        if(entity == null)
            throw new NullPointerException("setMaskBits: the entity can't be null");
        if(entity.getBody() == null)
            throw new NullPointerException("setMaskBits: the entity's body must be initialized");
        entity.setMaskBits(bits);
    }
}
