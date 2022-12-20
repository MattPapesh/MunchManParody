package fundamentals.UI.GUI;

/**
 * Use for declaring the GUI swap methods for a JointGUI instance. These declared methods can be given overridden definitions
 * to determine the conditions that must be met to swap between the two GUIs. 
 */
public interface JointGUIInterface
{
    /**
     * @return Whether or not a JointGUI should swap from the secondary GUI to the primary GUI;
     */
    public boolean swapToPrimaryGUI();

    /**
     * @return Whether or not a JointGUI should swap from the primary GUI to the secondary GUI;
     */
    public boolean swapToSecondaryGUI();
}