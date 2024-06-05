package org.hid4java.mechanics.behavior.lowerlevel.advancedbehaviors;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior.anchor_data;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior;
import org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors.EnemyRetreatBehavior;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorGroup;
import org.hid4java.mechanics.movement.EntityMovement;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;

public class EnemyRetreatingWanderBehavior extends EnemyBehaviorGroup
{
    public EnemyRetreatingWanderBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double turn_around_prob_pct, double retreat_prob_pct, int trig_retreat_distance_units, int retreat_distance_units, 
    int anchor_wandering_radius_units)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, -1, -1, true,
            new EnemyRetreatBehavior(enemy_movement, stage, enemy, munch_man, 
            retreat_prob_pct, trig_retreat_distance_units, retreat_distance_units)),

            new behavior(1.0, 3000, 0, false, 
            new EnemyAnchorBehavior(enemy_movement, stage, enemy, munch_man, new anchor_data() 
            {
                @Override public double getTurnAroundProbabilityPercentage() {return turn_around_prob_pct;}
                @Override public Coordinates getStageCoords() {return enemy.getStageCoords();}
                @Override public int getRadius() {return anchor_wandering_radius_units;}
            }))
        );
    }
}
