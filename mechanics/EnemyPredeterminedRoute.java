package mechanics;

import java.util.LinkedList;

import components.Enemy;
import fundamentals.mechanic.SequentialMechanicGroup;

public class EnemyPredeterminedRoute extends SequentialMechanicGroup
{
    private EntityMovement enemy_movement = null; 
    private Enemy enemy = null; 
    private LinkedList<EnemyMovement> enemy_paths = new LinkedList<EnemyMovement>();

    public EnemyPredeterminedRoute(EntityMovement enemy_movement, Enemy enemy)
    {
        this.enemy = enemy;
        this.enemy_movement = enemy_movement;
        addRequirements(enemy);
    }

    public void addRelativePath(int delta_stage_x, int delta_stage_y)
    {
        enemy_paths.addLast(new EnemyMovement(enemy_movement, enemy, delta_stage_x, delta_stage_y));
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
        return enemy_paths.size();
    }

    public void compileRelativePaths()
    {
        for(int i = 0; i < enemy_paths.size(); i++)
        {
            addMechanics(enemy_paths.get(i));
        }
    }
}
