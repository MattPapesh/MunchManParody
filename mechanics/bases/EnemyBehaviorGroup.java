package mechanics.bases;

import java.util.LinkedList;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.mechanic.MechanicBase;

public class EnemyBehaviorGroup extends MechanicBase 
{
    private LinkedList<EnemyBehaviorBase> behaviors = new LinkedList<EnemyBehaviorBase>();
    private int scheduled_behavior_index = -1;

    public EnemyBehaviorGroup(Stage stage,  Enemy enemy, MunchMan munch_man)
    {
        addRequirements(stage, enemy, munch_man);
    }

    public <GenericEnemyBehavior extends EnemyBehaviorBase> void addBehaviors(GenericEnemyBehavior... behaviors)
    {
        for(int i = 0; i < behaviors.length; i++)
        {
            this.behaviors.addLast(behaviors[i]);
        }
    }

    @Override
    public void execute()
    {
        for(int i = 0; i < behaviors.size(); i++)
        {
            if(behaviors.get(i).isSelfSchedulingConditionsMet() && i != scheduled_behavior_index)
            {
                behaviors.get(scheduled_behavior_index).setSelfScheduling(false);
                behaviors.get(i).setSelfScheduling(true);
                scheduled_behavior_index = i;
                break;
            }
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        if(scheduled_behavior_index >= 0)
        {
            behaviors.get(scheduled_behavior_index).setSelfScheduling(false);
        }
    }
}
