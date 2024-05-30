package mechanics.behavior.lowerlevel.advancedbehaviors;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.intermediatebehaviors.EnemyFlankHuntBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyCowardiceFlankHuntBehavior extends EnemyBehaviorGroup
{
    public EnemyCowardiceFlankHuntBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    int direct_hunt_distance_units, int flank_radius_units, int flank_degrees,
    double retreat_probability_pct, int trig_retreat_distance_units, int retreat_distance_units, int anchor_wandering_radius_units) 
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, 5000, 0, false,
            new EnemyRetreatingWanderBehavior(enemy_movement, stage, enemy, munch_man, 
            0.3, retreat_probability_pct, trig_retreat_distance_units, 
            retreat_distance_units, anchor_wandering_radius_units)),

            new behavior(1.0, 5000, 0, false,
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            direct_hunt_distance_units, flank_radius_units, flank_degrees))
        );
    }
}
