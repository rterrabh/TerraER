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

import java.io.IOException;

import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;
import org.jhotdraw.xml.DOMOutput;

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
public class DoubleLineConnectionGeneralizacaoFigure extends
		LabeledLineConnectionFigure {

	public DoubleLineConnectionGeneralizacaoFigure() {
		super();
		
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

		this.title=labels.getString("createElbowDoubleConnection");
		this.setAttribute(AttributeKeys.STROKE_TYPE, AttributeKeys.StrokeType.DOUBLE);
    	this.setAttribute(AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 3.0);
    	this.setLayouter(new LocatorLayouter());
    	TextFigure tf = new TextFigure(labels.getString("createElbowDoubleConnection.initial"));
        tf.setAttribute(AttributeKeys.FONT_ITALIC,Boolean.TRUE);
        tf.setFontSize(12);
        tf.setEditable(true);
        LocatorLayouter.LAYOUT_LOCATOR.set(tf, new BezierLabelLocator(0.25, -Math.PI / 4, 8));
        //this.setLiner(new ElbowLiner());
        this.add(tf);
	}
	
	@Override
	public void write(DOMOutput out) throws IOException {
		super.write(out);
        out.addAttribute("labelGeneralizacaoText", ((TextFigure)this.getChild(0)).getText());
        writeAttributes(out);
    }
	
	@Override
	public void read(DOMInput in) throws IOException {
		super.read(in);
		((TextFigure)this.getChild(0)).setText(in.getAttribute("labelGeneralizacaoText", "característica"));
	}

}
