/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.timetoerror.jabs.ui;

import java.io.Console;



/**
 *
 * @author Jackjan
 */
public class ConsoleListener implements Runnable
{

    private boolean running;
    private static ConsoleListener obj;

    private ConsoleListener() {
        running = true;
    }

    public static ConsoleListener getInstance() {
        if (obj == null) {
            return obj = new ConsoleListener();
        }
        return obj;
    }
    
    public void start()
    {
        Thread t = new Thread(this);
        t.setName("ConsoleListener Thread");
        t.start();
    }

    @Override
    public void run() {
        while(running)
        {
            
            Console con = System.console();
            if (con != null)
            {
                System.out.print("JUBS:> ");
                con.readLine();
            }
            
            // Process
        }
    }

    public void stop() {
        running = false;
    }
}
