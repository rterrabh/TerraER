/*
 * @(#)OSXTogglePaletteAction.java  1.0  June 11, 2006
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

package org.jhotdraw.application.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;

import org.jhotdraw.application.AbstractOSXApplication;

import application.ApplicationContext;

/**
 * OSXTogglePaletteAction.
 *
 * @author Werner Randelshofer.
 * @version 1.0 June 11, 2006 Created.
 */
public class OSXTogglePaletteAction extends AbstractAction {
    private Window palette;
    private WindowListener windowHandler;
    
    /** Creates a new instance. */
    public OSXTogglePaletteAction(Window palette, String label) {
        super(label);
        
        windowHandler = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                putValue(Actions.SELECTED_KEY, false);
            }
        };
        
        setPalette(palette);
        putValue(Actions.SELECTED_KEY, true);
    }
    
    public void putValue(String key, Object newValue) {
        super.putValue(key, newValue);
        if (key == Actions.SELECTED_KEY) {
            if (palette != null) {
                AbstractOSXApplication application = getApplication();
                boolean b = (Boolean) newValue;
                if (b) {
                    application.addPalette(palette);
                    palette.setVisible(true);
                } else {
                    application.removePalette(palette);
                    palette.setVisible(false);
                }
            }
        }
    }
    
    public void setPalette(Window newValue) {
        AbstractOSXApplication application = getApplication();
        if (palette != null) {
            palette.removeWindowListener(windowHandler);
        }
        
        palette = newValue;
        
        if (palette != null) {
            palette.addWindowListener(windowHandler);
            if (getValue(Actions.SELECTED_KEY) == Boolean.TRUE) {
                application.addPalette(palette);
                palette.setVisible(true);
            } else {
                application.removePalette(palette);
                palette.setVisible(false);
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        if (palette != null) {
            putValue(Actions.SELECTED_KEY, ! palette.isVisible());
        }
    }
    public AbstractOSXApplication getApplication() {
        return (AbstractOSXApplication) ApplicationContext.getInstance().getApplication();
    }
}
