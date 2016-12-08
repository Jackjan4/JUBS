package de.timetoerror.jabs.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Jackjan
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface module
{
    // The full name of the plugin
    String name() default "Unnamed module";
    
    // The version number of the plugin, presented as a full increasing number
    int version() default 0;
    
    // The version given as a String, can be a full qualified name or the version number
    String versionName() default "0";
    
    // Firsts do not need to listen on client keywords, they will get all messages
    // Their dependencies will be ignored
    boolean first() default false;
    
    // Lasts will close the stream, however, they need to listen on keywords
    boolean last() default false;
    
    // The name of the ModuleInfo this ModuleInfo depends on
    String dependency() default "";
    
    // The commands the ModuleInfo should listen on
    String[] cmd() default {""};
    
    // The client requests the ModuleInfo should listen on
    String[] req() default {""};
    
    // Indicates if this ModuleInfo is a read only plugin and cannot write to the server answer stream
    boolean readOnly() default false;
    
}
