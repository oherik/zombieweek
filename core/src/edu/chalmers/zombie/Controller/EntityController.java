package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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
        z.setSprite(new Sprite(new Texture("core/assets/zombie_test_sleep.png")));      //TODO temp, borde vara z.getDeadSprite() eller nåt
        z.scaleSprite(1f / Constants.TILE_SIZE);
        GameModel.getInstance().addEntityToRemove(z);
        z.knockOut();                                                                   //TODO Zombie bör få en hit() eller nåt istället
    }

    /**
     * The player attacks the zombie or vice versa
     * @param e1    The first entity
     * @param e2    The second entity
     */

    public static void attack(Entity e1, Entity e2){
        //TODO skriv denna
    }


    /* ---------------- PLAYER --------------------*/


    /*----------------- BOOK -----------------------*/
    public static void hitGround(Book book){
        //TODO göra boken mindre, lägga till ljud etc
        applyGroundFriction(book, 4f, 3f);
        book.setIsOnGround(true);
    }

    /**
     * Applies friction to the book, for example if it hits the wall and falls to the ground. If the book's body is set to null no friction is applied since the body handles the physics.
     * @param book  The book to apply friction on
     * @param linearDampening   How high the linear friction should be
     * @param angularDampening  How high the angular, or rotational, friciton should be
     */
    public static void applyGroundFriction(Book book, float linearDampening, float angularDampening) {
        if (book.getBody() != null) {
            book.getBody().setLinearDamping(linearDampening);
            book.getBody().setAngularDamping(angularDampening);
        }
    }

    /**
     * A zombie gets hit by a book
     * @param z The zombie
     * @param b The book
     */
    public static void applyHit(Zombie z, Book b){
        double damage = getDamage(b);
        //TODO först kolla om zombien ska knockas ut
        knockOut(z);
        GameModel.getInstance().addEntityToRemove(b);
        b.markForRemoval();
    }

    /**
     * Calculates the damage caused by the book
     * @param b The book in question
     * @return  The damage caused by the book
     */
    public static double getDamage(Book b){
        Vector2 vel = b.getVelocity();
        float mass = b.getBody().getMass();
        return Math.sqrt(vel.x*vel.x + vel.y*vel.y);    //TODO ta med massan här och nån konstant

    }

    /**
     * A player picks up a book
     * @param p The player
     * @param b The book
     */
    public static void pickUp(Player p, Book b){
        if(b.isOnGround()) {
            GameModel.getInstance().addEntityToRemove(b); //TODO behövs båda dessa?
            b.markForRemoval();             //TODO behövs båda dessa?
            p.increaseAmmunition();
        }
    }
}
