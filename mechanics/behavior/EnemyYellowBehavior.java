package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyYellowBehavior extends EnemyBehaviorGroup
{
    public EnemyYellowBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
    }
}