package simulation;

public class Main {
    public static void main(String[] args) {
        int atakujacy,broniacy;
        System.out.println("Welcome to the Battlefield simulation!\n");
        System.out.println("Give the number of attackers: ");
        atakujacy = Simulation.inputData();
        System.out.println("Give the number of defenders: ");
        broniacy = Simulation.inputData();
        Simulation symulacja = new Simulation(atakujacy,broniacy,10,10);
        symulacja.startSimulation();
    }
}