/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.timetoerror.jabs;

import de.timetoerror.jabs.moduling.ModuleManager;
import de.timetoerror.jabs.net.ConnectionManager;
import de.timetoerror.jabs.ui.ConsoleListener;

/**
 *
 * @author Jackjan
 */
public class Bootloader
{

    public Bootloader() {

    }

    public void run() {
        System.out.println("=== JUBS ===");
        System.out.println("=== Java Universal Backend Server ===\n");
        System.out.println("JUBS ist starting...\n");
        
        // Init EnvironmentManager
        Environment env = new Environment();
        env.init();

        // Check for first startup
        if (FirstStartHandler.isFirstStart()) {
            System.out.println("This JUBS installation is started for the first time!");
            FirstStartHandler.executeFirstStart();
        }

        
        // Init MooduleManager
        initModuleManager();
        System.out.println("ModuleManager initialized");

        // Init ConsoleListener
        ConsoleListener con = ConsoleListener.getInstance();
        con.start();

        // Init ConnectionManager
        ConnectionManager man = new ConnectionManager(1337);
        man.run();

    }

    private ModuleManager initModuleManager() {
        System.out.println("Init ModuleManager...");
        
        System.out.println("Module path: " + Environment.getInstance().getModulePath());
        
        return ModuleManager.getInstance(Environment.getInstance().getModulePath());
    }
}
