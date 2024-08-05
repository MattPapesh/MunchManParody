package org.hid4java.components;

import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.animation.Animation;
import org.hid4java.fundamentals.component.*;

public class Stage extends ComponentBase {
    
    private Animation stage = new Animation("stage.png");
    private int[][] stage_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA;

    public Stage()
    {
        addRequirements((stage.getImageWidth() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX(), 
        (stage.getImageHeight() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY(), 0, stage);

        Animation[] hued_stages = {
            stage, 
            new Animation(stage.getHSVAnimation(-0.10, 0, 0)),
            new Animation(stage.getHSVAnimation(0.40, 0, 0)), 
            new Animation(stage.getHSVAnimation(0.25, 0, 0)), 
            new Animation(stage.getHSVAnimation(0.1, 0, 0)),
            new Animation(stage.getHSVAnimation(-0.20, 0, 0)),
            new Animation(stage.getHSVAnimation(0.64, 0, 0))
        };

        importAnimations(hued_stages);
    }

    public int[][] getStageData() 
    {
        return stage_data;
    }


}
