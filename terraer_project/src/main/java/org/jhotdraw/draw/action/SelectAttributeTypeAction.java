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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.enums.AttributeTypeEnum;
import org.jhotdraw.interfaces.AttributeTypeElement;
/**
 * SelectSameAction.
 *
 * @author  Werner Randelshofer
 * @version 1.1 2006-06-05 Optimized performance.
 * <br>1.0 25. November 2003  Created.
 */
public class SelectAttributeTypeAction extends AbstractSelectedAction {
	
    /** Creates a new instance. */
    public SelectAttributeTypeAction(DrawingEditor editor) {
        super(editor);
        putValue(AbstractAction.NAME, labels.getString("editSelectAttributeType"));
        this.setEnabled(false);
        //  putValue(AbstractAction.MNEMONIC_KEY, labels.getString("editSelectSameMnem"));
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	selectAttributeType();
    }
    
    public void selectAttributeType() {
    	DrawingView v = this.getView();
    	if (v.getSelectedFigures() != null &&
    		v.getSelectedFigures().size() == 1 &&
    		v.getSelectedFigures().toArray()[0] instanceof AttributeTypeElement){
    		AttributeTypeElement att = (AttributeTypeElement) v.getSelectedFigures().iterator().next();
    		//String[] list = {"A", "B", "C"};
    		AttributeTypeEnum[] list = {AttributeTypeEnum.CHAR,
    						AttributeTypeEnum.DATE,
    						AttributeTypeEnum.INTEGER,
    						AttributeTypeEnum.NUMBER,
    						AttributeTypeEnum.TEXT
    		};
    		
    		JCheckBox chkNull = new JCheckBox(labels.getString("type.notnull"));
    		chkNull.setSelected(!att.isNullable());
    		
    		JComboBox<AttributeTypeEnum> jcbType = new JComboBox<AttributeTypeEnum>(list);
    		jcbType.setEditable(false);
    		
    		if (att.getAttributeType() != null){
    			jcbType.setSelectedItem(att.getAttributeType());
    		}
    		if (JOptionPane.showConfirmDialog( null, new Object[]{jcbType,chkNull}, "Select the attribute type", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
    			att.setAttributeType((AttributeTypeEnum)jcbType.getSelectedItem());
    			att.setNullable(!chkNull.isSelected());
    		}
    		
    	}
    }
}
