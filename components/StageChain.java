package components;

import fundamentals.animation.Animation;
import fundamentals.component.ComponentBase;

public class StageChain extends ComponentBase
{
    private Animation stage_chain = new Animation("chain.png");

    public StageChain() 
    {
        addRequirements((stage_chain.getImageWidth() / 2) - 50, stage_chain.getImageHeight() / 2, 0, stage_chain);
    }
}
