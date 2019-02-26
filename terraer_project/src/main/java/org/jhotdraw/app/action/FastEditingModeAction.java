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

import javax.swing.JToggleButton;

import org.jhotdraw.app.Application;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Cuts the selected region and places its contents into the system clipboard.
 * Acts on the EditableComponent or JTextComponent which had the focus when the
 * ActionEvent was generated.
 *
 * @author Werner Randelshofer
 * @version 2.0 2007-04-13 Use javax.swing.TransferHandler instead of interface
 *          EditableComponent. <br>
 *          1.0 October 9, 2005 Created.
 */
public class FastEditingModeAction extends AbstractProjectAction {
	public final static String ID = "fastediting";

	public static boolean ACTIVE = false;
	public static JToggleButton defaultToolButton = null;

	/** Creates a new instance. */
	public FastEditingModeAction(Application app) {
		super(app);
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
		labels.configureAction(this, ID);

	}

	public void actionPerformed(ActionEvent evt) {
		ACTIVE = !ACTIVE;
		if (defaultToolButton != null) {
			defaultToolButton.setSelected(!FastEditingModeAction.ACTIVE);
		}
	}
}
