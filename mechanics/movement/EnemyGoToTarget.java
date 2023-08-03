package mechanics.movement;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import components.Dot;
import components.Enemy;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.GameMath;
import mechanics.behavior.EnemyRetreatBehavior;

public class EnemyGoToTarget extends EnemyPredeterminedRoute
{
    private EntityMovement enemy_movement = null;
    private Enemy enemy = null; 
    private int[][] stage_data = null; 
    private Coordinates target_stage_coords = null;
    private double terminating_completion_pct = 0.0;
    private double turn_around_pct = -1.0;
    private boolean turn_around_status = false; 
    // Max number of shortest routes to find and choose from when looking with specific conditions needing to be met. 
    private final int MAX_SHORTEST_ROUTE_INDEX = 2; 

    public EnemyGoToTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, 
    double terminating_completion_pct, int target_stage_x, int target_stage_y)
    {   
        super(enemy_movement, enemy);
        this.terminating_completion_pct = Math.max(Math.min(terminating_completion_pct, 1.0), 0.0);
        this.enemy_movement = enemy_movement; 
        this.enemy = enemy; 
        stage_data = stage.getStageData().clone();
        target_stage_x = Math.max(Math.min(target_stage_x, stage_data[0].length - 1), 0);
        target_stage_y = Math.max(Math.min(target_stage_y, stage_data.length - 1), 0);
        setTargetStageCoords(target_stage_x, target_stage_y);
        addRequirements(stage, enemy);
    }

    public EnemyGoToTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, 
    double terminating_completion_pct, double turn_around_pct, int target_stage_x, int target_stage_y)
    {   
        super(enemy_movement, enemy);
        this.terminating_completion_pct = Math.max(Math.min(terminating_completion_pct, 1.0), 0.0);
        this.turn_around_pct = (turn_around_pct != -1.0) ? Math.max(Math.min(turn_around_pct, 1.0), 0.0) : -1.0;
        this.enemy_movement = enemy_movement; 
        this.enemy = enemy; 
        stage_data = stage.getStageData().clone();
        turn_around_status = GameMath.probability(this.turn_around_pct);
        target_stage_x = Math.max(Math.min(target_stage_x, stage_data[0].length - 1), 0);
        target_stage_y = Math.max(Math.min(target_stage_y, stage_data.length - 1), 0);
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

    public EnemyGoToTarget(double terminating_completion_pct, double turn_around_pct, EntityMovement enemy_movement, Stage stage, Enemy enemy)
    {   
        super(enemy_movement, enemy);
        this.terminating_completion_pct = Math.max(Math.min(terminating_completion_pct, 1.0), 0.0);
        this.turn_around_pct = (turn_around_pct != -1.0) ? Math.max(Math.min(turn_around_pct, 1.0), 0.0) : -1.0;
        this.enemy_movement = enemy_movement; 
        this.enemy = enemy; 
        stage_data = stage.getStageData().clone();
        turn_around_status = GameMath.probability(this.turn_around_pct);
        addRequirements(stage, enemy);
    }

    protected void setTargetStageCoords(int target_stage_x, int target_stage_y)
    {
        target_stage_x = Math.max(Math.min(stage_data[0].length, target_stage_x), 0);
        target_stage_y = Math.max(Math.min(stage_data.length, target_stage_y), 0);
        target_stage_coords = new Coordinates(target_stage_x, target_stage_y, 0);
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
            if(!GameMath.isCoordsEqual(route.getLast(), base_route.getLast()) 
            && (!GameMath.isOpposingCoordDirections(route.getLast(), base_route.getLast()) || base_route.size() == 1)
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

    private LinkedList<LinkedList<Coordinates>> getRoutesFound(LinkedList<LinkedList<Coordinates>> routes, int target_stage_x, int target_stage_y)
    {
        LinkedList<LinkedList<Coordinates>> routes_found = new LinkedList<LinkedList<Coordinates>>();
        for(int i = 0; i < routes.size(); i++)
        {
            if(routes.get(i).getLast().getX() == target_stage_x && routes.get(i).getLast().getY() == target_stage_y)
            {
                routes_found.addLast(routes.get(i));
            }
        }
        
        if(routes_found.isEmpty()) 
        {
            return routes_found;
        }

        GameMath.minMaxLengthEnemyRoutesMergeSort(routes_found);
        return routes_found; 
    }

    public LinkedList<Coordinates> getRoute(int target_stage_x, int target_stage_y)
    {
        LinkedList<Coordinates> initial_route = new LinkedList<Coordinates>();
        LinkedList<LinkedList<Coordinates>> logged_routes = new LinkedList<LinkedList<Coordinates>>();
        LinkedList<LinkedList<Coordinates>> found_logged_routes = new LinkedList<LinkedList<Coordinates>>();
        initial_route.addLast(new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), enemy.getStageCoords().getDegrees()));
        logged_routes.addLast(initial_route);
        LinkedList<Coordinates> prev_route = enemy.getRouteTraveled();
        boolean route_turns_around = !turn_around_status; 

        while(found_logged_routes.size() - 1 <= MAX_SHORTEST_ROUTE_INDEX)
        {
            LinkedList<LinkedList<Coordinates>> updated_logged_routes = new LinkedList<LinkedList<Coordinates>>();
            for(int i = 0; i < logged_routes.size(); i++)
            {
                updated_logged_routes.addAll(getBranchRoutes(logged_routes.get(i), target_stage_x, target_stage_y));
            }

            logged_routes.clear();
            logged_routes.addAll(updated_logged_routes);
            found_logged_routes = getRoutesFound(logged_routes, target_stage_x, target_stage_y);
            for(int next_shortest_route_index = 0; next_shortest_route_index < found_logged_routes.size()
            && next_shortest_route_index <= MAX_SHORTEST_ROUTE_INDEX; next_shortest_route_index++)
            {
                try
                {
                    route_turns_around = GameMath.isCoordsEqual(prev_route.get(prev_route.size() - 2), found_logged_routes.get(next_shortest_route_index).get(1));        
                    if(turn_around_pct == -1.0 || route_turns_around == turn_around_status) 
                    {
                        return found_logged_routes.get(next_shortest_route_index);
                    }
                }
                catch(IndexOutOfBoundsException e) {}
                catch(NoSuchElementException e) {}
                catch(NullPointerException e) {}
            }
        }

        return found_logged_routes.get(0);
    }

    // when building new route with routes: a route is already logged if the next route found is found in a logged route
    private LinkedList<Coordinates> computeRoute(int target_stage_x, int target_stage_y)
    {
        LinkedList<Coordinates> current_enemy_route = getRoute(target_stage_x, target_stage_y);
        compilePredeterminedRoute(current_enemy_route);
        return current_enemy_route;
    }

    public boolean getTurnAroundStatus()
    {
        return turn_around_status;
    }

    public int getRouteLength(int target_stage_x, int target_stage_y)
    {
        return getRoute(target_stage_x, target_stage_y).size();
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

    void printPaths(String prompt, LinkedList<Coordinates> route, int initial_index, int num_of_paths)
    {
        String output = prompt + ": { ";
        if(route.isEmpty() || initial_index < 0 || initial_index >= route.size())
        {
            System.out.println(prompt + ": NA");
            return;
        }

        for(int i = initial_index; i < route.size() && i - initial_index < num_of_paths; i++)
        {
            output = output + "(" + route.get(i).getX() + ", " + route.get(i).getY() + ")";
            output = (i < route.size() - 1 && i - initial_index < num_of_paths - 1) ? output + ", " : output + " }";
        }

        System.out.println(output);
    }

    @Override
    public void initialize()
    {
        if(enemy_movement != null && !enemy_movement.isScheduled())
        {
            enemy_movement.schedule();
        }
        
        LinkedList<Coordinates> current_route = computeRoute(target_stage_coords.getX(), target_stage_coords.getY());
        enemy.setRoute(current_route);
        //printPaths("[START] Route Trav", current_route, 0, 3);
        sequentialMechanicGroupInitialize();
    }

    @Override
    public void end(boolean interrupted)
    {
        sequentialMechanicGroupEnd(interrupted);
        LinkedList<Coordinates> route_traveled = enemy.getRouteTraveled();
        for(int i = getCurrentPathIndexScheduled(); getCurrentMechanicIndexScheduled() < getNumOfPaths() - 1
        && i < getNumOfPaths() && !route_traveled.isEmpty(); i++)
        {
            route_traveled.removeLast();
        }

        enemy.setRoute(route_traveled);
        //printPaths("[ENDED] Route Trav", route_traveled, route_traveled.size() - 3, 3);
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
