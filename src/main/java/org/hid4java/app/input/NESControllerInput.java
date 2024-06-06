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

    public final int HID_MIN_SUB_BUFFER_0_INDEX = 14; 
    public final int HID_MIN_SUB_BUFFER_1_INDEX = 12; 

    public final int HID_BUFFER_SIZE = 64;
    public final int HID_BUFFER_ELEMENT_SIZE = 16;
    public final int HID_SUB_BUFFER_0_SIZE =  HID_BUFFER_ELEMENT_SIZE - HID_MIN_SUB_BUFFER_0_INDEX;
    public final int HID_SUB_BUFFER_1_SIZE =  HID_BUFFER_ELEMENT_SIZE - HID_MIN_SUB_BUFFER_1_INDEX;

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

        BitSet buffer_0 = GameMath.getBinary((int)hid_buffer[HID_SUB_BUFFER_INDEX_0], HID_BUFFER_ELEMENT_SIZE, HID_MIN_SUB_BUFFER_0_INDEX);
        BitSet buffer_1 = GameMath.getBinary((int)hid_buffer[HID_SUB_BUFFER_INDEX_1], HID_BUFFER_ELEMENT_SIZE, HID_MIN_SUB_BUFFER_1_INDEX);
        System.out.print(GameMath.getBinaryString(buffer_0, HID_SUB_BUFFER_0_SIZE, HID_MIN_SUB_BUFFER_0_INDEX) + "\t");
        System.out.println(GameMath.getBinaryString(buffer_1, HID_SUB_BUFFER_1_SIZE, HID_MIN_SUB_BUFFER_1_INDEX));

        controller.close();
        hid_services.shutdown();
    }
}
