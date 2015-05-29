package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.utils.Timer;

/**
 * Created by daniel on 5/29/2015.
 */
public abstract class ZWTask extends Timer.Task{
    @Override
    public abstract void run();
}
