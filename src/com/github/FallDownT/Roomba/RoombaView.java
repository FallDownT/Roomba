package com.github.FallDownT.Roomba;

import javax.swing.*;
import java.awt.*;

/**
 * The GUI and user interface for project Roomba
 */
public class RoombaView {

    private JFrame frame;
    private JPanel workPanel, topPanel, arrowPanel;
    private DrawingPanel dPanel;
    private JLabel title, upArrow, downArrow, leftArrow, rightArrow;

    /**
     * Default constructor to create the GUI Frame and populate it.
     */
    public RoombaView() {
        // Create frame
        frame = new JFrame("Roomba - How does it know?..");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 565);
        frame.setVisible(true);

        // Define components
        workPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel = new JPanel(new BorderLayout());
        arrowPanel = new JPanel(new BorderLayout());

        dPanel = new DrawingPanel();
        dPanel.setPreferredSize(new Dimension(640, 400));

        title = new JLabel("~ Roomba! ~");
        title.setFont(new java.awt.Font("Comic Sans MS", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setPreferredSize(new Dimension(500, 25));

        upArrow = new JLabel();
        upArrow.setHorizontalAlignment(SwingConstants.CENTER);
        upArrow.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        upArrow.setIcon(new ImageIcon(getClass().getResource("Images/arrowUpGray.png")));
        downArrow = new JLabel();
        downArrow.setHorizontalAlignment(SwingConstants.CENTER);
        downArrow.setIcon(new ImageIcon(getClass().getResource("Images/arrowDownGray.png")));
        leftArrow = new JLabel();
        leftArrow.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
        leftArrow.setIcon(new ImageIcon(getClass().getResource("Images/arrowLeftGray.png")));
        rightArrow = new JLabel();
        rightArrow.setIcon(new ImageIcon(getClass().getResource("Images/arrowRightGray.png")));

        // Add components to frame
        arrowPanel.add(upArrow, BorderLayout.NORTH);
        arrowPanel.add(downArrow, BorderLayout.SOUTH);
        arrowPanel.add(leftArrow, BorderLayout.WEST);
        arrowPanel.add(rightArrow, BorderLayout.EAST);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(arrowPanel, BorderLayout.EAST);

        workPanel.add(topPanel, BorderLayout.NORTH);
        workPanel.add(dPanel, BorderLayout.CENTER);

        frame.setContentPane(workPanel);
        frame.setResizable(false);

        dPanel.setPoint(27, 270, 125);
        dPanel.setPoint(58, 580, 250);
        dPanel.setPoint(28, 280, 130);
    }
}
