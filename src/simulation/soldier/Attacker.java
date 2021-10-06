package simulation.soldier;

import simulation.Board;

import java.util.Random;

public class Attacker extends Soldier {


    public Attacker(int id,int row,int col) {//basic constructor
        super(row,col);//induce the Parent Class constructor - Soldier
        super.id=id;//add id
        super.type="A";//add type - "A" - Attacker
    }


    public void move(Board plansza) {
        int where;//pseudo random number from range 0-5, that indicates where certain soldier would want to go
        Random random = new Random();
        where = random.nextInt(5);
        if(super.isInBunker == false){//if certain soldier is in bunker, block the movement since he wants to take the bunker
            switch (where) {
                case 0://Move upwards attacker
                    if (super.getRow() > 0 && plansza.getSoldierFromBoard(super.row - 1, super.col) == null) {
                        //checks if such movement is even possible - for him to move upwards, he can maximally be on the row of index 1,
                        //if not, he would want to move out of bonds of our matrix (For example if he was on row of index 0, and such condition
                        //wouldn't be applied to our if statements, the soldier would procc the error in next lines of code, since he would like to
                        //change it's location to some of row index = -1.
                        //next we check if certain location, that the soldier aspires to go to, is clear, in other words, if no other soldier
                        //is currently there, for that we use our board, to check if certain location is clear, if not, such movement isn't possible.
                        plansza.setSoldierInBoard(plansza.getSoldierFromBoard(super.row, super.col), super.row - 1, super.col);
                        //if all of the statements are fulfilled, we change the state of the board, so we change the location of this soldier
                        //we do that through setSoldierInBoard method, from getSoldierFromBoard method we indicate, which soldier we want to move,
                        // and through two next arguments in setSoldierInBoard method, we indicate where do we want to move it (change location) on our board.
                        plansza.setSoldierInBoard(null, super.row, super.col);//after such movement, we clear the previous location of our soldier
                        super.row -= 1;//and we change its interior variables holding the information of its location
                    }
                    plansza.checkIfBunker(this);//after every movement check if current location of soldier overlaps with the location of generated bunker
                    break;
                case 1:
                case 2://to move horizontally we start moveLeftOrRight method from Soldier Class
                    super.moveLeftOrRight(plansza, where);
                    break;
                case 3:
                    //Move upwards left
                    if (super.row > 0 && super.col > 0 && plansza.getSoldierFromBoard(super.row - 1, super.col - 1) == null) {
                        plansza.setSoldierInBoard(plansza.getSoldierFromBoard(super.row, super.col), super.row - 1, super.col - 1);
                        plansza.setSoldierInBoard(null, super.row, super.col);
                        super.row -= 1;
                        super.col -= 1;
                    }plansza.checkIfBunker(this);
                    break;
                case 4://Move upwards right
                    if (super.row > 0 && super.col < plansza.getMaxYIndex() && plansza.getSoldierFromBoard(super.row - 1, super.col + 1) == null) {
                        plansza.setSoldierInBoard(plansza.getSoldierFromBoard(super.row, super.col), super.row - 1, super.col + 1);
                        plansza.setSoldierInBoard(null, super.row, super.col);
                        super.row -= 1;
                        super.col += 1;
                    }plansza.checkIfBunker(this);
                    break;
            }
        }
    }
}
