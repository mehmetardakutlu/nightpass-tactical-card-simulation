import java.util.ArrayList;

/**
 * An AVL Tree every of whose nodes store Cards in their queues. Used both as subtrees in attack tree and
 * as an independent tree to store discarded Cards in type-2.
 */
public class QueueTree extends  AVLTree{

    QueueTree(){
    }

    /**
     * Inserts a card to the tree regarding its curHP value. If there is no corresponding node, a new node is
     * created. Otherwise, the card is added to the queue of the matching node.
     * @param card The card to be inserted
     */
    public void insert(Card card){
        ArrayList<TreeNode> ans = find(card.getCurHP());
        if(ans == null){ // Tree is empty
            root = new TreeNode(card.getCurHP(), card);
            maxNode = root;
            maxVal = root.getData();
        }
        else if(ans.size() == 1){ // There is no matching node
            insertNode(new TreeNode(card.getCurHP(), card), ans.getFirst());
        }
        else if(ans.size() == 2){ // There is a matching node
            ans.getFirst().enqueue(card);
        }
    }

    /**
     * Inserts a card to the tree regarding its missingHP value. If there is no corresponding node, a new node is
     * created. Otherwise, the card is added to the queue of the matching node. Used in type-2.
     * @param card The card to be inserted
     */
    public void insertDead(Card card){
        ArrayList<TreeNode> ans = find(card.getMissingHP());
        if(ans == null){ // Tree is empty
            root = new TreeNode(card.getMissingHP(), card);
            maxNode = root;
            maxVal = root.getData();
        }
        else if(ans.size() == 1){ // The node was not created
            insertNode(new TreeNode(card.getMissingHP(), card), ans.getFirst());
        }
        else if(ans.size() == 2){ // The node exists
            ans.getFirst().enqueue(card);
        }
    }

    /**
     * Dequeues a card from the queue of the given node. If there are no remaining cards inside the node's queue,
     * it is removed.
     * @param node The node to perform dequeue and possible removal operations
     * @return The dequeued card
     */
    public Card remove(TreeNode node){
        Card ans = node.dequeue();
        if(node.isQueueEmpty()){
            remove(node, node.getParent());
        }
        return ans;
    }
}
