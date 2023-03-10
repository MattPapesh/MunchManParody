package mechanics;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.mechanic.Delay;
import fundamentals.mechanic.MechanicBase;
import fundamentals.mechanic.MechanicScheduler;

public class EnemyChaseTarget extends MechanicBase
{
    private Delay update_delay = new Delay(10000, null);
    private EntityMovement enemy_movement = null;
    private Stage stage = null; 
    private MunchMan munch_man = null;
    private Enemy enemy = null; 
    private EnemyPredeterminedRoute enemy_route = null; 
    private int[][] stage_data = null; 

    public EnemyChaseTarget(EntityMovement enemy_movement, Stage stage, MunchMan munch_man, Enemy enemy)
    {   
        this.enemy_movement = enemy_movement;
        this.stage = stage; 
        this.munch_man = munch_man;
        this.enemy = enemy; 
        stage_data = stage.getStageData().clone();
        addRequirements(stage, munch_man, enemy);
    }

    private boolean isCoordsEqual(Coordinates primary, Coordinates secondary)
    {
        return primary.getX() == secondary.getX() && primary.getY() == secondary.getY();
    }

    private boolean isOpposingCoordDirections(Coordinates primary, Coordinates secondary)
    {
        return Math.abs(primary.getDegrees() - secondary.getDegrees()) == 180; 
    }

    private EnemyPredeterminedRoute getPredeterminedRoute(LinkedList<Coordinates> route)
    {
        EnemyPredeterminedRoute predetermined_route = new EnemyPredeterminedRoute(enemy_movement, enemy);
        int paths_delta_stage_x = 0;
        int paths_delta_stage_y = 0;

        int prev_delta_stage_x = 0;
        int prev_delta_stage_y = 0;
        int current_delta_stage_x = 0;
        int current_delta_stage_y = 0;

        for(int i = 1; i < route.size(); i++)
        {
            prev_delta_stage_x = current_delta_stage_x;
            prev_delta_stage_y = current_delta_stage_y;
            current_delta_stage_x = route.get(i).getX() - route.get(i - 1).getX();
            current_delta_stage_y = route.get(i).getY() - route.get(i - 1).getY();

            if(prev_delta_stage_x == 0 && current_delta_stage_x != 0
            && prev_delta_stage_y != 0 && current_delta_stage_y == 0)
            {
                
            }

            predetermined_route.addRelativePath(, 
            );
        }

        predetermined_route.compileRelativePaths();
        return predetermined_route;
    }

    private LinkedList<Coordinates> getBranchRouteVariant(LinkedList<Coordinates> base_route, LinkedList<Coordinates> route, int target_stage_x, int target_stage_y)
    {
        try
        {
            if(!isCoordsEqual(route.getLast(), base_route.getLast()) && !isOpposingCoordDirections(route.getLast(), base_route.getLast()) 
            && stage_data[route.getLast().getY()][route.getLast().getX()] == 1)
            {
                return route;
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {}

        return null;
    }

    public LinkedList<LinkedList<Coordinates>> getBranchRoutes(LinkedList<Coordinates> base_route, int target_stage_x, int target_stage_y, int prev_recursion_level)
    {
        // Branch routes:
        LinkedList<LinkedList<Coordinates>> routes = new LinkedList<LinkedList<Coordinates>>();
        LinkedList<Coordinates> left_route = new LinkedList<Coordinates>(base_route);
        LinkedList<Coordinates> right_route = new LinkedList<Coordinates>(base_route);
        LinkedList<Coordinates> up_route = new LinkedList<Coordinates>(base_route);
        LinkedList<Coordinates> down_route = new LinkedList<Coordinates>(base_route);
        
        left_route.addLast(new Coordinates(base_route.getLast().getX() - 1, base_route.getLast().getY(), 180));
        right_route.addLast(new Coordinates(base_route.getLast().getX() + 1, base_route.getLast().getY(), 0));
        up_route.addLast(new Coordinates(base_route.getLast().getX(), base_route.getLast().getY() - 1, 90));
        down_route.addLast(new Coordinates(base_route.getLast().getX(), base_route.getLast().getY() + 1, 270));

        left_route = getBranchRouteVariant(base_route, left_route, target_stage_x, target_stage_y);
        right_route = getBranchRouteVariant(base_route, right_route, target_stage_x, target_stage_y);
        up_route = getBranchRouteVariant(base_route, up_route, target_stage_x, target_stage_y);
        down_route = getBranchRouteVariant(base_route, down_route, target_stage_x, target_stage_y);

        if(left_route != null)
            routes.addLast(left_route);
        if(right_route != null)
            routes.addLast(right_route);
        if(up_route != null)
            routes.addLast(up_route);
        if(down_route != null)
            routes.addLast(down_route);

        return routes;
    }    

    private LinkedList<Coordinates> getRouteFound(LinkedList<LinkedList<Coordinates>> routes, int target_stage_x, int target_stage_y)
    {
        for(int i = 0; i < routes.size(); i++)
        {
            if(routes.get(i).getLast().getX() == target_stage_x && routes.get(i).getLast().getY() == target_stage_y)
            {
                return routes.get(i);
            }
        }

        return null; 
    }

    // when building new route with routes: a route is already logged if the next route found is found in a logged route
    public EnemyPredeterminedRoute getRoute(int target_stage_x, int target_stage_y)
    {
        LinkedList<Coordinates> initial_route = new LinkedList<Coordinates>();
        LinkedList<LinkedList<Coordinates>> logged_routes = new LinkedList<LinkedList<Coordinates>>();
        initial_route.addLast(new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), 0));
        logged_routes.addLast(initial_route);

        while(getRouteFound(logged_routes, target_stage_x, target_stage_y) == null)
        {
            LinkedList<LinkedList<Coordinates>> updated_logged_routes = new LinkedList<LinkedList<Coordinates>>();
            for(int i = 0; i < logged_routes.size(); i++)
            {
                updated_logged_routes.addAll(getBranchRoutes(logged_routes.get(i), target_stage_x, target_stage_y, 0));
            }

            logged_routes.clear();
            logged_routes.addAll(updated_logged_routes);
        }

        return getPredeterminedRoute(getRouteFound(logged_routes, target_stage_x, target_stage_y));
    }

    @Override
    public void initialize()
    {
        if(!enemy_movement.isScheduled())
        {
            enemy_movement.schedule();
        }
    }

    @Override
    public void execute()
    {
        try
        {
            if(!enemy_route.isScheduled())
            {
                enemy_route = getRoute(munch_man.getStageCoords().getX(), munch_man.getStageCoords().getY());
                enemy_route.schedule();
            }
        }
        catch(NullPointerException e) 
        {
            enemy_route = getRoute(munch_man.getStageCoords().getX(), munch_man.getStageCoords().getY());
            enemy_route.schedule();
        }
    }

    @Override
    public void end(boolean interrupted)
    {

    }

    @Override
    public boolean isFinished()
    {
        return false; 
    }
}
