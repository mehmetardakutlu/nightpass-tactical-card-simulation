/**
 * Stores Card properties and methods.
 */
public class Card {
    private final String name;
    private final int initHP; // Base HP never changes
    private int initA;
    private int curHP;
    private int missingHP; // For type-2 cases
    private int curA;

    /**
     * Constructor of the class
     * @param name Name of the new card
     * @param initA Base attack value of the new card
     * @param initHP Base HP value of the new card
     */
    Card(String name, int initA, int initHP){
        this.name = name;
        this.initA = initA;
        this.initHP = initHP;
        curA = initA;
        curHP = initHP;
    }

    /**
     * Returns the name of the card.
     * @return The name of the card
     */
    public String getName(){
        return name;
    }

    /**
     * Return the missing HP of a dead card.
     * @return Remaining HP for a full revival
     */
    public int getMissingHP(){
        return missingHP;
    }

    /**
     * Performs revival operations on a card.
     * @param givenHP How many HP's are drawn from health pool
     */
    public void revive(int givenHP){
        missingHP -= givenHP;
        if(missingHP == 0){ // Full revival
            initA = (int) (initA * 90.0 / 100);
            curA = initA;
            curHP = initHP;
        }
        else{ // Partial revival
            initA = (int) (initA * 95.0 / 100);
        }
    }

    /**
     * Returns the current HP of the card.
     * @return Current HP of the card
     */
    public int getCurHP(){
        return curHP;
    }

    /**
     * Sets the new current HP of the card and adjusts its current attack accordingly.
     * @param curHP New current HP of the card
     */
    public void setCurHP(int curHP){
        curA =  Math.max(1, (int) ((initA * curHP * 1.0) / initHP)); // Lower HP weakens the card by reducing its current attack.
        this.curHP = curHP;
        if(curHP == 0){
            missingHP = initHP;
        }
    }

    /**
     * Returns the current attack value of the card.
     * @return Current attack value of the card
     */
    public int getCurA(){
        return  curA;
    }
}
