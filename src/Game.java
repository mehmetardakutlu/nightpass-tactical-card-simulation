/**
 * Stores the Survivor's both living and dead cards in an organized manner. Keeps track of the scores of the Survivor
 * and the Stranger. Involves methods for performing battle commands and queries.
 */
public class Game {
    private int deckSize;
    private int discardPileSize;
    private final QueueTree discardTree; // Discarded cards are stored
    private int survivorPoints;
    private int strangerPoints;
    private final FreeHTree healthTree;
    private final AttackTree attackTree;

    /**
     * Constructor of the game class.
     */
    Game(){
        discardTree = new QueueTree();
        healthTree = new FreeHTree();
        attackTree = new AttackTree();
    }

    /**
     * Draws a new card and adds it to the deck. There are two different trees tracking the cards: a health tree
     * that only stores health values as integers and a nested attack tree that stores both attack and health values.
     * Cards are stored in attack tree.
     * @param name Name of the new card
     * @param attack Attack of the new card
     * @param hp HP of the new card
     * @return The draw_card string that is written to the txt file
     */
    public String draw_card(String name, int attack, int hp){
        healthTree.insert(hp);
        attackTree.insert(new Card(name, attack, hp));
        deckSize ++;
        return "Added " + name + " to the deck\n";
    }

    /**
     * Checks the current number of cards in the deck.
     * @return The deckCount string that is written to the txt file
     */
    public String deckCount(){
        return "Number of cards in the deck: " + deckSize + "\n";
    }

    /**
     * Checks which side has more points. If there is a tie, the Survivor is accepted as winning.
     * @return Which side has more points, either the Survivor or the Stranger. The output
     * string is written to the txt file
     */
    public String findWinning(){
        if(survivorPoints >= strangerPoints){
            return "The Survivor, Score: " + survivorPoints + "\n";
        }
        else{
            return "The Stranger, Score: " + strangerPoints + "\n";
        }
    }

    /**
     * Checks how many cards are waiting in the discard pile.
     * @return The number of cards in the discard pile. The output string is written to the txt file
     */
    public String discardPileCount(){
        return "Number of cards in the discard pile: " + discardPileSize + "\n";
    }

    /**
     * Performs the healing phase after the battle phase for type-2 cases.
     * @param healPool The HP amount in the heal pool
     * @return How many cards are revived during the phase
     */
    public int revive(int healPool){
        int revivedCardNum = 0; // Stores the number of revived cards
        TreeNode maxNode;
        TreeNode minNode;
        while(healPool > 0){
            if(discardTree.isEmpty()){ // If no cards are left in the discard pile, remaining heal points go into waste
                return revivedCardNum;
            }
            maxNode = discardTree.getMaxNode();
            if(healPool >= maxNode.getData()){
                // If heal pool is greater than every dead card's missing HP, the card with max missing HP takes heal points
                Card targetCard = discardTree.remove(maxNode);
                int missHP = targetCard.getMissingHP();
                targetCard.revive(missHP); // Full revival
                healPool -= missHP; // Used points are deduced from the health pool
                healthTree.insert(targetCard.getCurHP()); // The card is sent back to the deck
                attackTree.insert(targetCard);
                revivedCardNum += 1;
                discardPileSize -= 1;
                deckSize += 1;
            }
            else{ // The heal pool is not enough to fully revive the card with the greatest missing HP
                minNode = discardTree.findMinNode();
                if(minNode.getData() <= healPool){ // There are still at least one card that can be fully revived
                    TreeNode targetNode = discardTree.findClosestLow(healPool);
                    Card targetCard = discardTree.remove(targetNode);
                    int missHP = targetCard.getMissingHP();
                    targetCard.revive(missHP); // Full revival
                    healPool -= missHP; // Used points are deduced from the health pool
                    healthTree.insert(targetCard.getCurHP()); // The card is sent back to the deck
                    attackTree.insert(targetCard);
                    revivedCardNum += 1;
                    discardPileSize -= 1;
                    deckSize += 1;
                }
                else{
                    // If the heal pool is not enough to fully revive any card, the card with the least missing HP
                    // takes remaining points
                    Card targetCard = discardTree.remove(minNode);
                    targetCard.revive(healPool); // Partial revival
                    healPool -= healPool; // Heal pool becomes empty
                    discardTree.insertDead(targetCard);
                }
            }
        }
        return revivedCardNum;
    }

    /**
     * Performs the battle command by choosing the appropriate card (if exists) in the deck and fighting the
     * chosen card with the Stranger's card. Updates the deck and both sides' points after the fight.
     * @param att The attack value of the Stranger's card
     * @param hp The HP of the stranger's card
     * @param heal The amount of the heal pool
     * @return The result of the battle, indicating which card is played with which priority, whether the played
     * card survived and how many cards are revived. The output string is written to the txt file
     */
    public String battle(int att, int hp, int heal){
        if(healthTree.isEmpty()){ // There might be no card in the deck to play
            int revivedNum = revive(heal);
            return "No cards to play, " + revivedNum + " cards revived\n";
        }
        int maxAttack = attackTree.getMaxVal();
        if(healthTree.getMaxVal() > att){ // There are cards that can survive, #P1 or #P2
            TreeNode attackNode;
            if(maxAttack >= hp){ // There are cards that can kill, but there is still a chance that any card
                // can't both kill and survive, so #P1 is not guaranteed
                attackNode = attackTree.findClosestHigh(hp); // Starting with the lowest attack card that can kill
                int maxHealth;
                while(attackNode != null){ // Traversing the attack tree
                    maxHealth = attackNode.getSubTree().getMaxVal();
                    if(maxHealth > att){ // Found desired node (one of the cards in this node can both can kill and survive)
                        TreeNode healthNode = attackNode.getSubTree().findClosestHigh(att + 1);
                        // The first card entered to the deck is chosen (tiebreaker)
                        Card targetCard = attackTree.returnCard(attackNode, healthNode);
                        healthTree.remove(targetCard.getCurHP());
                        survivorPoints += 2;
                        targetCard.setCurHP(targetCard.getCurHP() - att); // Played card survives
                        strangerPoints ++;
                        healthTree.insert(targetCard.getCurHP()); // and it is sent back to the deck
                        attackTree.insert(targetCard);
                        int revivedNum = revive(heal); // Revive method is called after the fight
                        return "Found with priority 1, Survivor plays " + targetCard.getName() +
                                ", the played card returned to deck, " + revivedNum + " cards revived\n";
                    }
                    else{ // If the current node doesn't satisfy #P1, goes to the next attack node in ascending order
                        attackNode = attackTree.findNext(attackNode);
                    }
                }
                // If the traversal is finished and no appropriate card is found, it means the correct priority is #P2
                attackNode = attackTree.findClosestLow(hp - 1);
                while(attackNode != null){ // Traversing the attack tree
                    maxHealth = attackNode.getSubTree().getMaxVal();
                    if(maxHealth > att) { // Found desired node (the card with the greatest attack that can still
                        // survive is in this node)
                        TreeNode healthNode = attackNode.getSubTree().findClosestHigh(att + 1);
                        // The first card entered to the deck is chosen (tiebreaker)
                        Card targetCard = attackTree.returnCard(attackNode, healthNode);
                        healthTree.remove(targetCard.getCurHP());
                        survivorPoints ++;
                        targetCard.setCurHP(targetCard.getCurHP() - att); // Played card survives
                        strangerPoints ++;
                        healthTree.insert(targetCard.getCurHP()); // and it is sent back to the deck
                        attackTree.insert(targetCard);
                        int revivedNum = revive(heal); // Revive method is called after the fight
                        return "Found with priority 2, Survivor plays " + targetCard.getName() +
                                ", the played card returned to deck, " + revivedNum + " cards revived\n";
                    }
                    else{ // If the current node doesn't satisfy #P2, goes to the next attack node in descending order
                        attackNode = attackTree.findPrev(attackNode);
                    }
                }
            }
            else{ // There are no cards that can kill, so #P2 is guaranteed
                attackNode = attackTree.findClosestLow(hp - 1);
                int maxHealth;
                while(attackNode != null){ // Traversing the attack tree
                    maxHealth = attackNode.getSubTree().getMaxVal();
                    if(maxHealth > att) { // Found desired node (the card with the greatest attack that can still
                        // survive is in this node)
                        TreeNode healthNode = attackNode.getSubTree().findClosestHigh(att + 1);
                        // The first card entered to the deck is chosen (tiebreaker)
                        Card targetCard = attackTree.returnCard(attackNode, healthNode);
                        healthTree.remove(targetCard.getCurHP());
                        survivorPoints ++;
                        targetCard.setCurHP(targetCard.getCurHP() - att); // Played card survives
                        strangerPoints ++;
                        healthTree.insert(targetCard.getCurHP()); // and it is sent back to the deck
                        attackTree.insert(targetCard);
                        int revivedNum = revive(heal); // Revive method is called after the fight
                        return "Found with priority 2, Survivor plays " + targetCard.getName() +
                                ", the played card returned to deck, " + revivedNum + " cards revived\n";
                    }
                    else{ // If the current node doesn't satisfy #P2, goes to the next attack node in descending order
                        attackNode = attackTree.findPrev(attackNode);
                    }
                }
            }
        }
        else{ // If no card can survive, #P3 or #P4
            TreeNode attackNode;
            if(maxAttack >= hp){ // There are cards that can kill, so #P3 is guaranteed
                attackNode = attackTree.findClosestHigh(hp); // Min attack that can kill is chosen
                TreeNode healthNode = attackNode.getSubTree().findMinNode(); // Prioritizing min health
                // The first card entered to the deck is chosen (tiebreaker)
                Card targetCard = attackTree.returnCard(attackNode, healthNode);
                healthTree.remove(targetCard.getCurHP());
                deckSize --;
                survivorPoints += 2;
                strangerPoints += 2;
                targetCard.setCurHP(0); // Played card dies
                discardTree.insertDead(targetCard); // and it is sent to the discard pile
                discardPileSize ++;
                int revivedNum = revive(heal); // Revive method is called after the fight
                return "Found with priority 3, Survivor plays " + targetCard.getName() +
                        ", the played card is discarded, " + revivedNum + " cards revived\n";
            }
            else{ // No card can either kill or survive, so #P4 is guaranteed
                attackNode = attackTree.getMaxNode();
                TreeNode healthNode = attackNode.getSubTree().findMinNode();
                // Max attack with min health is prioritized
                Card targetCard = attackTree.returnCard(attackNode, healthNode);
                // The first card entered to the deck is chosen (tiebreaker)
                healthTree.remove(targetCard.getCurHP());
                deckSize --;
                survivorPoints ++;
                strangerPoints += 2;
                targetCard.setCurHP(0); // Played card dies
                discardTree.insertDead(targetCard); // and it is sent to the discard pile
                discardPileSize ++;
                int revivedNum = revive(heal); // Revive method is called after the fight
                return "Found with priority 4, Survivor plays " + targetCard.getName() +
                        ", the played card is discarded, " + revivedNum + " cards revived\n";
            }
        }
        return "Error\n"; // The code must not be able to reach this line by its formation
    }

    /**
     * Performs the Stranger's card stealing ability by choosing the ideal card considering health and
     * attack limits.
     * @param att Open lower bound for attack values of the target cards (i.e. a card must have at least att + 1
     *            attack value to become the Stranger's target)
     * @param hp Open lower bound for health values of the target card (i.e. a card must have at least hp + 1
     *           health value to become the Stranger's target)
     * @return The result of the stealing attempt. The output string is written to the txt file
     */
    public String steal_card(int att, int hp){
        int maxAttack = attackTree.getMaxVal();
        int maxHealth = healthTree.getMaxVal();
        // Quick checks whether both of the limits can be reached
        if(maxAttack <= att || maxHealth <= hp){
            return "No card to steal\n";
        }
        TreeNode attackNode;
        attackNode = attackTree.findClosestHigh(att + 1); // Starting with the lowest attack satisfying the limit
        TreeNode healthNode;
        while(attackNode != null){ // Traversing the tree
            maxHealth = attackNode.getSubTree().getMaxVal();
            if(maxHealth > hp){ // Ideal target is in this health tree
                healthNode = attackNode.getSubTree().findClosestHigh(hp + 1);
                // The first card entered the deck is stolen (tiebreaker)
                Card targetCard = attackTree.returnCard(attackNode, healthNode);
                healthTree.remove(targetCard.getCurHP());
                // The stolen card isn't sent to either deck or the discard pile, it disappears
                deckSize -= 1;
                return "The Stranger stole the card: " + targetCard.getName() + "\n";
            }
            else{ // If the current node doesn't have a card satisfying stealing conditions, goes to the next
                  // attack node in ascending order
                attackNode = attackTree.findNext(attackNode);
            }
        } // No card may be satisfying both of the limits simultaneously
        return "No card to steal\n";
    }
}
