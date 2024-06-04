package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemyCowardiceFlankHuntBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyYellowBehavior extends EnemyBehaviorGroup
{
    public EnemyYellowBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, -1, -1, false, 
            new EnemyCowardiceFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            7, 3, 
            0, 0.8, 4, 
            25, 10))
        );
    }
}