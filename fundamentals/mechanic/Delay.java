package fundamentals.mechanic;

/**
 * A mechanic that when scheduled, runs for a period of milliseconds while periodically and consecutively scheduling another mechanic
 * on a loop during the idle period. 
 * 
 * @see Note: This mechanic is most useful when used within a SequentialMechanicGroup, causing delay between other sequentially scheduled
 * mechanics used within the group. 
 */
public class Delay extends MechanicBase
{
    private double millis = 0;
    private double initial_millis = 0;
    private double delta_millis = 0;
    private InstantMechanic idle_mechanic = null;

    /**
     * A mechanic that when scheduled, runs for a period of milliseconds while periodically and consecutively scheduling another mechanic
     * on a loop during the idle period. 
     * 
     * @see Note: This mechanic is most useful when used within a SequentialMechanicGroup, causing delay between other sequentially scheduled
     * mechanics used within the group. 
     * 
     * @param millis
     * - The amount of delay in milliseconds.
     * 
     * @param idle_mechanic
     * - The InstantMechanic to periodically and consecutively shedule on a loop during the idle period that the Delay mechanic is scheduled and running. 
     */
    public Delay(double millis, InstantMechanic idle_mechanic)
    {
        this.millis = millis;
        this.idle_mechanic = idle_mechanic;
        addRequirements();
        setExecutionalPeriodicDelay(1);
    }   

    @Override
    public void initialize() 
    {
        initial_millis = MechanicScheduler.getElapsedMillis();
    }

    @Override
    public void execute() 
    {
        delta_millis = MechanicScheduler.getElapsedMillis() - initial_millis;
        
        if(idle_mechanic != null)
        {
            idle_mechanic.schedule();
        }
    }
    
    @Override
    public void end(boolean interrupted) 
    {
        
    }

    @Override
    public boolean isFinished()
    {   
        return Math.abs(delta_millis) >= millis;
    }
}
