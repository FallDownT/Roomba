package com.github.FallDownT.Roomba;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class used to receive and transmit data from and to a serial port
 */
public class TwoWaySerialComm {
    // Define a reader and writer
    SerialReader reader;
    SerialWriter writer;

    public TwoWaySerialComm() {
        super();
    }

    public TwoWaySerialComm(String commPort) throws Exception {
        super();
        this.connect(commPort);
    }

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                (new Thread(reader = new SerialReader(in))).start();
                (new Thread(writer = new SerialWriter(out))).start();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
     * Class used to receive bytes from a serial port on its own thread
     */
    public static class SerialReader implements Runnable {
        InputStream in;

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int len;
            try {
                while ((len = this.in.read(buffer)) > -1) {
                    System.out.print(new String(buffer, 0, len));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Class used to transmit bytes to a serial port on its own thread
     */
    public static class SerialWriter implements Runnable {
        OutputStream out;
        byte circArrayBuffer[];
        int circArrayWriteIndex = 0;
        int circArraySendIndex = 0;
        final int circArraySize = 12;

        public SerialWriter(OutputStream out) {
            this.out = out;
            circArrayBuffer = new byte[circArraySize];
        }

        public void run() {
            try {
                int i = 0;          // Current iteration of busy waiting loop
                int iMax = 1000000; // Number of iterations of busy waiting loop delay
                while (true) {
                    if (circArrayWriteIndex != circArraySendIndex && i == iMax) {    // If data is waiting in queue
                        this.out.write(circArrayBuffer[circArraySendIndex]);        // Transmit that data
                        System.out.println("Transmission sent: " + circArrayBuffer[circArraySendIndex]);    // Debug statement
                        circArraySendIndex = (circArraySendIndex + 1) % circArraySize;  // Increment circular array index
                        i = 0;  // Reset busy waiting loop delay
                    }
                    i = i < iMax ? i + 1 : i;   // Increment i up to iMax
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void transmitByte(byte b) {
            circArrayBuffer[circArrayWriteIndex] = b;
            circArrayWriteIndex = (circArrayWriteIndex + 1) % circArraySize;
        }
    }

    public static void main(String[] args) {
        TwoWaySerialComm serialComm;
        try {
            serialComm = new TwoWaySerialComm("/dev/cu.ECE5-COM0");

            // Test Code:
            // byte primitive is signed and has a range -128 < b < 127 inclusive
            while (true) {
                System.out.print("> ");
                byte testByte = (byte) System.in.read();
                serialComm.writer.transmitByte(testByte);   // Send a 0x00 to the serial port
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}