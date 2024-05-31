package mechanics.behavior.lowerlevel.advancedbehaviors;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior.anchor_data;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyRetreatBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;
import components.Stage;
import fundamentals.Coordinates;
import components.Enemy;
import components.MunchMan;

public class EnemyRetreatingWanderBehavior extends EnemyBehaviorGroup
{
    public EnemyRetreatingWanderBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double turn_around_prob_pct, double retreat_prob_pct, int trig_retreat_distance_units, int retreat_distance_units, 
    int anchor_wandering_radius_units)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addBehaviors
        (
            new behavior(1.0, 3000, 0, true,
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
