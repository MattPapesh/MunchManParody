package mechanics.behaviorbases;

import java.util.LinkedList;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import fundamentals.mechanic.MechanicBase;
import mechanics.movement.EntityMovement;

public class EnemyBehaviorGroup extends EnemyBehaviorBase
{
    private LinkedList<behavior> behaviors = new LinkedList<behavior>();
    private int scheduled_behavior_index = -1;

    public class behavior
    {
        private EnemyBehaviorBase behavior = null; 
        
        public <GenericEnemyBehavior extends EnemyBehaviorBase> behavior(double scheduling_probability_pct, 
        int max_scheduled_millis, int cooldown_millis, GenericEnemyBehavior behavior)
        {
            this.behavior = behavior;
            this.behavior.setSelfSchedulingProbabilisticCondition(scheduling_probability_pct);
            this.behavior.setMaxScheduledMillis(max_scheduled_millis);
            this.behavior.setSchedulingCooldownMillis(cooldown_millis);
        }

        public <GenericEnemyBehavior extends EnemyBehaviorBase> behavior(double scheduling_probability_pct, 
        GenericEnemyBehavior behavior)
        {
            this.behavior = behavior;
            this.behavior.setSelfSchedulingProbabilisticCondition(scheduling_probability_pct);
        }

        public EnemyBehaviorBase get()
        {
            return behavior;
        }
    }

    public EnemyBehaviorGroup(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addRequirements(stage, enemy, munch_man);
    }

    public <GenericEnemyBehavior extends EnemyBehaviorBase> void addSelfSchedulingBehaviors(GenericEnemyBehavior... behaviors)
    {
        for(int i = 0; i < behaviors.length; i++)
        {
            this.behaviors.addLast(new behavior(-1.0, behaviors[i]));
        }
    }

    public void addProbabilisticBehaviors(behavior... behaviors)
    {
        for(int i = 0; i < behaviors.length; i++)
        {
            this.behaviors.addLast(behaviors[i]);
        }
    }

    @Override
    public void execute()
    {
        if(scheduled_behavior_index != -1 && behaviors.get(scheduled_behavior_index).get().isScheduled())
        {
            return;
        }

        for(int i = 0; i < behaviors.size(); i++)
        {
            if(behaviors.get(i).get().isSelfSchedulingConditionsMet() 
            && !behaviors.get(i).get().isSelfScheduling() && !behaviors.get(i).get().isScheduled()
            && scheduled_behavior_index != -1 && i != scheduled_behavior_index)
            {
                behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
                behaviors.get(i).get().setSelfScheduling(true);
                scheduled_behavior_index = i; System.out.println("D");
                
                break;
            }
            else if(behaviors.get(i).get().isSelfSchedulingConditionsMet() 
            && !behaviors.get(i).get().isSelfScheduling() && !behaviors.get(i).get().isScheduled()
            && scheduled_behavior_index == -1)
            {
                behaviors.get(i).get().setSelfScheduling(true);
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
            behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
            scheduled_behavior_index = -1;
        }
    }
}
