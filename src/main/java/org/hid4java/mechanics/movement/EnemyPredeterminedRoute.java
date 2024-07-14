package org.hid4java.mechanics.movement;

import java.util.LinkedList;

import org.hid4java.components.Enemy;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.fundamentals.mechanic.SequentialMechanicGroup;

public class EnemyPredeterminedRoute extends SequentialMechanicGroup
{
    private EntityMovement enemy_movement = null; 
    private Enemy enemy = null; 
    private Coordinates prev_gran_stage_coords = null;
    private LinkedList<EnemyPathFollowing> enemy_paths = new LinkedList<EnemyPathFollowing>();

    public EnemyPredeterminedRoute(EntityMovement enemy_movement, Enemy enemy)
    {
        this.enemy = enemy;
        this.enemy_movement = enemy_movement;
        addRequirements(enemy);
    }

    public void addRelativePath(int delta_stage_x, int delta_stage_y)
    {
        enemy_paths.addLast(new EnemyPathFollowing(enemy_movement, enemy, delta_stage_x, delta_stage_y));
    }

    public void removeRelativePath(int index)
    {
        try
        {
            enemy_paths.remove(index);
        }
        catch(IndexOutOfBoundsException e) {}
    }

    public int getNumOfPaths()
    {
        return getNumOfMechanics();
    }

    public int getCurrentPathIndexScheduled()
    {
        return getCurrentMechanicIndexScheduled();
    }

    public void compileRelativePaths()
    {
        for(int i = 0; i < enemy_paths.size(); i++)
        {
            addMechanics(enemy_paths.get(i));
        }
    }

    @Override
    public void initialize()
    {
        sequentialMechanicGroupInitialize();
        prev_gran_stage_coords = enemy.getGranularStageCoords();
    }

    @Override
    public void execute()
    {
        sequentialMechanicGroupExecute();
        prev_gran_stage_coords = enemy.getGranularStageCoords();
    }

    @Override
    public boolean isFinished()
    {
        return /*(GameMath.isCoordsEqual(prev_gran_stage_coords, enemy.getGranularStageCoords())) 
        || */sequentialMechanicGroupIsFinished();
    }
}
