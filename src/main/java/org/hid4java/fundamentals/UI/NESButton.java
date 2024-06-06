package org.hid4java.fundamentals.UI;

import java.util.LinkedList;

import org.hid4java.app.input.Button;
import org.hid4java.app.input.NESControllerInput;
import org.hid4java.fundamentals.mechanic.MechanicBase;

public class NESButton 
{
    // determines if the button is currently being held down and is active
    private boolean completed_lifetime_update = false; 
    private boolean current_is_active = false;
    private boolean prev_is_active = false;
    private NESControllerInput nes_input = null;
    private NESControllerInput.Button id = null;

    private LinkedList<MechanicBase> when_pressed_mechanics = new LinkedList<MechanicBase>();
    private LinkedList<MechanicBase> while_pressed_cont_mechanics = new LinkedList<MechanicBase>(); 

    public NESButton(NESControllerInput nes_input, NESControllerInput.Button button) {
        this.nes_input = nes_input;
        this.id = button;
    }
    
    public NESControllerInput.Button getButtonID() {
        return id;
    }

    public <GenericMechanic extends MechanicBase> void  removeWhenPressedMechanic(GenericMechanic mechanic) {
        for(int i = 0; i < when_pressed_mechanics.size(); i++) {
            if(when_pressed_mechanics.get(i).getMechanicID() == mechanic.getMechanicID()) {
                when_pressed_mechanics.remove(i);
                i--;
            }
        }
    }

    public <GenericMechanic extends MechanicBase> void  removeWhilePressedContinuousMechanic(GenericMechanic mechanic) {
        for(int i = 0; i < when_pressed_mechanics.size(); i++) {
            if(while_pressed_cont_mechanics.get(i).getMechanicID() == mechanic.getMechanicID()) {
                while_pressed_cont_mechanics.remove(i);
                i--;
            }
        }
    }

    public <GenericMechanic extends MechanicBase> void whenPressed(GenericMechanic mechanic) {
        when_pressed_mechanics.addLast(mechanic);
    }

    public <GenericMechanic extends MechanicBase> void whilePressedContinuous(GenericMechanic mechanic) {
        while_pressed_cont_mechanics.addLast(mechanic);
    }

    private void runButtonMechanics(LinkedList<MechanicBase> mechanics) {
        for(int i = 0; i < mechanics.size(); i++) {
            if(!mechanics.get(i).isScheduled()) {
                mechanics.get(i).schedule();
            }
        }
    }

    private void cancelButtonMechanics(LinkedList<MechanicBase> mechanics) {
        for(int i = 0; i < mechanics.size(); i++) {
            mechanics.get(i).cancel();
        }
    }

    public boolean isActive() {
        current_is_active = nes_input.isButtonActive(id);
        return current_is_active;
    }

    public void run() {
        prev_is_active = current_is_active;
        if(isActive() && !prev_is_active) {
            completed_lifetime_update = true;
            runButtonMechanics(when_pressed_mechanics);
        }
        else if(!isActive() && prev_is_active) {
            cancelButtonMechanics(when_pressed_mechanics);
        }
        if(isActive()) {
            runButtonMechanics(while_pressed_cont_mechanics);
        }
        else {
            cancelButtonMechanics(while_pressed_cont_mechanics);
        }
    }
};