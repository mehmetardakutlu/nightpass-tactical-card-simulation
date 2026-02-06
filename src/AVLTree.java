import java.util.ArrayList;

/**
 * Stores the properties and methods of my own AVL tree implementation. Inserts, removes, balances and finds tree nodes.
 */
public class AVLTree {
    protected TreeNode root;
    protected TreeNode maxNode; // The node with the maximum value
    protected int maxVal; // The value of the maxNode

    AVLTree(){
    }

    /**
     * Returns the absolute value of the height difference between the node's children.
     * For a balanced tree, the return value must not exceed one.
     * @param node The node to check
     * @return The absolute value of the height difference between child parents
     */
    public int heightDifference(TreeNode node){
        int leftHeight;
        int rightHeight;
        TreeNode leftChild = node.getLeft();
        TreeNode rightChild = node.getRight();
        if(leftChild == null){ // Null nodes have a height value of -1
            leftHeight = -1;
        }
        else{
            leftHeight = leftChild.getHeight();
        }
        if(rightChild == null){
            rightHeight = -1;
        }
        else{
            rightHeight = rightChild.getHeight();
        }
        return Math.abs(leftHeight - rightHeight);
    }

    /**
     * Checks if the given node satisfies the balance condition of the AVL tree.
     * @param node The node to check
     * @return Whether the given node is balanced
     */
    public boolean isBalanced(TreeNode node){
        return heightDifference(node) <= 1;
    }

    /**
     * Performs left rotation operation between two nodes. Changes the nodes' places and updates their height values.
     * @param child The child node to be rotated
     * @param parent The parent node to be rotated
     */
    public void leftRotation(TreeNode child, TreeNode parent){
        // Child node's left child becomes parent node's right child
        parent.setRight(child.getLeft());
        if(child.getLeft() != null){
            child.getLeft().setParent(parent);
        }
        child.setLeft(parent);
        child.setParent(parent.getParent());
        // If the parent node was the root, the child node becomes the new root
        if(parent.getParent() == null){
            root = child;
        }
        else{
            // Connecting the child node to the parent node's parent
            if(child.getData() < parent.getParent().getData()){
                parent.getParent().setLeft(child);
            }
            else{
                parent.getParent().setRight(child);
            }
        }
        parent.setParent(child);
        // Updating heights of the nodes that are affected by rotation
        parent.findNewHeight();
        child.findNewHeight();
        if(child.getParent() != null){
            child.getParent().findNewHeight();
        }
    }

    /**
     * Performs right rotation operation between two nodes. Changes the nodes' places and updates their height values.
     * @param child The child node to be rotated
     * @param parent The parent node to be rotated
     */
    public void rightRotation(TreeNode child, TreeNode parent){
        // Child node's right child becomes parent node's left child
        parent.setLeft(child.getRight());
        if(child.getRight() != null){
            child.getRight().setParent(parent);
        }
        child.setRight(parent);
        child.setParent(parent.getParent());
        // If the parent node was the root, the child node becomes the new root
        if(parent.getParent() == null){
            root = child;
        }
        else{
            // Connecting the child node to the parent node's parent
            if(child.getData() < parent.getParent().getData()){
                parent.getParent().setLeft(child);
            }
            else{
                parent.getParent().setRight(child);
            }
        }
        parent.setParent(child);
        // Updating heights of the nodes that are affected by rotation
        parent.findNewHeight();
        child.findNewHeight();
        if(child.getParent() != null){
            child.getParent().findNewHeight();
        }
    }

    /**
     * Inserts a new node to the tree and ensures the balance of the tree after insertion by
     * performing rotations.
     * @param data The data that the inserted node will have
     */
    public void insert(int data){
        if(root == null){ // Special case: If tree was empty, inserted node becomes the new root
            root = new TreeNode(data);
            maxNode = root;
            maxVal = root.getData();
        }
        else{
            // Determining the place of the new node using find() method
            ArrayList<TreeNode> ans = find(data);
            TreeNode parentNode = ans.getFirst();
            TreeNode newNode = new TreeNode(data);
            if(data < parentNode.getData()){
                parentNode.setLeft(newNode);
            }
            else if(data > parentNode.getData()){
                parentNode.setRight(newNode);
            }
            newNode.setParent(parentNode);
            TreeNode childNode = newNode;
            if(newNode.getData() > maxVal){ // If the inserted node is greater than maxNode, it becomes new maxNode
                maxNode = newNode;
                maxVal = newNode.getData();
            }
            int heightVal = 0;
            // After inserting the node, the code climbs up to the root while updating height values and looks
            // for a possible imbalance situation
            do{
                if(heightVal == parentNode.getHeight()){
                    if(!isBalanced(parentNode)){ // An imbalance is found
                        if(newNode.getData() < childNode.getData() && // Left-Left case
                           childNode.getData() < parentNode.getData()){
                            leftRotation(childNode, parentNode);
                        }
                        else if(newNode.getData() > childNode.getData() && // Right-Right case
                                childNode.getData() > parentNode.getData()){
                            rightRotation(childNode, parentNode);
                        }
                        else if(newNode.getData() > childNode.getData() && // Left-Right case
                                childNode.getData() < parentNode.getData()){
                            TreeNode rightSubtreeRoot = childNode.getRight();
                            leftRotation(rightSubtreeRoot, childNode);
                            rightRotation(rightSubtreeRoot, parentNode);
                        }
                        else if(newNode.getData() < childNode.getData() && // Right-Left case
                                childNode.getData() > parentNode.getData()){
                            TreeNode leftSubtreeRoot = childNode.getLeft();
                            rightRotation(leftSubtreeRoot, childNode);
                            leftRotation(leftSubtreeRoot, parentNode);
                        }
                        return;
                    }
                    parentNode.setHeight(heightVal + 1); // Updating the height values of the nodes above the inserted node
                    parentNode = parentNode.getParent();
                    childNode = childNode.getParent();
                    heightVal += 1;
                }
                else{
                    break;
                }
            }while(parentNode.getParent() != null); // Stops when the root is reached
        }
    }

    /**
     * Inserts a new node by connecting it to the given parent node and ensures the balance of the tree after insertion
     * by performing rotations.
     * @param data The data that the inserted node will have
     * @param parentNode The parent node of the inserted node
     */
    public void insertChild(int data, TreeNode parentNode){
        TreeNode newNode = new TreeNode(data);
        // Connecting new node to the parent node
        if(data < parentNode.getData()){
            parentNode.setLeft(newNode);
        }
        else if(data > parentNode.getData()){
            parentNode.setRight(newNode);
        }
        newNode.setParent(parentNode);
        TreeNode childNode = newNode;
        if(newNode.getData() > maxVal){ // If the inserted node is greater than maxNode, it becomes new maxNode
            maxNode = newNode;
            maxVal = newNode.getData();
        }
        int heightVal = 0;
        // After inserting the node, the code climbs up to the root while updating height values and looks
        // for a possible imbalance situation
        while(parentNode != null){ // Stops when the root is reached
            if(heightVal == parentNode.getHeight()){
                if(!isBalanced(parentNode)){
                    if(newNode.getData() < childNode.getData() && // Left-Left case
                            childNode.getData() < parentNode.getData()){
                        rightRotation(childNode, parentNode);
                    }
                    else if(newNode.getData() > childNode.getData() && // Right-Right case
                            childNode.getData() > parentNode.getData()){
                        leftRotation(childNode, parentNode);
                    }
                    else if(newNode.getData() > childNode.getData() && // Left-Right case
                            childNode.getData() < parentNode.getData()){
                        TreeNode rightSubtreeRoot = childNode.getRight();
                        leftRotation(rightSubtreeRoot, childNode);
                        rightRotation(rightSubtreeRoot, parentNode);
                    }
                    else if(newNode.getData() < childNode.getData() && // Right-Left case
                            childNode.getData() > parentNode.getData()){
                        TreeNode leftSubtreeRoot = childNode.getLeft();
                        rightRotation(leftSubtreeRoot, childNode);
                        leftRotation(leftSubtreeRoot, parentNode);
                    }
                    return;
                }
                parentNode.setHeight(heightVal + 1); // Updating the height values of the nodes above the inserted node
                parentNode = parentNode.getParent();
                childNode = childNode.getParent();
                heightVal += 1;
            }
            else{
                break;
            }
        }
    }

    /**
     * Inserts a given node by connecting it to the given parent node and ensures the balance of the tree after insertion
     * by performing rotations.
     * @param targetNode The node to be inserted
     * @param parentNode The parent of the node to be inserted
     */
    public void insertNode(TreeNode targetNode, TreeNode parentNode){
        int data = targetNode.getData();
        // Connecting the new node to its parent
        if(data < parentNode.getData()){
            parentNode.setLeft(targetNode);
        }
        else if(data > parentNode.getData()){
            parentNode.setRight(targetNode);
        }
        targetNode.setParent(parentNode);
        TreeNode childNode = targetNode;
        if(targetNode.getData() > maxVal){ // If the inserted node is greater than maxNode, it becomes new maxNode
            maxNode = targetNode;
            maxVal = targetNode.getData();
        }
        int heightVal = 0;
        // After inserting the node, the code climbs up to the root while updating height values and looks
        // for a possible imbalance situation
        while(parentNode != null){ // Stop when the root is reached
            if(heightVal == parentNode.getHeight()){
                if(!isBalanced(parentNode)){
                    if(targetNode.getData() < childNode.getData() && // Left-Left case
                            childNode.getData() < parentNode.getData()){
                        rightRotation(childNode, parentNode);
                    }
                    else if(targetNode.getData() > childNode.getData() && // Right-Right case
                            childNode.getData() > parentNode.getData()){
                        leftRotation(childNode, parentNode);
                    }
                    else if(targetNode.getData() > childNode.getData() && // Left-Right case
                            childNode.getData() < parentNode.getData()){
                        TreeNode rightSubtreeRoot = childNode.getRight();
                        leftRotation(rightSubtreeRoot, childNode);
                        rightRotation(rightSubtreeRoot, parentNode);
                    }
                    else if(targetNode.getData() < childNode.getData() && // Right-Left case
                            childNode.getData() > parentNode.getData()){
                        TreeNode leftSubtreeRoot = childNode.getLeft();
                        rightRotation(leftSubtreeRoot, childNode);
                        leftRotation(leftSubtreeRoot, parentNode);
                    }
                    return;
                }
                parentNode.setHeight(heightVal + 1); // Updating the height values of the nodes above the inserted node
                parentNode = parentNode.getParent();
                childNode = childNode.getParent();
                heightVal += 1;
            }
            else{
                break;
            }
        }
    }

    /**
     * Finds the lowest element in the node's right subtree.
     * @param node The target node to find its leftLeast child
     * @return The target node's leftLeast child
     */
    private TreeNode findLeftLeast(TreeNode node){
        TreeNode targetNode = node.getRight();
        if(targetNode != null){
            while(targetNode.getLeft() != null){
                targetNode = targetNode.getLeft();
            }
            return targetNode;
        }
        return null;
    }

    /**
     * Finds the highest element in the node's left subtree.
     * @param node The target node to find its rightMost child
     * @return The target node's rightMost child
     */
    private TreeNode findRightMost(TreeNode node){
        TreeNode targetNode = node.getLeft();
        if(targetNode != null){
            while(targetNode.getRight() != null){
                targetNode = targetNode.getRight();
            }
            return targetNode;
        }
        return null;
    }

    /**
     * Finds the smallest node that is larger than the given node.
     * @param node The node to find its successor
     * @return The smallest node that is still larger than the given node
     */
    public TreeNode findNext(TreeNode node){
        TreeNode leftLeast = findLeftLeast(node); // First possible answer is the node's leftLeast child
        if(leftLeast != null){
            return leftLeast;
        }
        else{//If the node doesn't have a leftLeast child, the code climbs up to the root and checks if a suitable node is found
            TreeNode parent = node.getParent();
            while(parent != null){
                if(node.getData() < parent.getData()){
                    return parent;
                }
                else{
                    parent = parent.getParent();
                }
            }
        }
        return null; // If no suitable node is found, it means that the node has the highest value in that tree
    }

    /**
     * Finds the largest node that is smaller than the given node.
     * @param node The node to find its predecessor
     * @return The largest node that is still smaller than the given node
     */
    public TreeNode findPrev(TreeNode node){
        TreeNode rightMost = findRightMost(node); // First possible answer is the node's rightMost child
        if(rightMost != null){
            return rightMost;
        }
        else{//If the node doesn't have a rightMost child, the code climbs up to the root and checks if a suitable node is found
            TreeNode parent = node.getParent();
            while(parent != null){
                if(node.getData() > parent.getData()){
                    return parent;
                }
                else{
                    parent = parent.getParent();
                }
            }
        }
        return null; // If no suitable node is found, it means that the node has the lowest value in that tree
    }

    /**
     * A universal method to fix an unbalanced node. Used inside remove method.
     * @param node The node to fix its imbalance
     */
    private void balance(TreeNode node){
        if(node.heightDifference() < 0){ // The node's right child is taller
            if(node.getRight().heightDifference() > 0){
                // If the right child's left subtree causes the imbalance, a right-left rotation is necessary
                TreeNode leftSubtreeRoot = node.getRight().getLeft();
                rightRotation(leftSubtreeRoot, leftSubtreeRoot.getParent());
                leftRotation(leftSubtreeRoot, leftSubtreeRoot.getParent());
            }
            else{
                // Otherwise, only a single left rotation is enough
                leftRotation(node.getRight(), node);
            }
        }
        else{ // The node's left child is taller
            if(node.getLeft().heightDifference() < 0){
                // If the left child's right subtree causes the imbalance, a left-right rotation is necessary
                TreeNode rightSubtreeRoot = node.getLeft().getRight();
                leftRotation(rightSubtreeRoot, rightSubtreeRoot.getParent());
                rightRotation(rightSubtreeRoot, rightSubtreeRoot.getParent());
            }
            else{
                // Otherwise, a single right rotation is enough
                rightRotation(node.getLeft(), node);
            }
        }
    }

    /**
     * Removes the given node from the tree and ensures the balance of the tree after removal operation
     * by performing rotations.
     * @param target The node to be removed
     * @param parent Target node's parent
     */
    public void remove(TreeNode target, TreeNode parent){
        if(target.getData() == maxVal){ // Updating maxVal and maxNode if the node to be removed is the maxNode
            if(target.getLeft() != null){
                maxNode = target.getLeft();
                maxVal = target.getLeft().getData();
            }
            else{
                if(parent != null){
                    maxNode = parent;
                    maxVal = parent.getData();
                }
            }
        }
        if(target.getRight() == null){ // Target node doesn't have a right child
            if(parent == null){ // Target is root
                root = target.getLeft();
                if(root != null){
                    root.setParent(null);
                    // Special case: It is guaranteed in this case that no imbalance will happen
                    // No need to check for imbalances
                }
            }
            else{ // Target is not root
                if(target.getData() > parent.getData()){
                    parent.setRight(target.getLeft());
                    if(parent.getRight() != null){
                        parent.getRight().setParent(parent);
                    }
                }
                else{
                    parent.setLeft(target.getLeft());
                    if(parent.getLeft() != null){
                        parent.getLeft().setParent(parent);
                    }
                }
                while(parent != null){
                    // Fixing imbalances and updating health values
                    parent.findNewHeight();
                    if(!isBalanced(parent)){
                        balance(parent);
                        return;
                    }
                    parent = parent.getParent();
                }
            }
        }
        else{ // Target node has a right child
            TreeNode leftLeast = findLeftLeast(target);
            if(leftLeast.getParent().getData() == target.getData()){ // leftLeast parent == target
                if(parent == null){ // If target is root
                    leftLeast.setLeft(target.getLeft());
                    if(leftLeast.getLeft() != null){
                        leftLeast.getLeft().setParent(leftLeast);
                    }
                    leftLeast.setParent(null);
                    root = leftLeast;
                    // Special case: Only checking the root's balance is enough
                    if(!isBalanced(root)){
                        balance(root);
                        return;
                    }
                    root.findNewHeight();
                }
                else{ // If target is not root
                    if(target.getData() > parent.getData()){
                        parent.setRight(leftLeast);
                        parent.getRight().setParent(parent);
                    }
                    else{
                        parent.setLeft(leftLeast);
                        parent.getLeft().setParent(parent);
                    }
                    leftLeast.setLeft(target.getLeft());
                    if(leftLeast.getLeft() != null){
                        leftLeast.getLeft().setParent(leftLeast);
                    }
                    // Fixing imbalances and updating health values
                    while(leftLeast != null){
                        leftLeast.findNewHeight();
                        if(!isBalanced(leftLeast)){
                            balance(leftLeast);
                            return;
                        }
                        leftLeast = leftLeast.getParent();
                    }
                }
            }
            else{ // leftLeast parent != target
                TreeNode leftLeastParent = leftLeast.getParent();
                if(parent == null){ // If target is root
                    leftLeastParent.setLeft(leftLeast.getRight());
                    if(leftLeastParent.getLeft() != null){
                        leftLeastParent.getLeft().setParent(leftLeastParent);
                    }
                    leftLeast.setLeft(target.getLeft());
                    if(leftLeast.getLeft() != null){
                        leftLeast.getLeft().setParent(leftLeast);
                    }
                    leftLeast.setRight(target.getRight());
                    if(leftLeast.getRight() != null){
                        leftLeast.getRight().setParent(leftLeast);
                    }
                    leftLeast.setParent(null);
                    root = leftLeast;
                    // Fixing imbalances and updating health values
                    while(leftLeastParent != null){
                        leftLeastParent.findNewHeight();
                        if(!isBalanced(leftLeastParent)){
                            balance(leftLeastParent);
                            // Since the leftLeast node replaced with the target, its height should be updated separately
                            root.findNewHeight();
                            return;
                        }
                        leftLeastParent = leftLeastParent.getParent();
                    }
                }
                else{ // If target is not root
                    if(target.getData() > parent.getData()){
                        leftLeastParent.setLeft(leftLeast.getRight());
                        if(leftLeastParent.getLeft() != null){
                            leftLeastParent.getLeft().setParent(leftLeastParent);
                        }
                        parent.setRight(leftLeast);
                        parent.getRight().setParent(parent);
                    }
                    else{
                        leftLeastParent.setLeft(leftLeast.getRight());
                        if(leftLeastParent.getLeft() != null){
                            leftLeastParent.getLeft().setParent(leftLeastParent);
                        }
                        parent.setLeft(leftLeast);
                        parent.getLeft().setParent(parent);
                    }
                    // Replacing target node with leftLeast node
                    leftLeast.setLeft(target.getLeft());
                    leftLeast.getLeft().setParent(leftLeast);
                    leftLeast.setRight(target.getRight());
                    leftLeast.getRight().setParent(leftLeast);
                    // Fixing imbalances and updating health values
                    while(leftLeastParent != null){
                        leftLeastParent.findNewHeight();
                        if(!isBalanced(leftLeastParent)){
                            balance(leftLeastParent);
                            // Since the leftLeast node replaced with the target, its height should be updated separately
                            leftLeast.findNewHeight();
                            return;
                        }
                        leftLeastParent = leftLeastParent.getParent();
                    }
                }
            }
        }
    }

    /**
     * Finds the given node and returns as an ArrayList as the format: [givenNode, parentNode].
     * If the node is not in the tree, the last node before the null value is returned as: [lastNode].
     * @param data The value of the node to be searched.
     * @return An ArrayList consisting of the target node and its parent (only the lastNode if the target node is not
     * in the tree or null if the tree is empty)
     */
    public ArrayList<TreeNode> find(int data){
        TreeNode targetNode = root;
        TreeNode targetParent = null;
        if(targetNode == null){ // Tree is empty
            return null;
        }
        else{
            while(targetNode != null){
                if(data == targetNode.getData()){ // The node is found
                    ArrayList<TreeNode> ans = new ArrayList<>();
                    ans.add(targetNode);
                    ans.add(targetParent);
                    return ans;
                }
                else if(data < targetNode.getData()){
                    targetParent = targetNode;
                    targetNode = targetNode.getLeft();
                }
                else{
                    targetParent = targetNode;
                    targetNode = targetNode.getRight();
                }
            }
        }
        ArrayList<TreeNode> ans = new ArrayList<>(); // The node does not exist
        ans.add(targetParent);
        return ans;
    }

    /**
     * Finds the node with the minimum value and returns.
     * @return The node with the minimum value
     */
    public TreeNode findMinNode(){
        if(root == null){
            return null;
        }
        else{
            TreeNode targetNode = root;
            while(targetNode.getLeft() != null){
                targetNode = targetNode.getLeft();
            }
            return targetNode;
        }
    }

    /**
     * Returns the node with the highest value in the tree.
     * @return The node with the highest value
     */
    public TreeNode getMaxNode(){
        return maxNode;
    }

    /**
     * Returns the highest node data in the tree.
     * @return The highest node data
     */
    public int getMaxVal(){
        return maxVal;
    }

    /**
     * Checks whether the tree is empty.
     * @return Whether the tree is empty
     */
    public boolean isEmpty(){
        return root == null;
    }

    /**
     * Searches the specified value in the tree. If a node with the exact same value exists, returns this node.
     * Otherwise, returns the node with the lowest value that is still greater than the specified value.
     * @param data The value to be searched
     * @return If exists, the node matching with the given value. Otherwise, the smallest node that is greater than the
     * given value.
     */
    public TreeNode findClosestHigh(int data){
        ArrayList<TreeNode> attackVals;
        attackVals = find(data);
        TreeNode targetNode = null;
        if(attackVals.size() == 2){ // Matching node case
            return attackVals.getFirst();
        }
        else if(attackVals.size() == 1){ // The node with the given value does not exist
            targetNode = attackVals.getFirst();
            if(targetNode.getData() < data){
                do{
                    targetNode = findNext(targetNode); // If the parent node is less, the next node is visited
                }while(targetNode.getData() < data);
            }
        }
        return targetNode;
    }

    /**
     * Searches the specified value in the tree. If a node with the exact same value exists, returns this node.
     * Otherwise, returns the node with the highest value that is still less than the specified value.
     * @param data The value to be searched
     * @return If exists, the node matching with the given value. Otherwise, the largest node that is less than the
     * given value
     */
    public TreeNode findClosestLow(int data){
        ArrayList<TreeNode> attackVals;
        attackVals = find(data);
        TreeNode targetNode = null;
        if(attackVals.size() == 2){ // Matching node case
            return attackVals.getFirst();
        }
        else if(attackVals.size() == 1){ // The node with the given value does not exist
            targetNode = attackVals.getFirst();
            if(targetNode.getData() > data){
                do{
                    targetNode = findPrev(targetNode); // If the parent node is greater, the previous node is visited
                }while(targetNode.getData() > data);
            }
        }
        return targetNode;
    }
}
