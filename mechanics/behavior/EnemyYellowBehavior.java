package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemyCowardiceFlankHuntBehavior;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemyRetreatingWanderBehavior;
import mechanics.behavior.lowerlevel.intermediatebehaviors.EnemyFlankHuntBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyYellowBehavior extends EnemyBehaviorGroup
{
    public EnemyYellowBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors(
            new behavior(0.3, 5000, 0000, false, 
            new EnemyCowardiceFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            7, 3, 
            0, 1.0, 5, 
            20, 10)),

            new behavior(1.0, 5000, 10000, false,
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            7, 5, 0))

            /*new behavior(0.0, 50000, 10000, false,
            new EnemyRetreatingWanderBehavior(enemy_movement, stage, enemy, munch_man, 
            0.30, 10, 8, 10))*/
        );
    }
}