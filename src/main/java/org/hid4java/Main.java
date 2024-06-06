package org.hid4java;

import org.hid4java.app.input.NESControllerInput;

public class Main
{
    public static void main(String[] args)
    {
        NESControllerInput controller_input = new NESControllerInput();
        //new App().startApp();
        while(true) {
            controller_input.run();
        }
    }
}