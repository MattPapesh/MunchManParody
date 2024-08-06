package org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.PowerPellet;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.mechanics.movement.EntityMovement;

public class WeakenedEnemyBehavior extends EnemyRetreatingWanderBehavior
{
    private boolean pellet_A_eaten = false, pellet_B_eaten = false, pellet_C_eaten = false, pellet_D_eaten = false, trigger = false; 
    private PowerPellet A = null, B = null, C = null, D = null;
    private int millis = 0; long init_millis = 0;
    private boolean eaten = false; 
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
        eaten = false; 
        enemy.setAnimation(enemy.getAnimation(1).getName());
        enemy.setSpeed(enemy.getSpeed() * 0.5);
    }

    
    @Override
    public void executeBehavior()
    {
        if(GameMath.isCoordsEqual(enemy.getStageCoords(), getMunchManStageCoords())) {
            eaten = true;
        }
        if(eaten) {
            enemy.reset(enemy.SPAWN_STAGE_COORD.getX(), enemy.SPAWN_STAGE_COORD.getY(), enemy.SPAWN_STAGE_COORD.getDegrees());
        }

        if(overrider != null && !overrider.get().isSelfScheduling() 
        && overrider.get().isSelfSchedulingConditionsMet())
        {
            overrider.get().setSelfScheduling(true);
            if(scheduled_behavior_index != -1) 
            {
                behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
                scheduled_behavior_index = -1;
            }
        }
        else if(overrider != null && overrider.get().isSelfScheduling() 
        && !overrider.get().isSelfSchedulingConditionsMet())
        {
            overrider.get().setSelfScheduling(false);
        }

        for(int i = 0; i < behaviors.size() && (overrider == null || !overrider.get().isSelfScheduling()); i++)
        {
            if(behaviors.get(i).get().isSelfSchedulingConditionsMet() 
            && !behaviors.get(i).get().isSelfScheduling() 
            && scheduled_behavior_index != -1 && scheduled_behavior_index != i
            && !behaviors.get(scheduled_behavior_index).get().isScheduled())
            {
                behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
                behaviors.get(i).get().setSelfScheduling(true);
                scheduled_behavior_index = i; 
                break;
            }
            else if(behaviors.get(i).get().isSelfSchedulingConditionsMet() 
            && !behaviors.get(i).get().isSelfScheduling() 
            && scheduled_behavior_index == -1)
            {
                behaviors.get(i).get().setSelfScheduling(true);
                scheduled_behavior_index = i;
                break;
            }
        }
    }

    @Override
    public void endBehavior(boolean interrupted)
    {   
        enemy.setSpeed(enemy.getSpeed() * 2.0);
        enemy.setAnimation(enemy.getAnimation(0).getName());
        
        if(scheduled_behavior_index >= 0) {
            behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
            scheduled_behavior_index = -1;
        }
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

        return enemy.isWeakenedState();  
    }
}
