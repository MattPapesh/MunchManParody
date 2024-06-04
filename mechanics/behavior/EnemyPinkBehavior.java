package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.advancedbehaviors.EnemyLurkBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyPinkBehavior extends EnemyBehaviorGroup
{
    public EnemyPinkBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, 
    Enemy other_0, Enemy other_1, Enemy other_2, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, 5000, 0, false, 
            new EnemyLurkBehavior(enemy_movement, stage, enemy, other_0, other_1, other_2, munch_man, 3))
        );
    }
}