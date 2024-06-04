package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemySideKickBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyBlueBehavior extends EnemyBehaviorGroup
{
    public EnemyBlueBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, Enemy hero, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, 5000, 0, false, 
            new EnemySideKickBehavior(enemy_movement, stage, enemy, hero, munch_man))
        );
    }
}