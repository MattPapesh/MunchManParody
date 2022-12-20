package fundamentals.UI.GUI;

import java.util.LinkedList;

import fundamentals.UI.Controller;
import fundamentals.appbase.AppBase;
import fundamentals.mechanic.InstantMechanic;

/**
     *  The GUI, or Graphical User Interface, is a collection of GUIOption instances. Each GUI option is passed into the constructor 
     *  in order to run the mechanic associated with the "current" GUI option that is selected. Moreover, the GUIOption instances are listed together
     *  and are all associated by index based on how they are passed into the contructor; the first instance passed in is index zero. Lastly,
     *  given that a GUIOption's superclass is the ComponentBase, each option is a component seen on-screen, where each option's behavior and their respective
     *  mechanics' behavior are controlled by the GUI
     * 
     *  @param UI_options
     *  - All of the options that will be included and controlled by the GUI. 
     */
public class GUI extends AppBase
{
    private final double UI_ID = Math.random();
    private LinkedList<GUIOption> UI_options = new LinkedList<GUIOption>();
    private int current_instance_index = 0;
    private int current_selected_index = 0;
    private boolean active = false;    
    private String next_option_file_name = "";
    private String activate_option_file_name = "";
    private GUIControllerConditions button_conditions = new GUIControllerConditions(true, true, true);
    private Controller GUI_controller = null;

    private InstantMechanic down_key_mechanic = new InstantMechanic(() ->
    {
        if(getActivity() && button_conditions != null && button_conditions.getDownCondition())
        {
            super.playAudioFile(activate_option_file_name); 
            getOptionSelected().getAssociatedMechanic().schedule();
        }
    });

    private InstantMechanic left_key_mechanic = new InstantMechanic(() ->
    {
        if(getActivity() && button_conditions != null && button_conditions.getLeftCondition())
        {
            int new_selected_index = getSelectedOptionIndex(); 
            if(getSelectedOptionIndex() - 1 >= 0)
            {
                new_selected_index--;
            }

            super.playAudioFile(next_option_file_name); 
            setSelectedOption(new_selected_index);
        }
    });

    private InstantMechanic right_key_mechanic = new InstantMechanic(() ->
    {
        if(getActivity() && button_conditions != null && button_conditions.getRightCondition())
        {
            int new_selected_index = getSelectedOptionIndex(); 
            if(getSelectedOptionIndex() + 1 < getAmountOfOptions())
            {
                new_selected_index++;
            }

            super.playAudioFile(next_option_file_name);
            setSelectedOption(new_selected_index);
        }
    });

    private class GUIControllerConditions
    {
        private boolean left_condition = true;
        private boolean right_condition = true;
        private boolean down_condition = true;

        public GUIControllerConditions(boolean left_condition, boolean right_condition, boolean down_condition)
        {
            this.left_condition = left_condition;
            this.right_condition = right_condition;
            this.down_condition = down_condition;
        }

        public boolean getLeftCondition()
        {
            return left_condition;
        }

        public boolean getRightCondition()
        {
            return right_condition;
        }

        public boolean getDownCondition()
        {
            return down_condition;
        }
    };

    /**
     *  The GUI, or Graphical User Interface, is a collection of GUIOption instances. Each GUI option is passed into the constructor 
     *  in order to run the mechanic associated with the "current" GUI option that is selected. Moreover, the GUIOption instances are listed together
     *  and are all associated by index based on how they are passed into the contructor; the first instance passed in is index zero. Lastly,
     *  given that a GUIOption's superclass is the ComponentBase, each option is a component seen on-screen, where each option's behavior and their respective
     *  mechanics' behavior are controlled by the GUI
     * 
     *  @param UI_options
     *  - All of the options that will be included and controlled by the GUI. 
     */
    public GUI(GUIOption... UI_options)
    {
        for(int i = 0; i < UI_options.length; i++)
        {
            UI_options[i].setParentGUI(this);
            this.UI_options.addLast(UI_options[i]);
        }
 
        setSelectedOption(0);
        toggleActivity(true);
    }

    private void setComponentActivity(boolean active)
    {
        for(int i = 0; i < UI_options.size(); i++)
        {
            UI_options.get(i).toggleActivity(active);
        }
    }

    /**
     * @return The current GUIOption selected in the GUI.
     */
    public GUIOption getOptionSelected()
    {
        return UI_options.get(current_selected_index);
    }

    /**
     * Used to toggle the activity of the Component instances that make up the GUIOptions and GUI.
     * 
     * @param active
     * - The active status to set. 
     */
    public void toggleActivity(boolean active)
    {
        if(!this.active && active)
        {
            this.active = true;
            setComponentActivity(true);
            GUIScheduler.registerGUI(this);
        }
        else if(this.active && !active)
        {
            this.active = false; 
            setComponentActivity(false);
            GUIScheduler.removeGUI(this);
        }
    }

    /**
     * @return Whether or not the GUI is currently active.
     */
    public boolean getActivity()
    {
        return active;
    }

    /**
     * Used to return a different GUIOption instance each time this method is called. Moreover, this method 
     * will run all GUIOption instances as it cycles through all of them when returning an instance each time this 
     * method is called. Finally, Contiuously calling this method will return GUIOptions, where the run() method can be continuously
     * called from each instance returned; this will run the GUIOption, allowing the GUI, and its options, to function appropriately.
     * 
     * @return The GUIOption instance returned. 
     */
    public GUIOption getOptionInstance()
    {
        GUIOption UI_option = null; 

        if(!UI_options.isEmpty() && current_instance_index < UI_options.size())
        {
            UI_option = UI_options.get(current_instance_index);
        }
        else if(!UI_options.isEmpty())
        {
            UI_option = UI_options.getFirst();
            current_instance_index = 0;
        }

        current_instance_index++;
        return UI_option;
    }

    /**
     * Used to make sure all GUIOptions are not selected. 
     */
    public void reset()
    {
        for(int i = 0; i < UI_options.size(); i++)
        {
            UI_options.get(i).toggleSelectedStatus(false);
        }
    }

    /**
     * @return The amount of GUIOptions. 
     */
    public int getAmountOfOptions()
    {
        return UI_options.size();
    }

    /**
     * @return The ID number associated with this GUI instance; a specific and unique value given to each GUI upon its instantiation. 
     */
    public double getID()
    {
        return UI_ID;
    }

    /**
     * Used to select one of the GUI's GUIOptions. Moreover, only one option can be selected at a time, and so all GUIOptions are
     * reset before selectiing the desired option based on the index passed in; the GUIOptions are listed based on index, where the list
     * and the indeces are determine based on the order of which each GUIOption is passed into the GUI's constructor. 
     * 
     * @param index 
     * - The index associated with the GUIOption that should be selected. 
     */
    public void setSelectedOption(int index)
    {
        try
        {
            reset();
            UI_options.get(index).toggleSelectedStatus(true);
            current_selected_index = index;
        }
        catch(IndexOutOfBoundsException e) {}
    }

    /**
     * @return The index of the GUIOption that is currently selected.
     * 
     * @see
     * Note: The GUIOption instances are indexed based on how they 
     * are passed into the GUI's constructor. 
     */
    public int getSelectedOptionIndex()
    {
        return current_selected_index;
    }

    /**
     * Assuming the built-in Controller support is used by calling the beginControllerGUI(...) method, this method is used 
     * to determine whether or not certain buttons should be enabled. This is useful for freezing a GUI's state while keeping the GUI
     * in an active state; to ignore controller input when certain conditions are not met. 
     * 
     *   @param enable_left
     *   - The condition used to determine if whether or not the left key should currently be enabled. 
     * 
     *   @param enable_right
     *   - The condition used to determine if whether or not the right key should currently be enabled.
     * 
     *  @param enable_down
     *   - The condition used to determine if whether or not the down key should currently be enabled.
     * 
     *  @see Note:
     *  This method may be continuously called to actively update the enability of each key on the controller when using the GUI.
     *  Otherwise, if not called continuously, this method may be used to simply just enable and disable different controller buttons. 
     */
    public void enablerGUIController(boolean enable_left, boolean enable_right, boolean enable_down)
    {
        button_conditions = new GUIControllerConditions(enable_left, enable_right, enable_down);
    }

    /**
     *   Used to allow the GUI to be interacted with by using three inputs from a controller. The left key, down key, and the right key. The left & right
     *   keys are used to translate from one GUI option to another. These options are recognized as a list orderd with indeces. The left key
     *   will move down towards the option associated with index zero, and vice versa for the right key. Finally, the down key is used
     *   to "run" the currently-selected GUI option's app mechanic that it was given during the GUIOption instance's instantiation.
     * 
     *   @param controller 
     *   - The controller that will be read to select the "current" GUI option while playing an associated audio file if present. 
     *   Moreover, upon observing when the down key is pressed, the mechanic associated with the selected GUI option is scheduled.
     *   This mechanic is continuously re-scheduled upon ending until the down key is no longer being pressed.
     * 
     *   @see Note:
     *   This method should only be called to enable simple GUI interaction with a Controller. However,
     *   other GUI methods may be used instead of calling this method if simple Controller interaction is not satisfactory.   
     *   Lastly, this method should be followed by eventually calling the endControllerGUI() method! Calling the end method
     *   will remove all Controller interaction mechanics from Controller button bindings!
     *  
     */
     public void beginControllerGUI(Controller controller)
    {
        GUI_controller = controller;
        controller.whenDownPressed(down_key_mechanic);
        controller.whenLeftPressed(left_key_mechanic);
        controller.whenRightPressed(right_key_mechanic);
    }

    /**
     * Used to remove all Controller interaction mechanics from Controller button bindings! This is required after the beginControllerGUI()
     * method has been called! This will disable Controller interaction for the GUI.
     */
    public void endControllerGUI()
    {
        if(GUI_controller != null)
        {
            GUI_controller.removeWhenDownPressedMechanic(down_key_mechanic);
            GUI_controller.removeWhenLeftPressedMechanic(left_key_mechanic);
            GUI_controller.removeWhenRightPressedMechanic(right_key_mechanic);
        }
    }

    /**
     * Used to play WAV audio files for whenever the GUI is interacted with; whether a new GUIOption is selected, or an option is activated.
     * 
     * @param next_option_file_name
     * - The WAV file to play once whenever another GUIOption is selected by moving from one option to another.
     * 
     * @param activate_option_file_name
     * - The WAV file to play once whenever the currently-selected GUIOption is activated. GUIOption activation occurs when the selected option
     * is "pressed", "clicked", etc. 
     * 
     * @see
     * Note: Only WAV file types can be used!
     */
    public void setAudio(String next_option_file_name, String activate_option_file_name)
    {
        this.next_option_file_name = next_option_file_name;
        this.activate_option_file_name = activate_option_file_name;
    }
}
