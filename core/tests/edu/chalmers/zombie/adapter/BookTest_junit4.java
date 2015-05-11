package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Direction;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Erik on 2015-05-07.
 */
public class BookTest_junit4 {
    private Book book;
    @Before
    public void setUp() throws Exception {
//       book = new Book(Direction.EAST, 2f ,2f, new World(new Vector2(0,0), true),new Vector2(3,3));
    }
    @Test
    public void testApplyFriction() throws Exception {
 //       book.applyFriction();
 //       System.out.println("Applied friction on book in JUNIT");
  //      Assert.assertTrue(book.getBody().getLinearDamping() > 0);
 //       Assert.assertTrue(book.getBody().getAngularDamping() > 0);
    }
}