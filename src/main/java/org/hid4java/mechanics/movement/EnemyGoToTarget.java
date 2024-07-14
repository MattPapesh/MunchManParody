package org.hid4java.mechanics.movement;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;

public class EnemyGoToTarget extends EnemyPredeterminedRoute
{
    private int[][] stage_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA;
    private final int STAGE_WIDTH = stage_data[0].length;
    private final int STAGE_HEIGHT = stage_data.length;
    private EntityMovement enemy_movement = null;
    private Enemy enemy = null; 
    private MunchMan munch_man = null;
    private Coordinates target_stage_coords = null;
    private double terminating_completion_pct = 0.0;
    private double turn_around_pct = -1.0;
    private boolean turn_around_status = false; 
    private LinkedList<Coordinates> avoiding_stage_coords = new LinkedList<Coordinates>();

    public EnemyGoToTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double terminating_completion_pct)
    {   
        super(enemy_movement, enemy);
        this.terminating_completion_pct = Math.max(Math.min(terminating_completion_pct, 1.0), 0.0);
        this.enemy_movement = enemy_movement; 
        this.enemy = enemy; 
        this.munch_man = munch_man;
        addRequirements(stage, enemy, munch_man);
    }

    public EnemyGoToTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    double terminating_completion_pct, double turn_around_pct)
    {   
        super(enemy_movement, enemy);
        this.terminating_completion_pct = Math.max(Math.min(terminating_completion_pct, 1.0), 0.0);
        this.turn_around_pct = (turn_around_pct != -1.0) ? Math.max(Math.min(turn_around_pct, 1.0), 0.0) : -1.0;
        this.enemy_movement = enemy_movement; 
        this.enemy = enemy; 
        this.munch_man = munch_man;
        turn_around_status = GameMath.probability(this.turn_around_pct);
        addRequirements(stage, enemy, munch_man);
    }

    public void setAvoidingStageCoordinates(Coordinates... avoiding_coords) {
        Coordinates[][] tunnels = Constants.STAGE_CHARACTERISTICS.STAGE_TUNNEL_REGIONS.clone();
        for(int i = 0; i < tunnels.length; i++) {
            Coordinates p0 = tunnels[i][0], p1 = tunnels[i][1];
            int min_x = Math.min(p0.getX(), p1.getX()), max_x = Math.max(p0.getX(), p1.getX());
            int min_y = Math.min(p0.getY(), p1.getY()), max_y = Math.max(p0.getY(), p1.getY());
            for(int k = 0; k < avoiding_coords.length; k++) {
                if(avoiding_coords[k] == null) {
                    continue;
                }

                int x = avoiding_coords[k].getX(), y = avoiding_coords[k].getY();
                if((x >= min_x && x <= max_x && y >= min_y && y <= max_y)) {
                    avoiding_coords[k] = null;
                }
            }
        }

        for(int i = 0; i < avoiding_coords.length; i++) {
            if(avoiding_coords[i] == null) {
                continue;
            }

            int x = avoiding_coords[i].getX();
            int y = avoiding_coords[i].getY();
            if(x >= 0 && x < STAGE_WIDTH && y >= 0 && y < STAGE_WIDTH
            && !(x == munch_man.getStageCoords().getX() && y == munch_man.getStageCoords().getY())) { 
                //this.avoiding_stage_coords.addLast(avoiding_coords[i]);
                //stage_data[y][x] = 0;
            }
        }
    }

    public void setTargetStageCoords(int target_stage_x, int target_stage_y)
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
            addRelativePath(delta_path_x, delta_path_y);
        }

        compileRelativePaths();
    }

    public LinkedList<Coordinates> getRoute(int target_stage_x, int target_stage_y)
    {
        try {
            // Marks "1" as not discovered, "0" as otherwise. 
            int[][] discovered_paths = new int[STAGE_HEIGHT][STAGE_WIDTH];
            int initial_degrees = enemy.getStageCoords().getDegrees();
            int ignore_direction = -1; 
            
            LinkedList<Coordinates> initial_route = new LinkedList<Coordinates>();
            LinkedList<LinkedList<Coordinates>> routes = new LinkedList<LinkedList<Coordinates>>();
            initial_route.addLast(new Coordinates(enemy.getStageCoords().getX(), enemy.getStageCoords().getY(), enemy.getStageCoords().getDegrees()));
            routes.addLast(initial_route);
            discovered_paths[enemy.getStageCoords().getY()][enemy.getStageCoords().getX()] = 1;

            if(!turn_around_status) {
                ignore_direction = initial_degrees + 180;
            }

            for(int i = 0; !(routes.getLast().getLast().getX() == target_stage_x && routes.getLast().getLast().getY() == target_stage_y); i++) {                
                LinkedList<Coordinates> route = routes.getFirst();
                int x = route.getLast().getX();
                int y = route.getLast().getY();

                if(i >= 4) {
                    routes.removeFirst();
                    ignore_direction = -1;
                    i = 0;
                }

                if(i == 0 && (ignore_direction == -1 || ignore_direction != 90)
                && y >= 1 && y < STAGE_HEIGHT && x >= 0 && x < STAGE_WIDTH 
                && stage_data[y - 1][x] == 1 && discovered_paths[y - 1][x] == 0) {
                    discovered_paths[y - 1][x] = 1;
                    LinkedList<Coordinates> up_route = new LinkedList<Coordinates>(route);
                    up_route.addLast(new Coordinates(x, y - 1, 90));
                    routes.addLast(up_route);
                }
                else if(i == 1 && (ignore_direction == -1 || ignore_direction != 270)
                && y >= 0 && y < STAGE_HEIGHT - 1 && x >= 0 && x < STAGE_WIDTH 
                && stage_data[y + 1][x] == 1 && discovered_paths[y + 1][x] == 0) {
                    discovered_paths[y + 1][x] = 1;
                    LinkedList<Coordinates> down_route = new LinkedList<Coordinates>(route);
                    down_route.addLast(new Coordinates(x, y + 1, 270));
                    routes.addLast(down_route);
                }
                else if(i == 2 && (ignore_direction == -1 || ignore_direction != 180)
                && y >= 0 && y < STAGE_HEIGHT && x >= 1 && x < STAGE_WIDTH 
                && stage_data[y][x - 1] == 1 && discovered_paths[y][x - 1] == 0) {
                    discovered_paths[y][x - 1] = 1;
                    LinkedList<Coordinates> left_route = new LinkedList<Coordinates>(route);
                    left_route.addLast(new Coordinates(x - 1, y, 180));
                    routes.addLast(left_route);
                }
                else if(i == 3 && (ignore_direction == -1 || ignore_direction != 0)
                && y >= 0 && y < STAGE_HEIGHT && x >= 0 && x < STAGE_WIDTH - 1 
                && stage_data[y][x + 1] == 1 && discovered_paths[y][x + 1] == 0) {
                    discovered_paths[y][x + 1] = 1;
                    LinkedList<Coordinates> right_route = new LinkedList<Coordinates>(route);
                    right_route.addLast(new Coordinates(x + 1, y, 0));
                    routes.addLast(right_route);
                } 
            }
            
            return routes.getLast();
        }
        catch(NoSuchElementException e) {
            return new LinkedList<Coordinates>();   
        }
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

    @Override
    public void initialize()
    {
        if(enemy_movement != null && !enemy_movement.isScheduled())
        {
            enemy_movement.schedule();
        }
        
        LinkedList<Coordinates> current_route = computeRoute(target_stage_coords.getX(), target_stage_coords.getY());
        enemy.setRoute(current_route);
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
    }
 
    @Override
    public boolean isFinished()
    {
        return sequentialMechanicGroupIsFinished() 
        || getCompletionPercentage() >= getTerminatingCompletionPercentage();
    }
}
