/*
 * @(#)EllipseFigure.java  2.4  2006-12-23
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

/**
 * EllipseFigure.
 *
 * @author Werner Randelshofer
 * @version 2.4 2006-12-12 Made ellipse protected.
 * <br>2.3 2006-06-17 Added method chop(Point2D.Double).
 * <br>2.2 2006-05-19 Support for stroke placement added.
 * <br>2.1 2006-03-22 Method getFigureDrawBounds added.
 * <br>2.0 2006-01-14 Changed to support double precison coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class AtributoMultivaloradoFigure extends GroupFigure {
	private TextFigure tf;
	private EllipseFigure ef;
    private static int counter = 0;
    private TerraResizeEventFunctions EventFunctions;
	
    public AtributoMultivaloradoFigure(){
    	super();
    }
    
	public AtributoMultivaloradoFigure init(){
		ef=new EllipseFigure();
		
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

		tf=new TextFigure(labels.getString("createAtributoMultivalorado")+Integer.toString(counter++));
		this.add(ef);
    	this.add(tf);
    	this.EventFunctions=new TerraResizeEventFunctions(this,ef,tf);
    	this.tf.addFigureListener(new FigureAdapter(){
			@Override
			public void figureAttributeChanged(FigureEvent e){
				EventFunctions.figureTextChanged(e);
			}
			
			@Override
			public void figureChanged(FigureEvent e) {
				EventFunctions.figureSizeChanged();
			}
    	});
    	return this;
	}
	
    public AbstractCompositeFigure clone() {
    	return (new AtributoMultivaloradoFigure()).init();
    }
	
	public String toString(){
		return tf.getText();
	}
	
    public void read(DOMInput in) throws IOException {
        super.read(in);
        
        java.util.Collection<Figure> lst=getDecomposition();
        for( Figure f : lst){
            if(f instanceof TextFigure){
                tf=(TextFigure)f;
            }
            else if(f instanceof EllipseFigure){
                ef=(EllipseFigure)f;
            }
        }
    }   	
	
}