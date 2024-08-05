package org.hid4java.components;

import java.util.LinkedList;

import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.Coordinates;
import org.hid4java.fundamentals.animation.Animation;
import org.hid4java.fundamentals.component.ComponentBase;

public class StageChain extends ComponentBase
{
    private Animation stage_chain = new Animation("chain.png");
    private LinkedList<Coordinates> logged_chain_data = new LinkedList<Coordinates>();
    private int coord_update_tolerance = 15; 
    public final int MAX_NUM_OF_CHAIN = 384;

    public StageChain() 
    {   
        stage_chain.setAlpha(0, 0, stage_chain.getImageWidth(), stage_chain.getImageHeight(), 0);
        addRequirements((stage_chain.getImageWidth() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getX(), 
        (stage_chain.getImageHeight() / 2) + Constants.STAGE_CHARACTERISTICS.COORD_DISPLACEMENT.getY(), 0, stage_chain);
    }

    private boolean isChainPlacementLogged(int stage_x, int stage_y)
    {
        for(int i = 0; i < logged_chain_data.size(); i++)
        {
            if(logged_chain_data.get(i).getX() == stage_x && logged_chain_data.get(i).getY() == stage_y)
            {
                return true; 
            }
        }

        return false; 
    }

    public void logChainPlacement(int stage_x, int stage_y)
    {
        if(stage_x >= 0 && stage_x < Constants.STAGE_CHARACTERISTICS.STAGE_DATA[0].length
        && stage_y >= 0 && stage_y < Constants.STAGE_CHARACTERISTICS.STAGE_DATA.length
        && !(stage_x >= Constants.STAGE_CHARACTERISTICS.SPAWN_REGION[0].getX() && stage_x <= Constants.STAGE_CHARACTERISTICS.SPAWN_REGION[1].getX()
        && stage_y >= Constants.STAGE_CHARACTERISTICS.SPAWN_REGION[0].getY() && stage_y <= Constants.STAGE_CHARACTERISTICS.SPAWN_REGION[1].getY())
        && !isChainPlacementLogged(stage_x, stage_y))
        {
            logged_chain_data.addLast(new Coordinates(stage_x, stage_y, 0));
        }
    }

    public void reset() 
    {
        logged_chain_data.clear();
        stage_chain.setAlpha(0, 0, Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH, 
        Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT, 0);
    }

    public boolean isAllChainPlaced()
    {
        return logged_chain_data.size() >= MAX_NUM_OF_CHAIN;
    }

    public int numOfChain()
    {
        return logged_chain_data.size();
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
