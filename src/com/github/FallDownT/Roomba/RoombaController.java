package com.github.FallDownT.Roomba;

/**
 * Manages the interaction between the view and the user through the model equations
 */
public class RoombaController {

    private RoombaModel model;
    private RoombaView view;

    /**
     * Default constructor.
     */
    public RoombaController() {
        model = new RoombaModel();
        view = new RoombaView();
    }

    /**
     * Overloaded constructor.
     *
     * @param m RoombaModel Object to use.
     * @param v RoombaView Object to use.
     */
    public RoombaController(RoombaModel m, RoombaView v) {
        model = m;
        view = v;
    }

    /**
     * Controls and manages the MVC implementation.
     */
    public void control() {

    }
}
