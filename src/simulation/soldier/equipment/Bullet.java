package simulation.soldier.equipment;

import simulation.soldier.Soldier;

import java.util.Random;

public class Bullet {
    private int bulletDmg=8;

    public void swoosh(Soldier target,boolean isInBunker){//bullet movement on the board and the probability of it to hit the certain target
        int doesItHit;
        Random random = new Random();
        doesItHit = random.nextInt(100);
        if(isInBunker==true) {//in case of shooter being inside the bunker, his probability of hitting the target increases
            if (doesItHit >= 25) {//probability of 3/4
                dealDmgHp(target);
            }
        }else {
            if (doesItHit >= 50) {//outside of the bunker, probability 1/2.
                dealDmgHp(target);
            }
        }
    }
    private void dealDmgHp(Soldier target){ //direct damage to a target,changing the target's Health points
        target.sethP(target.gethP()-bulletDmg);
    }
}
