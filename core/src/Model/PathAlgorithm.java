package Model;

import java.awt.*;
import java.util.*;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Erik on 2015-04-01.
 * This is the class which handles the path finding for the zombies. It uses A*, which is a modified version of Dijkstra's algorithm.
 */
public class PathAlgorithm {
    private TiledMapTileLayer emptyTiles;
    private Point startPos, endPos;

    /**
     * Constructor
     *
     * @param emptyTiles The current map in which the player and the zombie are
     */

    public PathAlgorithm(TiledMapTileLayer emptyTiles) {
        this.emptyTiles = this.emptyTiles;
    }

    /**
     * @param startPos The starting position (normally the zombie's position)
     * @param endPos   The end position (normally the player position)
     */

    public Iterator<Point> getPath(Point startPos, Point endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
        return calculatePath();
    }

    /**
     * Calculates the shortest path given the data from the constructor
     *
     * @return the shortest path or, if none found, null
     */

    private Iterator<Point> calculatePath() {
        PriorityQueue<QueueElement> queue = new PriorityQueue<QueueElement>();
        ArrayList<Point> path;
        QueueElement currentElement;
        int width = emptyTiles.getWidth();
        int height = emptyTiles.getHeight();
        boolean[][] closedNodes = new boolean[width][height];

        queue.add(new QueueElement(this.startPos, 0, new ArrayList<Point>()));

        while (!queue.isEmpty()) {
            currentElement = queue.poll();
            Point currentNode = currentElement.getNode();
            int x = currentNode.x;
            int y = currentNode.y;

            if (currentNode == this.endPos)
                return currentElement.getPath().iterator();
            else {
                for (int i = Math.max(0, x - 1); i <= Math.min(width, x + 1); i++) {
                    for (int j = Math.max(0, y - 1); j <= Math.min(height, y + 1); j++) {
                        if (!closedNodes[i][j] == true && emptyTiles.getCell(i, j) != null) {//i.e. if it's null or false and is walkable
                            int h = 10 * (Math.abs(endPos.x - i) + Math.abs(endPos.y - j));     //Manhattan distance
                            double g;
                            if ((j == x - 1 && i == y - 1) || (j == x - 1 && i == y + 1) || (j == x + 1 && i == y - 1) || (j == x + 1 && i == y + 1)) {       //Diagonal
                                g = currentElement.getCost() + 14;  // 10 * sqrt(2) is approx. 14
                            } else {
                                g = currentElement.getCost() + 10;
                            }
                            double cost = h + g;
                            ArrayList<Point> currentPath = new ArrayList<Point>(currentElement.getPath());
                            currentPath.add(currentNode);
                            queue.add(new QueueElement(new Point(i, j), cost, currentPath));
                        }
                    }
                }
            }

        }
        return null;    //No path found
    }//calculatePath
}