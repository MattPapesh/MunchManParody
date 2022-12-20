package app.input;

import java.util.LinkedList;

import fundamentals.mechanic.MechanicBase;

/**
 * Used to determine the state of a button on a Controller instance, Button makes use of AppInput to read a specific keyboard key
 * to determine when it's idle, pressed, and released. Moreover, this is especially useful because AppInput can read many keys 
 * consecutively, but not simultaneously. So, Button instances can still change states in what appears to be in a simultaneous fashion
 * even though AppInput can only read a single key at time. Furthermore, allowing a Button to read the state of only its key from AppInput to interpret what the
 * Button's current state should be.
 */
public class Button
{
    private AppInput app_input = null;
    private int key_id = 0;
    
    // determines if the button is currently being held down and is active
    private boolean completed_lifetime_update = false; 
    private boolean current_is_active = false;
    private boolean prev_is_active = false;
    
    private LinkedList<MechanicBase> when_pressed_mechanics = new LinkedList<MechanicBase>();
    private LinkedList<MechanicBase> while_pressed_cont_mechanics = new LinkedList<MechanicBase>(); 

    /**
     * Used to determine the state of a button on a Controller instance, Button makes use of AppInput to read a specific keyboard key
     * to determine when it's idle, pressed, and released. Moreover, this is especially useful because AppInput can read many keys 
     * consecutively, but not simultaneously. So, Button instances can still change states in what appears to be in a simultaneous fashion
     * even though AppInput can only read a single key at time. Furthermore, allowing a Button to read the state of only its key from AppInput to interpret what the
     * Button's current state should be.
     * 
     * @param app_input
     * - The AppInput instance; the instance passed in should be instantiated in GameBase. 
     * 
     * @param key_id
     * - The key code of the keyboard key to use for the Button. 
     */
    public Button(AppInput app_input, int key_id)
    {
        this.app_input = app_input;
        this.key_id = key_id;
    }

    /**
     * @return the key code of the Button's keyboard key. 
     */
    public int getKey()
    {
        return key_id;
    }

    /**
     * @return Whether or not the Button is currently being is active. 
     * 
     * @see
     * Note: A Button is considered to be active when it's detected that it's keyboard key is currently being pressed; 
     * the Button will remain active until it's been detected that its key had been released. 
     */
    public boolean getIsActive()
    {
        return current_is_active;
    }

    private boolean isActive()
    {
        if(app_input.isKeyPressed(key_id))
        {
            current_is_active = true;
        }
        else if(app_input.isKeyReleased(key_id))
        {
            current_is_active = false;
        }
        
        return current_is_active;
    }

    /**
     *  A button is either pressed or it is not. The "lifetime" of a button is from when an idle button becomes pressed, and then it
     * is released. Moreover, this method is repsonible for determining whether or not a new button lifetime (started by a press) was completed. 
     * (ended with a release).
     * 
     * @return Whether or not the button has completed any new lifetime cycles from being pressed to being released. 
     */
    public boolean completedLifetimeUpdate()
    {
        if(completed_lifetime_update)
        {
            completed_lifetime_update = false;
            return true;
        }

        return false;
    }

    /**
     * Used to remove a mechanic that is scheduled when the button is pressed. 
     * (Mechanics passed into the whenPressed(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass. 
     * 
     * @param mechanic
     * - The mechanic to remove. 
     */
    public <GenericMechanic extends MechanicBase> void  removeWhenPressedMechanic(GenericMechanic mechanic)
    {
        for(int i = 0; i < when_pressed_mechanics.size(); i++)
        {
            if(when_pressed_mechanics.get(i).getMechanicID() == mechanic.getMechanicID())
            {
                when_pressed_mechanics.remove(i);
                i--;
            }
        }
    }

    /**
     * Used to remove a mechanic that is scheduled while the button is continuously pressed. 
     * (Mechanics passed into the whenPressed(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass. 
     * 
     * @param mechanic
     * - The mechanic to remove. 
     */
    public <GenericMechanic extends MechanicBase> void  removeWhilePressedContinuousMechanic(GenericMechanic mechanic)
    {
        for(int i = 0; i < when_pressed_mechanics.size(); i++)
        {
            if(while_pressed_cont_mechanics.get(i).getMechanicID() == mechanic.getMechanicID())
            {
                while_pressed_cont_mechanics.remove(i);
                i--;
            }
        }
    }

    /**
     * Used to schedule and run a mechanic once for every time the Button has been pressed. 
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass. 
     * 
     * @param mechanic
     * - The mechanic to schedule and run when the button is pressed. 
     */
    public <GenericMechanic extends MechanicBase> void whenPressed(GenericMechanic mechanic)
    {
        for(int i = 0; i < when_pressed_mechanics.size(); i++)
        {
            if(when_pressed_mechanics.get(i).getMechanicID() == mechanic.getMechanicID())
            {
                return;
            }
        }

        when_pressed_mechanics.addLast(mechanic);
    }

    /**
     * Used to constinuously schedule and run a mechanic as long as the button remains pressed. Moreover, this method
     * continues to re-schedule the mechanic once it has either been interrupted, or has naturally ended by seeing its ending condition
     * being met.  
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass. 
     * 
     * @param mechanic
     * - The mechanic to constinuously schedule and run while the button is being pressed. 
     */
    public <GenericMechanic extends MechanicBase> void whilePressedContinuous(GenericMechanic mechanic)
    {
        while_pressed_cont_mechanics.addLast(mechanic);
    }

    private void runButtonMechanics(LinkedList<MechanicBase> mechanics)
    {
        for(int i = 0; i < mechanics.size(); i++)
        {
            if(!mechanics.get(i).isScheduled())
            {
                mechanics.get(i).schedule();
            }
        }
    }

    /**
     *  Used to evaluate the button's current state and activity to determine if and when mechanics binded to the button
     * should be scheduled and ran. Moreover, if this method is continuously called,it will continuously check to see if binded mechanics
     * can be scheduled based on the button's state. So, given that scheduling and running the binded mechanics serve as a response to 
     * pressing the button, continuously calling this method will ensure buttons immediately respond when pressed.  
     */
    public void run()
    {
        prev_is_active = current_is_active;

        if(isActive() && ! prev_is_active)
        {
            completed_lifetime_update = true;
            runButtonMechanics(when_pressed_mechanics);
        }

        if(isActive())
        {
            runButtonMechanics(while_pressed_cont_mechanics);
        }
    }
}