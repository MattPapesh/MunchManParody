package mechanics;

import java.util.LinkedList;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.Coordinates;
import fundamentals.mechanic.MechanicBase;

public class EnemyChaseTarget extends MechanicBase
{
    private EntityMovement enemy_movement = null;
    private Stage stage = null; 
    private MunchMan munch_man = null;
    private Enemy enemy = null; 
    private int[][] stage_data = null; 
    private int max_recursion_level = 100; 

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

    private LinkedList<Coordinates> getShortestRoute(LinkedList<LinkedList<Coordinates>> routes)
    {
        LinkedList<Coordinates> route = routes.getFirst();
        for(int i = 0; i < routes.size(); i++)
        {
            if(routes.get(i).size() < route.size())
            {
                route = routes.get(i);
            }
        }

        return route;
    }

    private EnemyPredeterminedRoute getPredeterminedRoute(LinkedList<Coordinates> route)
    {
        EnemyPredeterminedRoute predetermined_route = new EnemyPredeterminedRoute(enemy_movement, enemy);
        for(int i = 1; i < route.size(); i++)
        {
            predetermined_route.addRelativePath(route.get(i).getX() - route.get(i - 1).getX(), 
            route.get(i).getY() - route.get(i - 1).getY());
        }

        predetermined_route.compileRelativePaths();
        return predetermined_route;
    }
    
    private LinkedList<LinkedList<Coordinates>> getPathVariant(LinkedList<Coordinates> base_path, LinkedList<Coordinates> path, int target_stage_x, int target_stage_y,
    int recursion_level)
    {
        try
        {
            if(!isCoordsEqual(path.getLast(), base_path.getLast()) && !isCoordsEqual(path.getLast(), new Coordinates(target_stage_x, target_stage_y, 0))
            && !isOpposingCoordDirections(path.getLast(), base_path.getLast()) 
            && stage_data[path.getLast().getY()][path.getLast().getX()] == 1 && recursion_level < max_recursion_level)
            {
                return getRoutes(path, target_stage_x, target_stage_y, recursion_level);
            }
            else if(!isCoordsEqual(path.getLast(), base_path.getLast()) && isCoordsEqual(path.getLast(), new Coordinates(target_stage_x, target_stage_y, 0))
            && stage_data[path.getLast().getY()][path.getLast().getX()] == 1 && recursion_level < max_recursion_level)
            {
                LinkedList<LinkedList<Coordinates>> listed_path = new LinkedList<LinkedList<Coordinates>>();
                listed_path.addLast(path);
                return listed_path;
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {}

        return null;
    }

    public LinkedList<LinkedList<Coordinates>> getRoutes(LinkedList<Coordinates> base_route, int target_stage_x, int target_stage_y, int prev_recursion_level)
    {
        LinkedList<LinkedList<Coordinates>> routes = new LinkedList<LinkedList<Coordinates>>();
        LinkedList<Coordinates> left_paths = new LinkedList<Coordinates>(base_route);
        LinkedList<Coordinates> right_paths = new LinkedList<Coordinates>(base_route);
        LinkedList<Coordinates> up_paths = new LinkedList<Coordinates>(base_route);
        LinkedList<Coordinates> down_paths = new LinkedList<Coordinates>(base_route);
        
        left_paths.addLast(new Coordinates(base_route.getLast().getX() - 1, base_route.getLast().getY(), 180));
        right_paths.addLast(new Coordinates(base_route.getLast().getX() + 1, base_route.getLast().getY(), 0));
        up_paths.addLast(new Coordinates(base_route.getLast().getX(), base_route.getLast().getY() - 1, 90));
        down_paths.addLast(new Coordinates(base_route.getLast().getX(), base_route.getLast().getY() + 1, 270));

        try
        {
            if(prev_recursion_level + 1 < max_recursion_level) 
            {    
                LinkedList<LinkedList<Coordinates>> left_routes = getPathVariant(base_route, left_paths, target_stage_x, target_stage_y, prev_recursion_level + 1);
                LinkedList<LinkedList<Coordinates>> right_routes = getPathVariant(base_route, right_paths, target_stage_x, target_stage_y, prev_recursion_level + 1);
                LinkedList<LinkedList<Coordinates>> up_routes = getPathVariant(base_route, up_paths, target_stage_x, target_stage_y, prev_recursion_level + 1);
                LinkedList<LinkedList<Coordinates>> down_routes = getPathVariant(base_route, down_paths, target_stage_x, target_stage_y, prev_recursion_level + 1);

                if(left_routes != null) 
                    routes.addAll(left_routes);
                if(right_routes != null) 
                    routes.addAll(right_routes);
                if(up_routes != null) 
                    routes.addAll(up_routes);
                if(down_routes != null) 
                    routes.addAll(down_routes);
            }    
        }
        catch(StackOverflowError e) {}

        return routes;
    }

    public EnemyPredeterminedRoute getRoute(int target_stage_x, int target_stage_y)
    {
        LinkedList<Coordinates> initial_path = new LinkedList<Coordinates>();
        initial_path.addLast(new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), 0));
        LinkedList<LinkedList<Coordinates>> routes = getRoutes(initial_path, target_stage_x, target_stage_y, 0);
        LinkedList<Coordinates> shortest_route = getShortestRoute(routes);

        return getPredeterminedRoute(shortest_route); 
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
