package mechanics.behavior.lowerlevel.advancedbehaviors;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import mechanics.behavior.lowerlevel.intermediatebehaviors.EnemyFlankHuntBehavior;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior.anchor_data;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyRetreatBehavior;
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
            new behavior(0.0, 3000, 0, true,
            new EnemyRetreatBehavior(enemy_movement, stage, enemy, munch_man, 
            retreat_probability_pct, trig_retreat_distance_units, retreat_distance_units)),

            new behavior(0.5, 9000, 0, false, 
            new EnemyAnchorBehavior(enemy_movement, stage, enemy, munch_man, new anchor_data() 
            {
                @Override public double getTurnAroundProbabilityPercentage() {return 0.3;}
                @Override public Coordinates getStageCoords() {return enemy.getStageCoords();}
                @Override public int getRadius() {return anchor_wandering_radius_units;}
            })),

            new behavior(1.0, 3000, 0, false,
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            direct_hunt_distance_units, flank_radius_units, flank_degrees))
        );
    }
}
