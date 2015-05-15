package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.adapter.Book;
import edu.chalmers.zombie.model.GameModel;

/**
 * Handles the calculations that has to do withthe zombies,nthe player, books, etc
 * Created by Erik on 2015-05-15.
 */
public class EntityController {
    private GameModel gameModel;
    public EntityController(){
        gameModel = GameModel.getInstance();
    }

    /* ----------------  ZOMBIE -------------------*/



    /* ---------------- PLAYER --------------------*/


    /*----------------- BOOK -----------------------*/
    public static void hitGround(Book book){
        //TODO göra boken mindre, lägga till ljud etc
        applyGroundFriction(book, 4f, 3f);
    }
    /**
     * Applies friction to the book, for example if it hits the ground
     */

    public static void applyGroundFriction(Book book, float linearDampening, float angularDampening) {
        if (book.getBody() != null) {
            book.getBody().setLinearDamping(linearDampening);
            book.getBody().setAngularDamping(angularDampening)

        }
    }
}
