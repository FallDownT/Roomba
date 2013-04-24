package com.github.FallDownT.Roomba;

import javax.swing.*;
import java.awt.*;

/**
 * The GUI and user interface for project Roomba
 */
public class RoombaView {

    private JFrame frame;
    private JPanel workPanel;
    private DrawingPanel dPanel;
    private JLabel title;

    /**
     * Default constructor to create the GUI Frame and populate it.
     */
    public RoombaView() {
        // Create frame
        frame = new JFrame("Roomba - How does it know?..");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 540);
        frame.setVisible(true);

        // Define components
        workPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        dPanel = new DrawingPanel();
        dPanel.setPreferredSize(new Dimension(640, 400));
        dPanel.setMaximumSize(new Dimension(640, 400));
        dPanel.setMinimumSize(new Dimension(640, 400));

        title = new JLabel("~ Roomba! ~");
        title.setFont(new java.awt.Font("Comic Sans MS", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(640, 60));


        // Add components to frame
        workPanel.add(title, BorderLayout.NORTH);
        workPanel.add(dPanel, BorderLayout.CENTER);
        frame.setContentPane(workPanel);

        dPanel.setPoint(27, 270, 125);
        dPanel.setPoint(58, 600, 250);
    }
}
