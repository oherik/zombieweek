package edu.chalmers.zombie.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import edu.chalmers.zombie.testing.PathTest;
import edu.chalmers.zombie.ZombieWeek;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        System.out.println("För att starta pathfindingtest, skriv 'p1', 'p2' eller 'p3' och tryck enter. Annars tryck bara enter.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String s = br.readLine();
            if(s.equals("p1"))
                new LwjglApplication(new PathTest(1), config);
            else if(s.equals("p2"))
                new LwjglApplication(new PathTest(2), config);
            else if(s.equals("p3"))
                new LwjglApplication(new PathTest(3), config);
            else
                new LwjglApplication(new ZombieWeek(), config);
        }catch(java.io.IOException e){
            System.err.println("Invalid Format!");
        }


	}
}
