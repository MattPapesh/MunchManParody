package fundamentals.UI.GUI;

import java.util.LinkedList;

/**
 * Manages all GUI and GUIOption variables through means of static methods. All instantiated GUIs that have been 
 * "registered" may have GUIOption instances. Schedulers are used to periodically return different GUI instances to continuously 
 * call their GUIOptions' run() methods, allowing GUIs to operate apropriately. Moreover, allowing these run() methods 
 * to be accessed in AppBase.java to be looped for GUI functionality. 
 */

public class GUIScheduler 
{
    private static LinkedList<GUI> GUIs = new LinkedList<GUI>();
    private static int current_instance_index = 0;

    /**
     * Given that a GUI's Buttons' run() method must be continuously called in order to function, the GUIScheduler needs to be able to
     * have access to every GUI instance. All instances must be passed in for them to function apropriately. 
     * 
     * @param GUI
     * - The instance to register. 
     */
    protected static void registerGUI(GUI GUI)
    {
        if(GUI != null)
        {
            GUIs.addLast(GUI);
        }
    }

    /**
     * Removes all instances of the GUI instance passed in from the GUIScheduler's registered list of GUI instances. This may want to be
     * done if a GUI instance will no longer be used.
     * 
     * @param GUI
     * - The registered instance to remove.
     */
    protected static void removeGUI(GUI GUI)
    {
        for(int i = 0; i < GUIs.size(); i++)
        {
            if(GUIs.get(i).getID() == GUI.getID())
            {
                GUIs.remove(i);
            }
        }
    }

    /**
     * Returns a different registered GUI instance each time the method is called, and loops through the list of registered
     * GUI instances. Moreover, when the method is continuously called, and the run() method is ran from each returned instance's 
     * GUIOptions, then all registered GUI instances will function apropriately.
     * 
     * @return
     *  A different registered GUI instance each time the method is called. 
     */
    public static GUI getGUIInstance()
    {
        GUI GUI = null;

        if(!GUIs.isEmpty() && current_instance_index < GUIs.size())
        {
            GUI = GUIs.get(current_instance_index);
        }
        else if(!GUIs.isEmpty())
        {
            GUI = GUIs.getFirst();
            current_instance_index = 0;
        }

        current_instance_index++;
        return GUI;
    }
}
