package org.hid4java.components;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;

public class Enemy extends EntityBase
{
    // The magnitude of delta_x & delta_y in granular stage coordinates: 
    private double speed = 0; 
    private double hue = 0;
    private LinkedList<Coordinates> route_traveled = new LinkedList<Coordinates>();

    public Enemy() {}
    public Enemy(int stage_x, int stage_y, int degrees, double speed, double hue, Animation... enemy)
    {   
        Animation[] hued_enemy = new Animation[enemy.length];
        for(int i = 0; i < enemy.length; i++) {
            hued_enemy[i] = new Animation(enemy[i].getHuedAnimation(hue));
        }

        begin(stage_x, stage_y, degrees, hued_enemy);  
        this.speed = speed; 
        this.hue = hue; 
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
