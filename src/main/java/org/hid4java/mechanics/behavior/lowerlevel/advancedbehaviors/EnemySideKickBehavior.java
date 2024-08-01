package org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Constants;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorBase;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemySideKickBehavior extends EnemyBehaviorBase{
    private Enemy hero_enemy = null;
    private double route_completion_pct = 0.1;

    public EnemySideKickBehavior(EntityMovement enemy_movement, Stage stage, Enemy side_kick, Enemy hero, MunchMan munch_man)
    {
        super(enemy_movement, stage, side_kick, munch_man);
        this.hero_enemy = hero;
    }

    private void computeSideKickBehavior() 
    {
        int munch_man_x = getMunchManStageCoords().getX();
        int munch_man_y = getMunchManStageCoords().getY();
        int enemy_x = (2 * munch_man_x) - hero_enemy.getStageCoords().getX();
        int enemy_y = (2 * munch_man_y) - hero_enemy.getStageCoords().getY();

        double w = Constants.STAGE_CHARACTERISTICS.STAGE_DATA[0].length;
        double h = Constants.STAGE_CHARACTERISTICS.STAGE_DATA.length; 
        
        for(int i = 0; i < 1 && !(enemy_x >= 0 && enemy_x < w); i++)
            enemy_x = (int)((enemy_x >= w) ? enemy_x  - (2.0 * w): ((enemy_x < 0) ? -enemy_x : enemy_x));
        for(int i = 0; i < 1 && !(enemy_y >= 0 && enemy_y < h); i++)
            enemy_y = (int)((enemy_y >= h) ? enemy_y - (2.0 * h) : ((enemy_y < 0) ? -enemy_y : enemy_y));

        setEnemyTarget(route_completion_pct, 1, enemy_x, enemy_y);
    }

    @Override
    public void initializeBehavior() 
    {
        computeSideKickBehavior();
    }

    @Override
    public void executeBehavior()
    {
        if(isEnemyRouteCompleted())
        {
            computeSideKickBehavior();
        }
    }

    @Override
    public void endBehavior(boolean interrupted) 
    {
        setEnemyTarget(0, getEnemyStageCoords().getX(), getEnemyStageCoords().getY());
    }
}
