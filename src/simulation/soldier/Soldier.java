package simulation.soldier;

import simulation.Board;
import simulation.soldier.equipment.Armor;
import simulation.soldier.equipment.Rifle;

public abstract class Soldier {
    protected int id;//unique ID of the certain soldier
    protected int hP = 8;//health points
    protected String type;//helpful variable to indicate weather the soldiers is an attacker or defender
    protected int row;//variable indicates on which row does the soldier currently stays
    protected int col;//variable indicates on which column does the soldier currently stays
    protected int vision = 6;//variable shows the range around soldier, where he can spot the target
    protected Rifle rifle;
    private Armor armor = new Armor();
    protected boolean isInBunker=false;//holds the information if the soldier is currently occupying a bunker


    public Soldier(int row,int col){//basic constructor
        this.row=row;
        this.col=col;
        this.rifle = new Rifle();//creates the rifle
        hP=armor.equpArmor(hP);//adjusts the additional health points from Armor Class
    }

    public int getId() { return id; }//returns the ID of soldier
    public String getTypeofSoldier() { return type; }//returns type of soldier -> "A"-attacker, "D"-defender
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int gethP()  {return this.hP;}
    public void sethP(int ammount)  {this.hP=ammount;}//sets the new value of health points for this soldier
    public void setIsInBunker(boolean inBunker) {this.isInBunker = inBunker;}//sets the occupation of bunker to false/true
    public boolean getIsInBunker() {return isInBunker;}//gets the current information on the occupation of bunker

    public void moveLeftOrRight(Board plansza, int where){//both attackers and defenders left/right movement is based on the same operations...
                                                            //on their indexes, hence this method is being held in Parent Class - Soldier,...
                                                            //and only being inherited in Attacker and Defender
        //where argument in this method, indicates what random number was generated for this specific movement,
        //in case of 1, soldier wants to move left, in case of 2, target wants to move right
        if(isInBunker==false) {//being in bunker blocks the opportunity to move, since soldiers want to defend bunkers, no matter what cost!
            if (where == 1 && col > 0 && plansza.getSoldierFromBoard(row, col - 1) == null) {//checks where soldier wants to go, and if such movement is even possible
                //for such movement to be possible, he must be maximally on the column of index=1, and the position left hand side of the soldier must be clear
                //clear means none other soldier is currently staying there.
                plansza.setSoldierInBoard(plansza.getSoldierFromBoard(row, col), row, col - 1);//if the condition is fulfilled, move the soldier
                plansza.setSoldierInBoard(null, row, col);//clear the previous position of this soldier
                col -= 1;//change its interior variable that holds the info on it's column number
            } else if (where == 2 && col < plansza.getMaxYIndex() && plansza.getSoldierFromBoard(row, col + 1) == null) {
                plansza.setSoldierInBoard(plansza.getSoldierFromBoard(row, col), row, col + 1);
                plansza.setSoldierInBoard(null, row, col);
                col += 1;
            }
            plansza.checkIfBunker(this);//after every movement check if current location of soldier overlaps with the location of generated bunker
        }
    }
    protected void shoot(Soldier target,int przypadek) {//variable 'przypadek' indicates if next methods will count distance between shooter and target based on the difference in their columns or rows
        if (przypadek == 1){
            rifle.shootBullet(target, this.col, przypadek,isInBunker);//in case 1 - they are in the same row (horizontally) -> difference in columns
        } else{
            rifle.shootBullet(target, this.row, przypadek,isInBunker);//in case 0 - they are anywhere but horizontally, -> difference in their rows
        }
    }
    public void checkSurrounding(Board plansza){//0-upwards,downwards,diagonal 1-horizontal
        for(int i=1;i<=vision;i++) {//enlarges the i value until it is equal to the soldier's vision, checking every position in a circle around him with radius = vision.
            if (this.row-i>=0 && plansza.getSoldierFromBoard(this.row-i,this.col)!=null && plansza.getSoldierFromBoard(this.row-i,this.col).getTypeofSoldier()!=this.type) {//look upward Attacker/look downward Defender
                shoot(plansza.getSoldierFromBoard(this.row-i,this.col),0);
            //if statement checks: whether 'this.row-i' statement hasn't gone out of bounds for our matrix , further on it checks if this certain...
            //location isn't clear, so if any soldier is currently staying on this specified position, at the end it checks if the soldier...
            //on that certain position, is his enemy -> (if their type variable differences)
            // if all of the above is fulfilled, shooter takes action, and directly shoots the certain soldier - his enemy.(shoot method, explained earlier)
            //this description applies to all of the cases below, the only thing that changes is the direction of the searching process of the potential shooter,
            //if certain enemy is spotted the loop is being stopped. -> break;
                break;
            }else if (this.col+i<=plansza.getMaxYIndex() && plansza.getSoldierFromBoard(this.row,this.col+i)!=null && plansza.getSoldierFromBoard(this.row,this.col+i).getTypeofSoldier()!=this.type) {//look right attacker/look left Defender
                shoot(plansza.getSoldierFromBoard(this.row,this.col+i),1);
                break;
            }else if(this.col-i>=0 && plansza.getSoldierFromBoard(this.row,this.col-i)!=null && plansza.getSoldierFromBoard(this.row,this.col-i).getTypeofSoldier()!=this.type){//look left attacker/look right Defender
                shoot(plansza.getSoldierFromBoard(this.row,this.col-i),1);
                break;
            }else if( (this.row-i>=0 && this.col+i<=plansza.getMaxYIndex())&& plansza.getSoldierFromBoard(this.row-i,this.col+i)!=null &&plansza.getSoldierFromBoard(this.row-i,this.col+i).getTypeofSoldier()!=this.type){//look upwards right attacker/look backwards left defender
                shoot(plansza.getSoldierFromBoard(this.row-i,this.col+i),0);
                break;
            }else if( (this.row-i>=0 && this.col-i>=0) && plansza.getSoldierFromBoard(this.row-i,this.col-i)!=null && plansza.getSoldierFromBoard(this.row-i,this.col-i).getTypeofSoldier()!=this.type){//look upwards left attacker/look downwards right defender
                shoot(plansza.getSoldierFromBoard(this.row-i,this.col-i),0);
                break;
            }else if(this.row+i<=plansza.getMaxXIndex() && plansza.getSoldierFromBoard(this.row+i,this.col)!=null && plansza.getSoldierFromBoard(this.row+i,this.col).getTypeofSoldier()!=this.type){//look downwards Attacker/look upwards Defender
                shoot(plansza.getSoldierFromBoard(this.row+i,this.col),0);
                break;
            }else if( (this.row+i<=plansza.getMaxXIndex() && this.col-i>=0) && plansza.getSoldierFromBoard(this.row+i,this.col-i)!=null && plansza.getSoldierFromBoard(this.row+i,this.col-i).getTypeofSoldier()!=this.type){//look downwards left Attacker//look upwards right Defender
                shoot(plansza.getSoldierFromBoard(this.row+i,this.col-i),0);
                break;
            }else if( (this.row+i<=plansza.getMaxXIndex() && this.col+i<=plansza.getMaxYIndex()) && plansza.getSoldierFromBoard(this.row+i,this.col+i)!=null && plansza.getSoldierFromBoard(this.row+i,this.col+i).getTypeofSoldier()!=this.type){//look downwards right Attacker/look upwards left Defender
                this.shoot(plansza.getSoldierFromBoard(this.row+i,this.col+i),0);
                break;
            }
        }
    }
}