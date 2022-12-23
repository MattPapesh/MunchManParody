package components;

import fundamentals.Constants;
import fundamentals.animation.Animation;
import fundamentals.component.*;

public class Stage extends ComponentBase {
    
    private Animation stage = new Animation("stage.png");
    private int[][] stage_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA;

    public Stage()
    {
        addRequirements((stage.getImageWidth() / 2) - 50, stage.getImageHeight() / 2, 0, stage);
    }

    public int[][] getStageData() 
    {
        return stage_data;
    }
}
