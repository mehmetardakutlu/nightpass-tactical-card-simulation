import java.util.ArrayList;

/**
 * AVL Tree that only stores the curHP values. Every node has a cardCount number which indicates the number
 * of cards having the same curHP value as the node.
 */
public class FreeHTree extends AVLTree{

    FreeHTree(){
    }

    /**
     * Checks if the node's cardCount value dropped to zero.
     * @param node The node to be checked
     * @return Whether there are no cards that has the same curHP value compared to the node's value left
     */
    public boolean isNoCardLeft (TreeNode node){
        return node.getCardCount() == 0;
    }

    /**
     * Inserts a node with the given data to the tree if the node doesn't exist or increment the node's
     * cardCount otherwise.
     * @param data The data that the inserted node will have
     */
    @Override
    public void insert(int data){
        ArrayList<TreeNode> ans = find(data);
        if(ans == null){
            root = new TreeNode(data);
            maxNode = root;
            maxVal = root.getData();
        }
        else if(ans.size() == 1){
            insertChild(data,ans.getFirst());
        }
        else if(ans.size() == 2){
            ans.getFirst().incrementCount();
        }
    }

    /**
     * Decrements the cardCount of the node with the given data. If the cardCount drops to zero, the node is removed.
     * @param data The data to be removed
     */
    public void remove(int data){
        ArrayList<TreeNode> ans = find(data);
        if(ans == null){
            System.out.println("FreeHTree is empty");
        }
        else if(ans.size() == 1){
            System.out.println("Target node is not int the tree");
        }
        else if(ans.size() == 2){
            ans.getFirst().decrementCount();
            if(isNoCardLeft(ans.getFirst())){
                remove(ans.getFirst(), ans.getLast());
            }
        }
    }
}
