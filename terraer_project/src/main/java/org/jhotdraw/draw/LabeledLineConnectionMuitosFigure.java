/*
 * @(#)LabeledLineConnection.java  1.1  2006-02-14
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

package org.jhotdraw.draw;

import org.jhotdraw.util.ResourceBundleUtil;

/**
 * A LineConnection with labels.
 * <p>
 * Usage:
 * 
 * <pre>
 * LineConnectionFigure lcf = new LineConnectionFigure();
 * lcf.setLayouter(new LocatorLayouter());
 * TextFigure label = new TextFigure();
 * label.setText(&quot;Hello&quot;);
 * LocatorLayouter.LAYOUT_LOCATOR.set(label, new BezierLabelLocator(0,
 * 		-Math.PI / 4, 8));
 * lcf.add(label);
 * </pre>
 * 
 * @author Werner Randelshofer
 * @version 1.1 2006-02-14 Do not include labels in logical bounds. <br>
 *          1.0 23. Januar 2006 Created.
 */
public class LabeledLineConnectionMuitosFigure extends
		LabeledLineConnectionFigure {

	public LabeledLineConnectionMuitosFigure() {
		super();
		
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

		this.title=labels.getString("createElbowMuitosConnection");
    	this.setLayouter(new LocatorLayouter());
        TextFigure tf = new TextFigure("N");
        tf.setAttribute(AttributeKeys.FONT_BOLD,Boolean.TRUE);
        tf.setFontSize(16);
        tf.setEditable(false);
        LocatorLayouter.LAYOUT_LOCATOR.set(tf, new BezierLabelLocator(0.5, -Math.PI / 4, 8));
        //this.setLiner(new ElbowLiner());
        this.add(tf);
	}

}
