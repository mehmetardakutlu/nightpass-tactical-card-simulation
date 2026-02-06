/**
 * Stores the properties of my own queue implementation.
 * Queues keep the cards inside them to decide which card to choose in tiebreaker.
 */
public class MyQueue {
    private QueueNode head;
    private QueueNode tail;

    MyQueue(){
    }

    /**
     * Inserts a new node to the queue.
     * @param card Data in the new node to be inserted
     */
    public void enqueue(Card card){
        QueueNode newNode = new QueueNode(card);
        if(isEmpty()){ // Special case for empty queues.
            head = newNode;
        }
        else{
            tail.setNext(newNode);
        }
        tail = newNode;
    }

    /**
     * Removes the first inserted node and returns the data.
     * @return The data in the head node
     */
    public Card dequeue(){
        if(isEmpty()){
            return null;
        }
        QueueNode tmp = head;
        head = head.getNext();
        if(head == null){ // Special case for empty queues.
            tail = null;
        }
        return tmp.getData();
    }

    /**
     * Checks if the queue is empty.
     * @return Whether the queue is empty
     */
    public boolean isEmpty(){
        return head == null;
    }
}
