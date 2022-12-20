package fundamentals.mechanic;

/**
 * MechanicInterface is the interface for MechanicBase. MechanicInterface houses declorations for the four methods that make up
 * the basis of every mechanic. Moreover, every mechanic has the following phases: initialization, execution, the ending condition, 
 * and the ending once the ending condition is met. 
 */
public interface MechanicInterface 
{
    /**
     * This method is immediately called, and only once, once a mechanic has been scheduled. Moreover,
     * this method is the first phase of every mechanic; anything that needs to occur before the mechanic can run once it has been
     * scheduled should be written in this method's definition. 
     */
    public void initialize();

    /**
     * This method is immediately called after the initialize() method has been called when a mechanic has been scheduled. Moreover,
     * unlike initialize(), this method will be continuously called as long as a mechanic is scheduled and running. Lastly, this method
     * will stop being called once either the mechanic's ending condition is met, or if the mechanic is canceled.
     * 
     * @see
     * Note: This method will be called at a variable periodic rate that can be set be set with MechanicBase's  
     * setExecutionalPeriodicDelay(int millis) method. 
     */
    public void execute();

    /**
     * This method is immediately called only once, and it is called either after the mechanic is canceled and is interrupted, or
     * after isFinished() returns true; its ending condition has been met. 
     * Lastly, the parameter allows for the method to know why the mechanic
     * is finished running. The parameter can be used to write different algorithms in the method's definition to 
     * allow the method to behave differently based on if the method ended naturally, or if it was suddenly interrupted and canceled. 
     * 
     * @param interrupted
     * - Whether or not if the mechanic has been interrupted by a cancelation when finishing, or if the mechanic's
     * ending condition has been met, allowing it to end naturally. 
     * 
     * @see
     * Note: Calling MechanicBase's cancel() method will immediately put a scheduled and running mechanic to a haulting stop.
     * This will be considered in interruption and cause the parameter to pass in as true, where a ending where isFinished() returns true
     * will result in the parameter to pass in as false. 
     */
    public void end(boolean interrupted);

    /**
     * Used to determine when a scheduled mechanic is ready to conclude running and naturally end based on a returned ending condition.  
     * 
     * @return Whether or not the mechanic's ending condition has been met, declaring whether or not the mechanic is finished running. 
     */
    public boolean isFinished();
}