package org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyFlankHuntBehavior;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorGroup;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyFlankHuntingWander extends EnemyBehaviorGroup
{
    public EnemyFlankHuntingWander(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double hunt_scheduling_probability_pct, int hunt_max_scheduled_millis, int hunt_cooldown_millis,
    int direct_hunt_distance_units, int flank_radius_units, int flank_degrees, 
    double wander_scheduling_probability_pct, int wander_max_scheduled_millis, int wander_cooldown_millis, 
    double turn_around_prob_pct, int anchor_wandering_raidus_units) 
    {
        super(enemy_movement, stage, enemy, munch_man);
        EnemyAnchorBehavior.anchor_data anchor = new EnemyAnchorBehavior.anchor_data() {
            @Override public double getTurnAroundProbabilityPercentage() {return turn_around_prob_pct;}
            @Override public Coordinates getStageCoords() {return new Coordinates(getEnemyStageCoords().getX(), getEnemyStageCoords().getY(), 0);}
            @Override public int getRadius() {return anchor_wandering_raidus_units;}
        };
        
        addBehaviors
        (
            new behavior(hunt_scheduling_probability_pct, 
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            direct_hunt_distance_units, flank_radius_units, flank_degrees)),

            new behavior(wander_scheduling_probability_pct, 
            new EnemyAnchorBehavior(enemy_movement, stage, enemy, munch_man, anchor))
        );
    }    
}
