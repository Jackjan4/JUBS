/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.timetoerror.jabs.moduling;

import de.timetoerror.jabs.net.ClientRequest;
import de.timetoerror.jabs.ui.ConsoleRequest;
import de.timetoerror.jputils.io.FileUtils;
import de.timetoerror.jputils.reflection.JarUtils;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ModuleManager loads and manages the
 *
 * @author Jackjan
 */
public class ModuleManager {

    // The directory of the modules
    private final File dir;
    private static ModuleManager obj;

    // Modules - Saved by their name
    private final HashMap<String, Module> modulesByName;

    // Modules - Saved by their accepted requests
    private final HashMap<String, Module> modulesByReq;

    // Modules - Saved by their accepted console commands
    private final HashMap<String, Module> modulesByCmd;

    private Module first;

    private Module last;

    private ModuleManager(File p) {
        modulesByName = new HashMap<>();
        modulesByReq = new HashMap<>();
        modulesByCmd = new HashMap<>();

        dir = p;

        // Load modules into Memory
        loadModules(dir);

        // Init Modules
        initModules();
    }

    /**
     * Inits the modules by injection neccessary fields and calling their
     * init-Method
     */
    private void initModules() {
        modulesByName.forEach((k, v) -> {
            v.initModule();
        });
    }

    /**
     * Processes a client request in the modules
     *
     * @param req
     */
    public void processInModules(ClientRequest req) {

        // Process on first
        if (first != null) {
            first.process(req);
        }

        // Find out finish module by listened cmd
        ArrayDeque<Module> stack = new ArrayDeque<>();
        System.out.println("User had the following header: " + req.getMessageHeader());
        Module m = modulesByReq.get(req.getMessageHeader());

        // Get all needed dependencies
        while (m != null) {
            stack.addFirst(m);
            m = modulesByName.get(m.getDependency());
        }

        // Process in all needed modules
        while (!stack.isEmpty()) {
            Module proc = stack.removeFirst();
            proc.process(req);
        }
    }

    /**
     * Processes a console command in the modules
     *
     * @param req
     */
    public void processInModules(ConsoleRequest req) {

        // Process on first
        if (first != null) {
            first.process(req);
        }

        // Find out finish module by listened cmd
        ArrayDeque<Module> stack = new ArrayDeque<>();
        Module m = modulesByCmd.get(req.getCommand());

        m.process(req);
    }

    /**
     * Returns the instance of ModuleManager
     *
     * @param p
     * @return
     */
    public static ModuleManager getInstance(File p) {
        if (obj == null) {
            obj = new ModuleManager(p);
        }
        return obj;
    }

    public static ModuleManager getInstance() {
        if (obj == null) {
            return null;
        }
        return obj;
    }

    /**
     * Loads all modules and saves them to the Collection fields above
     */
    private void loadModules(File dir) {
        ArrayList<File> mods = listModules(dir);

        if (mods != null) {
            for (File f : mods) {
                Class c = loadModule(f.toPath(), JUBSModule.class);
                if (c != null) {
                    Module m = new Module(c);

                    modulesByName.put(m.getName(), m);

                    System.out.println("Loaded module: " + m.getName() + " " + m.getVersion());
                    // Check if module has first privileg
                    if (first == null) {
                        if (m.isFirst()) {
                            first = m;
                            continue;
                        }
                    }

                    for (String s : m.getListenedReqs()) {
                        modulesByReq.put(s, m);
                    }

                    for (String s : m.getListenedCmds()) {
                        modulesByCmd.put(s, m);
                    }
                }
            }
        }

    }

    /**
     * Scans and Loads a module by its main class and returns the main class for
     * further processing
     *
     * @param path - The path to the .jar-module. (Ending with .jar)
     * @return - The main class of the module or null if no class is found or an
     * IOException occurs
     */
    private Class loadModule(Path c, Class par) {
        return loadModule(c.toString(), par);
    }

    /**
     * Scans and Loads a module by its main class and returns the main class for
     * further processing
     *
     * @param path - The path to the .jar-module. (Ending with .jar)
     * @return - The main class of the module or null if no class is found or an
     * IOException occurs
     */
    private Class loadModule(String path, Class inter) {
        try {
            ArrayList<String> classes = JarUtils.getClasses(path);

            ClassLoader loader = new URLClassLoader(new URL[]{new URL("file:" + path)});
            for (String classe : classes) {
                Class c = loader.loadClass(classe);
                if (inter.isAssignableFrom(c)) {
                    return c;
                }
            }
        } catch (MalformedURLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Lists all modules available in the module folder
     *
     * @return
     */
    private ArrayList<File> listModules(File dir) {
        return FileUtils.getFilesInFolder(dir, ".jar");
    }

}
