package org.hid4java.app.input;

import java.util.BitSet;

import org.hid4java.event.HidDevice;
import org.hid4java.event.HidManager;
import org.hid4java.event.HidServices;
import org.hid4java.event.HidServicesEvent;
import org.hid4java.event.HidServicesListener;
import org.hid4java.event.HidServicesSpecification;
import org.hid4java.fundamentals.GameMath;

public class NESControllerInput 
{
    public final int HID_VENDOR_ID = 0x0079;
    public final int HID_PRODUCT_ID = 0x1804;
    
    public final int HID_SUB_BUFFER_INDEX_0 = 0;
    public final int HID_SUB_BUFFER_INDEX_1 = 2;

    private final int HID_MIN_SUB_BUFFER_0_INDEX = 8;//14; 
    private final int HID_MIN_SUB_BUFFER_1_INDEX = 12; 

    private final int HID_BUFFER_SIZE = 64;
    private final int HID_BUFFER_ELEMENT_SIZE = 16;

    private final int[][] HID_FORMATTED_SUB_BUFFER_0_INDECES = {{8, 9}, {14, 15}};
    private final int[][] HID_FORMATTED_SUB_BUFFER_1_INDECES = {{12, 15}};
    private final int HID_FORMATTED_SUB_BUFFER_0_SIZE = 4;
    private final int HID_FORMATTED_SUB_BUFFER_1_SIZE = 4;
    private final int CONTROLLER_BUFFER_SIZE = 8;

    private BitSet controller_buffer = new BitSet(CONTROLLER_BUFFER_SIZE);

    private  HidServicesListener listener = new HidServicesListener() {
        @Override public void hidDeviceAttached(HidServicesEvent event) {}
        @Override public void hidDeviceDetached(HidServicesEvent event) {}       
        @Override public void hidFailure(HidServicesEvent event) { }
        @Override public void hidDataReceived(HidServicesEvent event) {}
    };

    public NESControllerInput() {}

    public void run() {
        // Configure to use custom specification
        HidServicesSpecification hid_services_specification = new HidServicesSpecification();
        // Use the v0.7.0 manual start feature to get immediate attach events
        hid_services_specification.setAutoStart(false);
        HidServices hid_services = HidManager.getHidServices(hid_services_specification);
        hid_services.addHidServicesListener(listener);

        HidDevice controller = null;
        for(HidDevice device : hid_services.getAttachedHidDevices()) {
            if(device.getVendorId() == HID_VENDOR_ID && device.getProductId() == HID_PRODUCT_ID) {
                controller = device;
                break;
            }
        }

        if(controller == null) {
            return;
        }

        controller.open();
        byte[] hid_buffer = new byte[HID_BUFFER_SIZE];
        
        controller.read(hid_buffer);
        controller.close();
        hid_services.shutdown();

        BitSet buffer_0 = GameMath.getBinary((int)hid_buffer[HID_SUB_BUFFER_INDEX_0], HID_BUFFER_ELEMENT_SIZE, HID_MIN_SUB_BUFFER_0_INDEX);
        BitSet buffer_1 = GameMath.getBinary((int)hid_buffer[HID_SUB_BUFFER_INDEX_1], HID_BUFFER_ELEMENT_SIZE, HID_MIN_SUB_BUFFER_1_INDEX);
        controller_buffer.clear();

        int formatted_buffer_0_index = 0;
        for(int group_index = 0; group_index < HID_FORMATTED_SUB_BUFFER_0_INDECES.length; group_index++) {
            int group_min = HID_FORMATTED_SUB_BUFFER_0_INDECES[group_index][0];
            int group_max = HID_FORMATTED_SUB_BUFFER_0_INDECES[group_index][1];
            for(int bit_index = group_min; bit_index <= group_max; bit_index++) {
                if(buffer_0.get(bit_index)) {
                    controller_buffer.set(formatted_buffer_0_index + bit_index - group_min);
                }
            }

            formatted_buffer_0_index += group_max - group_min + 1;
        }

        int formatted_buffer_1_index = 0;
        for(int group_index = 0; group_index < HID_FORMATTED_SUB_BUFFER_1_INDECES.length; group_index++) {
            int group_min = HID_FORMATTED_SUB_BUFFER_1_INDECES[group_index][0];
            int group_max = HID_FORMATTED_SUB_BUFFER_1_INDECES[group_index][1];
            for(int bit_index = group_min; bit_index <= group_max; bit_index++) {
                if(buffer_1.get(bit_index)) {
                    controller_buffer.set(HID_FORMATTED_SUB_BUFFER_0_SIZE + formatted_buffer_1_index + bit_index - group_min);
                }
            } 

            formatted_buffer_1_index += group_max - group_min + 1;
        }

        System.out.println(GameMath.getBinaryString(controller_buffer, 8, 0));
    }
}
