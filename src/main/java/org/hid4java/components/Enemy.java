package org.hid4java.components;

import java.util.LinkedList;

import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;

public class Enemy extends EntityBase
{
    // The magnitude of delta_x & delta_y in granular stage coordinates: 
    private double speed = 0; 
    private LinkedList<Coordinates> route_traveled = new LinkedList<Coordinates>();

    public Enemy() {}
    public Enemy(int stage_x, int stage_y, double speed, Animation... enemy)
    {
        begin(stage_x, stage_y, enemy);  
        this.speed = speed; 
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
