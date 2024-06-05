package org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyFlankHuntBehavior;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior.anchor_data;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyRetreatBehavior;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorGroup;
import org.hid4java.mechanics.movement.EntityMovement;

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

            new behavior(0.7, 9000, 0, false, 
            new EnemyAnchorBehavior(enemy_movement, stage, enemy, munch_man, new anchor_data() 
            {
                @Override public double getTurnAroundProbabilityPercentage() {return 0.3;}
                @Override public Coordinates getStageCoords() {return enemy.getStageCoords();}
                @Override public int getRadius() {return anchor_wandering_radius_units;}
            })),

            new behavior(1.0, 4500, 0, false, 
            new EnemyFlankHuntBehavior(enemy_movement, stage, enemy, munch_man, 
            direct_hunt_distance_units, flank_radius_units, flank_degrees))
        );
    }
}
