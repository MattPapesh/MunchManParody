package mechanics.behavior.lowerlevel.simplebehaviors;

import components.MunchMan;
import components.Enemy;
import components.Stage;
import fundamentals.Coordinates;
import mechanics.behaviorbases.EnemyBehaviorBase;
import mechanics.movement.EntityMovement;

public class EnemyAnchorBehavior extends EnemyBehaviorBase
{
    private int[][] stage_data = null;
    private double route_completion_pct = 1.0;
    private anchor_data anchor = null;

    public interface anchor_data
    {
        double getTurnAroundProbabilityPercentage();
        Coordinates getStageCoords();
        int getRadius();
    }

    public EnemyAnchorBehavior(EntityMovement enemy_movement, Stage stage,  Enemy enemy, MunchMan munch_man, 
    double turn_around_prob_pct, int anchor_stage_x, int anchor_stage_y, int radius_units)
    {
        super(enemy_movement, stage, enemy, munch_man);
        stage_data = stage.getStageData().clone(); 
        anchor = new anchor_data() 
        {
            @Override public double getTurnAroundProbabilityPercentage() {return turn_around_prob_pct;}
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
            double stage_x = (2.0 * (Math.random() - 0.5) * (double)radius_units) + anchor_stage_x;
            double phi = Math.pow(radius_units, 2) - Math.pow(stage_x - anchor_stage_x, 2);
            double stage_y = (2.0 * (Math.random() - 0.5) * Math.pow(phi, 0.5)) + anchor_stage_y;
            
            if((int)stage_x >= 0 && (int)stage_x < stage_data[0].length
            && (int)stage_y >= 0 && (int)stage_y < stage_data.length)
            {
                return new Coordinates((int)stage_x, (int)stage_y, 0);
            }
        }
    }

    private void computeAnchorBehavior()
    {
        System.out.println("Anchor!");
        Coordinates anchored_stage_coords = getRandomStageCoords(anchor.getStageCoords().getX(), anchor.getStageCoords().getY(), anchor.getRadius());
        setEnemyTarget(route_completion_pct, anchor.getTurnAroundProbabilityPercentage(), anchored_stage_coords.getX(), anchored_stage_coords.getY());
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
