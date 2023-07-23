package mechanics.behavior;

import components.MunchMan;

import java.util.Random;

import components.Enemy;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.mechanic.MechanicScheduler;
import mechanics.bases.EnemyBehaviorBase;

public class EnemyAnchorBehavior extends EnemyBehaviorBase
{
    private int[][] stage_data = null;
    private double route_completion_pct = 1.0;
    private Coordinates anchor_stage_coords = null;
    private int radius_units = 0;

    public EnemyAnchorBehavior(Stage stage,  Enemy enemy, MunchMan munch_man, 
    int anchor_stage_x, int anchor_stage_y, int radius_units)
    {
        super(stage, enemy, munch_man);
        stage_data = stage.getStageData().clone(); 
        anchor_stage_coords = new Coordinates(anchor_stage_x, anchor_stage_y, 0);
        this.radius_units = radius_units;
    }    

    private Coordinates getRandomStageCoords(int anchor_stage_x, int anchor_stage_y, int radius_units)
    {
        while(true)
        {
            double stage_x = (double)(stage_data[0].length - 1) * Math.random();
            double stage_y = (double)(stage_data.length - 1) * Math.random();
            double phi = Math.pow(radius_units, 2) - Math.pow(stage_x - anchor_stage_x, 2);
            
            if(phi >= 0 && Math.abs((stage_y - anchor_stage_y) * Math.pow(phi, -0.5)) <= 1) 
            {
                return new Coordinates((int)stage_x, (int)stage_y, 0);
            }
        }
    }

    private void computeAnchorBehavior()
    {
        Coordinates anchored_stage_coords = getRandomStageCoords(anchor_stage_coords.getX(), anchor_stage_coords.getY(), radius_units);
        System.out.println("( " + anchored_stage_coords.getX() + ", " + anchored_stage_coords.getY() + " )");
        setEnemyTarget(route_completion_pct, anchored_stage_coords.getX(), anchored_stage_coords.getY());
    }

    @Override
    public void initializeBehavior()
    {
        computeAnchorBehavior();
    }

    @Override
    public void executeBehavior()
    {
        if(isEnemyRouteCompleted())
        {
            computeAnchorBehavior();
        }
    }

    @Override
    public void endBehavior(boolean interrupted)
    {

    }

    @Override
    public boolean isBehaviorFinished()
    {
        return false;
    }
}
