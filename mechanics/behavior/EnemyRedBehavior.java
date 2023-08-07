package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.EnemyFlankHuntBehavior;
import mechanics.behavior.lowerlevel.EnemyRetreatingWanderBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyRedBehavior extends EnemyBehaviorGroup
{
    public EnemyRedBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addProbabilisticBehaviors(
            new behavior(0.60, 100000, 10000, 
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            12, 10, 180)),

            new behavior(0.40, 50000, 10000, 
            new EnemyRetreatingWanderBehavior(enemy_movement, stage, enemy, munch_man, 
            0.30, 20, 20, 10))
        );
    }
}