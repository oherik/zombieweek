package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;

/**
 * Created by daniel on 5/29/2015.
 */
public class ZWEarClippingTriangulator {
    private EarClippingTriangulator ect;
    public ZWEarClippingTriangulator(){
        ect = new EarClippingTriangulator();
    }
    public short[] computeTriangles(float vertices[]){
        ShortArray s = ect.computeTriangles(vertices);
        return s.toArray();
    }
}
