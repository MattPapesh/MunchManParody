package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemyCowardiceFlankHuntBehavior;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemyRetreatingWanderBehavior;
import mechanics.behavior.lowerlevel.intermediatebehaviors.EnemyFlankHuntBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyRedBehavior extends EnemyBehaviorGroup
{
    public EnemyRedBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors(
            new behavior(1.0, new EnemyCowardiceFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            50, 0, 0, 10))
            /*new behavior(0.60, 100000, 10000, false,
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            12, 10, 180)),

            new behavior(0.40, 50000, 10000, false,
            new EnemyRetreatingWanderBehavior(enemy_movement, stage, enemy, munch_man, 
            0.30, 20, 20, 10))*/
        );
    }
}