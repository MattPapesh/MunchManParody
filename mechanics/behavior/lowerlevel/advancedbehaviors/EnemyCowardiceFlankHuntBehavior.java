package mechanics.behavior.lowerlevel.advancedbehaviors;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.intermediatebehaviors.EnemyFlankHuntBehavior;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyRetreatBehavior;
import mechanics.behaviorbases.EnemyBehaviorBase;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;

public class EnemyCowardiceFlankHuntBehavior extends EnemyBehaviorGroup
{
    public EnemyCowardiceFlankHuntBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    int direct_hunt_distance_units, int flank_radius_units, int flank_degrees,
    int retreat_distance_units) 
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            //new behavior(0.0, -1, -1, true,
            //new EnemyRetreatBehavior(enemy_movement, stage, enemy, munch_man, 
            //retreat_distance_units)),

            new behavior(1.0, -1, -1, false, 
            new EnemyFlankHuntingWander(enemy_movement, stage, enemy, munch_man, 
            0.201, -1, -1, 
            direct_hunt_distance_units, flank_radius_units, flank_degrees,
            1.0, -1, -1, 
            0.25, 10))
        );
    }
}
