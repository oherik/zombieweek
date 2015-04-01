package Model;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by Erik on 2015-04-01.
 * This is the class which handles the path finding for the zombies. It uses A*, which is a modified version of Dijkstra's algorithm.
 */
public class PathAlgorithm {
    private TiledMap map;
    private Point startPos, endPos;

    /**
     * Constructor
     * @param map   The current map in which the player and the zombie are
     */

    public PathAlgorithm(TiledMap map){
        this.map = map;
    }

    /**
    * @param startPos  The starting position (normally the zombie's position)
    * @param endPos    The end position (normally the player position)
    */

    public Iterator<Point> getPath(Point startPos, Point endPos){
        this.startPos = startPos;
        this.endPos = endPos;
        return calculatePath();
    }


    private Iterator<Point> calculatePath(){
        PriorityQueue<QueueElement> queue;
        


    }





}

    public Iterator<E> shortestPath(int from, int to) {
        if (from < 0)
            throw new IndexOutOfBoundsException(
                    "DirectedGraph - shortestPath(int from, int to): from f?r inte vara negativ");
        else if (to < 0)
            throw new IndexOutOfBoundsException(
                    "DirectedGraph - shortestPath(int from, int to): to f?r inte vara negativ");
        // Variabeldeklaration
        PriorityQueue<CompDijkstraPath<E>> dijkstraQueue = new PriorityQueue<CompDijkstraPath<E>>();
        ArrayList<E> currentPath;
        CompDijkstraPath<E> currentElement;
        Set<Integer> visitedNodes = new HashSet<Integer>();

        dijkstraQueue.add(new CompDijkstraPath<E>(from, 0, new ArrayList<E>())); // L?gg
        // till
        // startnoden
        while (!dijkstraQueue.isEmpty()) {
            currentElement = dijkstraQueue.poll(); // H?mta elementet med
            // kortast v?g
            int currentNode = currentElement.getNode();
            if (!visitedNodes.contains(currentNode)) {
                if (currentNode == to)
                    return currentElement.getPath().iterator(); // Har kommit
                    // fram,
                    // returnera
                    // v?gen dit
                else {
                    visitedNodes.add(currentNode);
                    for (E e : edgeListArray[currentNode]) {
                        if (!visitedNodes.contains(e.getDest())) {
                            currentPath = new ArrayList<E>(
                                    currentElement.getPath()); // Kopiera v?gen
                            // fr?n
                            // elementet
                            // till en ny
                            // lista
                            currentPath.add(e); // Och l?gg till den aktuella
                            // b?gen
                            dijkstraQueue.add(new CompDijkstraPath<E>(e
                                    .getDest(), e.getWeight()
                                    + currentElement.getCost(), currentPath)); // L?gg
                            // till
                            // k?elementet
                            // i
                            // prioritetsk?n
                        }
                    }
                }
            }
        }
        return null; // Ingen v?g funnen
    }