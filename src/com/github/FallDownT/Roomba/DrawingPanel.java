package com.github.FallDownT.Roomba;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Extension of JPanel which uses Graphics2d
 */
public class DrawingPanel extends JPanel {

    int numPoints, numParams;
    double[][] points;
    double[][] fieldOfVision;
    GeneralPath fovConnect, pointConnect;

    /**
     * Default constructor which assigns points.
     */
    public DrawingPanel() {
        numPoints = 64;
        numParams = 2;
        points = new double[2 * numPoints][numParams];
        fieldOfVision = new double[2 * numPoints][numParams];

        fovConnect = new GeneralPath();
        pointConnect = new GeneralPath();

        // Create all points
        for (int i = 0; i < 2 * numPoints; i++) {
            double angle;
            if (i < numPoints) {
                angle = Math.PI - Math.PI * i / (numPoints - 1);
                fieldOfVision[i][0] = 320 + 43 * getX(1, angle);
                fieldOfVision[i][1] = 320 - 43 * getY(1, angle);
            } else {
                angle = Math.PI * (i - numPoints) / (numPoints - 1);
                fieldOfVision[i][0] = 320 + 319 * getX(1, angle);
                fieldOfVision[i][1] = 320 - 319 * getY(1, angle);
            }
            points[i][0] = 320 + 319 * getX(1, angle);
            points[i][1] = 320 - 319 * getY(1, angle);
//            System.out.println("FOV   " + i + ": (" + fieldOfVision[i][0] + "," + fieldOfVision[i][1] + ")  \tangle = " + angle);
//            System.out.println("Point " + i + ": (" + points[i][0] + "," + points[i][1] + ")  \tangle = " + angle);
        }

    }

    /**
     * Override the paint component of the drawing panel.
     *
     * @param g Graphics object of this Object.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw and fill default robot field of vision
        fovConnect.moveTo(fieldOfVision[0][0], fieldOfVision[0][1]);    // Set starting point
        for (int i = 1; i < 2 * numPoints; i++) {                          // Connect all points
            fovConnect.lineTo(fieldOfVision[i][0], fieldOfVision[i][1]);
        }
        fovConnect.closePath();     // Close path
        g2.setColor(Color.GREEN);   // Set color of Fill to Green
        g2.fill(fovConnect);        // Fill the shape

        // Draw and fill points
        pointConnect.moveTo(points[0][0], points[0][1]);    // Set starting point
        for (int i = 1; i < 2 * numPoints; i++) {              // Connect all points
            pointConnect.lineTo(points[i][0], points[i][1]);
        }
        pointConnect.closePath();       // Close path
        g2.setColor(Color.DARK_GRAY);   // Set color of Fill to Gray
        g2.fill(pointConnect);          // Fill the shape

        // Draw and fill Robot Rectangle
        g2.setColor(Color.RED);
        g2.fillRoundRect(293, 320, 54, 76, 10, 10);

        // Write Label on the Robot
        g2.setColor(Color.BLACK);
        g2.drawString("Roomba", 296, 360);

    }

    /**
     * Reassign a point on the screen by point index.
     *
     * @param pointIndex The index of the point to be changed.  Range 0 - (numPoints-1).
     * @param newX       New x cartesian coordinate to set.
     * @param newY       New y cartesian coordinate to set.
     */
    public void setPoint(int pointIndex, int newX, int newY) {
        points[pointIndex][0] = newX;
        points[pointIndex][1] = newY;
    }

    /**
     * Get the x 2D cartesian coordinate of a polar 2D coordinate.
     *
     * @param r     Input radius in from 0-1.
     * @param angle Input angle from 0-2pi
     * @return Double representing the x 2D cartesian coordinate.
     */
    public double getX(double r, double angle) {
        return r * Math.cos(angle);
    }

    /**
     * Get the y 2D cartesian coordinate of a polar 2D coordinate.
     *
     * @param r     Input radius in from 0-1.
     * @param angle Input angle from 0-2pi
     * @return Double representing the y 2D cartesian coordinate.
     */
    public double getY(double r, double angle) {
        return r * Math.sin(angle);
    }
}