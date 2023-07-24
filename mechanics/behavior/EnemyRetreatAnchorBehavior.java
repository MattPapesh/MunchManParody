package mechanics.behavior;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;

public class EnemyRetreatAnchorBehavior extends EnemyAnchorBehavior
{
    private int retreat_distance_units = 0;

    public EnemyRetreatAnchorBehavior(Stage stage,  Enemy enemy, MunchMan munch_man, 
    int retreat_distance_units, int anchor_radius_units)
    {
        super(stage, enemy, munch_man, new anchor_data() 
        {
            @Override public Coordinates getStageCoords() {return enemy.getStageCoords();}
            @Override public int getRadius() {return anchor_radius_units;}
        });

        this.retreat_distance_units = retreat_distance_units;
    }
    
    @Override
    public boolean isSelfSchedulingConditionsMet()
    {
        int delta_stage_x = getEnemyStageCoords().getX() - getMunchManStageCoords().getX();
        int delta_stage_y = getEnemyStageCoords().getY() - getMunchManStageCoords().getY();
        return Math.pow(Math.pow(delta_stage_x, 2) + Math.pow(delta_stage_y, 2), 0.5) >= retreat_distance_units;
    }  

    @Override 
    public boolean isBehaviorFinished()
    {
        return !isSelfSchedulingConditionsMet();
    }
}
