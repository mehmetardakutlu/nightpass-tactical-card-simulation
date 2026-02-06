import java.util.ArrayList;

/**
 * Nested AVL tree whose nodes correspond to curA values and stores inner QueueTrees. These subtrees'
 * nodes correspond to curHP values, and they store Cards in their queues. Every Card that is alive
 * finds itself a place in the attack tree.
 */
public class AttackTree extends AVLTree{

    AttackTree(){
    }

    /**
     * Inserts a card to the attack tree in two phases. First, redirects the card to the corresponding attack node.
     * If no attack node matches with the card's curA, a new node is created. After that, the card is inserted to the
     * subtree of the matching attack node.
     * @param card The card to be inserted
     */
    public void insert(Card card){
        ArrayList<TreeNode> ans = find(card.getCurA());
        if(ans == null){ // Tree is empty
            root = new TreeNode(card.getCurA(), card, new QueueTree());
            maxNode = root;
            maxVal = root.getData();
        }
        else if(ans.size() == 1){ // Node does not exist
            insertNode(new TreeNode(card.getCurA(), card, new QueueTree()), ans.getFirst());
        }
        else if(ans.size() == 2){ // Node exists
            ans.getFirst().insert(card);
        }
    }

    /**
     * Dequeues a card from the specified QueueTree node (healthNode). If the healthNode has no remaining cards,
     * it is removed. If the subtree hosting the healthNode becomes empty after the removal, the attack node
     * storing the subtree is also removed.
     * @param attackNode The node in AttackTree storing the subtree
     * @param healthNode The node in subtree (which is a QueueTree) storing the card
     * @return The dequeued card
     */
    public Card returnCard(TreeNode attackNode, TreeNode healthNode){
        Card ans = attackNode.getSubTree().remove(healthNode);
        if(attackNode.isTreeEmpty()){
            remove(attackNode, attackNode.getParent());
        }
        return ans;
    }
}
