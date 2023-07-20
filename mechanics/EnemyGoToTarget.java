package mechanics;

import java.util.LinkedList;

import components.Dot;
import components.Enemy;
import components.Stage;
import fundamentals.Coordinates;

public class EnemyGoToTarget extends EnemyPredeterminedRoute
{
    private EntityMovement enemy_movement = null;
    private Enemy enemy = null; 
    private int[][] stage_data = null; 
    private Coordinates target_stage_coords = null;
    private double terminating_completion_pct = 0.0;

    public EnemyGoToTarget(double terminating_completion_pct, EntityMovement enemy_movement, Stage stage, Enemy enemy, int target_stage_x, int target_stage_y)
    {   
        super(enemy_movement, enemy);
        this.terminating_completion_pct = Math.max(Math.min(terminating_completion_pct, 1.0), 0.0);
        this.enemy_movement = enemy_movement; 
        this.enemy = enemy; 
        stage_data = stage.getStageData().clone();
        setTargetStageCoords(target_stage_x, target_stage_y);
        addRequirements(stage, enemy);
    }

    public EnemyGoToTarget(double terminating_completion_pct, EntityMovement enemy_movement, Stage stage, Enemy enemy)
    {   
        super(enemy_movement, enemy);
        this.terminating_completion_pct = Math.max(Math.min(terminating_completion_pct, 1.0), 0.0);
        this.enemy_movement = enemy_movement; 
        this.enemy = enemy; 
        stage_data = stage.getStageData().clone();
        addRequirements(stage, enemy);
    }

    protected void setTargetStageCoords(int target_stage_x, int target_stage_y)
    {
        target_stage_x = Math.max(Math.min(stage_data[0].length, target_stage_x), 0);
        target_stage_y = Math.max(Math.min(stage_data.length, target_stage_y), 0);
        target_stage_coords = new Coordinates(target_stage_x, target_stage_y, 0);
    }

    protected boolean isCoordsEqual(Coordinates primary, Coordinates secondary)
    {
        return primary.getX() == secondary.getX() && primary.getY() == secondary.getY();
    }

    protected boolean isOpposingCoordDirections(Coordinates primary, Coordinates secondary)
    {
        return Math.abs(primary.getDegrees() - secondary.getDegrees()) == 180; 
    }

    private void compilePredeterminedRoute(LinkedList<Coordinates> route)
    {
        int delta_path_x = 0;
        int delta_path_y = 0;

        for(int i = 1; i < route.size(); i++) 
        {
            delta_path_x = route.get(i).getX() - route.get(i - 1).getX();
            delta_path_y = route.get(i).getY() - route.get(i - 1).getY();
            dots.addLast(new Dot(route.get(i).getX(), route.get(i).getY()));
            addRelativePath(delta_path_x, delta_path_y);
        }

        compileRelativePaths();
    }

    private LinkedList<Coordinates> getBranchRouteVariant(LinkedList<Coordinates> base_route, LinkedList<Coordinates> route, int target_stage_x, int target_stage_y)
    {
        try
        {
            if(!isCoordsEqual(route.getLast(), base_route.getLast()) && (!isOpposingCoordDirections(route.getLast(), base_route.getLast()) || base_route.size() == 1)
            && stage_data[route.getLast().getY()][route.getLast().getX()] == 1)
            {
                return route;
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {}
        return null;
    }

    public LinkedList<LinkedList<Coordinates>> getBranchRoutes(LinkedList<Coordinates> base_route, int target_stage_x, int target_stage_y)
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
    private void computeRoute(int target_stage_x, int target_stage_y)
    {
        LinkedList<Coordinates> initial_route = new LinkedList<Coordinates>();
        LinkedList<LinkedList<Coordinates>> logged_routes = new LinkedList<LinkedList<Coordinates>>();
        initial_route.addLast(new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), enemy.getStageCoords().getDegrees()));
        logged_routes.addLast(initial_route);
        LinkedList<Coordinates> route_found = getRouteFound(logged_routes, target_stage_x, target_stage_y);

        while(route_found == null)
        {
            LinkedList<LinkedList<Coordinates>> updated_logged_routes = new LinkedList<LinkedList<Coordinates>>();
            for(int i = 0; i < logged_routes.size(); i++)
            {
                updated_logged_routes.addAll(getBranchRoutes(logged_routes.get(i), target_stage_x, target_stage_y));
            }

            logged_routes.clear();
            logged_routes.addAll(updated_logged_routes);
            route_found = getRouteFound(logged_routes, target_stage_x, target_stage_y);
        }

        compilePredeterminedRoute(route_found);
    }

    public double getTerminatingCompletionPercentage()
    {
        return terminating_completion_pct;
    }

    public double getCompletionPercentage()
    {
        if(getNumOfPaths() == 0)
        {
            return 0.0;
        }

        return (double)getCurrentPathIndexScheduled() / (double)getNumOfPaths();
    }

    @Override
    public void initialize()
    {
        if(enemy_movement != null && !enemy_movement.isScheduled())
        {
            enemy_movement.schedule();
        }

        computeRoute(target_stage_coords.getX(), target_stage_coords.getY());
        sequentialMechanicGroupInitialize();
    }

    @Override
    public void end(boolean interrupted)
    {
        sequentialMechanicGroupEnd(interrupted);
        for(int i = 0; i < dots.size(); i++)
        {
            dots.get(i).delete();
        }
    }

    @Override
    public boolean isFinished()
    {
        return sequentialMechanicGroupIsFinished() 
        || getCompletionPercentage() >= getTerminatingCompletionPercentage();
    }
}
