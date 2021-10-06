package simulation.soldier.equipment;

import simulation.soldier.Soldier;

import static java.lang.Math.abs;

public class Rifle {
    private int range=4;//range of the rifle in which it can try to hit the target

    public void shootBullet(Soldier target,int wartosc,int przypadek,boolean isInBunker){
        if(przypadek==1){//in case 1, when target and shooter are in the same row, the distance beetwen them is counted based on the difference in their columns
            if(abs(target.getCol()-wartosc)<=range) {//counting the distance between target and shooter, 'wartosc' is the shooter's column
                Bullet bullet = new Bullet();//if the condition is fulfilled, rifle creates a bullet and shoots the target
                bullet.swoosh(target,isInBunker);//movement of the bullet
            }
        }else{//in case 0, which means literally any other positions of target-shooter on the board, the distance between them is counted based on the difference in their rows.
            if(abs(target.getRow()-wartosc)<=range) {//counting the distance between target and shooter, 'wartosc' is the shooter's row
                Bullet bullet = new Bullet();//if the condition is fulfilled, rifle creates a bullet and shoots the target
                bullet.swoosh(target,isInBunker);//movement of the bullet
            }
        }
    }
}
