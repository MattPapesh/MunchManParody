package mechanics; 
import java.util.LinkedList;

import components.Enemy;
import components.Stage;
import fundamentals.mechanic.MechanicBase;
import fundamentals.Coordinates;

public class EnemyGoNearTarget extends MechanicBase {
    
    private int[][] stage_data = null; 
    private Coordinates target_stage_coords = null;
    private EnemyGoToTarget enemy_go_to_target = null;

    public EnemyGoNearTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, int target_stage_x, int target_stage_y) 
    {
        stage_data = stage.getStageData().clone();
        target_stage_x = Math.max(Math.min(stage_data[0].length, target_stage_x), 0);
        target_stage_y = Math.max(Math.min(stage_data.length, target_stage_y), 0);
        target_stage_coords = getNearTargetStageCoords(target_stage_x, target_stage_y);
        addRequirements(stage, enemy);
        enemy_go_to_target = new EnemyGoToTarget(enemy_movement, stage, enemy, target_stage_coords.getX(), target_stage_coords.getY());
    }

    private boolean isCoordsEqual(Coordinates primary, Coordinates secondary)
    {
        return primary.getX() == secondary.getX() && primary.getY() == secondary.getY();
    }

    private boolean isOpposingCoordDirections(Coordinates primary, Coordinates secondary)
    {
        return Math.abs(primary.getDegrees() - secondary.getDegrees()) == 180; 
    }

    private LinkedList<Coordinates> getBranchRouteVariant(LinkedList<Coordinates> base_route, LinkedList<Coordinates> route, int target_stage_x, int target_stage_y)
    {
        try
        {
            if(!isCoordsEqual(route.getLast(), base_route.getLast()) && !isOpposingCoordDirections(route.getLast(), base_route.getLast()))
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
        try
        {
            for(int i = 0; i < routes.size(); i++)
            {
                if(stage_data[routes.get(i).getLast().getY()][routes.get(i).getLast().getX()] == 1)
                {
                    return routes.get(i);
                }
            }

        }
        catch(ArrayIndexOutOfBoundsException e) {}
        return null; 
    }

    // when building new route with routes: a route is already logged if the next route found is found in a logged route
    public Coordinates getNearTargetStageCoords(int target_stage_x, int target_stage_y)
    {
        LinkedList<Coordinates> initial_route = new LinkedList<Coordinates>();
        LinkedList<LinkedList<Coordinates>> logged_routes = new LinkedList<LinkedList<Coordinates>>();
        initial_route.addLast(new Coordinates(target_stage_x, target_stage_y, 0));
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

        return route_found.getLast();
    }

    @Override
    public void initialize()
    {
        enemy_go_to_target.schedule();
    }

    @Override
    public void end(boolean interrupted)
    {
        enemy_go_to_target.cancel();
    }

    @Override
    public boolean isFinished() 
    {
        return isInitialized() && !enemy_go_to_target.isScheduled();
    }
}
