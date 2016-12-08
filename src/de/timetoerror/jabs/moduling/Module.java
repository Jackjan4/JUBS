package de.timetoerror.jabs.moduling;

import de.timetoerror.jabs.Environment;
import de.timetoerror.jabs.net.ClientRequest;
import de.timetoerror.jputils.reflection.ReflectionUtils;
import java.lang.reflect.Constructor;
import de.timetoerror.jabs.annot.module;
import de.timetoerror.jabs.ui.ConsoleRequest;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Jackjan
 */
public class Module {

    // The class object of the mod
    private final Class mod;

    // The instanc object of the mod
    private final Object instance;
    private final int version;
    private final String name;
    private final String[] listenedCmds;
    private final String[] listenedReqs;
    private final String dependency;
    private final boolean first;
    private boolean last;
    private final boolean readOnly;

    public Module(Class c) {
        mod = c;

        // Init all properties
        instance = initInstance(mod);
        
        name = ((module) mod.getDeclaredAnnotation(module.class)).name();
        version = ((module) mod.getDeclaredAnnotation(module.class)).version();
        
        first = ((module) mod.getDeclaredAnnotation(module.class)).first();
        
        dependency = ((module) mod.getDeclaredAnnotation(module.class)).dependency();
        
        listenedReqs = ((module) mod.getDeclaredAnnotation(module.class)).req();
        
        listenedCmds = ((module) mod.getDeclaredAnnotation(module.class)).cmd();
        
        readOnly = ((module) mod.getDeclaredAnnotation(module.class)).readOnly();
    }

    /**
     * Inits the instance object of the class
     *
     * @param c
     * @return
     */
    private Object initInstance(Class c) {
        try {
            Constructor con = mod.getDeclaredConstructor();
            return c.newInstance();
        } catch (Exception ex) {
            System.out.println("Error: ");
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Initializes this by injecting the Environment and calling the "init"-Method
     */
    public void initModule() {
        injectEnv();
        try {
            ReflectionUtils.callMethod(mod, "init", instance, null, null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex) {
            
        }

    }

    /**
     * Injects the MySQLDB
     */
    private void injectEnv() {
        Environment env = Environment.getInstance();

        ReflectionUtils.injectField(mod, "env", instance, env);
    }

    /**
     * Executes the given cmd in the Module
     * @param cmd 
     */
    public void exeCmd(String cmd) {
        try {
            ReflectionUtils.callMethod(mod, "exeCmd", instance, new Class[]{String.class}, cmd);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex) {
            
        }
    }

    /**
     * Processess a ClientRequest in this Module
     * @param request 
     */
    public void process(ClientRequest request) {
        try {
            ReflectionUtils.callMethod(mod, "process", instance, new Class[]{ClientRequest.class}, request);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex) {
            
        }
    }
    
    
    /**
     * Processess a ConsoleRequest in this Module
     * @param request 
     */
    public void process(ConsoleRequest request) {
        try {
            ReflectionUtils.callMethod(mod, "process", instance, new Class[]{ConsoleRequest.class}, request);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex) {
            
        }
    }

    public String getName() {
        return name;
    }

    public String getDependency() {
        return dependency;
    }

    public boolean isFirst() {
        return first;
    }
    
    public String[] getListenedReqs()
    {
        return listenedReqs;
    }

    public String[] getListenedCmds() {
        return listenedCmds;
    }
    
    

    public int getVersion() {
        return version;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
    
    
    
    
    

}
