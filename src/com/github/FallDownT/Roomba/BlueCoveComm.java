package com.github.FallDownT.Roomba;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 * User: kevin
 * Date: 4/23/13
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlueCoveComm {
    public static void main(String args[]) {

        try {
            StreamConnection conn = (StreamConnection) Connector.open("btspp://00A09618B2D3:1");

            PrintStream out = new PrintStream(
                    conn.openOutputStream());

            out.println("A1");

            conn.close();
        } catch (IOException e) {
            System.err.print(e.toString());
        }
    }
}
