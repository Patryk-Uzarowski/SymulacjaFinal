package simulation.soldier.equipment;

public class Armor {
    private int armorClass = 8;


    public int equpArmor(int hP){
        return hP+armorClass;
    }//equip the armor, give certain soldier additional health points
}
