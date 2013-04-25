package com.github.FallDownT.Roomba;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Bluetooth reader listens to the Bluetooth to read any inputs it may receive.
 */
public class BluetoothReader implements Runnable {
    private BufferedReader reader;
    private BluetoothObserver observer;

    /**
     * Overloaded constructor to pass in the BufferedReader.
     *
     * @param r BufferedReader to listen to.
     */
    public BluetoothReader(BufferedReader r, bluetoothControl b) {
        reader = r;
        observer = b;
    }

    /**
     * Start this to listen to the BufferedReader.
     */
    @Override
    public void run() {
        char[] buffer;
        byte[] packet = new byte[4];
        int len;
        char inChar;
        String inString;
        int i = 0;
        while (true) {
            buffer = new char[1024];
            try {
                if ((len = reader.read(buffer)) > -1) {
                    inString = new String(buffer, 0, len);
                    inChar = (len > 0) ? inString.charAt(0) : '@';
                    if (i == 3) {
                        packet[i] = (byte) inChar;
                        i = 0;
                        observer.update(packet);    // Send Packets
                    } else if (i > 0 || inChar == 'A') {
                        packet[i] = (byte) inChar;
                        i++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}