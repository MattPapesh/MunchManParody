package mechanics.behaviorbases;

import java.util.LinkedList;

import components.Enemy;
import components.MunchMan;
import components.Stage;
import mechanics.movement.EntityMovement;

public class EnemyBehaviorGroup extends EnemyBehaviorBase
{
    private behavior overrider = null;
    private LinkedList<behavior> behaviors = new LinkedList<behavior>();
    private int scheduled_behavior_index = -1;

    public class behavior
    {
        private EnemyBehaviorBase behavior = null; 
        private boolean is_override_behavior = false;

        public <GenericEnemyBehavior extends EnemyBehaviorBase> behavior(double scheduling_probability_pct, 
        int max_scheduled_millis, int cooldown_millis, boolean is_override_behavior, GenericEnemyBehavior behavior)
        {
            this.is_override_behavior = is_override_behavior;
            this.behavior = behavior;
            this.behavior.setSelfSchedulingProbabilisticCondition(scheduling_probability_pct);
            this.behavior.setMaxScheduledMillis(max_scheduled_millis);
            this.behavior.setSchedulingCooldownMillis(cooldown_millis);
        }

        public <GenericEnemyBehavior extends EnemyBehaviorBase> behavior(double scheduling_probability_pct, GenericEnemyBehavior behavior)
        {
            this.behavior = behavior;
            this.behavior.setSelfSchedulingProbabilisticCondition(scheduling_probability_pct);
        }

        public <GenericEnemyBehavior extends EnemyBehaviorBase> behavior(GenericEnemyBehavior behavior)
        {
            this.behavior = behavior;
            this.behavior.setSelfSchedulingProbabilisticCondition(0.0);
        }

        public EnemyBehaviorBase get()
        {
            return behavior;
        }

        public boolean isOverrideBehavior() 
        {
            return is_override_behavior;
        }
    }

    public EnemyBehaviorGroup(EntityMovement enemy_movement, Stage stage, Enemy enemy, MunchMan munch_man)
    {
        super(enemy_movement, stage, enemy, munch_man);
        addRequirements(stage, enemy, munch_man);
    }

    public void addBehaviors(behavior... behaviors)
    {
        for(int i = 0; i < behaviors.length; i++)
        {
            if(behaviors[i].isOverrideBehavior()) 
            {
                overrider = behaviors[i];
            }
            else 
            {
                this.behaviors.addLast(behaviors[i]);
            }
        }
    }

    @Override
    public void execute()
    {
        if(overrider != null && !overrider.get().isSelfScheduling() && overrider.get().isSelfSchedulingConditionsMet() && !overrider.get().isScheduled())
        {
            overrider.get().setSelfScheduling(true);
            if(scheduled_behavior_index != -1) 
            {
                behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
                scheduled_behavior_index = -1;
            }

            return;
        }
        else if(overrider != null && overrider.get().isSelfScheduling() && overrider.get().isSelfSchedulingConditionsMet() && overrider.get().isScheduled()) 
        {
            return;
        }
        else if(overrider != null && overrider.get().isSelfScheduling() && !overrider.get().isSelfSchedulingConditionsMet() && overrider.get().isScheduled())
        {
            overrider.get().setSelfScheduling(false);
        }

        for(int i = 0; i < behaviors.size(); i++)
        {
            if(behaviors.get(i).get().isSelfSchedulingConditionsMet() 
            && !behaviors.get(i).get().isSelfScheduling() && !behaviors.get(i).get().isScheduled()
            && scheduled_behavior_index != -1 && i != scheduled_behavior_index
            && !behaviors.get(scheduled_behavior_index).get().isScheduled())
            {
                behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
                behaviors.get(i).get().setSelfScheduling(true);
                scheduled_behavior_index = i; 
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
