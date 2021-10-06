package simulation;

import simulation.soldier.Attacker;
import simulation.soldier.Defender;
import simulation.soldier.Soldier;

public class Board {
    private int x;//amounts of rows in matrix
    private int y;//amount of cols in matrix
    private Soldier[][] board;//overall matrix where agents can take actions
    private Bunker bunker;



    public Board(int x, int y)//basic constructor indicating the size of board
    {
        this.x = x;
        this.y = y;
        this.board = new Soldier[this.x][this.y];

    }

    public void addjustBunker(int howManyDefender){//method that generates a bunker in random position, which is located in range of the defending
        //team location, so if for example we have 20 defenders, the bunker will be generated in random location up to the row of number 1
        //if 30 defenders, random location up to the row of number 2 and so on and so fourth.
        if(howManyDefender%10==0&&howManyDefender!=0)
            bunker = new Bunker(y,(howManyDefender/10)-1);
        else{
            bunker = new Bunker(y,howManyDefender/10);
        }
    }

    public void printBoard(){//prints the board, '__'  meaning no one currently stays on that position, A[number], D[number], indicates where
            //agents from teams are located.
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                if (this.board[i][j] != null) {
                    System.out.print(this.board[i][j].getTypeofSoldier()+this.board[i][j].getId()+" ");
                } else {
                    System.out.print("__ ");
                }if(j==y-1){
                    System.out.println("\n");
                }
            }
            System.out.println();
        }
    }



      public void addjustAttackers(int how_many,Simulation simulation){//this method sets up the attacking team on the top side of the matrix
        //(bottom side in the actual simulation in perspective of a viewer), so it sets up the team accordingly starting from location [max Row,0]
        int id_A=0;//in addition to adjusting soldiers it also creates the soldiers and from this point on, holds the information on every soldier,
                   //inside our Soldier board matrix
        for(int k=0,i=0,j=x-1;i<how_many;i++){
            if(k==y){
                j-=1;
                k=0;
            }
            Attacker sample_Attacker = new Attacker(id_A,j,k);//creates the new attackers and sends the required properties to the Class itself,
            this.board[j][k]= sample_Attacker;//assigning created soldier to the certain position on the board
            simulation.setAttackers(sample_Attacker);//we send the information, to the Simulation, on adding created soldier to the List of soldiers
            k++;//we increase the k variable, and in case it hits the maximum column, (look at the if(k==y)...,it changes the row to the next one,
                //so in case of an attacker, we change the row from maximum, to maximum-1, and start to adjust next attackers from location [maxRow-1,0]
            id_A++;//we increase the ID of the soldiers, starting from 0.

        }
      }
    public void addjustDefenders(int how_many,Simulation simulation){//same mechanism as in adjustAttackers.
        int id_D=0;
        for(int k=0,i=0,j=0;i<how_many;i++){
            if(k==y){
                j+=1;
                k=0;
            }
            Defender sample_Defender = new Defender(id_D,j,k);
            this.board[j][k]= sample_Defender;
            simulation.setDefenders(sample_Defender);
            k++;
            id_D++;

        }
    }
    public int getMaxXIndex(){
        return this.x-1;
    }
    public int getMaxYIndex(){
        return this.y-1;
    }
    public Soldier getSoldierFromBoard(int x,int y){
        return this.board[x][y];
    }
    public void setSoldierInBoard(Soldier soldier,int x,int y){
        this.board[x][y]=soldier;
    }
    public Bunker getBunker(){
        return this.bunker;
    }

    public void checkIfBunker(Soldier soldier){//board checks if location of certain soldier overlaps with pre-generated location of a bunker.
        if(soldier.getRow()==bunker.getBunkerRow()&&soldier.getCol()==bunker.getBunkerCol()&&bunker.getOccupation()==0) {
            soldier.setIsInBunker(true);//we change the isInBunker property to true
            bunker.boost(soldier);//we give soldier addition health points, and in Bullet class we also increase the probability of a bullet to hit a target
            switch (soldier.getTypeofSoldier()) {//to hold the info on what type of soldier is currently occupying bunker, we change the occupation from 0-2
                case "A":
                    bunker.setOccupation(2);//if it is an attacker, we set the occupation value to 2
                    break;
                case "D":
                    bunker.setOccupation(1);//if it is an defender, we set the occupation value to 1
                    break;
            }
        }
    }
}

