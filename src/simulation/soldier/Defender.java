package simulation.soldier;

import simulation.Board;

import java.util.Random;

public class Defender extends Soldier {

    public Defender(int id,int row,int col) {
        super(row,col);
        super.id=id;
        super.type="D";
    }


    public void move(Board plansza) {//mechanism described in Attacker Class! Only difference here, is that the defenders don't want to destroy their
        //strategy for fight and go too far in our board, so we implemented that, maximally they can move to 1/3 of the board (their side)
        //for that reason every time we check row in if statements we check weather they are lower then 2, maximally giving opportunity for defenders
        //to move to the row of number 3.
        int where;
        Random random = new Random();
        where = random.nextInt(5);
        if (super.isInBunker == false) {
            switch (where) {
                case 0://Move upwards defender //
                    if (super.getRow() < 3 && plansza.getSoldierFromBoard(super.row + 1, super.col) == null) {
                        plansza.setSoldierInBoard(plansza.getSoldierFromBoard(super.row, super.col), super.row + 1, super.col);
                        plansza.setSoldierInBoard(null, super.row, super.col);
                        super.row += 1;
                    }
                    plansza.checkIfBunker(this);
                    break;
                case 1:
                case 2:
                    super.moveLeftOrRight(plansza, where);
                    break;
                case 3:
                    //Move upwards left Defender
                    if (super.row < 3 && super.col > 0 && plansza.getSoldierFromBoard(super.row + 1, super.col - 1) == null) {
                        plansza.setSoldierInBoard(plansza.getSoldierFromBoard(super.row, super.col), super.row + 1, super.col - 1);
                        plansza.setSoldierInBoard(null, super.row, super.col);
                        super.row += 1;
                        super.col -= 1;
                    }
                    plansza.checkIfBunker(this);
                    break;
                case 4://Move upwards right Defender
                    if (super.row < 3 && super.col < plansza.getMaxYIndex() && plansza.getSoldierFromBoard(super.row + 1, super.col + 1) == null) {
                        plansza.setSoldierInBoard(plansza.getSoldierFromBoard(super.row, super.col), super.row + 1, super.col + 1);
                        plansza.setSoldierInBoard(null, super.row, super.col);
                        super.row += 1;
                        super.col += 1;
                    }
                    plansza.checkIfBunker(this);
                    break;
            }
        }
    }
}