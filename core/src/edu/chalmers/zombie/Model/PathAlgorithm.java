package edu.chalmers.zombie.Model;

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
     *  @throws NullPointerException if emptyTiles is null
     */

    public PathAlgorithm(TiledMapTileLayer emptyTiles) {
        if (emptyTiles == null)
            throw new NullPointerException("PathAlgorithm: the layer given cannot be null");
        this.emptyTiles = emptyTiles;
    }

    /** The start function for calculating the shortest path between two points on the map.
     * 
     * @param startPos The starting position (normally the zombie's position)
     * @param endPos   The end position (normally the player position)
     * @return  the shortest path as an iterator over points
     * @throws IndexOutOfBoundsException if the start or end position is negative
     * @throws  NullPointerException    if either of the points is null
     */

    public Iterator<Point> getPath(Point startPos, Point endPos) {
        if(startPos == null || endPos == null)
            throw new NullPointerException("PathAlgorithm: the points given cannot be null");
        if(startPos.y<0 || startPos.x <0)
            throw new IndexOutOfBoundsException("PathAlgorithm: the start position must be positive");
        if(endPos.x<0 || endPos.y<0)
            throw new IndexOutOfBoundsException("PathAlgorithm: the end position must be positive");
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
        int[][] gCost = new int[width][height];
        int[][] hCost = new int[width][height];
        queue.add(new QueueElement(this.startPos, 0, 0, new ArrayList<Point>()));

        while (!queue.isEmpty()) {
            currentElement = queue.poll();
            Point currentNode = currentElement.getNode();
            //System.out.println(currentNode.x + ", "+ currentNode.y);
            if (currentNode.equals(this.endPos))
                return currentElement.getPath().iterator();
            else {
                int x = currentNode.x;
                int y = currentNode.y;
                closedNodes[x][y] = true;
                for(int i = 0; i<height; i++){
                    for(int j = 0; j<width; j ++)
                        System.out.print(gCost[j][i] + hCost[j][i] +"\t");
                    System.out.println("");
                }
                System.out.println("\n");
                for (int i = Math.max(0, x - 1); i <= Math.min(width-1, x + 1); i++) {
                    for (int j = Math.max(0, y - 1); j <= Math.min(height-1, y + 1); j++) {
                        if (walkableTile(closedNodes, i, j)
                                && noCornersCut(closedNodes, currentNode, i, j)) {
                            int g;
                            int h = 10 * (Math.abs(endPos.x - i) + Math.abs(endPos.y - j));     //Manhattan distance
                            if(isDiagonal(currentNode, i, j))
                                g = currentElement.getGCost() + 14;  // 10 * sqrt(2) is approx. 14
                            else
                                g = currentElement.getGCost() + 10;
                            if(gCost[i][j]==0 || gCost[i][j] > g) {
                                ArrayList<Point> currentPath = new ArrayList<Point>(currentElement.getPath());
                                currentPath.add(currentNode);
                                queue.add(new QueueElement(new Point(i, j), g, h, currentPath));
                                gCost[i][j]=g;
                            }

                            hCost[i][j]=h;
                        }
                    }
                }
            }

        }
        return null;    //No path found
    }//calculatePath

    /**Checks if the node in questions is walkable
     * @param closedNodes the matrix of closed nodes
     * @param x     The node's x variable
     * @param y     The node's y variable
     * @return  true if it's walkable, false if it isn't
     */
    private boolean walkableTile(boolean[][] closedNodes, int x, int y){
        return !closedNodes[x][y] == true && emptyTiles.getCell(x, y) != null;
    }


    /**Checks if the path is walkable, i.e. no corners are cut
     * @param closedNodes the matrix of closed nodes
     * @param parent    The parent element
     * @param x     The node's x variable
     * @param y     The node's y variable
     * @return true if it's walkable, false if it cuts corners
     */
    private boolean noCornersCut(boolean[][] closedNodes, Point parent, int x, int y){
        int parentX = parent.x;
        int parentY = parent.y;
        if(isDiagonal(parent, x, y))
            return walkableTile(closedNodes, parentX, y) && walkableTile(closedNodes, x, parentY);
        else
            return true;
    }


    /** Checks if a point is diagonal to the parent
     *
     * @param parentNode The parent node
     * @param x     The node's x variable
     * @param y     The node's y variable
     * @return true if it is diagonal, false if it isn't
     */
    private boolean isDiagonal(Point parentNode, int x, int y){
        int parentX = parentNode.x;
        int parentY = parentNode.y;
        return(x == parentX - 1 && y == parentY - 1) ||
                (x == parentX - 1 && y == parentY + 1) ||
                (x == parentX + 1 && y == parentY - 1) ||
                (x == parentX + 1 && y == parentY + 1);
    }
}