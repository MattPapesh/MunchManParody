package mechanics.behavior.lowerlevel.advancedbehaviors;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior.anchor_data;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyAnchorBehavior;
import mechanics.behavior.lowerlevel.simplebehaviors.EnemyRetreatBehavior;
import mechanics.behaviorbases.EnemyBehaviorGroup;
import mechanics.movement.EntityMovement;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.GameMath;
import components.Enemy;
import components.MunchMan;

public class EnemyCowardiceRetreatingWanderBehavior extends EnemyRetreatingWanderBehavior
{
    private int trig_retreat_distance_units = 0;
    private int retreat_distance_units = 0;
    private double retreat_prob_pct = 0;
    private boolean scheduling_trig = false;
    
    public EnemyCowardiceRetreatingWanderBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double turn_around_prob_pct, double retreat_prob_pct, int trig_retreat_distance_units, int retreat_distance_units, 
    int anchor_wandering_radius_units)
    {
        super(enemy_movement, stage, enemy, munch_man, turn_around_prob_pct, retreat_prob_pct, trig_retreat_distance_units, 
        retreat_distance_units, anchor_wandering_radius_units);
        this.trig_retreat_distance_units = trig_retreat_distance_units;
        this.retreat_distance_units = retreat_distance_units; 
        this.retreat_prob_pct = retreat_prob_pct;
    }

    @Override
    public boolean isSelfSchedulingConditionsMet() {
        int delta_stage_x = getEnemyStageCoords().getX() - getMunchManStageCoords().getX();
        int delta_stage_y = getEnemyStageCoords().getY() - getMunchManStageCoords().getY();
        double distance = Math.pow(Math.pow(delta_stage_x, 2) + Math.pow(delta_stage_y, 2), 0.5);
        if(!scheduling_trig && distance <= trig_retreat_distance_units) {
            scheduling_trig = true; 
        }

        return scheduling_trig && distance <= retreat_distance_units && GameMath.probability(retreat_prob_pct); 
    }
}
