package fundamentals.UI.GUI;

import fundamentals.UI.Controller;
import fundamentals.mechanic.MechanicBase;

/**
 * As the name implies, JointGUI is a joint, or combined, interface between two separate GUIs. The primary use of a JointGUI is to
 * easily transition between using two different GUIs. Moreover, a JointGUI is primarily meant for GUIs that make use of the GUIs' 
 * built-in Controller support; GUIs that call the beginControllerGUI(...) method. So, when a Controller is in use, the JointGUI's
 * primary GUI will be active 
 */
public class JointGUI extends MechanicBase
{
    private GUI current_GUI = null;
    private GUI primary_GUI = null;
    private GUI secondary_GUI = null;
    private JointGUIInterface joint_GUI_behavior = null;
    private Controller joint_GUI_controller = null;

    public JointGUI(GUI primary_GUI, GUI secondary_GUI, JointGUIInterface joint_GUI_behavior)
    {
        this.current_GUI = primary_GUI;
        this.primary_GUI = primary_GUI;
        this.secondary_GUI = secondary_GUI;
        this.joint_GUI_behavior = joint_GUI_behavior;
    }

    public void enablePrimaryGUIController(boolean enable_left, boolean enable_right, boolean enable_cargo_down)
    {
        primary_GUI.enablerGUIController(enable_left, enable_right, enable_cargo_down);
    }

    public void enableSecondaryGUIController(boolean enable_left, boolean enable_right, boolean enable_cargo_down)
    {
        secondary_GUI.enablerGUIController(enable_left, enable_right, enable_cargo_down);
    }

    public void toggleActivity(boolean active)
    {
        primary_GUI.toggleActivity(active);
        secondary_GUI.toggleActivity(active);
    }

    public void beginJointControllerGUI(Controller controller)
    {
        joint_GUI_controller = controller;
        primary_GUI.beginControllerGUI(controller);
        super.schedule();
    }

    public void endJointControllerGUI()
    {
        primary_GUI.endControllerGUI();
        secondary_GUI.endControllerGUI();
        super.cancel();
    }

    private void runJointGUI()
    {
        if(current_GUI == primary_GUI && joint_GUI_behavior.swapToSecondaryGUI())
        {
            current_GUI = secondary_GUI;
            
            primary_GUI.endControllerGUI();
            secondary_GUI.beginControllerGUI(joint_GUI_controller);
        }
        else if(current_GUI == secondary_GUI && joint_GUI_behavior.swapToPrimaryGUI())
        {
            current_GUI = primary_GUI;

            primary_GUI.beginControllerGUI(joint_GUI_controller);
            secondary_GUI.endControllerGUI();
        }

    }

    @Override 
    public void execute()
    {
        runJointGUI();
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
