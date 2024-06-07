package org.hid4java.mechanics.movement;
 
import java.util.LinkedList;
import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;

public class EnemyGoNearTarget extends EnemyGoToTarget 
{    
    private int[][] stage_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA; 

    public EnemyGoNearTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double terminating_completion_pct, int target_stage_x, int target_stage_y) 
    {
        super(enemy_movement, stage, enemy, munch_man, terminating_completion_pct);
        target_stage_x = Math.max(Math.min(target_stage_x, stage_data[0].length - 1), 0);
        target_stage_y = Math.max(Math.min(target_stage_y, stage_data.length - 1), 0);
        Coordinates target_stage_coords = getNearTargetStageCoords(target_stage_x, target_stage_y);
        setTargetStageCoords(target_stage_coords.getX(), target_stage_coords.getY());
        addRequirements(stage, enemy);
    }

    public EnemyGoNearTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double terminating_completion_pct, double turn_around_pct, int target_stage_x, int target_stage_y) 
    {
        super(enemy_movement, stage, enemy, munch_man, terminating_completion_pct, turn_around_pct);
        target_stage_x = Math.max(Math.min(target_stage_x, stage_data[0].length - 1), 0);
        target_stage_y = Math.max(Math.min(target_stage_y, stage_data.length - 1), 0);
        Coordinates target_stage_coords = getNearTargetStageCoords(target_stage_x, target_stage_y);
        setTargetStageCoords(target_stage_coords.getX(), target_stage_coords.getY());
        addRequirements(stage, enemy);
    }

    private LinkedList<Coordinates> getBranchSearchVariant(LinkedList<Coordinates> base_search, LinkedList<Coordinates> search, int target_stage_x, int target_stage_y)
    {
        try
        {
            if(!GameMath.isCoordsEqual(search.getLast(), base_search.getLast()) 
            && search.getLast().getX() >= 0 && search.getLast().getY() >= 0
            && search.getLast().getX() < stage_data[0].length && search.getLast().getY() < stage_data.length
            && (!GameMath.isOpposingCoordDirections(search.getLast(), base_search.getLast()) || base_search.size() == 1))
            {
                return search;
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {}
        return null;
    }

    public LinkedList<LinkedList<Coordinates>> getBranchSearches(LinkedList<Coordinates> base_search, int target_stage_x, int target_stage_y)
    {
        // Branch searches:
        LinkedList<LinkedList<Coordinates>> searches = new LinkedList<LinkedList<Coordinates>>();
        LinkedList<Coordinates> left_search = new LinkedList<Coordinates>(base_search);
        LinkedList<Coordinates> right_search = new LinkedList<Coordinates>(base_search);
        LinkedList<Coordinates> up_search = new LinkedList<Coordinates>(base_search);
        LinkedList<Coordinates> down_search = new LinkedList<Coordinates>(base_search);
        
        left_search.addLast(new Coordinates(base_search.getLast().getX() - 1, base_search.getLast().getY(), 180));
        right_search.addLast(new Coordinates(base_search.getLast().getX() + 1, base_search.getLast().getY(), 0));
        up_search.addLast(new Coordinates(base_search.getLast().getX(), base_search.getLast().getY() - 1, 90));
        down_search.addLast(new Coordinates(base_search.getLast().getX(), base_search.getLast().getY() + 1, 270));

        left_search = getBranchSearchVariant(base_search, left_search, target_stage_x, target_stage_y);
        right_search = getBranchSearchVariant(base_search, right_search, target_stage_x, target_stage_y);
        up_search = getBranchSearchVariant(base_search, up_search, target_stage_x, target_stage_y);
        down_search = getBranchSearchVariant(base_search, down_search, target_stage_x, target_stage_y);

        if(left_search != null)
            searches.addLast(left_search);
        if(right_search != null)
            searches.addLast(right_search);
        if(up_search != null)
            searches.addLast(up_search);
        if(down_search != null)
            searches.addLast(down_search);

        return searches;
    }    

    private LinkedList<Coordinates> getSearchFound(LinkedList<LinkedList<Coordinates>> searches, int target_stage_x, int target_stage_y)
    {
        try
        {
            for(int i = 0; i < searches.size(); i++)
            {
                if(stage_data[searches.get(i).getLast().getY()][searches.get(i).getLast().getX()] == 1)
                {
                    return searches.get(i);
                }
            }

        }
        catch(ArrayIndexOutOfBoundsException e) {}
        return null; 
    }

    // when building new search with searches: a search is already logged if the next search found is found in a logged search
    public Coordinates getNearTargetStageCoords(int target_stage_x, int target_stage_y)
    {
        LinkedList<Coordinates> initial_search = new LinkedList<Coordinates>();
        LinkedList<LinkedList<Coordinates>> logged_searches = new LinkedList<LinkedList<Coordinates>>();
        initial_search.addLast(new Coordinates(target_stage_x, target_stage_y, 0));
        logged_searches.addLast(initial_search);
        LinkedList<Coordinates> search_found = getSearchFound(logged_searches, target_stage_x, target_stage_y);

        while(search_found == null)
        {
            LinkedList<LinkedList<Coordinates>> updated_logged_searchs = new LinkedList<LinkedList<Coordinates>>();
            for(int i = 0; i < logged_searches.size(); i++)
            {
                updated_logged_searchs.addAll(getBranchSearches(logged_searches.get(i), target_stage_x, target_stage_y));
            }

            logged_searches.clear();
            logged_searches.addAll(updated_logged_searchs);
            search_found = getSearchFound(logged_searches, target_stage_x, target_stage_y);
        }

        return search_found.getLast();
    }
}
