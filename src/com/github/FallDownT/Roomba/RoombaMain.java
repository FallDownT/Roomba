package com.github.FallDownT.Roomba;

import javax.swing.*;
import java.io.IOException;

/**
 * Class containing the main method.  No other function.
 */
public class RoombaMain {

    /**
     * Creates the bluetoothControl and passes it a RoombaView.
     */
    public static void main(String args[]) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RoombaView view = new RoombaView();
                bluetoothControl controller;

                try {
                    controller = new bluetoothControl(view);

                    // Transfer Control
                    controller.control();

                } catch (IOException e) {
                    System.out.println("Bad IO Bro!");
                    e.printStackTrace();
                }
            }
        });

    }

}
