package fundamentals.UI;

import java.util.LinkedList;
import app.input.AppInput;
import app.input.Button;
import fundamentals.mechanic.MechanicBase;

/**
 * Utilizes multiple keyboard keys. These keys are used together to represent a controller on a keyboard. Moreover, 
 * the status of each key in a controller can be determined with this class while also assigning mechanics to keys; either scheduling
 * each keys' respective mechanic once or continuously when finished whenever a key is pressed. 
 */
public class Controller 
{
    private final double CONTROLLER_ID = Math.random();
    private int current_instance_index = 0;
    private boolean active = false;

    private LinkedList<Button> buttons = new LinkedList<Button>();
    private Button left_key = null;
    private Button right_key = null;
    private Button up_key = null;
    private Button down_key = null;

    /**
     * Utilizes multiple keyboard keys. These keys are used together to represent a controller on a keyboard. Moreover, 
     * the status of each key in a controller can be determined with this class while also assigning mechanics to keys; either scheduling
     * each keys' respective mechanic once or continuously when finished whenever a key is pressed. 
     * 
     * @param app_input
     * - An AppInput instance that will be used to constantly read the status of each key. 
     * 
     * @param left_key_id
     * - The key code for the left key in a WASD format
     * 
     * @param right_key_id
     * - The key code for the right key in a WASD format
     * 
     * @param up_key_id
     * - The key code for the up/up key in a WASD format
     * 
     * @param down_key_id
     * - The key code for the down/down key in a WASD format
     */
    
    public Controller(AppInput app_input, int left_key_id, int right_key_id, int up_key_id, int down_key_id)
    {
        left_key = new Button(app_input, left_key_id);
        right_key = new Button(app_input, right_key_id);
        up_key = new Button(app_input, up_key_id);
        down_key = new Button(app_input, down_key_id);

        appendButtons(left_key, right_key, up_key, down_key);
        toggleActivity(true);
    }

    private void appendButtons(Button... buttons)
    {
        for(int i = 0; i < buttons.length; i++)
        {
            this.buttons.addLast(buttons[i]);
        }
    }

    /**
     * Toggles the controller's activity to either be enabled or disabled.
     * 
     * @param active
     * - The new toggled active state for the controller.
     */
    public void toggleActivity(boolean active)
    {
        if(!this.active && active)
        {
            this.active = true;
            ControllerScheduler.registerController(this);
        }
        else if(this.active & !active)
        {
            this.active = false; 
            ControllerScheduler.removeController(this);
        }
    }

    protected double getControllerID()
    {
        return CONTROLLER_ID;
    }

    /**
     * @return The amount of buttons in a controller; there's four keys in a WASD format, and so there's four buttons.
     */
    public int getAmountOfButtons()
    {
        return buttons.size();
    }

    /**
     * @return The status of the left key in a WASD format; whether or not the key is currently being pressed.
     */
    public boolean isLeftPressed()
    {
        return left_key.getIsActive();
    }

    /**
     *  A keyboard key is either pressed or it is not. The "lifetime" of a key is from when an idle key becomes pressed, and then it
     * is released. Moreover, this method is repsonible for determining whether or not the left key lifetime (started by a press) was completed. 
     * (ended with a release)
     * 
     * @return Whether or not the key has completed any new lifetime cycles from being pressed to being released. 
     */
    public boolean completedLeftLifetimeUpdate()
    {
        return left_key.completedLifetimeUpdate();
    }

    /**
     * Used to remove a mechanic that is scheduled while the left keyboard key is continuously pressed. 
     * (Mechanics that were passed into the whileLeftPressedContinuous(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhileLeftPressedContinuousMechanic(GenericMechanic mechanic)
    {
        left_key.removeWhilePressedContinuousMechanic(mechanic);
    }

    /**
     * Used to remove a mechanic that is scheduled when the left keyboard key is pressed. 
     * (Mechanics that were passed into the whenLeftPressed(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhenLeftPressedMechanic(GenericMechanic mechanic)
    {
        left_key.removeWhenPressedMechanic(mechanic);
    }

    /**
     * A mechanic will be scheduled while the left key is pressed. The mechanic will continuously be re-scheduled 
     * upon ending as long as the key is still being pressed; continuously running the mechanic in a looped fashion.
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled while the left key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whileLeftPressedContinuous(GenericMechanic mechanic)
    {
        left_key.whilePressedContinuous(mechanic);
    }

    /**
     * A mechanic will be scheduled when the left key is pressed. The mechanic will only be scheduled
     *  once for everytime the key is pressed. 
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled once whenever the left key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whenLeftPressed(GenericMechanic mechanic)
    {
        left_key.whenPressed(mechanic);
    }

    /**
     * @return The status of the right key in a WASD format; whether or not the key is currently being pressed.
     */
    public boolean isRightPressed()
    {
        return right_key.getIsActive();
    }

    /**
     *  A keyboard key is either pressed or it is not. The "lifetime" of a key is from when an idle key becomes pressed, and then it
     * is released. Moreover, this method is repsonible for determining whether or not the right key lifetime (started by a press) was completed. 
     * (ended with a release)
     * 
     * @return Whether or not the key has completed any new lifetime cycles from being pressed to being released. 
     */
    public boolean completedRightLifetimeUpdate()
    {
        return right_key.completedLifetimeUpdate();
    }

    /**
     * Used to remove a mechanic that is scheduled while the right keyboard key is continuously pressed. 
     * (Mechanics that were passed into the whileRightPressedContinuous(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhileRightPressedContinuousMechanic(GenericMechanic mechanic)
    {
        right_key.removeWhilePressedContinuousMechanic(mechanic);
    }

    /**
     * Used to remove a mechanic that is scheduled when the right keyboard key is pressed. 
     * (Mechanics that were passed into the whenRightPressed(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhenRightPressedMechanic(GenericMechanic mechanic)
    {
        right_key.removeWhenPressedMechanic(mechanic);
    }

    /**
     * A mechanic will be scheduled while the right key is pressed. The mechanic will continuously be re-scheduled 
     * upon ending as long as the key is still being pressed; continuously running the mechanic in a looped fashion.
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled while the right key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whileRightPressedContinuous(GenericMechanic mechanic)
    {
        right_key.whilePressedContinuous(mechanic);
    }

    /**
     * A mechanic will be scheduled when the right key is pressed. The mechanic will only be scheduled
     *  once for everytime the key is pressed. 
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled once whenever the right key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whenRightPressed(GenericMechanic mechanic)
    {
        right_key.whenPressed(mechanic);
    }

    /**
     * Used to remove a mechanic that is scheduled while the up/up keyboard key is continuously pressed. 
     * (Mechanics that were passed into the whileUpPressedContinuous(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhileUpPressedContinuousMechanic(GenericMechanic mechanic)
    {
        up_key.removeWhilePressedContinuousMechanic(mechanic);
    }

    /**
     * Used to remove a mechanic that is scheduled when the up/up keyboard key is pressed. 
     * (Mechanics that were passed into the whenUpPressed(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhenUpPressedMechanic(GenericMechanic mechanic)
    {
        up_key.removeWhenPressedMechanic(mechanic);
    }

    /**
     * @return The status of the up/up key in a WASD format; whether or not the key is currently being pressed.
     */
    public boolean isUpPressed()
    {
        return up_key.getIsActive();
    }

    /**
     *  A keyboard key is either pressed or it is not. The "lifetime" of a key is from when an idle key becomes pressed, and then it
     * is released. Moreover, this method is repsonible for determining whether or not the up/up key lifetime (started by a press) was completed. 
     * (ended with a release)
     * 
     * @return Whether or not the key has completed any new lifetime cycles from being pressed to being released. 
     */
    public boolean completedUpLifetimeUpdate()
    {
        return up_key.completedLifetimeUpdate();
    }

    /**
     * A mechanic will be scheduled while the up/up key is pressed. The mechanic will continuously be re-scheduled 
     * upon ending as long as the key is still being pressed; continuously running the mechanic in a looped fashion.
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled while the up/up key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whileUpPressedContinuous(GenericMechanic mechanic)
    {
        up_key.whilePressedContinuous(mechanic);
    }

    /**
     * A mechanic will be scheduled when the up/up key is pressed. The mechanic will only be scheduled
     *  once for everytime the key is pressed. 
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled once whenever the up/up key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whenUpPressed(GenericMechanic mechanic)
    {
        up_key.whenPressed(mechanic);
    }

    /**
     * Used to remove a mechanic that is scheduled while the down/down keyboard key is continuously pressed. 
     * (Mechanics that were passed into the whileDownPressedContinuous(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhileDownPressedContinuousMechanic(GenericMechanic mechanic)
    {
        down_key.removeWhilePressedContinuousMechanic(mechanic);
    }

    /**
     * Used to remove a mechanic that is scheduled when the down/down keyboard key is pressed. 
     * (Mechanics that were passed into the whenDownPressed(...) method!)
     * 
     * @param <GenericMechanic>
     * - Any mechanic instance that possesses MechanicBase as a superclass.
     * 
     * @param mechanic
     * - The mechanic to remove.
     */
    public <GenericMechanic extends MechanicBase> void removeWhenDownPressedMechanic(GenericMechanic mechanic)
    {
        down_key.removeWhenPressedMechanic(mechanic);
    }

    /**
     * @return The status of the down/down key in a WASD format; whether or not the key is currently being pressed.
     */
    public boolean isDownPressed()
    {
        return down_key.getIsActive();
    }

    /**
     *  A keyboard key is either pressed or it is not. The "lifetime" of a key is from when an idle key becomes pressed, and then it
     * is released. Moreover, this method is repsonible for determining whether or not the down/down key lifetime (started by a press) was completed. 
     * (ended with a release)
     * 
     * @return Whether or not the key has completed any new lifetime cycles from being pressed to being released. 
     */
    public boolean completedDownLifetimeUpdate()
    {
        return down_key.completedLifetimeUpdate();
    }

    /**
     * A mechanic will be scheduled while the down/down key is pressed. The mechanic will continuously be re-scheduled 
     * upon ending as long as the key is still being pressed; continuously running the mechanic in a looped fashion.
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled while the down/down key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whileDownPressedContinuous(GenericMechanic mechanic)
    {
        down_key.whilePressedContinuous(mechanic);
    }

    /**
     * A mechanic will be scheduled when the down/down key is pressed. The mechanic will only be scheduled
     *  once for everytime the key is pressed. 
     * 
     * @param <GenericMechanic>
     *  Any mechanic extending MechanicBase for its superclass (required for every mechanic).
     * 
     * @param mechanic
     * - The mechanic to be scheduled once whenever the down/down key is pressed.
     */
    public <GenericMechanic extends MechanicBase> void whenDownPressed(GenericMechanic mechanic)
    {
        down_key.whenPressed(mechanic);
    }

    /**
     * Returns a different Button instance each time the method is called, and loops through the list of Button 
     * instances. Moreover, when the method is continuously called, and the run() method is called from each instance returned, 
     * then all Button instances will function apropriately.
     * 
     * @return
     *  A different Button instance each time the method is called. 
     */
    public Button getButtonInstance()
    {
        Button button = null;

        if(!buttons.isEmpty() && current_instance_index < buttons.size())
        {
            button = buttons.get(current_instance_index);
        }
        else if(!buttons.isEmpty())
        {
            button = buttons.getFirst();
            current_instance_index = 0;
        }

        current_instance_index++;
        return button;
    }
}