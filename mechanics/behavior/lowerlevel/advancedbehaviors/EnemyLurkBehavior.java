package mechanics.behavior.lowerlevel.advancedbehaviors;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.GameMath;
import mechanics.behaviorbases.EnemyBehaviorBase;
import mechanics.movement.EntityMovement;

public class EnemyLurkBehavior extends EnemyBehaviorBase
{   
    // Other Enemies:
    private Enemy other_0 = null; 
    private Enemy other_1 = null; 
    private Enemy other_2 = null; 
    // Lurk Calculation Tuned coefficients:
    private double c0 = 2.56; 
    private double c1 = 0.16;
    private double c2 = 0.73; 
    private double c3 = 1.11; 

    public EnemyLurkBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, 
    Enemy other_0, Enemy other_1, Enemy other_2, MunchMan munch_man, int wandering_radius_units)
    {
        super(enemy_movement, stage, enemy, munch_man);

        this.other_0 = other_0;
        this.other_1 = other_1;
        this.other_2 = other_2;
    } 

    /**
     * @brief Sigmoid function outputting a postive constant for the negative domain that curves 
     * to converge to zero for the positive domain. 
     * 
     * @param x (double) : Specified function domain. 
     * @return The function range. 
     */ 
    private double collapseFunction(double x)
    {
        return Math.PI - (2.0 * Math.atan(Math.pow(Math.E, (0.25 * x) - 3.0)));
    }

    /**
     * @brief Sigmoid function outputting closer to unit value one for small inputs and converges to 
     * zero while bounded by a percentage range [0.0, 1.0]. 
     * 
     * @param x (double) : Specified function domain.  
     * @return The function range. 
     */
    private double unitCollapseFunction(double x)
    {
        return 1.0 - (2.0 / Math.PI) * Math.atan(Math.pow(Math.E, (0.25 * Math.abs(x)) - 3.0));
    } 

    protected void computeLurkBehavior()
    {
        // Munch Man Coords:
        double mx = getMunchManStageCoords().getX(), my = getMunchManStageCoords().getY(); // px,py
        // Other Enemy Coords:
        double x0 = other_0.getStageCoords().getX(), y0 = other_0.getStageCoords().getY();
        double x1 = other_1.getStageCoords().getX(), y1 = other_1.getStageCoords().getY();
        double x2 = other_2.getStageCoords().getX(), y2 = other_2.getStageCoords().getY();

        // Center Coord of Other Enemies:
        double other_enemy_center_x = ((x0 + x1 + x2) / 3.0); //Cx
        double other_enemy_center_y =  ((y0 + y1 + y2) / 3.0); //Cy

        // Pivot Coord to Lurk About: [Offset Other-Enemy Group Center by Munch Man Positioning]
        double pivot_x = ((3.0 * other_enemy_center_x) + mx) / 4.0; //Mx
        double pivot_y = ((3.0 * other_enemy_center_y) + my) / 4.0; //My

        // Find permiter lengths of the triangle drawn by the three other enemies. 
        double perim_0 = GameMath.getDistance(x0, y0, x1, y1);
        double perim_1 = GameMath.getDistance(x1, y1, x2, y2);
        double perim_2 = GameMath.getDistance(x2, y2, x0, y0);

        // Find base lengths from triangle vertices [Other enemy positions] to pivot point.
        double base_0 = GameMath.getDistance(x0, y0, pivot_x, pivot_y);
        double base_1 = GameMath.getDistance(x1, y1, pivot_x, pivot_y);
        double base_2 = GameMath.getDistance(x2, y2, pivot_x, pivot_y);

        // Find lurk function coefficients:
        double radians = Math.atan2(my - other_enemy_center_y, mx - other_enemy_center_x);
        double alpha = (base_0 + base_1 + base_2) / 3.0; //d1
        double beta = GameMath.getDistance(mx, my, pivot_x, pivot_y); //d2
        double zeta = (perim_0 + perim_1 + perim_2) / 3.0; //d3

        // Compute lurk displacements from Munch Man:
        double delta_x = c0 * alpha * unitCollapseFunction(zeta) * collapseFunction(c1 * beta * zeta) * Math.cos(radians);
        double delta_y = c0 * c2 * alpha * unitCollapseFunction(zeta) * collapseFunction(c1 * c3 * beta * zeta) * Math.sin(radians);
        // Compute lurk coords:
        Coordinates enemy_coords = new Coordinates((int)(mx + delta_x), (int)(my + delta_y), 0);
        int enemy_degrees = getEnemyStageCoords().getDegrees();
        int new_degrees = (int)Math.toDegrees(radians);
        double turn_around_pct = (Math.abs(new_degrees - enemy_degrees) <= 90) ? 0.0 : 1.0;
    
        if(isEnemyRouteCompleted()) 
        {
            setEnemyTarget(0.1, turn_around_pct, enemy_coords.getX(), enemy_coords.getY());
        }
    }

    @Override
    public void initializeBehavior()
    {
        setEnemyTarget(0.1, 0.0, getMunchManStageCoords().getX(), getMunchManStageCoords().getY());
    }

    @Override 
    public void executeBehavior()
    {
        if(isEnemyRouteCompleted()) 
        {
            computeLurkBehavior();
        }
    }  

    @Override
    public void endBehavior(boolean interrupted) 
    {
        setEnemyTarget(0, getEnemyStageCoords().getX(), getEnemyStageCoords().getY());
    }
}
