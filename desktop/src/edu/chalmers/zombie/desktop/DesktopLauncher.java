package edu.chalmers.zombie.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import edu.chalmers.zombie.testing.CollisionTest;
import edu.chalmers.zombie.testing.PathTest;
import edu.chalmers.zombie.ZombieWeek;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        System.out.println("För att starta kollision-/pathfindingtest skriv 't'.\n Annars tryck bara enter.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String s = br.readLine();
             if(s.equals("t"))
                new LwjglApplication(new CollisionTest(), config);
            else
                new LwjglApplication(new ZombieWeek(), config);
        }catch(java.io.IOException e){
            System.err.println("Invalid Format!");
        }


	}
}
