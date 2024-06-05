package org.hid4java.mechanics.behavior.lowerlevel.simplebehaviors;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorBase;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyRetreatBehavior extends EnemyBehaviorBase
{
    private int[][] stage_data = null;
    private double route_completion_pct = 0.4;
    private int retreat_coord_length = 3;
    private int min_route_length = 10;
    private int max_route_length = 15;
    private boolean scheduling_trig = false; 
    private boolean entered_munch_man_proximity = false;

    private int trig_retreat_distance_units = 0;
    private int retreat_distance_units = 0;
    private double retreat_probability_pct = 1.0;
    
    private double distance = 0;
    private Coordinates prev_coords = null;
    private Coordinates current_coords = null;

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
        int enemy_x = getEnemyStageCoords().getX();
        int enemy_y = getEnemyStageCoords().getY();
        int munch_man_x = getMunchManStageCoords().getX();
        int munch_man_y = getMunchManStageCoords().getY();

        int delta_x = retreat_coord_length * ((enemy_x < munch_man_x) ? -1 : 1);
        int delta_y = retreat_coord_length * ((enemy_y < munch_man_y) ? -1 : 1);
        boolean x_out_of_bounds = enemy_x + delta_x < 0 || enemy_x + delta_x >= stage_data[0].length;
        boolean y_out_of_bounds = enemy_y + delta_y < 0 || enemy_y + delta_y >= stage_data.length;

        delta_x *= (((x_out_of_bounds) ? -1 : 1));
        delta_y *= (((y_out_of_bounds) ? -1 : 1));

        int x = enemy_x + delta_x;
        int y = enemy_y + delta_y;
        int route_length = getRouteLength(x, y, getCurrentEnemyRouteTurnAroundStatus());

        x = (route_length > max_route_length) ? x - delta_x + 1 : ((route_length < min_route_length) ? x + delta_x : x);
        y = (route_length > max_route_length) ? y - delta_y + 1 : ((route_length < min_route_length) ? y + delta_y : y);

        x = Math.max(Math.min(x, stage_data[0].length - 1), 0);
        y = Math.max(Math.min(y, stage_data.length - 1), 0);

        setEnemyTarget(route_completion_pct, x, y);   
    }

    @Override
    public boolean isSelfSchedulingConditionsMet()
    {
        int delta_x = getEnemyStageCoords().getX() - getMunchManStageCoords().getX();
        int delta_y = getEnemyStageCoords().getY() - getMunchManStageCoords().getY();
        double abs_distance = Math.pow(Math.pow(delta_x, 2) + Math.pow(delta_y, 2), 0.5);

        if(!entered_munch_man_proximity && abs_distance <= trig_retreat_distance_units) {
            distance = 0;
            entered_munch_man_proximity = true;
            scheduling_trig = GameMath.probability(retreat_probability_pct);
        }
        else if(distance > retreat_distance_units) {
            entered_munch_man_proximity = false;
            scheduling_trig = false;
        }

        return scheduling_trig;
    }

    @Override
    public void initializeBehavior()
    {
        prev_coords = getEnemyStageCoords();
        current_coords = getEnemyStageCoords();
        computeRetreatBehavior();
    }

    @Override
    public void executeBehavior()
    {
        prev_coords = current_coords;
        current_coords = getEnemyStageCoords(); 
        
        int delta_x = current_coords.getX() - prev_coords.getX();
        int delta_y = current_coords.getY() - prev_coords.getY();
        distance += Math.pow(Math.pow(delta_x, 2) + Math.pow(delta_y, 2), 0.5);

        if(isEnemyRouteCompleted())
        {
            computeRetreatBehavior();
        }
    }

    @Override
    public void endBehavior(boolean interrupted)
    {
        setEnemyTarget(0, getEnemyStageCoords().getX(), getEnemyStageCoords().getY());
    }

    @Override
    public boolean isBehaviorFinished()
    {
        return !isSelfSchedulingConditionsMet();
    }
}
