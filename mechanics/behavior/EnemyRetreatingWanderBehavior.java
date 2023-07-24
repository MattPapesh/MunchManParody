package mechanics.behavior;
import mechanics.bases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;
import components.Stage;
import components.Enemy;
import components.MunchMan;

public class EnemyRetreatingWanderBehavior extends EnemyBehaviorGroup
{
    public EnemyRetreatingWanderBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    int retreat_distance_units, int anchor_wandering_raidus_units)
    {
        super(stage, enemy, munch_man);
        addBehaviors
        (
            new EnemyRetreatBehavior(enemy_movement, stage, enemy, munch_man, retreat_distance_units), 
            new EnemyRetreatAnchorBehavior(enemy_movement, stage, enemy, munch_man, retreat_distance_units, anchor_wandering_raidus_units)
        );
    }
}
