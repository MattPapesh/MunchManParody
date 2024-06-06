package org.hid4java.fundamentals.UI;

public abstract class ControllerBase {
    private final double CONTROLLER_ID = Math.random();
    private boolean active = false;

    public ControllerBase() {}

    /**
     * Toggles the controller's activity to either be enabled or disabled.
     * 
     * @param active
     * - The new toggled active state for the controller.
     */
    public void toggleActivity(boolean active) {
        if(!this.active && active) {
            this.active = true;
            ControllerScheduler.registerController(this);
        }
        else if(this.active & !active) {
            this.active = false; 
            ControllerScheduler.removeController(this);
        }
    }

    protected double getControllerID() {
        return CONTROLLER_ID;
    }

    abstract public void run();
}
