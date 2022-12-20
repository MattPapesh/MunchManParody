package fundamentals.UI;

import java.util.LinkedList;

/**
 * Manages all Controller and Button variables through means of static methods. All instantiated Controllers that have been 
 * "registered" may have Button instances. Schedulers are used to periodically return different Controller instances to continuously 
 * call their Buttons' run() methods, allowing Controllers to operate apropriately. Moreover, allowing these run() methods 
 * to be accessed in AppBase.java to be looped for Controller functionality. 
 */
public class ControllerScheduler 
{
    private static LinkedList<Controller> controllers = new LinkedList<Controller>();
    private static int current_instance_index = 0;

    /**
     * Given that a Controller's Buttons' run() method must be continuously called in order to function, the GUIScheduler needs to be able to
     * have access to every Controller instance. All instances must be passed in for them to function apropriately. 
     * 
     * @param controller
     * - The instance to register. 
     */
    public static void registerController(Controller controller)
    {
        if(controller != null)
        {
            controllers.addLast(controller);
        }
    }

    /**
     * Removes all instances of the Controller instance passed in from the ControllerScheduler's registered list 
     * of Controller instances. This may want to be done if a Controller instance will no longer be used.
     * 
     * @param controller
     * - The registered instance to remove.
     */
    public static void removeController(Controller controller)
    {
        for(int i = 0; i < controllers.size(); i++)
        {
            if(controllers.get(i).getControllerID() == controller.getControllerID())
            {
                controllers.remove(i);
                i--;
            }
        }
    }

    /**
     * Returns a different registered Controller instance each time the method is called, and loops through the list of registered
     * Controller instances. Moreover, when the method is continuously called, and the run() method is ran from each returned instance's 
     * Buttons, then all registered GUI instances will function apropriately.
     * 
     * @return
     *  A different registered Controller instance each time the method is called. 
     */
    public static Controller getControllerInstance()
    {
        Controller controller = null;

        if(!controllers.isEmpty() && current_instance_index < controllers.size())
        {
            controller = controllers.get(current_instance_index);
        }
        else if(!controllers.isEmpty())
        {
            controller = controllers.getFirst();
            current_instance_index = 0;
        }

        current_instance_index++;
        return controller;
    }
}
