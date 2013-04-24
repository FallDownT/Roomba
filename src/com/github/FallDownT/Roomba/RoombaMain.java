package com.github.FallDownT.Roomba;

import javax.swing.*;

/**
 * Creates the Roomba controller and passes it a model and view
 */
public class RoombaMain {

    public static void main(String args[]) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RoombaModel model = new RoombaModel();
                RoombaView view = new RoombaView();
                RoombaController controller = new RoombaController(model, view);

                // Transfer Control
                controller.control();
            }
        });

    }

}
