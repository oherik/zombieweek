package edu.chalmers.zombie.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import edu.chalmers.zombie.adapter.*;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import edu.chalmers.zombie.utils.PotionType;
import edu.chalmers.zombie.utils.ResourceManager;

import java.awt.*;

/**
 * Handles the calculations that has to do with the zombies, the player, books, etc
 * Created by Erik on 2015-05-15.
 */
public class EntityController {

    public static void knockBack(Entity attacker, Entity victim, int amount){
        float dx = victim.getX()-attacker.getX();
        float dy = victim.getY()-attacker.getY();
        Vector push = new Vector(dx,dy);
        push.setLength(amount);
        victim.applyLinearImpulse(push);
    }

    /* ----------------  ZOMBIE -------------------*/

    /**
     * A zombie gets knocked out
     * @param z The zombie
     */
    public static void knockOut(Zombie z){
        z.setSprite("core/assets/zombie_test_sleep.png");      //TODO temp, borde vara z.getDeadSprite() eller n�t
        z.scaleSprite(1f / Constants.TILE_SIZE);
        GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),z);
        z.knockOut();                                                                   //TODO Zombie b�r f� en hit() eller n�t ist�llet
        AudioController.playSound(GameModel.getInstance().res.getSound("zombie_sleeping"));

    }

    /* ---------------- PLAYER --------------------*/

    /**
     * @return  The current player
     */
    public static Player getPlayer(){
        return GameModel.getInstance().getPlayer();
    }
    /**
     * Sets the current player
     * @param player The new player
     */
    public static void setPlayer(Player player){
        GameModel.getInstance().setPlayer(player);
    }

    /**
     * Creates a new player and sets it in the game model.
     * @return  The newly created player
     */
    public static Player createNewPlayer(){
        MapController mapController = new MapController();

        GameModel gameModel = GameModel.getInstance();
        ResourceManager res = gameModel.res;

        Point position = mapController.getPlayerBufferPosition();
        Player player;
        try {
            player = new Player(res.getTexture("emilia"), gameModel.getRoom().getWorld(), position.x, position.y);
        }catch (NullPointerException e){
            System.err.println("No buffered position found. Placing player at room spawn.");
            position = GameModel.getInstance().getRoom().getPlayerSpawn();
            player = new Player(res.getTexture("emilia"),gameModel.getRoom().getWorld(), position.x, position.y);
        }
        setPlayer(player); //TODO test);
        return player;
    }

    /**
     * Increse the number of sneak tiles the player is touching by one. The player will start to sneak.
     * @param player    The player
     */
    public static void increaseSneakTilesTouching(Player player){
        if(player.getSneakTilesTouching()<0) {
            player.setSneakTilesTouching(0);        //Negative numbers for tiles youching wouldn't make sense
        }
        player.setSneakTilesTouching(player.getSneakTilesTouching() + 1);
        //TODO add more sneak stuff
        player.setHidden(true); //TODO Nödvändigt? Zombien kommer ändå inte kunna hitta till spelaren
        setFriction(player, Constants.PLAYER_FRICTION_SNEAK, Constants.PLAYER_FRICTION_SNEAK);  //TODO onödigt att göra varje gång?
    }

    /**
     * Decrease the number of sneak tiles the player is touching by one. The player will stop to sneak if the total number of sneak tiles it's touching is 0.
     * @param player The player
     */
    public static void decreaseSneakTilesTouching(Player player){
        player.setSneakTilesTouching(player.getSneakTilesTouching() - 1);
        if(player.getSneakTilesTouching()<1){
            //TODO Sluta sneaka
            player.setHidden(false);
            EntityController.setFriction(player, Constants.PLAYER_FRICTION_DEFAULT, Constants.PLAYER_FRICTION_DEFAULT);
            if(player.getSneakTilesTouching()<0) {
                player.setSneakTilesTouching(0);        //Negative numbers for tiles touching wouldn't make sense
            }
        }
    }

    /**
     * Increse the number of water tiles the player is touching by one. The player will slow down.
     * @param player    The player
     */
    public static void increaseWaterTilesTouching(Player player){
        if(player.getWaterTilesTouching()<0) {
            player.setWaterTilesTouching(0);
        }
        player.setWaterTilesTouching(player.getWaterTilesTouching() + 1);
            //TODO add water stuff
        setFriction(player, Constants.PLAYER_FRICTION_WATER, Constants.PLAYER_FRICTION_WATER);
    }

    /**
     * Decrease the number of water tiles the player is touching by one. The player will get out of the water if the total number of water tiles it's touching is 0.
     * @param player The player
     */
    public static void decreaseWaterTilesTouching(Player player){
        player.setWaterTilesTouching(player.getWaterTilesTouching() - 1);
        if(player.getWaterTilesTouching()<1){
            EntityController.setFriction(player, Constants.PLAYER_FRICTION_DEFAULT, Constants.PLAYER_FRICTION_DEFAULT);
            if(player.getWaterTilesTouching()<0) {
                player.setWaterTilesTouching(0);
            }
        }
    }


    /*----------------- BOOK -----------------------*/

    /**
     * Performs the calculations necessary for the book to hit the ground. It calls other methods to set the collision detection to players and other books as well and to apply friction.
     * @param book  The book
     */
    public static void hitGround(Book book){
        book.setOnGround(true);
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

        z.setIsAggressive(true);
        if(!b.isOnGround()) {
            int damage = b.getDamage();
            z.decHp(damage);
            if (z.getHP() <= 0) {
                knockOut(z);
            }
            knockBack(b,z,damage/10);
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
            GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),b); //TODO beh�vs b�da dessa?
            b.markForRemoval();             //TODO beh�vs b�da dessa?
            p.increaseAmmunition();
            AudioController.playSound(GameModel.getInstance().res.getSound("pick_up_book"));
    }


    /* ------------------------ ENTITY ----------------------------*/
    public static void remove(Entity entity){
        GameModel.getInstance().addEntityToRemove(GameModel.getInstance().getRoom(),entity);
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


    public static void spawnPotion(String typeName, Room room, int x, int y){
        spawnPotion(PotionType.valueOf(typeName), room, x, y);
    }

    public static void spawnPotion(PotionType type, Room room, int x, int y){
        switch(type){
            case HEALTH:
                room.addPotion(new Potion(type, new Sprite(GameModel.getInstance().res.getTexture("potion-health")), room.getWorld(), x, y));
                break;
            case SPEED:
                room.addPotion(new Potion(type, new Sprite(GameModel.getInstance().res.getTexture("potion-speed")), room.getWorld(), x, y));
                break;
            default:
                //TODO randomize?
                break;
        }


    }

}
