package edu.chalmers.zombie.utils;

import java.awt.*;

/** A ray tracing class based on a grid. It's very crude since the resolution is so low, but is very fast and enough for some of the methods
 * Created by Erik on 2015-05-21.
 */
public class TileRayTracing {
    /**
     * Checks if the path is obstructed by a wall using a modified version of Bresenham's line algorithm while taking into account how the maps are constructed.
     * @param start  The original position
     * @param end   The end position
     * @param obstructedTiles   A grid where a true flag indicated that the bath is obstructed
     * @return  true if the path is obstructed, false otherwise
     * @throws NullPointerException if either parameter is null
     * @throws IndexOutOfBoundsException if either point is out of bounds
     */
    public static boolean pathObstructed(Point start, Point end, short[][] obstructedTiles, short collisionBit) throws NullPointerException, IndexOutOfBoundsException{
        if(obstructedTiles == null)
            throw new NullPointerException("pathObstructed: obstructed tile grid was null");
        if(start == null)
            throw new NullPointerException("pathObstructed: the start point was null");
        if(end == null)
            throw new NullPointerException("pathObstructed: the end point was null");
        if(start.x < 0 || start.y < 0 || start.x >= obstructedTiles.length || start.y >= obstructedTiles[0].length)
            throw new IndexOutOfBoundsException("The start point is out of bounds");
        if(end.x < 0 || end.y < 0 || end.x >= obstructedTiles.length || end.y >= obstructedTiles[0].length)
            throw new IndexOutOfBoundsException("The end point is out of bounds");

        int dx = end.x - start.x;
        int dy = end.y - start.y;
        int dxSign = (dx<0) ? -1 : 1;
        double error = 0;
        double deltaError = ((double)dy/(double)dx < 0) ? -(double)dy/(double)dx : (double)dy/(double)dx;
        int ySign = (end.y-start.y)<0 ? -1 : 1;
        int y = start.y;
        for(int x = start.x; x != end.x + dxSign && x>=0 && x<obstructedTiles.length; x = x + dxSign){
            if((obstructedTiles[x][y]&collisionBit) == collisionBit) {
                return true;
            }
            error = error + deltaError;
            while(error>=0.5 && y>= 0 && y < obstructedTiles[0].length){
                if((obstructedTiles[x][y]&collisionBit) == collisionBit) {
                    return true;
                }
                y = y + ySign;
                error = error -1;
            }
        }
        return false;
    }
}
