package org.hid4java.components;

import org.hid4java.fundamentals.animation.Animation;

public class PowerPellet extends EntityBase
{
    double tick_speed = 0; 
    double ticks = 0;
    final double MAX_DEGREES = 30.0;
    boolean eaten = false; 
    private Animation[] pellet = {
        new Animation("apple.png"), new Animation("orange.png"), 
        new Animation("cherry.png"), new Animation("pear.png"), 
        new Animation("watermelon.png"), new Animation("banana.png")}; 

    public PowerPellet(int stage_x, int stage_y, double tick_speed) 
    {
        begin(stage_x, stage_y, pellet);
        this.tick_speed = tick_speed;
    }

    public void run() 
    {
        ticks = (ticks + tick_speed <= 2.0 * Math.PI) ? ticks + tick_speed : 0;
        setCoordinates(getCoordinates().getX(), getCoordinates().getY(), (int)(MAX_DEGREES * Math.sin(ticks)));
        if(ticks == 0) {
            reset();
        }
    }

    public void eat() 
    {
        setOpacity(0);
        eaten = true; 
    }

    public boolean getEaten() 
    {
        return eaten;
    }

    public void reset() 
    {
        eaten = false;
        setOpacity(1);
        setNextAnimation();
    }
}
