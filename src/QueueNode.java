/**
 * Stores the node properties and methods of my own Queue implementation.
 */
public class QueueNode {
    private final Card data;
    private QueueNode next;

    QueueNode(Card card){
        data = card;
    }

    /**
     * Returns the data in the node.
     * @return The data in the node
     */
    public Card getData(){
        return data;
    }

    /**
     * Returns the next element.
     * @return The next queue element
     */
    public QueueNode getNext() {
        return next;
    }

    /**
     * Sets the next element of the current node.
     * @param node The next element to be inserted
     */
    public void setNext(QueueNode node){
        next = node;
    }
}
