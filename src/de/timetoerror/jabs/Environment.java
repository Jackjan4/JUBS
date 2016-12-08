/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.timetoerror.jabs;

import de.timetoerror.jputils.CommonUtils;
import java.io.File;

/**
 *
 * @author jackjan
 */
public final class Environment {

    private static Environment env;

    public static Environment getInstance() {
        if (env == null) {
            env = new Environment();
        }

        return env;
    }

    public Environment() {

    }

    public void init() {
        this.env = this;
    }

    /**
     * Returns the environment path
     *
     * @return
     */
    public File getEnvPath() {
        return new File(CommonUtils.getClassPath(this.getClass())).getParentFile();
    }

    /**
     * Returns the database path
     *
     * @return
     */
    public File getDbPath() {
        File f = new File(new File(CommonUtils.getClassPath(this.getClass())).getParent() + File.separator + "db");

        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    /**
     * Returns the database path
     *
     * @return
     */
    public File getStoragePath() {
         File f = new File(new File(CommonUtils.getClassPath(this.getClass())).getParent() + File.separator + "storage");

        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    /**
     * Returns the database path
     *
     * @return
     */
    public File getExtStoragePath() {
         File f = new File(new File(CommonUtils.getClassPath(this.getClass())).getParent() + File.separator + "storage");

        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    /**
     * Returns the database path
     *
     * @return
     */
    public File getModulePath() {
         File f = new File(new File(CommonUtils.getClassPath(this.getClass())).getParent() + File.separator + "modules" + File.separator);

        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }
    
    
    /**
     * Returns the logging path
     *
     * @return
     */
    public File getLogPath() {
         File f = new File(new File(CommonUtils.getClassPath(this.getClass())).getParent() + File.separator + "logs" + File.separator);

        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    public void callModule(String moduleName, String methodName, Object... paras) {

    }

}
