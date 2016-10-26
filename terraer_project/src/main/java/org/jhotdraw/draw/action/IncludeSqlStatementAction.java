/*
 * @(#)SelectSameAction.java  1.1  2006-06-05
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

package org.jhotdraw.draw.action;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.jhotdraw.draw.AtributoDerivadoFigure;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
/**
 * SelectSameAction.
 *
 * @author  Werner Randelshofer
 * @version 1.1 2006-06-05 Optimized performance.
 * <br>1.0 25. November 2003  Created.
 */
public class IncludeSqlStatementAction extends AbstractSelectedAction {
	
    /** Creates a new instance. */
    public IncludeSqlStatementAction(DrawingEditor editor) {
        super(editor);
        putValue(AbstractAction.NAME, labels.getString("includeSqlStatement"));
        this.setEnabled(false);
        //  putValue(AbstractAction.MNEMONIC_KEY, labels.getString("editSelectSameMnem"));
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	includeSQLStatement();
    }
    
    public void includeSQLStatement() {    	
    	DrawingView v = this.getView();
    	if (v.getSelectedFigures() != null &&
    		v.getSelectedFigures().size() == 1 &&
    		v.getSelectedFigures().toArray()[0] instanceof AtributoDerivadoFigure){
    		AtributoDerivadoFigure att = (AtributoDerivadoFigure) v.getSelectedFigures().iterator().next();
    		String sql = JOptionPane.showInputDialog("SQL:",att.getSql());
    		att.setSql(sql);
    	}
    }
}
