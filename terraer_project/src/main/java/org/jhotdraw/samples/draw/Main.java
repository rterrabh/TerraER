/*
 * @(#)Main.java  1.0  June 10, 2006
 *
 * Copyright (c) 1996-2006 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */

package org.jhotdraw.samples.draw;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.DefaultOSXApplication;
import org.jhotdraw.app.DefaultSDIApplication;
import org.jhotdraw.util.ResourceBundleUtil;
/**
 * Main.
 *
 * @author Werner Randelshofer.
 * @version 1.0 June 10, 2006 Created.
 */
public class Main {
    
    /** Creates a new instance. */
    public static void main(String[] args) {
        Application app;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("mac")) {
        	String version = System.getProperty("java.version");
        	//System.out.println(version);
        	if (version.compareTo("1.8")>=0){
        	    app = new DefaultSDIApplication();
        		//app = new DefaultOSXApplication();
        	}else {
        	    app = new DefaultSDIApplication();
        		//app = new DefaultOSXApplication();
        	}
        } else if (os.startsWith("win")) {
            app = new DefaultSDIApplication();
        } else {
            app = new DefaultSDIApplication();
        }
        
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TerraER");
        
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
        DrawApplicationModel model = new DrawApplicationModel();
        model.setName("TerraER");
        model.setVersion("3.14");
        model.setCopyright(labels.getString("copyright"));
        model.setProjectClassName("org.jhotdraw.samples.draw.TerraProject");
        app.setModel(model);
        app.launch(args);
    }
    
}
