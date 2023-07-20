package components;

import fundamentals.animation.Animation;

public class Enemy extends EntityBase
{
    // The magnitude of delta_x & delta_y in granular stage coordinates: 
    private int speed = 0; 

    public Enemy() {}
    public Enemy(int stage_x, int stage_y, int speed, Animation... enemy)
    {
        begin(stage_x, stage_y, enemy);  
        this.speed = speed; 
    }

    public void setSpeed(int speed) 
    {
        this.speed = speed;
    }

    public int getSpeed()
    {
        return speed; 
    }
}
