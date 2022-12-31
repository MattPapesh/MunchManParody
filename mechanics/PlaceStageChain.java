package mechanics;

import components.MunchMan;
import components.Stage;
import components.StageChain;
import fundamentals.mechanic.MechanicBase;

public class PlaceStageChain extends MechanicBase
{
    private MunchMan munch_man = null;
    private Stage stage = null;
    private StageChain stage_chain = null;

    public PlaceStageChain(MunchMan munch_man, Stage stage, StageChain stage_chain)
    {
        this.munch_man = munch_man;
        this.stage = stage;
        this.stage_chain = stage_chain;

        setExecutionalPeriodicDelay(0);
        addRequirements(munch_man, stage, stage_chain);
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
    }

    @Override
    public void end(boolean interrupted)
    {
        System.out.print("You Win! :)");
    }

    @Override
    public boolean isFinished()
    {
        return stage_chain.isAllChainPlaced();
    }
}
