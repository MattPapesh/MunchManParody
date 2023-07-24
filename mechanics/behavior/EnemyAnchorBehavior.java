package mechanics.behavior;

import components.MunchMan;
import components.Enemy;
import components.Stage;
import fundamentals.Coordinates;
import mechanics.bases.EnemyBehaviorBase;
import mechanics.movement.EntityMovement;

public class EnemyAnchorBehavior extends EnemyBehaviorBase
{
    private int[][] stage_data = null;
    private double route_completion_pct = 1.0;
    private anchor_data anchor = null;
    
    public interface anchor_data
    {
        Coordinates getStageCoords();
        int getRadius();
    }

    public EnemyAnchorBehavior(EntityMovement enemy_movement, Stage stage,  Enemy enemy, MunchMan munch_man, 
    int anchor_stage_x, int anchor_stage_y, int radius_units)
    {
        super(enemy_movement, stage, enemy, munch_man);
        stage_data = stage.getStageData().clone(); 
        anchor = new anchor_data() 
        {
            @Override public Coordinates getStageCoords() {return new Coordinates(anchor_stage_x, anchor_stage_y, 0);}
            @Override public int getRadius() {return radius_units;}
        };
    }    

    public EnemyAnchorBehavior(EntityMovement enemy_movement, Stage stage,  Enemy enemy, MunchMan munch_man, 
    anchor_data anchor)
    {
        super(enemy_movement, stage, enemy, munch_man);
        stage_data = stage.getStageData().clone(); 
        this.anchor = anchor;
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
        Coordinates anchored_stage_coords = getRandomStageCoords(anchor.getStageCoords().getX(), anchor.getStageCoords().getY(), anchor.getRadius());
        //System.out.println("( " + anchored_stage_coords.getX() + ", " + anchored_stage_coords.getY() + " )");
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
        setEnemyTarget(0, getEnemyStageCoords().getX(), getEnemyStageCoords().getY());
    }
}
