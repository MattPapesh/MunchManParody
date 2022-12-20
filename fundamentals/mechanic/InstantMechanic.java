package fundamentals.mechanic;

/**
 * A variation of MechanicBase, InstantMechanics are mechanics that when instantiated, require a lambda function
 * that will describe the mechanic's behvior. 
 * 
 * @see 
 * Moreover, the lambda function will be called once at the event that the InstantMechanic
 * is scheduled; this mechanic does not offer initializing, executing, or ending phases, and does not offer the use of an ending conditon!
 * The lambda function is the entire mechanic. 
 */
public class InstantMechanic extends MechanicBase 
{
    private mechanic_behavior behavior = null;

    @FunctionalInterface
    public interface mechanic_behavior 
    {
        public void behavior();  
    }

    /**
     * A variation of MechanicBase, InstantMechanics are mechanics that when instantiated, require a lambda function
     * that will describe the mechanic's behvior. 
     * 
     * @see 
     * Moreover, the lambda function will be called once at the event that the InstantMechanic
     * is scheduled; this mechanic does not offer initializing, executing, or ending phases, and does not offer the use of an ending conditon!
     * The lambda function is the entire mechanic. 
     */
    public InstantMechanic(mechanic_behavior behavior)
    {
        this.behavior = behavior;
    }

    @Override
    public void initialize()
    {
        behavior.behavior();
        this.cancel();
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
