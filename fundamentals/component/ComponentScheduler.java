package fundamentals.component;

import java.util.LinkedList;

/**
 * Manages all Component variables through means of static methods. All instantiated Components that have been 
 * "registered" by calling a component's superclass method, addRequirements(...),
 * will be accessible from the ComponentScheduler for AppGraphics in order to display every component on-screen. 
 * Moreover, AppGraphics will always have access to all "registered" Component instances so that any changes that 
 * occur to any instance, such as updated coordinates or image, will immediately be refelected
 * on-screen when components are toggled to be active. 
 */
public class ComponentScheduler 
{
    private static LinkedList<ComponentBase> components = new LinkedList<ComponentBase>();
   
    /**
    * Allows the Comoponent instance passed in to be accessible by the ComponentScheduler so that AppGraphics can display all
    * registered Components on-screen. 
    *
    * @see
    * Note: This method is called by a Component's superclass method, addRequirements(...). Therefore requiring
    * addRequirements(...) to be called by every Component to function appropriately. 
    */
    protected static void registerComponent(ComponentBase component)
    {
        for(int i = 0; i < components.size(); i++)
        {
            if(component != null && component.getComponentID() == components.get(i).getComponentID())
            {
                return;   
            }
        }

        components.addLast(component);
    }

    /**
     * Removes the Component passed in from the ComponentScheduler's list of registered Components.
     */
    public static void removeComponent(ComponentBase component)
    {
        for(int i = 0; i < components.size(); i++)
        {
            if(components.get(i).getComponentID() == component.getComponentID())
            {
                components.remove(i);
            }
        }
    }

    /**
     * @return A LinkedList of the ComponentScheduler's registered Components. 
     */
    public static LinkedList<ComponentBase> getComponents()
    {
        return components;
    }
}
