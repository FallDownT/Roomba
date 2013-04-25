package com.github.FallDownT.Roomba;

import javax.swing.*;
import java.io.IOException;

/**
 * Creates the Roomba controller and passes it a model and view
 */
public class RoombaMain {

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
                    e.printStackTrace();
                }
            }
        });

    }

}
