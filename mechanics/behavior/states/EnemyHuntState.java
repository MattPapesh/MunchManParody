package mechanics.behavior.states;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.behavior.lowerlevel.EnemyFlankHuntBehavior;
import mechanics.behavior.lowerlevel.EnemyHuntBehavior.flank_data;
import mechanics.movement.EntityMovement;

public class EnemyHuntState extends EnemyFlankHuntBehavior
{
    public EnemyHuntState(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man, flank_data flank)
    {
        super(enemy_movement, stage, enemy, munch_man, flank);
        //set
    }

    @Override
    public boolean isSelfSchedulingConditionsMet()
    {
        return false;
    }

    @Override
    public boolean isBehaviorFinished()
    {
        return !isSelfSchedulingConditionsMet();
    }
}
