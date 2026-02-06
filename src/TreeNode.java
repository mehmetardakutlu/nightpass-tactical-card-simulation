/**
 * Stores the node properties and methods of my own AVL tree implementation.
 */
public class TreeNode {
    private final int data;
    private TreeNode parent;
    private TreeNode leftChild;
    private TreeNode rightChild;
    private int height = 0; // Used for AVL implementation
    private int cardCount; // Used in FreeHTree
    private MyQueue cardQueue; // Used in QueueTree
    private QueueTree subTree; // Used in AttackTree

    /**
     * Constructor of the independent health tree.
     * @param data The health value
     */
    TreeNode(int data){
        this.data = data;
        cardCount = 1;
    }

    /**
     * Constructor of the dependent health tree inside the attack tree.
     * @param data The health value
     * @param card The card that the node will store in its queue.
     */
    TreeNode(int data, Card card){
        this.data = data;
        cardQueue = new MyQueue();
        cardQueue.enqueue(card);
    }

    /**
     * Constructor of the nested attack tree. All attack tree nodes have health trees inside them, which hold
     * cards inside the queues in their health tree nodes.
     * @param data The attack value
     * @param card The card that will be inserted to the sub health tree
     * @param healthTree The sub health tree inside the attack node
     */
    TreeNode(int data, Card card, QueueTree healthTree){
        this.data = data;
        subTree = healthTree;
        subTree.insert(card);
    }

    /**
     * Adds a card to the queue inside the node (for QueueTree nodes)
     * @param card The card to be inserted
     */
    public void enqueue(Card card){
        if(cardQueue == null){
            cardQueue = new MyQueue();
        }
        cardQueue.enqueue(card);
    }

    /**
     * Removes a card from the queue inside the node (for QueueTree nodes)
     * @return The removed card
     */
    public Card dequeue(){
        return cardQueue.dequeue();
    }

    /**
     * Inserts a card to the subtree of an attack node.
     * @param card The card to be inserted
     */
    public void insert(Card card){
        if(subTree == null){
            subTree = new QueueTree();
        }
        subTree.insert(card);
    }

    /**
     * Returns the subtree of the attack node.
     * @return The subtree inside the attack node
     */
    public QueueTree getSubTree(){
        return subTree;
    }

    /**
     * Checks whether the tree is empty.
     * @return Whether the tree is empty
     */
    public boolean isTreeEmpty(){
        return subTree.isEmpty();
    }

    /**
     * Checks if the queue inside a QueueTree node is empty.
     * @return Whether the queue is empty
     */
    public boolean isQueueEmpty(){
        return cardQueue.isEmpty();
    }

    /**
     * Returns the integer value stored in the node.
     * @return The value in integer form
     */
    public int getData(){
        return data;
    }

    /**
     * Returns the parent of the given node.
     * @return The parent of the given node
     */
    public TreeNode getParent(){
        return parent;
    }

    /**
     * Sets the new parent of the given node
     * @param parent The new parent
     */
    public void setParent(TreeNode parent){
        this.parent = parent;
    }

    /**
     * Returns the given node's left child.
     * @return The given node's left child
     */
    public TreeNode getLeft(){
        return leftChild;
    }

    /**
     * Sets the given node's left child.
     * @param leftChild The new left child
     */
    public void setLeft(TreeNode leftChild){
        this.leftChild = leftChild;
    }

    /**
     * Returns the given node's right child.
     * @return The given node's right child
     */
    public TreeNode getRight(){
        return rightChild;
    }

    /**
     * Sets the node's right child
     * @param rightChild The new tight child
     */
    public void setRight(TreeNode rightChild){
        this.rightChild = rightChild;
    }

    /**
     * Returns the height of the node (i.e. the longest path from the node to a leaf).
     * @return The height of the node
     */
    public int getHeight(){
        return height;
    }

    /**
     * Sets the new height of the node.
     * @param height The new height
     */
    public void setHeight(int height){
        this.height = height;
    }

    /**
     * Sets the new height of the node by checking the heights of its left and right child.
     */
    public void findNewHeight(){
        if(leftChild != null && rightChild != null){
            setHeight(Math.max(leftChild.getHeight() + 1, rightChild.getHeight() + 1));
        }
        else{
            if(leftChild == null && rightChild != null){ // Height of the null nodes is -1
                setHeight(rightChild.getHeight() + 1);
            }
            else if(leftChild != null){
                setHeight(leftChild.getHeight() + 1);
            }
            else{
                setHeight(0);
            }
        }
    }

    /**
     * Returns the height difference between the node's child nodes.
     * @return Height difference in leftChildHeight - rightChildHeight format
     */
    public int heightDifference(){
        if(leftChild != null && rightChild != null){
            return leftChild.getHeight() - rightChild.getHeight();
        }
        else{
            if(leftChild == null && rightChild != null){ // Height of the null nodes is -1
                return -1 - rightChild.getHeight();
            }
            else if(leftChild != null){
                return leftChild.getHeight() + 1;
            }
            else{
                return 0;
            }
        }
    }

    /**
     * Adds 1 to the card count for FreeHTree nodes.
     * (Card count shows how many cards' health value is the same as the node's data)
     */
    public void incrementCount(){
        cardCount += 1;
    }

    /**
     * Subtracts 1 from the card count for FreeHTree nodes.
     */
    public void decrementCount(){
        cardCount -= 1;
    }

    /**
     * Returns the current cardCount value.
     * @return How many cards have the same data as the node
     */
    public int getCardCount(){
        return cardCount;
    }
}
