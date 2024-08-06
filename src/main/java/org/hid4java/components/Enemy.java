package org.hid4java.components;

import java.util.LinkedList;
import java.util.function.Function;

import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.GameMath;
import org.hid4java.fundamentals.animation.Animation;
import org.hid4java.mechanics.movement.EntityMovement;

public class Enemy extends EntityBase
{
    // The magnitude of delta_x & delta_y in granular stage coordinates: 
    private double speed = 0; 
    private LinkedList<Coordinates> route_traveled = new LinkedList<Coordinates>();
    
    private Animation[] enemy_0 = new Animation[Constants.ENEMY[0].length];
    private Animation[] enemy_1 = new Animation[Constants.ENEMY[1].length];
    private Animation[] enemy_2 = new Animation[Constants.ENEMY[2].length];
    private Animation[] enemy_3 = new Animation[Constants.ENEMY[3].length];

    private Animation[] weakened_enemy_0 = new Animation[Constants.ENEMY[0].length];
    private Animation[] weakened_enemy_1 = new Animation[Constants.ENEMY[1].length];
    private Animation[] weakened_enemy_2 = new Animation[Constants.ENEMY[2].length];
    private Animation[] weakened_enemy_3 = new Animation[Constants.ENEMY[3].length];

    private boolean is_weakened_state = false; 
    private EntityMovement enemy_movement = null; 
    public final Coordinates SPAWN_STAGE_COORD;
    private int enemy_index = 0;

    private boolean initialized = false;
    public boolean targeting = false; 

    public Enemy(int stage_x, int stage_y, int degrees, double speed, 
    double hue, double weakened_hue, Function<Integer, Double> weakened_saturation)
    {   
        // The type is the kind of monster 
        /*for(int type = 0; type < Constants.ENEMY.length; type++) {

            for(int i = 0; i < Constants.ENEMY[type].length; i++) {
                try {
                    this.enemy[type][i] = new Animation(Constants.ENEMY[type][i].getHSVAnimation(hue, 0, 0));
                } catch(NullPointerException e) {}
            }

            for(int i = 0; i < Constants.ENEMY[type].length; i++) {
                try {
                    this.weakened_enemy[type][i] = new Animation(Constants.ENEMY[type][i].getHSVAnimation(weakened_hue, weakened_saturation.apply(i), 0));
                } catch(NullPointerException e) {}
            }
        }*/

        for(int i = 0; i < enemy_0.length; i++) {
            enemy_0[i] = new Animation(Constants.ENEMY[0][i].getHSVAnimation(hue, 0, 0));
            weakened_enemy_0[i] = new Animation(Constants.ENEMY[0][i].getHSVAnimation(weakened_hue, weakened_saturation.apply(i), 0));
        }

        for(int i = 0; i < enemy_1.length; i++) {
            enemy_1[i] = new Animation(Constants.ENEMY[1][i].getHSVAnimation(hue, 0, 0));
            weakened_enemy_1[i] = new Animation(Constants.ENEMY[1][i].getHSVAnimation(weakened_hue, weakened_saturation.apply(i), 0));
        }

        for(int i = 0; i < enemy_2.length; i++) {
            enemy_2[i] = new Animation(Constants.ENEMY[2][i].getHSVAnimation(hue, 0, 0));
            weakened_enemy_2[i] = new Animation(Constants.ENEMY[2][i].getHSVAnimation(weakened_hue, weakened_saturation.apply(i), 0));
        }

        for(int i = 0; i < enemy_3.length; i++) {
            enemy_3[i] = new Animation(Constants.ENEMY[3][i].getHSVAnimation(hue, 0, 0));
            weakened_enemy_3[i] = new Animation(Constants.ENEMY[3][i].getHSVAnimation(weakened_hue, weakened_saturation.apply(i), 0));
        }

        begin(stage_x, stage_y, degrees, enemy_0);  
        enableWeakenedState(false);
        this.enemy_index = GameMath.getWrapAroundMapping(4, Constants.level);
        this.speed = speed; 
        this.SPAWN_STAGE_COORD = new Coordinates(stage_x, stage_y, degrees);
    }

    private Animation[] getEnemyType(int type) 
    {
        switch(type) {
            case(0): return enemy_0;
            case(1): return enemy_1;
            case(2): return enemy_2;
            case(3): return enemy_3;
            default: return null;
        }
    }

    private Animation[] getWeakenedEnemyType(int type) 
    {
        switch(type) {
            case(0): return weakened_enemy_0;
            case(1): return weakened_enemy_1;
            case(2): return weakened_enemy_2;
            case(3): return weakened_enemy_3;
            default: return null;
        }
    }

    public void appendEntityMovement(EntityMovement movement) 
    {
        enemy_movement = movement;
    } 

    public void reset(int stage_x, int stage_y, int degree) 
    {
        enemy_index = GameMath.getWrapAroundMapping(4, Constants.level);
        initialized = false;
        if(enemy_movement != null) {
            enemy_movement.reset(stage_x, stage_y, degree);
            //enableWeakenedState(is_weakened_state);
            importAnimations(getEnemyType(enemy_index));
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
            importAnimations(getWeakenedEnemyType(enemy_index));
        }
        else {
            importAnimations(getEnemyType(enemy_index));
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
