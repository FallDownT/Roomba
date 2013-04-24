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
 * Created with IntelliJ IDEA.
 * User: ulab
 * Date: 4/24/13
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class bluetoothControl {

    PrintStream transmit;
    BufferedReader receive;
    boolean connected;
    double motor1dir = 0;
    double motor2dir = 0;
    String motor1 = null;
    String motor2 = null;

    public static void main(String args[]) throws IOException {
        bluetoothControl btc = new bluetoothControl();
    }

    public bluetoothControl() throws IOException {

        StreamConnection conn = (StreamConnection) Connector.open("btspp://00A09618B2D3:1");

        transmit = new PrintStream(conn.openOutputStream());
        //BufferedWriter receive = new BufferedWriter(new OutputStreamWriter(conn.openOutputStream()));
        receive = new BufferedReader(new InputStreamReader(conn.openInputStream()));
        test();
    }

    public void test() {
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
                    } else if (magnitude > 0.30) {
                        motor1 = "A3";
                    } else if (magnitude > 0.60) {
                        motor1 = "A2";
                    } else if (magnitude > 0.80) {
                        motor1 = "A1";
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
                    } else if (magnitude > 0.30) {
                        motor2 = "B3";
                    } else if (magnitude > 0.60) {
                        motor2 = "B2";
                    } else if (magnitude > 0.80) {
                        motor2 = "B1";
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
