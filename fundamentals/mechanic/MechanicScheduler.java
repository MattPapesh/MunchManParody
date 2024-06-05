package fundamentals.mechanic;

import java.util.LinkedList;
import fundamentals.Constants;

/**
 * Manages all mechanic variables through means of static methods. All types of mechanics inherit MechanicBase methods, regardless 
 * of the type of mechanic. Moreover, each mechanic scheduled when calling a mechanic's superclass method, schedule(), will 
 * "register" the mechanic with the MechanicScheduler either until the mechanic is interrupted, or has its ending condition met 
 * and the mechanic naturally ends. Lastly, all registered mechanics will be ran by the MechanicScheduler until the mechanic somehow ends,
 * allowing it to be de-registered. 
 */
public class MechanicScheduler 
{
    private static LinkedList<MechanicBase> mechanics = new LinkedList<MechanicBase>();
    private static double elapsed_millis = 0;

    /**
     * @return The elapsed time in milliseconds since the first time the MechanicScheduler was called. (when the program began running!)
     * 
     * @see
     * Note: Given that the MechanicScheduler immediately begins running once the application begins running, the scheduler's elapsed
     * time can be treated as the amount of time the application as been running. 
     */
    public static int getElapsedMillis()
    {
        return (int)elapsed_millis;
    }

    public static int getNumOfMechanics() 
    {
        return mechanics.size();
    }

    /**
    * Allows the mechanic instance passed in to be accessible by the MechanicScheduler so that the mechanic can 
    * live out its four-phase life time and begin running once it has been scheduled. 
    *
    * @see
    * Note: This method is called by a mechanic's superclass method, schedule(). Therefore mechanics need to be scheduled
    * with the schedule() method in order to run and function appropriately. 
    */
    protected static void registerMechanic(MechanicBase mechanic)
    {
        if(mechanic !=  null)
        {
            mechanics.addLast(mechanic);
        }
    }

    /**
     * Removes the mechanic passed in from the MechanicScheduler's list of registered mechanics.
     */
    public static void removeMechanic(MechanicBase mechanic)
    {
        for(int i = 0; i < mechanics.size(); i++)
        {
            if(mechanics.get(i).getMechanicID() == mechanic.getMechanicID())
            {
                mechanics.remove(i);
            }  
        }
    }

    /**
     * This method is periodically called based on the application's refresh rate! This must happen!
     * 
     * @return The next registered mechanic instance from the scheduler's list of registered mechanics. 
     * 
     * @see
     * Note: The refresh rate is the fixed rate of delay in milliseconds that the entire application is updated at or, in terms of 
     * methods, called at; not part of the program can ever be refreshed, updated, or called faster that this refresh rate. 
     */

    public static void run()
    {
        elapsed_millis += (Constants.WINDOW_CHARACTERISTICS.REFRESH_RATE_NANOS / 100000.0);   
        for(int i = 0; i < mechanics.size(); i++) 
        {
            mechanics.get(i).run();
        }
    }
}
