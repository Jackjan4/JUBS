/*
 * Copyright 2016, Jan-Philipp Roslan, Alle Rechte vorbehalten
 */
package de.timetoerror.jabs;

import de.timetoerror.jputils.conf.Configuration;

/**
 *
 * @author Jackjan
 */
public class FirstStartHandler
{
    
    public static boolean isFirstStart()
    {
        return !Configuration.getInstance(false).exists();
    }
    
    public static void executeFirstStart()
    {
        
    }
}
