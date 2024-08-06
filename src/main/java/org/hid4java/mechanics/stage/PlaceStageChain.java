package org.hid4java.mechanics.stage;

import org.hid4java.app.audio.AppAudio;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.components.StageChain;
import org.hid4java.fundamentals.Constants;
import org.hid4java.fundamentals.mechanic.MechanicBase;

public class PlaceStageChain extends MechanicBase
{
    private MunchMan munch_man = null;
    private StageChain stage_chain = null;
    private int chain_count = 0; 

    public PlaceStageChain(MunchMan munch_man, Stage stage, StageChain stage_chain)
    {
        this.munch_man = munch_man;
        this.stage_chain = stage_chain;

        setExecutionalPeriodicDelay(0);
        addRequirements(munch_man, stage_chain);
    }

    @Override
    public void initialize()
    {

    }

    @Override
    public void execute()
    {   
        stage_chain.update(munch_man.getCoordinates().getX(), munch_man.getCoordinates().getY());
        stage_chain.logChainPlacement(munch_man.getStageCoords().getX(), munch_man.getStageCoords().getY());
        int pts = Constants.PELLET_PTS * (Math.max(stage_chain.numOfChain() - chain_count, 0));
        Constants.score += pts; 
        chain_count = stage_chain.numOfChain();
        if(pts > 0) {
            AppAudio.playAudioFile("Chain.wav");
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
