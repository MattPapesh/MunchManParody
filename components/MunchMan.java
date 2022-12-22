package components;

import fundamentals.animation.Animation;
import fundamentals.component.ComponentBase;

public class MunchMan extends ComponentBase 
{
    private Animation munch_man = new Animation("mm1.png");  
    
    public MunchMan() 
    {
        addRequirements(105, 150, 0, munch_man);
    }
}
