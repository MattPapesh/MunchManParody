package components;

import fundamentals.animation.Animation;
import fundamentals.component.*;;

public class Stage extends ComponentBase {
    
    private Animation stage = new Animation("stage.png");
    private final int[][] stage_tiles = 
    {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {}
    };

    public Stage()
    {
        addRequirements(stage.getImageWidth() / 2, stage.getImageHeight() / 2, 0, stage);
    }
}
