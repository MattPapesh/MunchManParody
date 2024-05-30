package mechanics.behavior.lowerlevel.simplebehaviors;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.GameMath;
import mechanics.behaviorbases.EnemyBehaviorBase;
import mechanics.movement.EntityMovement;

public class EnemyRetreatBehavior extends EnemyBehaviorBase
{
    private int[][] stage_data = null;
    private double route_completion_pct = 0.4;
    private int retreat_coord_length = 3;
    private int min_route_length = 10;
    private int max_route_length = 15;
    private boolean scheduling_trig = false; 
    private int trig_retreat_distance_units = 0;
    private int retreat_distance_units = 0;
    private double retreat_probability_pct = 1.0;

    public EnemyRetreatBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    double retreat_probability_pct, int trig_retreat_distance_units, int retreat_distance_units)
    {
        super(enemy_movement, stage, enemy, munch_man);
        this.stage_data = stage.getStageData().clone();
        this.trig_retreat_distance_units = trig_retreat_distance_units;
        this.retreat_distance_units = retreat_distance_units;
        this.retreat_probability_pct = retreat_probability_pct;
    }

    private void computeRetreatBehavior()
    {
        int enemy_stage_x = getEnemyStageCoords().getX();
        int enemy_stage_y = getEnemyStageCoords().getY();
        int munch_man_stage_x = getMunchManStageCoords().getX();
        int munch_man_stage_y = getMunchManStageCoords().getY();

        int delta_stage_x = retreat_coord_length * ((enemy_stage_x < munch_man_stage_x) ? -1 : 1);
        int delta_stage_y = retreat_coord_length * ((enemy_stage_y < munch_man_stage_y) ? -1 : 1);
        boolean x_out_of_bounds = enemy_stage_x + delta_stage_x < 0 || enemy_stage_x + delta_stage_x >= stage_data[0].length;
        boolean y_out_of_bounds = enemy_stage_y + delta_stage_y < 0 || enemy_stage_y + delta_stage_y >= stage_data.length;

        delta_stage_x *= ( ((x_out_of_bounds) ? -1 : 1));
        delta_stage_y *= ( ((y_out_of_bounds) ? -1 : 1));

        int stage_x = enemy_stage_x + delta_stage_x;
        int stage_y = enemy_stage_y + delta_stage_y;
        int route_length = getRouteLength(stage_x, stage_y, getCurrentEnemyRouteTurnAroundStatus());

        stage_x = (route_length > max_route_length) ? stage_x - delta_stage_x + 1 : ((route_length < min_route_length) ? stage_x + delta_stage_x : stage_x);
        stage_y = (route_length > max_route_length) ? stage_y - delta_stage_y + 1 : ((route_length < min_route_length) ? stage_y + delta_stage_y : stage_y);

        stage_x = Math.max(Math.min(stage_x, stage_data[0].length - 1), 0);
        stage_y = Math.max(Math.min(stage_y, stage_data.length - 1), 0);

        setEnemyTarget(route_completion_pct, stage_x, stage_y);   
    }

    @Override
    public boolean isSelfSchedulingConditionsMet()
    {
        int delta_stage_x = getEnemyStageCoords().getX() - getMunchManStageCoords().getX();
        int delta_stage_y = getEnemyStageCoords().getY() - getMunchManStageCoords().getY();
        double distance = Math.pow(Math.pow(delta_stage_x, 2) + Math.pow(delta_stage_y, 2), 0.5);
        if(!scheduling_trig && distance <= trig_retreat_distance_units) {
            scheduling_trig = true; 
        }

        return scheduling_trig && distance <= retreat_distance_units && GameMath.probability(retreat_probability_pct);
    }

    @Override
    public void initializeBehavior()
    {
        computeRetreatBehavior();
    }

    @Override
    public void executeBehavior()
    {
        System.out.println("EnemyRetreat!");
        if(isEnemyRouteCompleted())
        {
            computeRetreatBehavior();
        }
    }

    @Override
    public void endBehavior(boolean interrupted)
    {
        scheduling_trig = false; 
        setEnemyTarget(0, getEnemyStageCoords().getX(), getEnemyStageCoords().getY());
    }

    @Override
    public boolean isBehaviorFinished()
    {
        return !isSelfSchedulingConditionsMet();
    }
}
