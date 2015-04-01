package Model;

import java.awt.*;

/** A helper class for PathAlgorithm. It's used in the priority queue.
 * Created by Erik on 2015-04-01.
 */
public class QueueElement implements Comparable<QueueElement>{
    private Point node;
    private double cost;

    public QueueElement(Point node, double cost){
        if(node == null)
            throw new NullPointerException("QueueElement: node cannot be null");
        if(node.getX()<0 || node.getY() < 0)
            throw new IndexOutOfBoundsException("QueueElement: x and y values must be positive");
        this.node = node;
        this.cost = cost;
    }

    /**
     *
     * @return the cost of the path to the node
     */

    public double getCost() {
        return cost;
    }

    /**
     * @return the specified node
     */

    public Point getNode() {
        return node;
    }


    /**
     * Compares the cost with another queue element
     *
     * @param otherElement
     *            the other element
     * @returns 0 if the cost is the same, a number less than 0 if this element's cost is the smallest or a number more than 0 if the other element's cost is the least.
     * @throws NullPointerException if the other element is null
     */
    @Override
    public int compareTo(QueueElement otherElement) {
        if (otherElement == null)
            throw new NullPointerException(
                    "QueueElement: Cannot compare with something that is null");
        return Double.compare(this.cost, otherElement.getCost());
    }

}


