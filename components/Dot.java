package components;
import fundamentals.animation.Animation;
import fundamentals.component.ComponentScheduler;

public class Dot extends EntityBase
{
    private Animation dot_anim = new Animation("dot.png");

    public Dot(int stage_x, int stage_y)
    {
        begin(stage_x, stage_y, dot_anim);
    }

    public void delete()
    {
        ComponentScheduler.removeComponent(this);
    } 
}
