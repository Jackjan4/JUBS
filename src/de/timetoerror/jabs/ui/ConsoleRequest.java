/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.timetoerror.jabs.ui;

import java.util.HashMap;

/**
 *
 * @author jackjan
 */
public class ConsoleRequest {

    private final String command;
    private final HashMap<String, String> params;

    public ConsoleRequest(String command, String[] params, String... args) {
        this.command = command;
        this.params = new HashMap<>();

        for (int i = 0; i < params.length; i++) {
            this.params.put(params[i], args[i]);
        }
    }

    public String getCommand() {
        return command;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

}
