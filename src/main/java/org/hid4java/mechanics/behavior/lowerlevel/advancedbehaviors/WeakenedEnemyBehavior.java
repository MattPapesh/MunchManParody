package org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.PowerPellet;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.mechanic.MechanicScheduler;
import org.hid4java.mechanics.movement.EntityMovement;

public class WeakenedEnemyBehavior extends EnemyRetreatingWanderBehavior
{
    private boolean pellet_A_eaten = false, pellet_B_eaten = false, pellet_C_eaten = false, pellet_D_eaten = false, trigger = false; 
    private PowerPellet A = null, B = null, C = null, D = null;
    private int millis = 0; long init_millis = 0;
    private Enemy enemy = null;

    public WeakenedEnemyBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    PowerPellet A, PowerPellet B, PowerPellet C, PowerPellet D, int millis) 
    {
        super(enemy_movement, stage, enemy, munch_man, 1.0, 1.0, 10, 20, 10);
        this.A = A; 
        this.B = B;
        this.C = C; 
        this.D = D; 
        this.enemy = enemy;
        this.millis = millis;
    }

    @Override
    public void initializeBehavior() 
    {
        enemy.setAnimation(enemy.getAnimation(1).getName());;
    }

    @Override
    public void endBehavior(boolean interrupted) 
    {
        enemy.setAnimation(enemy.getAnimation(0).getName());
    }

    @Override
    public boolean isBehaviorFinished() 
    {
        return !isSelfSchedulingConditionsMet();
    }

    @Override
    public boolean isSelfSchedulingConditionsMet() {
        if(!pellet_A_eaten && A.getEaten()) {
            trigger = true;
            pellet_A_eaten = true; 
            init_millis = System.currentTimeMillis();
        }
        if(!pellet_B_eaten && B.getEaten()) {
            trigger = true;
            pellet_B_eaten = true; 
            init_millis = System.currentTimeMillis();
        }
        if(!pellet_C_eaten && C.getEaten()) {
            trigger = true;
            pellet_B_eaten = true; 
            init_millis = System.currentTimeMillis();
        }
        if(!pellet_D_eaten && D.getEaten()) {
            trigger = true;
            pellet_D_eaten = true; 
            init_millis = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - init_millis >= millis) {
            trigger = false; 
        }

        return trigger;  
    }
}
