package org.hid4java.mechanics.behavior;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.mechanics.behaviorbases.EnemyBehaviorBase;
import org.hid4java.mechanics.movement.EntityMovement;

public class LevelBehavior extends EnemyBehaviorBase
{
    private Coordinates spawn_stage_coord = null;
    private Coordinates start_stage_coord = null;
    private Coordinates home_stage_coord = null; 
    private EnemyBehaviorBase behavior = null; 
    private Enemy enemy = null; 

    private boolean spawned = false; 
    private boolean started = false; 
    private boolean homed = false; 
    private int level = 0;

    public LevelBehavior(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man,
    Coordinates spawn_stage_coord, Coordinates start_stage_coord, Coordinates home_stage_coord, 
    EnemyBehaviorBase behavior) 
    {
        super(enemy_movement, stage, enemy, munch_man);
        this.spawn_stage_coord = spawn_stage_coord;
        this.start_stage_coord = start_stage_coord;
        this.home_stage_coord = home_stage_coord;
        this.behavior = behavior;
        this.enemy = enemy;
        reset();
    }    

    private void setLevel(int level) 
    {
        this.level = level;
        spawned = false; 
        started = false; 
        homed = false; 
        enemy.setStageCoords(spawn_stage_coord.getX(), spawn_stage_coord.getY(), spawn_stage_coord.getDegrees());
        behavior.cancel();
    }

    public void nextLevel(int level) 
    {
        setLevel(level + 1);
    }

    public void reset() 
    {
        setLevel(0);
    }

    @Override
    public void initializeBehavior() 
    {

    }

    @Override
    public void executeBehavior() 
    {
        if(!spawned) 
        {
            spawned = true;
            setEnemyTarget(1.0, start_stage_coord.getX(), start_stage_coord.getY());
        }
        else if(GameMath.isCoordsEqual(enemy.getStageCoords(), start_stage_coord) && !started) 
        {
            started = true;
            setEnemyTarget(0, enemy.getStageCoords().getX(), enemy.getStageCoords().getY());
            setEnemyTarget(1.0, home_stage_coord.getX(), home_stage_coord.getY());
        }
        else if(GameMath.isCoordsEqual(enemy.getStageCoords(), home_stage_coord) && !homed) 
        {
            setEnemyTarget(0, enemy.getStageCoords().getX(), enemy.getStageCoords().getY());
            behavior.schedule();
        }
    }

    @Override 
    public void endBehavior(boolean interrupted) 
    {

    }

    @Override
    public boolean isBehaviorFinished() 
    {
        return false; 
    }
}
