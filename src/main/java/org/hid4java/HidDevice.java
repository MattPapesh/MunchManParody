/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Gary Rowe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.hid4java;

import org.hid4java.jna.HidApi;
import org.hid4java.jna.HidDeviceInfoStructure;
import org.hid4java.jna.HidDeviceStructure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * High level wrapper to provide the following to API consumers:
 *
 * <ul>
 * <li>Simplified access to the underlying JNA HidDeviceStructure</li>
 * </ul>
 *
 * @since 0.0.1
 */
public class HidDevice {

  private final HidDeviceManager hidDeviceManager;
  private HidDeviceStructure hidDeviceStructure;

  private final String path;
  private final int vendorId;
  private final int productId;
  private String serialNumber;
  private final int releaseNumber;
  private String manufacturer;
  private String product;
  private final int usagePage;
  private final int usage;
  private final int interfaceNumber;

  private final boolean autoDataRead;
  private final int dataReadInterval;

  /**
   * The data read thread
   * We use a Thread instead of Executor since it may be stopped/paused/restarted frequently
   * and executors are more heavyweight in this regard
   */
  private Thread dataReadThread = null;

  /**
   * @param infoStructure            The HID device info structure providing details
   * @param hidDeviceManager         The HID device manager providing access to device enumeration for post IO scanning
   * @param hidServicesSpecification The HID services specification providing configuration details
   * @since 0.1.0
   */
  public HidDevice(HidDeviceInfoStructure infoStructure, HidDeviceManager hidDeviceManager, HidServicesSpecification hidServicesSpecification) {

    this.hidDeviceManager = hidDeviceManager;

    this.dataReadInterval = hidServicesSpecification.getDataReadInterval();
    this.autoDataRead = hidServicesSpecification.isAutoDataRead();

    this.hidDeviceStructure = null;

    this.path = infoStructure.path;

    // Note that the low-level HidDeviceInfoStructure is directly written to by
    // the JNA library and implies an unsigned short which is not available in Java.
    // The bitmask converts from [-32768, 32767] to [0,65535]
    // In Java 8 Short.toUnsignedInt() is available.
    this.vendorId = infoStructure.vendor_id & 0xffff;
    this.productId = infoStructure.product_id & 0xffff;

    this.releaseNumber = infoStructure.release_number;
    if (infoStructure.serial_number != null) {
      this.serialNumber = infoStructure.serial_number.toString();
    }
    if (infoStructure.manufacturer_string != null) {
      this.manufacturer = infoStructure.manufacturer_string.toString();
    }
    if (infoStructure.product_string != null) {
      this.product = infoStructure.product_string.toString();
    }
    this.usagePage = infoStructure.usage_page;
    this.usage = infoStructure.usage;
    this.interfaceNumber = infoStructure.interface_number;
  }

  /**
   * Handles the process of starting the data read thread
   */
  private void startDataReadThread() {

    // Check for previous start
    if (this.isDataRead()) {
      return;
    }

    // Perform an immediate data read
    dataRead();

    // Ensure we have a scan thread available
    configureDataReadThread(getDataReadRunnable());

  }

  /**
   * Stop the data read thread
   */
  private synchronized void stopDataReadThread() {

    if (isDataRead()) {
      dataReadThread.interrupt();
    }

  }

  /**
   * Configures the data read thread to allow recovery from stop or pause
   */
  private synchronized void configureDataReadThread(Runnable dataReadRunnable) {

    if (autoDataRead) {
      stopDataReadThread();
    }

    // Require a new one
    dataReadThread = new Thread(dataReadRunnable);
    dataReadThread.setDaemon(true);
    dataReadThread.setName("hid4java data reader");
    dataReadThread.start();

  }

  private synchronized Runnable getDataReadRunnable() {

    return new Runnable() {
      @Override
      public void run() {

        while (true) {
          try {
            //noinspection BusyWait
            Thread.sleep(dataReadInterval);
          } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
          }
          dataRead();
        }
      }
    };

  }

  /**
   * @return True if the data read thread is running
   */
  private boolean isDataRead() {
    return dataReadThread != null && dataReadThread.isAlive();
  }


  /**
   * Attempt to read all data from the device input buffer as part
   * of the automatic data read process
   * <br>
   * Will fire attach/detach events as appropriate.
   */
  private synchronized void dataRead() {

    byte[] dataRead = readAll(100);

    // Fire the event on a separate thread
    hidDeviceManager.afterDeviceDataRead(this, dataRead);

  }

  /**
   * The "path" is well-supported across Windows, Mac and Linux so makes a
   * better choice for a unique ID
   * <br>
   * See hid4java issue #8 for details
   *
   * @return A unique device ID made up from vendor ID, product ID and serial number
   * @since 0.1.0
   */
  public String getId() {
    return path;
  }

  /**
   * @return The device path
   * @since 0.1.0
   */
  public String getPath() {
    return path;
  }

  /**
   * @return Int version of vendor ID
   * @since 0.1.0
   */
  public int getVendorId() {
    return vendorId;
  }

  /**
   * @return Int version of product ID
   * @since 0.1.0
   */
  public int getProductId() {
    return productId;
  }

  /**
   * @return The device serial number
   * @since 0.1.0
   */
  public String getSerialNumber() {
    return serialNumber;
  }

  /**
   * @return The release number
   * @since 0.1.0
   */
  public int getReleaseNumber() {
    return releaseNumber;
  }

  /**
   * @return The manufacturer
   * @since 0.1.0
   */
  public String getManufacturer() {
    return manufacturer;
  }

  /**
   * @return The product
   * @since 0.1.0
   */
  public String getProduct() {
    return product;
  }

  /**
   * @return The usage page
   * @since 0.1.0
   */
  public int getUsagePage() {
    return usagePage;
  }

  /**
   * @return The usage information
   * @since 0.1.0
   */
  public int getUsage() {
    return usage;
  }

  public int getInterfaceNumber() {
    return interfaceNumber;
  }

  /**
   * Open this device and obtain a device structure
   *
   * @return True if the device was successfully opened
   * @since 0.1.0
   */
  public boolean open() {
    hidDeviceStructure = HidApi.open(path);

    // Configure automatic data read
    if (autoDataRead) {
      startDataReadThread();
    }

    return hidDeviceStructure != null;
  }

  /**
   * @return True if the device structure is present
   * @since 0.1.0
   * @deprecated Use isClosed() instead of !isOpen() to improve code clarity
   */
  @Deprecated
  public boolean isOpen() {
    return !isClosed();
  }

  /**
   * @return True if the device structure is not present (device closed)
   * @since 0.8.0
   */
  public boolean isClosed() {
    return hidDeviceStructure == null;
  }

  /**
   * Close this device freeing the HidApi resources
   *
   * @since 0.1.0
   */
  public void close() {
    if (isClosed()) {
      return;
    }

    // Prevent further automatic data read attempts
    stopDataReadThread();

    // Close the Hidapi reference
    HidApi.close(hidDeviceStructure);

    // Ensure structure is removed from memory and prevent further interaction
    hidDeviceStructure = null;
  }

  /**
   * Set the device handle to be non-blocking
   * <br>
   * In non-blocking mode calls to hid_read() will return immediately with a
   * value of 0 if there is no data to be read. In blocking mode, hid_read()
   * will wait (block) until there is data to read before returning
   * <br>
   * Non-blocking can be turned on and off at any time
   *
   * @param nonBlocking True if non-blocking mode is required
   * @since 0.1.0
   */
  public void setNonBlocking(boolean nonBlocking) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    HidApi.setNonBlocking(hidDeviceStructure, nonBlocking);
  }

  /**
   * Read an Input report from a HID device
   * <br>
   * Input reports are returned to the host through the INTERRUPT IN endpoint.
   * The first byte will contain the Report number if the device uses numbered
   * reports
   *
   * @param data The buffer to read into
   * @return The actual number of bytes read and -1 on error. If no packet was
   * available to be read and the handle is in non-blocking mode, this
   * function returns 0.
   * @since 0.1.0
   */
  public int read(byte[] data) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    return HidApi.read(hidDeviceStructure, data);
  }

  /**
   * Read an Input report from a HID device
   * <br>
   * Input reports are returned to the host through the INTERRUPT IN endpoint.
   * The first byte will contain the Report number if the device uses numbered
   * reports
   *
   * @param amountToRead  the number of bytes to read
   * @param timeoutMillis The number of milliseconds to wait before giving up
   * @return a Byte array of the read data
   * @since 0.1.0
   */
  public Byte[] read(int amountToRead, int timeoutMillis) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }

    byte[] bytes = new byte[amountToRead];
    int read = HidApi.read(hidDeviceStructure, bytes, timeoutMillis);
    Byte[] retData = new Byte[read];
    for (int i = 0; i < read; i++) {
      retData[i] = bytes[i];
    }
    return retData;
  }

  /**
   * Read an Input report from a HID device
   * Input reports are returned to the host through the INTERRUPT IN endpoint.
   * The first byte will contain the Report number if the device uses numbered
   * reports
   *
   * @param amountToRead the number of bytes to read.
   * @return a Byte array of the read data
   * @since 0.1.0
   */
  public Byte[] read(int amountToRead) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }

    byte[] bytes = new byte[amountToRead];
    int read = HidApi.read(hidDeviceStructure, bytes);
    Byte[] retData = new Byte[read];
    for (int i = 0; i < read; i++) {
      retData[i] = bytes[i];
    }
    return retData;
  }

  /**
   * Read an Input report (64 bytes) from a HID device with a 1000ms timeout.
   * <br>
   * Input reports are returned to the host through the INTERRUPT IN endpoint.
   * The first byte will contain the Report number if the device uses numbered
   * reports.
   *
   * @return A Byte array of the read data
   * @since 0.1.0
   */
  public Byte[] read() {
    return read(64, 1000);
  }

  /**
   * Read an Input report from a HID device with timeout
   *
   * @param bytes         The buffer to read into
   * @param timeoutMillis The number of milliseconds to wait before giving up
   * @return The actual number of bytes read and -1 on error. If no packet was
   * available to be read within the timeout period returns 0.
   * @since 0.1.0
   */
  public int read(byte[] bytes, int timeoutMillis) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    return HidApi.read(hidDeviceStructure, bytes, timeoutMillis);

  }

  /**
   * Read an Input report from a HID device with timeout
   *
   * @param timeoutMillis The number of milliseconds to wait before giving up
   * @return A byte[] of the read data
   * @since 0.8.0
   */
  public byte[] readAll(int timeoutMillis) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }

    // Overall data storage
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    // Prepare to read a single HID packet
    boolean morePackets = true;
    while (morePackets) {
      byte[] packet = new byte[64];

      // This method will block while awaiting data
      int bytesRead = read(packet, timeoutMillis);

      if (bytesRead > 0) {
        try {
          output.write(packet);
        } catch (IOException e) {
          morePackets = false;
        }
      } else {
        morePackets = false;
      }
    }

    return output.toByteArray();
  }


  /**
   * Get a feature report from a HID device
   * Under the covers the HID library will set the first byte of data[] to the
   * Report ID of the report to be read. Upon return, the first byte will
   * still contain the Report ID, and the report data will start in data[1]
   * <br>
   * This method handles all the wide string and array manipulation for you
   *
   * @param data     The buffer to contain the report
   * @param reportId The report ID (or (byte) 0x00)
   * @return The number of bytes read plus one for the report ID (which has
   * been removed from the first byte), or -1 on error.
   * @since 0.1.0
   */
  public int getFeatureReport(byte[] data, byte reportId) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    return HidApi.getFeatureReport(hidDeviceStructure, data, reportId);
  }

  /**
   * Send a Feature report to the device
   * <br>
   * Under the covers, feature reports are sent over the Control endpoint as a
   * Set_Report transfer. The first byte of data[] must contain the Report ID.
   * For devices which only support a single report, this must be set to 0x0.
   * The remaining bytes contain the report data
   * <br>
   * Since the Report ID is mandatory, calls to hid_send_feature_report() will
   * always contain one more byte than the report contains. For example, if a
   * hid report is 16 bytes long, 17 bytes must be passed to
   * hid_send_feature_report(): the Report ID (or 0x0, for devices which do
   * not use numbered reports), followed by the report data (16 bytes). In
   * this example, the length passed in would be 17
   * <br>
   * This method handles all the array manipulation for you
   *
   * @param data     The feature report data (will be widened and have the report
   *                 ID pre-pended)
   * @param reportId The report ID (or (byte) 0x00)
   * @return This function returns the actual number of bytes written and -1
   * on error.
   * @since 0.1.0
   */
  public int sendFeatureReport(byte[] data, byte reportId) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    return HidApi.sendFeatureReport(hidDeviceStructure, data, reportId);
  }

  /**
   * Get a string from a HID device, based on its string index
   *
   * @param index The index
   * @return The string
   * @since 0.1.0
   */
  public String getIndexedString(int index) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    return HidApi.getIndexedString(hidDeviceStructure, index);
  }

  /**
   * Get a report descriptor from a HID device
   * <br>
   * A report descriptor provides significant detail on the characteristics of
   * an attached device using a defined format.
   * <br>
   * You may find these supporting documents helpful:
   * <a href="https://usb.org/document-library/hid-usage-tables-15">HID Usage Tables 1.5 (PDF)</a>
   * <br>
   * <a href="https://www.usb.org/document-library/device-class-definition-hid-111">Device Class Definition HID 1.11 (PDF)</a>
   *
   * @param buffer The buffer for descriptor (4096 bytes recommended).
   * @return A non-negative number of bytes actually copied, or -1 on error.
   * @since 0.14.0 hidapi
   */
  public int getReportDescriptor(byte[] buffer) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    return HidApi.getReportDescriptor(hidDeviceStructure, buffer, buffer.length);
  }

  /**
   * Write the message to the HID API without zero byte padding.
   * <br>
   * Note that the report ID will be prefixed to the HID packet as per HID rules.
   *
   * @param message      The message
   * @param packetLength The packet length
   * @param reportId     The report ID (will be prefixed to the HID packet)
   * @return The number of bytes written (including report ID), or -1 if an error occurs
   * @since 0.1.0
   */
  public int write(byte[] message, int packetLength, byte reportId) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }
    return write(message, packetLength, reportId, false);
  }

  /**
   * Write the message to the HID API with optional zero byte padding to packet length.
   * <br>
   * Note that the report ID will be prefixed to the HID packet as per HID rules.
   *
   * @param message      The message
   * @param packetLength The packet length
   * @param reportId     The report ID
   * @param applyPadding True if the message should be filled with zero bytes to the packet length
   * @return The number of bytes written (including report ID), or -1 if an error occurs
   * @since 0.8.0
   */
  public int write(byte[] message, int packetLength, byte reportId, boolean applyPadding) {
    if (isClosed()) {
      throw new IllegalStateException("Device has not been opened");
    }

    if (applyPadding) {
      message = Arrays.copyOf(message, packetLength + 1);
    }

    int result = HidApi.write(hidDeviceStructure, message, packetLength, reportId);
    // Update HID manager
    hidDeviceManager.afterDeviceWrite();
    return result;

  }

  /**
   * @return The last error message from HID API
   * @since 0.1.0
   */
  public String getLastErrorMessage() {
    return HidApi.getLastErrorMessage(hidDeviceStructure);
  }

  /**
   * @param vendorId     The vendor ID
   * @param productId    The product ID
   * @param serialNumber The serial number
   * @return True if the device matches the given the combination with vendorId, productId being zero acting as a wildcard
   * @since 0.1.0
   */
  public boolean isVidPidSerial(int vendorId, int productId, String serialNumber) {
    if (serialNumber == null)
      return (vendorId == 0 || this.vendorId == vendorId)
        && (productId == 0 || this.productId == productId);
    else
      return (vendorId == 0 || this.vendorId == vendorId)
        && (productId == 0 || this.productId == productId)
        && (this.serialNumber.equals(serialNumber));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HidDevice hidDevice = (HidDevice) o;

    return path.equals(hidDevice.path);

  }

  @Override
  public int hashCode() {
    return path.hashCode();
  }

  @Override
  public String toString() {
    return "HidDevice [path=" + path
      + ", vendorId=0x" + Integer.toHexString(vendorId)
      + ", productId=0x" + Integer.toHexString(productId)
      + ", serialNumber=" + serialNumber
      + ", releaseNumber=0x" + Integer.toHexString(releaseNumber)
      + ", manufacturer=" + manufacturer
      + ", product=" + product
      + ", usagePage=0x" + Integer.toHexString(usagePage)
      + ", usage=0x" + Integer.toHexString(usage)
      + ", interfaceNumber=" + interfaceNumber
      + "]";
  }

}
