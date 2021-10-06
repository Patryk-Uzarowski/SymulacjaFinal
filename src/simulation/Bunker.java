package simulation;

import simulation.soldier.Soldier;

import java.util.Random;

public class Bunker {
    private int occupation;//0 - not occupied, 1 - occupied by defender, 2 - occupied by attacker
    private int bunkerRow;//info on what row was generated for the bunker
    private int bunkerCol;//info on what col was generated for the bunker

    public void boost(Soldier boostedSoldier){//method that boosts the soldier's health points by 8 units
        boostedSoldier.sethP(boostedSoldier.gethP()+8);

    }
    public Bunker(int maxY,int maxX){//basic constructor
        int whereToPlaceBunker;
        Random random = new Random();
        whereToPlaceBunker = random.nextInt(maxY);//random number in range from 0 to (maximal Column-1)
        bunkerCol=whereToPlaceBunker;//adjusts this random number to the variable responsible for holding the info on column number of bunker
        whereToPlaceBunker = random.nextInt(maxX+1);//random number in range from 0 to (Maximal Row number+1)
        bunkerRow = whereToPlaceBunker;//adjusts this random number to the variable responsible for holding the info on row number of bunker
    }

    public int getBunkerRow(){
        return this.bunkerRow;
    }
    public int getBunkerCol(){
        return this.bunkerCol;
    }
    public void setOccupation(int occupation){
        this.occupation=occupation;
    }
    public int getOccupation(){
        return occupation;
    }
}
