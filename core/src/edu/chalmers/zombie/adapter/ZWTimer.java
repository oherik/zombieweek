package edu.chalmers.zombie.adapter;

import com.badlogic.gdx.utils.Timer;

/**
 * Created by daniel on 5/29/2015.
 */
public class ZWTimer {
    private Timer timer;
    public ZWTimer(){
        timer = new Timer();
    }
    public void scheduleTask(ZWTask task, float delaySeconds){
        timer.scheduleTask(task, delaySeconds);
    }
    public void start(){
        timer.start();
    }
}
