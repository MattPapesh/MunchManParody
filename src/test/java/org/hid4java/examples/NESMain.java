package org.hid4java.examples;

import java.util.BitSet;

import org.hid4java.*;
import org.hid4java.event.HidServicesEvent;
import org.hid4java.fundamentals.GameMath;

public class NESMain {
    private static final int BUFFER_SIZE = 64;
    private static final int VENDOR_ID = 0x0079; // Replace with your device's Vendor ID
    private static final int PRODUCT_ID = 0x1804; // Replace with your device's Product ID

    private static HidServicesListener listener = new HidServicesListener() {
        @Override public void hidDeviceAttached(HidServicesEvent event) {}
        @Override public void hidDeviceDetached(HidServicesEvent event) {}       
        @Override public void hidFailure(HidServicesEvent event) { }
        @Override public void hidDataReceived(HidServicesEvent event) {}
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
                        BitSet binary = GameMath.getBinary((int)buffer[data_index], size);
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