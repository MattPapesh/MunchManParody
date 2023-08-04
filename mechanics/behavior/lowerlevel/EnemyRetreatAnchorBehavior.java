package mechanics.behavior.lowerlevel;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import mechanics.movement.EntityMovement;

public class EnemyRetreatAnchorBehavior extends EnemyAnchorBehavior
{
    private int anchor_distance_units = 0;

    public EnemyRetreatAnchorBehavior(EntityMovement enemy_movement, Stage stage,  Enemy enemy, MunchMan munch_man,
    double turn_around_prob_pct, int anchor_distance_units, int anchor_radius_units)
    {
        super(enemy_movement, stage, enemy, munch_man, new anchor_data() 
        {
            @Override public double getTurnAroundProbabilityPercentage() {return turn_around_prob_pct;}
            @Override public Coordinates getStageCoords() {return enemy.getStageCoords();}
            @Override public int getRadius() {return anchor_radius_units;}
        });

        this.anchor_distance_units = anchor_distance_units;
    }
    
    @Override
    public boolean isSelfSchedulingConditionsMet()
    {
        int delta_stage_x = getEnemyStageCoords().getX() - getMunchManStageCoords().getX();
        int delta_stage_y = getEnemyStageCoords().getY() - getMunchManStageCoords().getY();
        return Math.pow(Math.pow(delta_stage_x, 2) + Math.pow(delta_stage_y, 2), 0.5) >= anchor_distance_units;
    }  

    @Override 
    public boolean isBehaviorFinished()
    {
        return !isSelfSchedulingConditionsMet();
    }
}
