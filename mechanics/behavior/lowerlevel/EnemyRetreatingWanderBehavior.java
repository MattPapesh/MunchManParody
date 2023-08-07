package mechanics.behavior.lowerlevel;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;
import components.Stage;
import components.Enemy;
import components.MunchMan;

public class EnemyRetreatingWanderBehavior extends EnemyBehaviorGroup
{
    public EnemyRetreatingWanderBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double turn_around_prob_pct, int retreat_distance_units, int anchor_distance_units, int anchor_wandering_raidus_units)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addSelfSchedulingBehaviors
        (
            new EnemyRetreatBehavior(enemy_movement, stage, enemy, munch_man, retreat_distance_units), 
            new EnemyRetreatAnchorBehavior(enemy_movement, stage, enemy, munch_man, 
            turn_around_prob_pct, anchor_distance_units, anchor_wandering_raidus_units)
        );
    }
}
