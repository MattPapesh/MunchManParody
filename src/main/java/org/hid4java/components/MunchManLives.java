package org.hid4java.components;

import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.animation.Animation;
import org.hid4java.fundamentals.component.ComponentBase;

public class MunchManLives extends ComponentBase
{
    private Animation[] lives = {new Animation("lives3.png"), 
    new Animation("lives2.png"), new Animation("lives1.png")};
    public MunchManLives()
    {
        double width = Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH;
        double height = Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT;
        addRequirements((int)(width * 0.6), (int)(height * 0.03), 0, lives);
    } 

    void set(int lives) 
    {
        setAnimation(getAnimation(3 - lives).getName());
    }
}
