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

import java.awt.geom.Point2D.Double;

import java.io.IOException;

import org.jhotdraw.draw.AttributeKeys.StrokeType;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;

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
public class EntidadeFracaFigure extends GroupFigure {
	
    private TextFigure tf;
    private RectangleFigure rec;
    private static int counter = 0;
    private TerraResizeEventFunctions EventFunctions;
	
    public EntidadeFracaFigure(){
    	super();
    }
    
    public EntidadeFracaFigure init(){
    	rec=new RectangleFigure();
    	rec.setAttribute(AttributeKeys.STROKE_TYPE, StrokeType.DOUBLE);
		rec.setAttribute(AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 3.0);
		
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

    	tf=new TextFigure(labels.getString("createEntidadeFraca").toUpperCase()+Integer.toString(counter++));
    	this.add(rec);
    	this.add(tf);
    	this.EventFunctions=new TerraResizeEventFunctions(this,rec,tf);
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
    
    @Override
	public String getToolTipText(Double p) {
		return this.toString();
	}
    
    public AbstractCompositeFigure clone() {
//    	EntidadeFracaFigure f = (EntidadeFracaFigure) super.clone();
//    	f.init();
    	EntidadeFracaFigure f = new EntidadeFracaFigure().init();
    	
    	f.willChange();
		f.tf.setBounds(this.tf.getBounds());
		f.rec.setBounds(this.rec.getBounds());
		f.changed();
		
    	return f;
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
            else if(f instanceof RectangleFigure){
                rec=(RectangleFigure)f;
            }
        }
        this.EventFunctions=new TerraResizeEventFunctions(this,rec,tf);
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
    }
	
    
}
