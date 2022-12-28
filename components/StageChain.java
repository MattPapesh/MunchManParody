package components;

import fundamentals.Constants;
import fundamentals.Coordinates;
import fundamentals.animation.Animation;
import fundamentals.component.ComponentBase;

public class StageChain extends ComponentBase
{
    private Animation stage_chain = new Animation("chain.png");
    private int[][] stage_chain_data = Constants.STAGE_CHARACTERISTICS.STAGE_DATA.clone();
    private int coord_update_tolerance = 15; 
    private final int CHAIN_ID = 2; 

    public StageChain() 
    {   
        stage_chain.setAlpha(0, 0, stage_chain.getImageWidth(), stage_chain.getImageHeight(), 0);
        addRequirements((stage_chain.getImageWidth() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX(), 
        (stage_chain.getImageHeight() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY(), 0, stage_chain);
    }

    public void logChainPlacement(int stage_x, int stage_y)
    {
        if(stage_x < stage_chain_data[0].length && stage_y < stage_chain_data.length)
        {
            stage_chain_data[stage_y][stage_x] = CHAIN_ID;
        }
    }

    public void update(int raw_x, int raw_y)
    {
        stage_chain.setAlpha(raw_x - coord_update_tolerance - Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX(), 
        raw_y - coord_update_tolerance - Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY(), 
        raw_x + coord_update_tolerance - Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX(), 
        raw_y + coord_update_tolerance - Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY(),
        255);        
    }
}
