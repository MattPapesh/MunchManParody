package mechanics.behavior.lowerlevel.advancedbehaviors;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behaviorbases.EnemyBehaviorBase;
import mechanics.movement.EntityMovement;

public class EnemySideKickBehavior extends EnemyBehaviorBase{
    private Enemy hero_enemy = null;
    private double route_completion_pct = 0.3;

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
        setEnemyTarget(route_completion_pct, enemy_x, enemy_y);
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
