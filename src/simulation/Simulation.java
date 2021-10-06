package simulation;

import simulation.soldier.Attacker;
import simulation.soldier.Defender;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Simulation {
    private List<Attacker> listOfAttackers = new ArrayList<>();//list of attackers
    private List<Defender> listOfDefenders = new ArrayList<>();//list of defenders
    private long startTime;//starting time of simulation
    private long totalTime;//total time of simulation
    private Board plansza;//our Board, where every action takes place
    private int startingAmmountOfAttackers;//helping variable, holding the info of the starting amount of attackers
    private int startingAmmountOfDefenders;//helping variable, holding the info of the starting amount of defenders
    private int whoWon;//0 - Defenders won, 1 - Attackers won

    public Simulation(int ammountOfAttackers, int ammountOfDefenders, int boardMaxX, int boardMaxY) {//constructor
        this.plansza = new Board(boardMaxX, boardMaxY);//we create a new board for the sake of this simulation
        plansza.addjustAttackers(ammountOfAttackers, this);//we adjust the attackers on one side of the board
        plansza.addjustDefenders(ammountOfDefenders, this);//we adjust the attackers on the opposite side of the board
        startingAmmountOfAttackers = ammountOfAttackers;
        startingAmmountOfDefenders = ammountOfDefenders;
        plansza.addjustBunker(ammountOfDefenders);//we create a pseudo-randomly generated bunker and append it to our board
    }

    public void setAttackers(Attacker attacker) {//this method is responsible for adding new attackers to our list
        listOfAttackers.add(attacker); }

    public void setDefenders(Defender defender) {
        listOfDefenders.add(defender); }//this method is responsible for adding new defenders to our list

    private void checkStateOfSoldiers() {//simulation checks if any soldier has died after certain soldier's turn
        for (int i = 0; i < listOfAttackers.size(); i++) {
            if (listOfAttackers.get(i).gethP() <= 0) {//checks if any soldier's health points got down to 0, which means death
                int deathRow = listOfAttackers.get(i).getRow();//if so, we get his location
                int deathCol = listOfAttackers.get(i).getCol();
                if (listOfAttackers.get(i).getIsInBunker() == true) {//if this soldier was in a bunker, we set bunker's occupation to...
                                                                    //not occupied
                    plansza.getBunker().setOccupation(0);
                }plansza.setSoldierInBoard(null, deathRow, deathCol);//we set his location to null, as he is dead
                listOfAttackers.remove(i);//at the end we remove this soldier form the list

            }
        }
        for (int i = 0; i < listOfDefenders.size(); i++) {//same mechanism as above
            if (listOfDefenders.get(i).gethP() <= 0) {
                int deathRow = listOfDefenders.get(i).getRow();
                int deathCol = listOfDefenders.get(i).getCol();
                if (listOfDefenders.get(i).getIsInBunker() == true) {
                    plansza.getBunker().setOccupation(0);
                }
                plansza.setSoldierInBoard(null, deathRow, deathCol);
                listOfDefenders.remove(i);
            }
        }
    }

    private int stop() {//return 0 - not end, return 1 - end
        if (listOfAttackers.size() <= startingAmmountOfAttackers / 5) {//if the overall number of current team is lower or equal...
            //to the 1/3 of the starting amount,the team retreats, and the battle is won by the enemy.
            whoWon = 0;//we set the whoWon variable to 0 indicating that defenders won
            return 1;
        } else if (listOfDefenders.size() <= startingAmmountOfDefenders / 5) {//--||--
            whoWon = 1;//we set the whoWon variable to 1 indicating that attackers won
            return 1;
        } else if (plansza.getBunker().getOccupation() == 2) {//attackers take bunker - they win immediately
            whoWon = 1;//we set the whoWon variable to 1 indicating that attackers won
            return 1;
        }
        return 0;
    }

    public void startSimulation() {//method responsible for starting the simulation
        boolean simCase;
        startTime = System.nanoTime();//we start to count the time from now point on
        System.out.println("Beggining state of simulation: \n");
        plansza.printBoard();//we print our board for the first time to visualize the prior adjustment of both teams
        System.out.println("\n\n");
        for (int i = 0; i < listOfDefenders.size(); i++) {//since the bunker always generates in range of defenders, we check is one of them,
                                                          //overlaps with it
            plansza.checkIfBunker(listOfDefenders.get(i));
        }
        do {
            simCase = mainTurn();//we proceed to the main turn as long as it returns true
        } while (simCase);
        executeSimulation();//we initiate execute simulation method
    }

    private boolean mainTurn() {//Main turn of our simulation
        System.out.println("Main Turn of the simulation\n");
        for (int j = 0; j < listOfAttackers.size(); j++) {//attackers are always starting since they are starting the conflict
            listOfAttackers.get(j).move(plansza);//turn consist of movement and possible shot, here attacker moves
            listOfAttackers.get(j).checkSurrounding(plansza);//and here he checks his surrounding in order to take a shot at the enemy
            checkStateOfSoldiers();//after each movement we check if certain soldier hasn't been killed
        }
        System.out.println("State of board after Attacker's turn: \n");//after Turn of every attacker we print our board
        plansza.printBoard();
        System.out.println("\n\n");
        if (stop() == 1){//we check if stop method indicates that specific team has already won, in that case we print state of soldiers and proceed to the execute method
            System.out.println("State of soldiers after the turn: (if the certain soldier is missing in the list it means he got killed during the turn)\n");
            System.out.println("Attackers:");
            for(int i=0;i<listOfAttackers.size();i++){//printing health points of every soldier that has lasted to this point
                if(listOfAttackers.get(i).getIsInBunker()==true){
                    System.out.println(listOfAttackers.get(i).getTypeofSoldier()+listOfAttackers.get(i).getId()+" Hp: "+listOfAttackers.get(i).gethP()+" - is currently occupying a bunker");
                }else {
                    System.out.println(listOfAttackers.get(i).getTypeofSoldier() + listOfAttackers.get(i).getId() + " Hp: " + listOfAttackers.get(i).gethP());
                }
            }
            System.out.println("\nDefenders:");
            for(int i=0;i<listOfDefenders.size();i++){//--||--
                if(listOfDefenders.get(i).getIsInBunker()==true){
                    System.out.println(listOfDefenders.get(i).getTypeofSoldier()+listOfDefenders.get(i).getId()+" Hp: "+listOfDefenders.get(i).gethP()+" - is currently occupying a bunker");
                }else{
                    System.out.println(listOfDefenders.get(i).getTypeofSoldier()+listOfDefenders.get(i).getId()+" Hp: "+listOfDefenders.get(i).gethP());
                }

            }
            System.out.println("\n\n");
            return false;}//we are ending our method and proceeding to execute method
        for (int i = 0; i < listOfDefenders.size(); i++) {//same mechanism here
            listOfDefenders.get(i).move(plansza);
            listOfDefenders.get(i).checkSurrounding(plansza);
            checkStateOfSoldiers();
        }
        System.out.println("State of board after Defender's turn: \n");
        plansza.printBoard();
        System.out.println("\n");
        System.out.println("State of soldiers after the turn: (if the certain soldier is missing in the list it means he got killed during the turn)\n");
        System.out.println("Attackers:");
        for(int i=0;i<listOfAttackers.size();i++){
            if(listOfAttackers.get(i).getIsInBunker()==true){
                System.out.println(listOfAttackers.get(i).getTypeofSoldier()+listOfAttackers.get(i).getId()+" Hp: "+listOfAttackers.get(i).gethP()+" - is currently occupying a bunker");
            }else{
                System.out.println(listOfAttackers.get(i).getTypeofSoldier()+listOfAttackers.get(i).getId()+" Hp: "+listOfAttackers.get(i).gethP());
            }
        }
        System.out.println("\nDefenders:");
        for(int i=0;i<listOfDefenders.size();i++){
            if(listOfDefenders.get(i).getIsInBunker()==true){
                System.out.println(listOfDefenders.get(i).getTypeofSoldier()+listOfDefenders.get(i).getId()+" Hp: "+listOfDefenders.get(i).gethP()+" - is currently occupying a bunker ");
            }else{
                System.out.println(listOfDefenders.get(i).getTypeofSoldier()+listOfDefenders.get(i).getId()+" Hp: "+listOfDefenders.get(i).gethP());
            }
        }
        System.out.println("\n\n");
        if (stop() == 1)
            return false;
        return true;//if no condition on weather to stop the simulation was fulfilled, we proceed to the next turn.
    }

    private void executeSimulation() {
        long endTime = System.nanoTime();
        totalTime = (endTime - startTime);// at the end of simulation we check how long the battle actually took
        if(whoWon==0)
            System.out.println("Winner: Defenders, time: " + totalTime/Math.pow(10,9)+" sec.");//printing results( time in seconds)
        else
            System.out.println("Winner: Attackers, time: " + totalTime/Math.pow(10,9)+" sec.");//printing results( time in seconds)
        exportResults();//exporting the results to the exterior .txt file

    }

    private void exportResults() {//standard method for exporting the results
        String resultsForExport = "Results at the end of simulation:\n";
        resultsForExport += "Ammount of attackers: " + listOfAttackers.size() + "\n";
        resultsForExport += "Ammount of defenders: " + listOfDefenders.size() + "\n";
        if (whoWon == 0)
            resultsForExport += "Winning team: Defenders\n";
        else
            resultsForExport += "Winning team: Attackers\n";
        resultsForExport += "Total time of the battle: " + totalTime/Math.pow(10,9) + " sec.\n";
        try {
            FileWriter write = new FileWriter("results.txt");
            write.write(resultsForExport);
            write.close();
        } catch (IOException e) {
            System.out.println("Error with writing to the file!");
        }
        System.out.println("\nResults has been sent to the exterior file called results.txt, it is located in the SymulacjaFinal file ");
    }

    public static int inputData(){//Static method that is responsible for checking weather input data is fulfilling our conditions, and if it is
                                  // an integer at all.
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Integer number from range <0,50>: ");
            try {
                    int input = scanner.nextInt();
                    if(input<0||input>50){//here if it is from range <0,50>, since we have a 10x10 matrix
                        System.out.println("Wrong!");
                        scanner.nextLine();

                    }else
                        return input;
            }catch (java.util.InputMismatchException e) {//here we check if user input is an integer
                System.out.println("Wrong!");
                scanner.nextLine();}
        }
    }
}

