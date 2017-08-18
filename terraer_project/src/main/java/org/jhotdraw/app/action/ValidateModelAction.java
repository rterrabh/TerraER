/*
 * @(#)CutAction.java  2.0  2007-04-13
 *
 * Copyright (c) 1996-2007 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */

package org.jhotdraw.app.action;

import java.awt.event.ActionEvent;

import org.jhotdraw.app.Application;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.samples.draw.DrawProject;
import org.jhotdraw.util.ResourceBundleUtil;
/**
 * Cuts the selected region and places its contents into the system clipboard.
 * Acts on the EditableComponent or JTextComponent which had the focus when
 * the ActionEvent was generated.
 *
 * @author Werner Randelshofer
 * @version 2.0 2007-04-13 Use javax.swing.TransferHandler instead of 
 * interface EditableComponent. 
 * <br>1.0 October 9, 2005 Created.
 */
public class ValidateModelAction extends AbstractProjectAction {
    public final static String ID = "validateModel";
    public final static String ID2 = "validateModel2";
   
    /** Creates a new instance. */
    public ValidateModelAction(Application app) {
    	super(app);
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, ID2);
        
    }
    
    public void actionPerformed(ActionEvent evt) {
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
    	
    	if (LineConnectionFigure.validateEnabled){
    		//labels.configureAction(this, ID2);
    		LineConnectionFigure.validateEnabled = false;
    	}else{
    		//labels.configureAction(this, ID);
    		LineConnectionFigure.validateEnabled = true;
    	}    	
    	
    	DrawProject project = (DrawProject) getCurrentProject();
    	for (Figure f : project.getEditor().getActiveView().getDrawing().getFigures()){
    		if (f instanceof LineConnectionFigure){
    			((LineConnectionFigure) f).updateConnection();
    		}
    	}
    	
    }
}
