package org.hid4java.components;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.function.Function;

import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;
import org.hid4java.mechanics.movement.EntityMovement;

public class Enemy extends EntityBase
{
    // The magnitude of delta_x & delta_y in granular stage coordinates: 
    private double speed = 0; 
    private LinkedList<Coordinates> route_traveled = new LinkedList<Coordinates>();
    private Animation[] enemy = null; 
    private Animation[] weakened_enemy = null;
    private boolean is_weakened_state = false; 
    private EntityMovement enemy_movement = null; 
    public final Coordinates SPAWN_STAGE_COORD;

    boolean initialized = false; 

    public Enemy(int stage_x, int stage_y, int degrees, double speed, 
    double hue, double weakened_hue, Function<Integer, Double> weakened_saturation, Animation... enemy)
    {   
        Animation[] hued_enemy = new Animation[enemy.length];
        for(int i = 0; i < enemy.length; i++) {
            hued_enemy[i] = new Animation(enemy[i].getHSVAnimation(hue, 0, 0));
        }

        Animation[] weakened_hued_enemy = new Animation[enemy.length];
        for(int i = 0; i < enemy.length; i++) {
            weakened_hued_enemy[i] = new Animation(enemy[i].getHSVAnimation(weakened_hue, weakened_saturation.apply(i), 0));
        }

        begin(stage_x, stage_y, degrees, hued_enemy);  
        this.speed = speed; 
        this.enemy = hued_enemy;
        this.weakened_enemy = weakened_hued_enemy;
        this.SPAWN_STAGE_COORD = new Coordinates(stage_x, stage_y, degrees);
    }

    public void appendEntityMovement(EntityMovement movement) 
    {
        enemy_movement = movement;
    } 

    public void reset(int stage_x, int stage_y, int degree) 
    {
        initialized = false;
        if(enemy_movement != null) {
            enemy_movement.reset(stage_x, stage_y, degree);
            importAnimations(enemy);
        }
    } 

    public void reset() 
    {
        reset(SPAWN_STAGE_COORD.getX(), SPAWN_STAGE_COORD.getY(), SPAWN_STAGE_COORD.getDegrees());
    }

    public boolean isEnemyInitialized() 
    {
        return initialized;
    }

    public void initializeEnemy() 
    {
        initialized = true;
    }
    
    public void enableWeakenedState(boolean enable) 
    {
        is_weakened_state = enable;
        if(enable) {
            importAnimations(weakened_enemy);
        }
        else {
            importAnimations(enemy);
        }
    }

    public boolean isWeakenedState() 
    {
        return is_weakened_state;
    }

    public void setRoute(LinkedList<Coordinates> route) 
    {
        this.route_traveled = route;
    }

    public LinkedList<Coordinates> getRouteTraveled()
    {
        return route_traveled;
    }

    public void setSpeed(double speed) 
    {
        this.speed = speed;
    }

    public double getSpeed()
    {
        return speed; 
    }
}
