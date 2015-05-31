package edu.chalmers.zombie.controller;

import edu.chalmers.zombie.model.Book;
import edu.chalmers.zombie.model.Player;
import edu.chalmers.zombie.model.Zombie;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.model.Room;
import edu.chalmers.zombie.utils.Constants;

/**
 * Created by Erik on 2015-05-29.
 * Modified by Neda.
 */
public class ProjectileController {

    /**
     * Performs the calculations necessary for the book to hit the ground. It calls other methods to set the collision detection to players and other books as well and to apply friction.
     * @param book  The book
     */
    public static void hitGround(Book book){
        book.setOnGround(true);
        //TODO g?ra boken mindre, l?gga till ljud etc
        short maskBits = Constants.COLLISION_OBSTACLE | Constants.COLLISION_ENTITY | Constants.COLLISION_WATER | Constants.COLLISION_ACTOR_OBSTACLE | Constants.COLLISION_DOOR;
        EntityController.setMaskBits(book, maskBits);
        EntityController.setFriction(book, 4f, 3f);
    }

    public static void spawnStillBook(Room room, int x, int y){
        Book book = new Book(x,y,room);
        hitGround(book);
        GameModel.getInstance().addBook(book);
    }



    /**
     * A zombie gets hit by a book
     * @param z The zombie
     * @param b The book
     */
    public static void applyHit(Zombie z, Book b){

        z.setIsAggressive(true);
        if(!b.isOnGround()) {
            int damage = b.getDamage();
            z.decHp(damage);
            if (z.getHp() <= 0) {
                ZombieController.knockOut(z);
                GameModel.getInstance().getPlayer().incKillCount();
            }
            EntityController.knockBack(b, z, (float)damage / 10f);
            hitGround(b);
            // GameModel.getInstance().addEntityToRemove(b);
            //b.markForRemoval();
            AudioController.playSound(GameModel.getInstance().res.getSound("zombie_hit"));
        }
    }

    /**
     * A player picks up a book
     * @param p The player
     * @param b The book
     */
    public static void pickUp(Player p, Book b){
        GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),b); //TODO beh?vs b?da dessa?
        b.markForRemoval();             //TODO beh?vs b?da dessa?
        p.increaseAmmunition();
        AudioController.playSound(GameModel.getInstance().res.getSound("pick_up_book"));
    }


}
