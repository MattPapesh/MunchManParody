package org.hid4java.mechanics.movement;
 
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;

public class EnemyGoNearTarget extends EnemyGoToTarget 
{    
    private int[][] stage_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA;
    private int STAGE_WIDTH = stage_data[0].length;
    private int STAGE_HEIGHT = stage_data.length;
    private Enemy enemy = null;

    public EnemyGoNearTarget(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, 
    double terminating_completion_pct, int target_stage_x, int target_stage_y) 
    {
        super(enemy_movement, stage, enemy, munch_man, terminating_completion_pct);
        this.enemy = enemy;
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
        this.enemy = enemy;
        target_stage_x = Math.max(Math.min(target_stage_x, stage_data[0].length - 1), 0);
        target_stage_y = Math.max(Math.min(target_stage_y, stage_data.length - 1), 0);
        Coordinates target_stage_coords = getNearTargetStageCoords(target_stage_x, target_stage_y);
        setTargetStageCoords(target_stage_coords.getX(), target_stage_coords.getY());
        addRequirements(stage, enemy);
    }

    public Coordinates getNearTargetStageCoords(int target_stage_x, int target_stage_y) 
    {
        target_stage_x = Math.min(Math.max(target_stage_x, 0), STAGE_WIDTH - 1);
        target_stage_y = Math.min(Math.max(target_stage_y, 0), STAGE_HEIGHT - 1);
        if(stage_data[target_stage_y][target_stage_x] == 1) {
            return new Coordinates(target_stage_x, target_stage_y, enemy.getStageCoords().getDegrees());
        }

       // try {
            // Marks "1" as not discovered, "0" as otherwise. 
            int[][] discovered_paths = new int[STAGE_HEIGHT][STAGE_WIDTH];
            
            LinkedList<Coordinates> initial_search = new LinkedList<Coordinates>();
            LinkedList<LinkedList<Coordinates>> searches = new LinkedList<LinkedList<Coordinates>>();
            initial_search.addLast(new Coordinates(target_stage_x, target_stage_y, enemy.getStageCoords().getDegrees()));
            searches.addLast(initial_search);
            discovered_paths[target_stage_y][target_stage_x] = 1;

            for(int i = 0; stage_data[searches.getLast().getLast().getY()][searches.getLast().getLast().getX()] != 1; i++) {                
                LinkedList<Coordinates> search = searches.getFirst();
                int x = search.getLast().getX();
                int y = search.getLast().getY();

                if(i >= 4) { 
                    searches.removeFirst();
                    i = 0;
                }

                if(i == 0 
                && y >= 1 
                && y < STAGE_HEIGHT 
                && x >= 0 
                && x < STAGE_WIDTH 
                && discovered_paths[y - 1][x] == 0) {
                    discovered_paths[y - 1][x] = 1;
                    LinkedList<Coordinates> up_search = new LinkedList<Coordinates>(search);
                    up_search.addLast(new Coordinates(x, y - 1, enemy.getStageCoords().getDegrees()));
                    searches.addLast(up_search);
                }
                else if(i == 1 
                && y >= 0 
                && y < STAGE_HEIGHT - 1 
                && x >= 0 
                && x < STAGE_WIDTH 
                && discovered_paths[y + 1][x] == 0) {
                    discovered_paths[y + 1][x] = 1;
                    LinkedList<Coordinates> down_search = new LinkedList<Coordinates>(search);
                    down_search.addLast(new Coordinates(x, y + 1, enemy.getStageCoords().getDegrees()));
                    searches.addLast(down_search);
                }
                else if(i == 2 
                && y >= 0 
                && y < STAGE_HEIGHT 
                && x >= 1 
                && x < STAGE_WIDTH 
                && discovered_paths[y][x - 1] == 0) {
                    discovered_paths[y][x - 1] = 1;
                    LinkedList<Coordinates> left_search = new LinkedList<Coordinates>(search);
                    left_search.addLast(new Coordinates(x - 1, y, enemy.getStageCoords().getDegrees()));
                    searches.addLast(left_search);
                }
                else if(i == 3 
                && y >= 0 
                && y < STAGE_HEIGHT 
                && x >= 0 && x < STAGE_WIDTH - 1 
                && discovered_paths[y][x + 1] == 0) {
                    discovered_paths[y][x + 1] = 1;
                    LinkedList<Coordinates> right_search = new LinkedList<Coordinates>(search);
                    right_search.addLast(new Coordinates(x + 1, y, enemy.getStageCoords().getDegrees()));
                    searches.addLast(right_search);
                } 
            }
            
            return searches.getLast().getLast();
        //}
        //catch(NoSuchElementException e) {
        //    return new Coordinates(target_stage_x, target_stage_y, enemy.getStageCoords().getDegrees());   
        //}
    }    
}