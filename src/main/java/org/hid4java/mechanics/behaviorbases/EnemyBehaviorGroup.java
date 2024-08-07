package org.hid4java.mechanics.behaviorbases;

import java.util.LinkedList;

import org.hid4java.components.Enemy;
import org.hid4java.components.MunchMan;
import org.hid4java.components.Stage;
import org.hid4java.mechanics.movement.EntityMovement;

public class EnemyBehaviorGroup extends EnemyBehaviorBase
{
    protected behavior overrider = null;
    protected LinkedList<behavior> behaviors = new LinkedList<behavior>();
    protected int scheduled_behavior_index = -1;

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
    public void executeBehavior()
    {
        if(overrider != null && !overrider.get().isSelfScheduling() 
        && overrider.get().isSelfSchedulingConditionsMet())
        {
            overrider.get().setSelfScheduling(true);
            if(scheduled_behavior_index != -1) 
            {
                behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
                scheduled_behavior_index = -1;
            }
        }
        else if(overrider != null && overrider.get().isSelfScheduling() 
        && !overrider.get().isSelfSchedulingConditionsMet())
        {
            overrider.get().setSelfScheduling(false);
        }

        for(int i = 0; i < behaviors.size() && (overrider == null || !overrider.get().isSelfScheduling()); i++)
        {
            if(behaviors.get(i).get().isSelfSchedulingConditionsMet() 
            && !behaviors.get(i).get().isSelfScheduling() 
            && scheduled_behavior_index != -1 && scheduled_behavior_index != i
            && !behaviors.get(scheduled_behavior_index).get().isScheduled())
            {
                behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
                behaviors.get(i).get().setSelfScheduling(true);
                scheduled_behavior_index = i; 
                break;
            }
            else if(behaviors.get(i).get().isSelfSchedulingConditionsMet() 
            && !behaviors.get(i).get().isSelfScheduling() 
            && scheduled_behavior_index == -1)
            {
                behaviors.get(i).get().setSelfScheduling(true);
                scheduled_behavior_index = i;
                break;
            }
        }
    }

    @Override
    public void endBehavior(boolean interrupted)
    {
        if(scheduled_behavior_index >= 0)
        {
            behaviors.get(scheduled_behavior_index).get().setSelfScheduling(false);
            scheduled_behavior_index = -1;
        }
    }
}
