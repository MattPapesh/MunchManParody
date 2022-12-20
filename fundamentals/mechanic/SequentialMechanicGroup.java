package fundamentals.mechanic;

import java.util.LinkedList;

/**
 * A variation of MechanicBase, SequentialMechanicGroup mechanics are used to add or append other mechanics to it in a consecutive
 * fashion in order to schedule and run a sequence of mechanics in a orderly fashion. 
 * 
 * @see 
 * Moreover, this mechanic is used to make use of its addMechanics(GenericMechanic... mechanics) method. this mechanic does not offer 
 * initializing, executing, or ending phases, and does not offer the use of an ending conditon! This mechanic is strictly meant for
 * sequentially scheduling and running added mechanics. 
 */
public class SequentialMechanicGroup extends MechanicBase
{
    private LinkedList<MechanicBase> group_mechanics = new LinkedList<MechanicBase>();
    private int current_mechanic_index = 0;
    private boolean current_mechanic_never_scheduled = true;

    /**
     * A variation of MechanicBase, SequentialMechanicGroup mechanics are used to add or append other mechanics to it in a consecutive
     * fashion in order to schedule and run a sequence of mechanics in a orderly fashion. 
     * 
     * @see 
     * Moreover, this mechanic is used to make use of its addMechanics(GenericMechanic... mechanics) method. this mechanic does not offer 
     * initializing, executing, or ending phases, and does not offer the use of an ending conditon! This mechanic is strictly meant for
     * sequentially scheduling and running added mechanics. 
     */
    public SequentialMechanicGroup()
    {
        setExecutionalPeriodicDelay(1);
        MechanicScheduler.registerMechanic(this);
    }

    /**
     * Used to add any type of mechanic instance to the SequentialMechanicGroup, where these mechanics
     * will be consecutively ran upon scheduling the SequentialMechanicGroup itself. Moreover, the order at which mechanics
     * are passed in as parameters determines what order that the mechanics will be consecutively scheduled and ran; 
     * the first argument will be the first mechanic scheduled, and the final argument passed in will be the last to be scheduled.
     * 
     * @see
     * Note: Given that added mechanics are ran one after the other, an added mechanic will only be scheduled and ran once the previous
     * added mechanic has either been interrupted or ended from meeting its ending condition. 
     * 
     * @param <GenericMechanic>
     * @param mechanics
     */
    public <GenericMechanic extends MechanicBase> void addMechanics(GenericMechanic... mechanics)
    {
        for(int i = 0; i < mechanics.length; i++)
        {
            this.group_mechanics.addLast(mechanics[i]);
        }
    }

    @Override
    public void initialize() {}
    
    @Override
    public void execute()
    {
        try
        { 
            MechanicBase current_mechanic = group_mechanics.get(current_mechanic_index);
            
            if(!current_mechanic.isScheduled() && current_mechanic_never_scheduled)
            {
                current_mechanic_never_scheduled = false;
                group_mechanics.get(current_mechanic_index).schedule();
            }
            else if(!current_mechanic.isScheduled() && !current_mechanic_never_scheduled)
            {
                current_mechanic_never_scheduled = true;
                current_mechanic_index++;
            }
        }
        catch(IndexOutOfBoundsException e) {}
    }

    @Override
    public void end(boolean interrupted) 
    {

    }

    @Override
    public boolean isFinished()
    {
        return current_mechanic_index >= group_mechanics.size();
    }
}
