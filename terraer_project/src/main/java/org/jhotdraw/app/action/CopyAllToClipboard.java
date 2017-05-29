/*
 * @(#)SelectAllActioin.java  1.0  February 27, 2006
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

package org.jhotdraw.app.action;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;

import org.jhotdraw.app.EditableComponent;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * SelectAllAction.
 * 
 * @author Werner Randelshofer.
 * @version 1.0 February 27, 2006 Created.
 */
public class CopyAllToClipboard extends AbstractAction {
	public final static String ID = "copyAllToClipboard";

	/** Creates a new instance. */
	public CopyAllToClipboard() {
		ResourceBundleUtil labels = ResourceBundleUtil
				.getLAFBundle("org.jhotdraw.app.Labels");
		labels.configureAction(this, ID);
	}

	public void actionPerformed(ActionEvent evt) {
		Component focusOwner = KeyboardFocusManager
				.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
		if (focusOwner != null) {
			if (focusOwner instanceof EditableComponent) {
				((EditableComponent) focusOwner).selectAll();
			} else if (focusOwner instanceof JTextComponent) {
				((JTextComponent) focusOwner).selectAll();
			} else {
				focusOwner.getToolkit().beep();
			}
		}
		if (focusOwner != null && focusOwner instanceof JComponent) {
			JComponent component = (JComponent) focusOwner;
			if (component.getTransferHandler()!=null){
				//component.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
				component.getTransferHandler().exportToClipboard(component,
						component.getToolkit().getSystemClipboard(),
						TransferHandler.COPY);
				//TODO: Obede, se basear aqui para criar o exportar como imagem.
			}
		}
	}
}
