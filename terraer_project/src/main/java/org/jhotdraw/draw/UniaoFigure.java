/*
 * @(#)RectangleFigure.java  2.3  2006-12-23
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

import java.awt.Color;
import java.awt.geom.Point2D.Double;

import org.jhotdraw.util.ResourceBundleUtil;

/**
 * RectangleFigure.
 *
 * @author Werner Randelshofer
 * @version 2.3 2006-12-23 Made rectangle protected. 
 * <br>2.2 2006-03-23 Take stroke size into account in method contains.
 * <br>2.1 2006-03-22 Method getFigureDrawBounds added.
 * <br>2.0 2006-01-14 Changed to support double precison coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class UniaoFigure extends GroupFigure {

	private static int counter = 0;
    private String title; 
    private CircleFigure cf;
    private TextNegritoFigure tf;
	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");


    public UniaoFigure(){
    	super();
    }
    
    public UniaoFigure init(){
    	cf = new CircleFigure();
    	cf.setAttribute(AttributeKeys.FILL_COLOR, new Color(245, 242, 224));
    	
    	tf = new TextNegritoFigure(labels.getString("createUniao.letra"));
    	tf.setAttribute(tf.getAttributeKey("fontBold"), Boolean.TRUE);
		tf.setFontSize(16);
		tf.setEditable(false);
		
    	title=labels.getString("createUniao")+Integer.toString(counter++);
		this.add(cf);
    	this.add(tf);
    	return this;
	}

    @Override
	public String getToolTipText(Double p) {
		return this.toString();
	}
    
	public AbstractCompositeFigure clone() {
//		UniaoFigure f = (UniaoFigure) super.clone();
//		f.init();
		UniaoFigure f = new UniaoFigure().init();

		f.willChange();
		f.cf.setBounds(this.cf.getBounds());
		f.tf.setBounds(this.tf.getBounds());
		f.changed();

		return f;
	}
	
	public String toString(){
		return title;
	}

}
