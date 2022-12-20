package fundamentals.mechanic;

import java.util.LinkedList;
import fundamentals.component.ComponentBase;

/**
 * The superclass to every app mechanic. Fundamentally, MechanicBase is broken up into four crucial phases: initilization before
 * before running a mechanic after being scheduled, execution of the mechanic itself, the condition that needs to be met to ensure that
 * the mechanic is finished running, and the ending. Moreover, MechanicBase also allows mechanic classes to inherit and override methods for 
 * controlling a mechanic's behavior. Finally, it is required of every mechanic to extend MechanicBase as a superclass while 
 * calling the addRequirements(GenericComponent... components) method as long as if that mechanic isn't already 
 * extending another Mechanic superclass for apropriate functionality.
 * 
 * @see
 * Note: Mechanic superclasses: MechanicBase, InstantMechanic, and SequentialMechanicGroup.
 */
public class MechanicBase implements MechanicInterface
{
    private final double MECHANIC_ID = Math.random();
    private LinkedList<Double> component_IDs = new LinkedList<Double>();
    private boolean scheduled = false; 
    private boolean initialized = false;
    private boolean interrupted = false;
    private int initial_millis = 0;
    private int executional_periodic_delay_millis = 0; 

    @Override public void initialize() {}
    @Override public void execute() {}
    @Override public boolean isFinished() {return false;}
    @Override public void end(boolean interrupted) {}

    /**
     * Once MechanicBase has been extended and become a superclass to a sublcass, the subclass must call 
     * this method in order for the subclass to apropriately function as a app mechanic. Moreover, any components 
     * that will be used by the mechanic must be passed in.
     */
    public <GenericComponent extends ComponentBase> void addRequirements(GenericComponent... components)
    {
        if(components.length != 0 && components[0].getClass().getSuperclass().getName() == ComponentBase.class.getName())
        {
            for(int i = 0; i < components.length; i++)
            {
                this.component_IDs.addLast(components[i].getComponentID());
            }
        }
    }

    /**
     * @return The mechanic's personal ID; a specific and unique value that is assigned upon instantiation.
     */
    public double getMechanicID()
    {
        return MECHANIC_ID;
    }

    /**
     * The execute() method is continuously called once a mechanic is scheduled, and so this method 
     * is used to determine how often the execute() method should be called. 
     * 
     * @param millis 
     *  - The amount of periodic delay in milliseconds.
     */
    public void setExecutionalPeriodicDelay(int millis)
    {
        executional_periodic_delay_millis = millis;
    }

    /**
     * @return How often the execute() method is called  in millisseconds when it's 
     * continuously being called once a mechanic is scheduled. 
     */
    public int getExecutionalPeriodicDelay()
    {
        return executional_periodic_delay_millis;
    }

    /**
     * "Schedules" a mechanic to run. Once scheduled by calling this method, a mechanic will call initialize() once,
     * and then continuously call execute() until isFinished()'s ending condition returns true, and finally end() is called once. 
     */
    public void schedule()
    {
        scheduled = true; 
        MechanicScheduler.registerMechanic(this);
    }

    /**
     * Used to immediately hault a scheduled and running mechanic. 
     */
    public void cancel()
    {
        interrupted = true;
    }

    /**
     * This method is continuously called by the MechanicScheduler for any "registered" mechanics, where a mechanic is registered
     * with the MechanicScheduler once a mechanic is scheduled; the mechanic remains scheduled, and this method is continuously called for 
     * the mechanic's functionality until either the mechanic meets its finishing condition as isFinished() returns true, or if the 
     * mechanic is ever canceled while running. 
     */
    public void run()
    {
        if(scheduled && !initialized && !isFinished())
        {
            initial_millis = MechanicScheduler.getElapsedMillis();
            this.initialize();
            initialized = true;
        }
        else if(scheduled && initialized && !isFinished() && 
        Math.abs(MechanicScheduler.getElapsedMillis() - initial_millis) >= executional_periodic_delay_millis)
        {
            this.execute();
            initial_millis = MechanicScheduler.getElapsedMillis();
        }
        
        if(scheduled && (this.isFinished() || interrupted))
        {
            this.end(interrupted);
            scheduled = false; 
            initialized = false;
            interrupted = false;
            MechanicScheduler.removeMechanic(this);
        }
    }

    /**
     * @return Whether or not the mechanic is scheduled and is running. 
     */
    public boolean isScheduled()
    {
        return scheduled;
    }

    /**
     * @return Whether or not the mechanic has been initialized and the initialize() method has been called. 
     */
    public boolean isInitialized()
    {
        return initialized;
    }

    /**
     * @return Whether or not the mechanic has been interrupted by a cancelation. 
     */
    public boolean isInterrupted()
    {
        return interrupted;
    }

    /**
     * @return The mechanic's personal ID; a specific and unique value that is assigned upon instantiation.
     */
    public LinkedList<Double> getComponentIDs()
    {
        return component_IDs;
    }
}