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
    private double motor1dir, motor2dir, leftStickMag, rightStickMag;
    private int motorBase, motor1Offset, motor2Offset, motor1Mag, motor2Mag;
    private String motor1, motor2;
    private RoombaView view;

    /**
     * Overloaded constructor which initializes data members and the GUI.
     *
     * @param v The RoombaView object to assign.
     * @throws IOException Bad IO.
     */
    public bluetoothControl(RoombaView v) throws IOException {
//        StreamConnection conn = (StreamConnection) Connector.open("btspp://00A09618B2D3:1");    // ECE5
        StreamConnection conn = (StreamConnection) Connector.open("btspp://00a0961009d2:1");    // Roomba

        transmit = new PrintStream(conn.openOutputStream());
        receive = new BufferedReader(new InputStreamReader(conn.openInputStream()));
        motor1dir = motor2dir = leftStickMag = rightStickMag = 0;
        motor1 = motor2 = null;
        motorBase = motor1Offset = motor2Offset = motor1Mag = motor2Mag = 0;
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
        double angleIndex = (incPacket[1] + 128) / 4;
        double angle = Math.PI - (Math.PI * angleIndex / 63);
        double radius = (1 - (incPacket[2] / 127)) * 320;
        double xCoord = view.getDrawingPanel().getX(radius, angle);
        double yCoord = view.getDrawingPanel().getY(radius, angle);
//        view.getDrawingPanel().setPoint((int) angleIndex, (int) xCoord, (int) yCoord);

        // Debug statements
        System.out.println("incPacket[0] = " + incPacket[0] + "\tincPacket[1] = " + incPacket[1] + "\tincPacket[2] = " + incPacket[2]);
        System.out.println("\tRadius = " + radius + "\tAngleIndex = " + angleIndex + "\tAngle = " + angle);
        System.out.println("\tCoordinate " + angleIndex + " = (" + xCoord + ", " + yCoord + ")");
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
                recalcSpeed();
                recalcTurn();
                transmitToMotors();
            }

            public void rightThumbDirection(double direction) {
                motor2dir = direction;
                recalcSpeed();
                recalcTurn();
                transmitToMotors();
            }

            public void leftThumbMagnitude(double magnitude) {
                leftStickMag = magnitude;
                recalcSpeed();
                recalcTurn();
                transmitToMotors();
            }

            public void rightThumbMagnitude(double magnitude) {
                rightStickMag = magnitude;
                recalcSpeed();
                recalcTurn();
                transmitToMotors();
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

            /**
             * Recalculate the motor speed.
             */
            private void recalcSpeed() {
                if (motor1dir < 60 || motor1dir > 300) {    // Move forward
                    if (leftStickMag > 0.80) {
                        motorBase = 9;
                        view.setArrowLight("up", true);
                        view.setArrowLight("down", false);
                    } else if (leftStickMag > 0.70) {
                        motorBase = 8;
                        view.setArrowLight("up", true);
                        view.setArrowLight("down", false);
                    } else if (leftStickMag > 0.60) {
                        motorBase = 7;
                        view.setArrowLight("up", true);
                        view.setArrowLight("down", false);
                    } else if (leftStickMag > 0.40) {
                        motorBase = 6;
                        view.setArrowLight("up", true);
                        view.setArrowLight("down", false);
                    } else if (leftStickMag > 0.20) {
                        motorBase = 5;
                        view.setArrowLight("up", true);
                        view.setArrowLight("down", false);
                    } else if (leftStickMag <= 0.20) {
                        motorBase = 4;
                        view.setArrowLight("up", false);
                        view.setArrowLight("down", false);
                    }
                } else if (motor1dir < 240 && motor1dir > 120) {  // Move backward
                    if (leftStickMag > 0.80) {
                        motorBase = 0;
                        view.setArrowLight("up", false);
                        view.setArrowLight("down", true);
                    } else if (leftStickMag > 0.60) {
                        motorBase = 1;
                        view.setArrowLight("up", false);
                        view.setArrowLight("down", true);
                    } else if (leftStickMag > 0.40) {
                        motorBase = 2;
                        view.setArrowLight("up", false);
                        view.setArrowLight("down", true);
                    } else if (leftStickMag > 0.20) {
                        motorBase = 3;
                        view.setArrowLight("up", false);
                        view.setArrowLight("down", true);
                    } else if (leftStickMag <= 0.20) {
                        motorBase = 4;
                        view.setArrowLight("up", false);
                        view.setArrowLight("down", false);
                    }
                }
            }

            /**
             * Recalculate the motor offsets
             */
            private void recalcTurn() {
                if (motor2dir < 330 && motor2dir > 210) {    // Turn left
                    if (rightStickMag > 0.80) {
                        motor1Offset = -2;
                        motor2Offset = 2;
                        view.setArrowLight("left", true);
                        view.setArrowLight("right", false);
                    } else if (rightStickMag > 0.60) {
                        motor1Offset = -1;
                        motor2Offset = 2;
                        view.setArrowLight("left", true);
                        view.setArrowLight("right", false);
                    } else if (rightStickMag > 0.40) {
                        motor1Offset = -1;
                        motor2Offset = 1;
                        view.setArrowLight("left", true);
                        view.setArrowLight("right", false);
                    } else if (rightStickMag > 0.20) {
                        motor1Offset = 0;
                        motor2Offset = 1;
                        view.setArrowLight("left", true);
                        view.setArrowLight("right", false);
                    } else if (rightStickMag <= 0.20) {
                        motor1Offset = 0;
                        motor2Offset = 0;
                        view.setArrowLight("left", false);
                        view.setArrowLight("right", false);
                    }
                } else if (motor2dir < 150 && motor2dir > 30) {  // Turn right
                    if (rightStickMag > 0.80) {
                        motor1Offset = 2;
                        motor2Offset = -2;
                        view.setArrowLight("left", false);
                        view.setArrowLight("right", true);
                    } else if (rightStickMag > 0.60) {
                        motor1Offset = 2;
                        motor2Offset = -1;
                        view.setArrowLight("left", false);
                        view.setArrowLight("right", true);
                    } else if (rightStickMag > 0.40) {
                        motor1Offset = 1;
                        motor2Offset = -1;
                        view.setArrowLight("left", false);
                        view.setArrowLight("right", true);
                    } else if (rightStickMag > 0.20) {
                        motor1Offset = 1;
                        motor2Offset = 0;
                        view.setArrowLight("left", false);
                        view.setArrowLight("right", true);
                    } else if (rightStickMag <= 0.20) {
                        motor1Offset = 0;
                        motor2Offset = 0;
                        view.setArrowLight("left", false);
                        view.setArrowLight("right", false);
                    }
                }
            }

            /**
             * Calculate motor magnitudes and transmit to motors.
             */
            private void transmitToMotors() {
                // Calculate motor magnitudes based on base speed and offset
                motor1Mag = motorBase + motor1Offset;
                motor2Mag = motorBase + motor2Offset;

                // Adjust motor magnitudes to be within acceptable range
                while (motor1Mag < 0) {  // Increment motor1Mag to a value above 0
                    motor1Mag++;
                }
                while (motor1Mag > 9) {  // Increment motor1Mag to a value above 0
                    motor1Mag--;
                }
                while (motor2Mag < 0) {  // Increment motor2Mag to a value below 9
                    motor2Mag++;
                }
                while (motor2Mag > 9) {  // Increment motor2Mag to a value below 9
                    motor2Mag--;
                }

                // Assign Strings for Bluetooth transmission
                motor1 = "A" + motor1Mag;
                motor2 = "B" + motor2Mag;

                // Transmit motor adjustment commands via Bluetooth
                transmit.println(motor1);
                transmit.println(motor2);
            }
        });

    }
}
