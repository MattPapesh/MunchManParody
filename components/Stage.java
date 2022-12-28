package components;

import fundamentals.Constants;
import fundamentals.animation.Animation;
import fundamentals.component.*;

public class Stage extends ComponentBase {
    
    private Animation stage = new Animation("stage.png");
    private int[][] stage_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA.clone();

    public Stage()
    {
        addRequirements((stage.getImageWidth() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX(), 
        (stage.getImageHeight() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY(), 0, stage);
    }

    public int[][] getStageData() 
    {
        return stage_data;
    }
}
