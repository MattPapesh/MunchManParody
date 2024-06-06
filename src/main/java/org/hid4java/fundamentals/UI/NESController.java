package org.hid4java.fundamentals.UI;

import java.util.LinkedList;

import org.hid4java.app.input.NESControllerInput;
import org.hid4java.app.input.NESControllerInput.Button;
import org.hid4java.fundamentals.mechanic.MechanicBase;

public class NESController extends ControllerBase
{
    private LinkedList<NESButton> buttons = new LinkedList<NESButton>();
    private NESControllerInput nes_input = null;

    private NESButton DPAD = null;
    private NESButton LEFT = null; 
    private NESButton RIGHT = null;
    private NESButton UP = null; 
    private NESButton DOWN = null; 
    
    private NESButton SELECT = null; 
    private NESButton START = null; 
    private NESButton A = null; 
    private NESButton B = null; 

    public NESController(NESControllerInput nes_input) {
        this.nes_input = nes_input;
        DPAD = new NESButton(nes_input, Button.D_PAD);
        LEFT = new NESButton(nes_input, Button.LEFT);
        RIGHT = new NESButton(nes_input, Button.RIGHT);
        UP = new NESButton(nes_input, Button.UP);
        DOWN = new NESButton(nes_input, Button.DOWN);

        SELECT = new NESButton(nes_input, Button.SELECT);
        START = new NESButton(nes_input, Button.START);
        A = new NESButton(nes_input, Button.A);
        B = new NESButton(nes_input, Button.B);

        appendButtons(DPAD, LEFT, RIGHT, UP, DOWN, SELECT, START, A, B);
        toggleActivity(true);
    }

    private void appendButtons(NESButton... buttons)
    {
        for(int i = 0; i < buttons.length; i++)
        {
            this.buttons.addLast(buttons[i]);
        }
    }

    public boolean isDPadPressed() { return DPAD.isActive(); }
    public boolean isLeftPressed() { return LEFT.isActive(); }
    public boolean isRightPressed() { return RIGHT.isActive(); }
    public boolean isUpPressed() { return UP.isActive(); }
    public boolean isDownPressed() { return DOWN.isActive(); }

    public boolean isSelectPressed() { return SELECT.isActive(); }
    public boolean isStartPressed() { return START.isActive(); }
    public boolean isButtonAPressed() { return A.isActive(); }
    public boolean isButtonBPressed() { return B.isActive(); }

    public <GenericMechanic extends MechanicBase> void removeWhenPressedMechanic(Button button, GenericMechanic mechanic) {
        for(int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i).getButtonID() == button) {
                buttons.get(i).removeWhenPressedMechanic(mechanic);
            }
        }
    }

    public <GenericMechanic extends MechanicBase> void  removeWhilePressedContinuousMechanic(Button button, GenericMechanic mechanic) {
        for(int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i).getButtonID() == button) {
                buttons.get(i).removeWhilePressedContinuousMechanic(mechanic);
            }
        }
    }

    public <GenericMechanic extends MechanicBase> void whenPressed(Button button, GenericMechanic mechanic)
    {
        for(int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i).getButtonID() == button) {
                buttons.get(i).whenPressed(mechanic);
            }
        }
    }

    public <GenericMechanic extends MechanicBase> void whilePressedContinuous(Button button, GenericMechanic mechanic)
    {
        for(int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i).getButtonID() == button) {
                buttons.get(i).whilePressedContinuous(mechanic);
            }
        }
    }

    

    public void run() {
        nes_input.run();
        for(int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i) != null) {
                buttons.get(i).run();
            }
        }
    }
}
