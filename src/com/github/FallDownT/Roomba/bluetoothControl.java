package com.github.FallDownT.Roomba;

import ch.aplu.xboxcontroller.XboxController;
import ch.aplu.xboxcontroller.XboxControllerAdapter;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Controls the Roomba system by interfacing between the user and the RoombaView.
 */
public class bluetoothControl implements BluetoothObserver {

    private PrintStream transmit;
    private BufferedReader receive;
    private boolean connected;
    private double motor1dir, motor2dir;
    private String motor1, motor2;
    private RoombaView view;

    /**
     * Overloaded constructor which initializes data members and the GUI.
     *
     * @param v The RoombaView object to assign.
     * @throws IOException Bad IO.
     */
    public bluetoothControl(RoombaView v) throws IOException {
        StreamConnection conn = (StreamConnection) Connector.open("btspp://00A09618B2D3:1");

        transmit = new PrintStream(conn.openOutputStream());
        receive = new BufferedReader(new InputStreamReader(conn.openInputStream()));
        motor1dir = 0;
        motor2dir = 0;
        motor1 = null;
        motor2 = null;
        (new Thread(new BluetoothReader(receive, this))).start();   // Start the BluetoothReader thread
        view = v;
    }

    /**
     * Use incPacket of bytes to update the xCoord and yCoord.
     *
     * @param incPacket Array of data with size of 4.  Data is {'A', angle, angleIndex, radius}.
     */
    @Override
    public void update(byte[] incPacket) {
        int angle = incPacket[1];
        int angleIndex = incPacket[2];
        int radius = incPacket[3];
        double xCoord = view.getDrawingPanel().getX(radius, angle);
        double yCoord = view.getDrawingPanel().getY(radius, angle);
        System.out.println("incPacket = " + incPacket.toString() + "\t Coordinate " + angleIndex + " = (" + xCoord + ", " + yCoord + ")");
        view.getDrawingPanel().setPoint(angleIndex, (int) xCoord, (int) yCoord);
    }

    /**
     * Method which verifies a connected controller and instantiates the bluetooth listener and the controller listener.
     * This method controls essentially controls the system after initialization.
     */
    public void control() {
        XboxController controller = new XboxController("C:\\Users\\ulab\\IdeaProjects\\xboxcontrollertest\\src\\xboxcontroller.dll", 1, 50, 50);

        if (!controller.isConnected()) {
            System.out.println("*Connect a controller*");
            connected = false;
            while (!connected) {
                if (controller.isConnected()) {
                    System.out.println("*Controller Connected*");
                    connected = true;
                }
            }
        } else {
            System.out.println("*Controller Connected*");
        }


        view.getDrawingPanel().setPoint(15, 150, 288);
        view.getDrawingPanel().setPoint(31, 310, 100);
        view.getDrawingPanel().setPoint(14, 140, 260);

        controller.addXboxControllerListener(new XboxControllerAdapter() {
            public void isConnected(boolean b) {
                connected = b;
            }

            public void leftTrigger(double value) {
            }

            public void rightTrigger(double value) {
            }

            public void leftThumbDirection(double direction) {
                motor1dir = direction;
            }

            public void rightThumbDirection(double direction) {
                motor2dir = direction;
            }

            public void leftThumbMagnitude(double magnitude) {

                if (motor1dir < 60 || motor1dir > 300)
                    if (magnitude == 0) {
                        motor1 = "A4";
                    } else if (magnitude > 0.80) {
                        motor1 = "A9";
                    } else if (magnitude > 0.70) {
                        motor1 = "A8";
                    } else if (magnitude > 0.60) {
                        motor1 = "A7";
                    } else if (magnitude > 0.40) {
                        motor1 = "A6";
                    } else if (magnitude > 0.20) {
                        motor1 = "A5";
                    } else {
                        motor1 = "A4";
                    }

                else if (motor1dir < 240 || motor1dir > 120) {
                    if (magnitude == 0) {
                        motor1 = "A4";
                    } else if (magnitude > 0.80) {
                        motor1 = "A0";
                    } else if (magnitude > 0.60) {
                        motor1 = "A1";
                    } else if (magnitude > 0.40) {
                        motor1 = "A2";
                    } else if (magnitude > 0.20) {
                        motor1 = "A3";
                    } else {
                        motor1 = "A4";
                    }

                }

                transmit.println(motor1);
            }

            public void rightThumbMagnitude(double magnitude) {

                if (motor2dir < 60 || motor2dir > 300)
                    if (magnitude == 0) {
                        motor2 = "B4";
                    } else if (magnitude > 0.80) {
                        motor2 = "B9";
                    } else if (magnitude > 0.70) {
                        motor2 = "B8";
                    } else if (magnitude > 0.60) {
                        motor2 = "B7";
                    } else if (magnitude > 0.40) {
                        motor2 = "B6";
                    } else if (magnitude > 0.20) {
                        motor2 = "B5";
                    } else {
                        motor2 = "B4";
                    }

                else if (motor2dir < 240 || motor2dir > 120) {
                    if (magnitude == 0) {
                        motor2 = "B4";
                    } else if (magnitude > 0.80) {
                        motor2 = "B0";
                    } else if (magnitude > 0.60) {
                        motor2 = "B1";
                    } else if (magnitude > 0.40) {
                        motor2 = "B2";
                    } else if (magnitude > 0.20) {
                        motor2 = "B3";
                    } else {
                        motor2 = "B4";
                    }

                }

                transmit.println(motor2);
            }

            public void buttonA(boolean pressed) {
            }

            public void buttonB(boolean pressed) {
            }

            public void buttonX(boolean pressed) {
            }

            public void buttonY(boolean pressed) {
            }

            public void back(boolean pressed) {
            }

            public void start(boolean pressed) {
            }

            public void leftShoulder(boolean pressed) {
            }

            public void rightShoulder(boolean pressed) {
            }

            public void leftThumb(boolean pressed) {
            }

            public void rightThumb(boolean pressed) {
            }

            public void dpad(int direction, boolean pressed) {
            }
        });

    }
}
