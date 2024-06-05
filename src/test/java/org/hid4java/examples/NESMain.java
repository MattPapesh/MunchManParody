package org.hid4java.examples;

import java.util.BitSet;

import org.hid4java.*;
import org.hid4java.event.HidServicesEvent;

public class NESMain {

    public static BitSet getBinary(int register, int size) {
        BitSet binary = new BitSet(size);
        register = Math.abs(register);
        for(int i = size - 1; i >= 0 && register > 0; i--) {
            int value = (int)Math.pow(2, i);
            if(value <= register) {
                register -= value;
                binary.set(size - i - 1);
            }
        }

        return binary;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static final int BYTE_START_INDEX_1 = 0;//24; 
    private static final int BUFFER_SIZE = 64;
    private static final int VENDOR_ID = 0x0079; // Replace with your device's Vendor ID
    private static final int PRODUCT_ID = 0x1804; // Replace with your device's Product ID

    private static HidServicesListener listener = new HidServicesListener() {
        @Override public void hidDeviceAttached(HidServicesEvent event) {
            System.out.println(ANSI_BLUE + "Device attached: " + event + ANSI_RESET);
        }

        @Override public void hidDeviceDetached(HidServicesEvent event) {
            System.out.println(ANSI_YELLOW + "Device detached: " + event + ANSI_RESET);
        }

        @Override public void hidFailure(HidServicesEvent event) {
            System.out.println(ANSI_RED + "HID failure: " + event + ANSI_RESET);
        }

        @Override public void hidDataReceived(HidServicesEvent event) {
            System.out.printf(ANSI_PURPLE + "Data received:%n", event.getDataReceived());
        }
    };

    public static void main(String[] args) {
        while(true) {
            // Configure to use custom specification
            HidServicesSpecification hid_services_specification = new HidServicesSpecification();
            // Use the v0.7.0 manual start feature to get immediate attach events
            hid_services_specification.setAutoStart(false);
            HidServices hid_services = HidManager.getHidServices(hid_services_specification);
            hid_services.addHidServicesListener(listener);

            for(HidDevice device : hid_services.getAttachedHidDevices()) {
                if(device.getVendorId() == VENDOR_ID && device.getProductId() == PRODUCT_ID) {
                    device.open();
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count = device.read(buffer);
                    if(count > 0) {//ByteBuffer.wrap(buffer).getShort()
                        System.out.print("Data: {");
                    }

                    for(int data_index = 0; data_index < count; data_index++) {
                        String delim = (data_index < count - 1) ? ", " : " }";
                        int size = 16;
                        BitSet binary = getBinary((int)buffer[data_index], size);
                        String binary_str = "";

                        for(int bit_index = 0; bit_index < 4; bit_index++) {
                            binary_str += ((binary.get(size - bit_index - 1)) ? 1 : 0);
                        }
                        
                        if(data_index == 0 || data_index == 2)
                        System.out.print(binary_str + delim);
                    }

                    System.out.println("");
                    device.close();
                }
            }

            hid_services.shutdown();
        }
    }
}