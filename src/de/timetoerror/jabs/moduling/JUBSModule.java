package de.timetoerror.jabs.moduling;

import de.timetoerror.jabs.Environment;
import de.timetoerror.jabs.net.ClientRequest;
import de.timetoerror.jabs.ui.ConsoleRequest;

/**
 * Module.java represents the template for a module. 
 * Every concrete module needs to extend this class to function as a JASB module.
 * JASB itself needs this class to call the abstract functions
 * @author Jackjan
 */
public abstract class JUBSModule {
    
    private Environment env;
    
    
    /**
     * Gets called when the module is loaded.
     * Necessary attributes or fields for working should be initialized here, since
     * modules don't have constructors.
     */
    protected abstract void init();
    
    /**
     * Gets called when a client connection is procedured in this module with a request command
     * that is listened by this module
     * 
     * @param req 
     */
    protected abstract void process(ClientRequest req);
    
    /**
     * Gets called when a client connection is procedured in this module with a request command
     * that is listened by this module
     * 
     * @param req 
     */
    protected abstract void process(ConsoleRequest req);
    
}
