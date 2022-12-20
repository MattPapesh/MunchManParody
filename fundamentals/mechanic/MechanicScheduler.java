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
    private static int current_instance_index = 0;
    private static int elapsed_millis = 0;

    /**
     * @return The elapsed time in milliseconds since the first time the MechanicScheduler was called. (when the program began running!)
     * 
     * @see
     * Note: Given that the MechanicScheduler immediately begins running once the application begins running, the scheduler's elapsed
     * time can be treated as the amount of time the application as been running. 
     */
    public static int getElapsedMillis()
    {
        return elapsed_millis;
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

    public static void interruptSimultaneousComponentUtilization()
    {
        MechanicBase primary_mechanic = mechanics.get(current_instance_index);

        for(int i = 0; i < mechanics.size(); i++)
        {
            if(i != current_instance_index && primary_mechanic.isScheduled() && mechanics.get(i).isScheduled())
            {
                LinkedList<Double> primary_component_IDs = primary_mechanic.getComponentIDs();
                LinkedList<Double> secondary_component_IDs = mechanics.get(i).getComponentIDs();

                if(isSharedComponentAmongMechanics(primary_component_IDs, secondary_component_IDs))
                {
                    mechanics.get(current_instance_index).cancel();
                    mechanics.get(i).cancel();
                    System.err.println("MechanicScheduler.java: Simultaneous component utilization among mechanics exeception! ");
                }
            }
        }
    }

    private static boolean isSharedComponentAmongMechanics(LinkedList<Double> primary_component_IDs, 
    LinkedList<Double> secondary_component_IDs)
    {
        for(int current_primary_index = 0; current_primary_index < primary_component_IDs.size(); current_primary_index++)
        {
            for(int current_secondary_index = 0; current_secondary_index < secondary_component_IDs.size(); current_secondary_index++)
            {
                if(primary_component_IDs.get(current_primary_index) == secondary_component_IDs.get(current_secondary_index))
                {
                    return true;
                }
            }
        }

        return false;
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

     /**
     * Returns a different registered mechanic instance each time the method is called, and loops through the list of registered
     * mechanic instances. Moreover, when the method is continuously called, and the returned instance's run() method is called,
     * all registered mechnanic instances will function apropriately.
     * 
     * @return
     *  A different registered mechanic instance each time the method is called. 
     * 
     * @see
     * Note: This method is periodically called based on the application's refresh rate! This must happen!
     * The refresh rate is the fixed rate of delay in milliseconds that the entire application is updated at or, in terms of 
     * methods, called at. No part of the program can ever be refreshed, updated, or called faster than this refresh rate. 
     */
    public static MechanicBase getInstance()
    {
        elapsed_millis += Constants.WINDOW_CHARACTERISTICS.REFRESH_RATE_MILLIS;  
        MechanicBase mechanic = null;   

        if(!mechanics.isEmpty() && current_instance_index < mechanics.size())
        {
            mechanic = mechanics.get(current_instance_index); 
        }
        else if(!mechanics.isEmpty())
        {
            mechanic = mechanics.getFirst(); 
            current_instance_index = 0;
        }

        current_instance_index++;
        return mechanic;
    }
}
