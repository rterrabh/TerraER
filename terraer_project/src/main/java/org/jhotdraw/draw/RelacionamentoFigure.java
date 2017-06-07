/*
 * @(#)DiamondFigure.java  1.1  2007-05-12
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


package org.jhotdraw.draw;

import java.awt.geom.Point2D.Double;

import java.io.IOException;

import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;

/**
 * A diamond with vertices at the midpoints of its enclosing rectangle.
 *
 *
 * @author Werner Randelshofer
 * @version 1.1 2007-05-12 Removed convenience getters and setters for 
 * IS_QUADRATIC attribute. 
 * <br>1.0 2006-03-27 Created.
 */
public class RelacionamentoFigure extends GroupFigure {

    private TextFigure tf;
    private DiamondFigure df;
	private static int counter = 0;
    private TerraResizeEventFunctions EventFunctions;
	
	public RelacionamentoFigure(){
    	super();
    }
    
    public RelacionamentoFigure init(){
    	df=new DiamondFigure();
    	
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

    	tf=new TextFigure(labels.getString("createRelacionamento")+Integer.toString(counter++));
    	this.add(df);
    	this.add(tf);
    	this.EventFunctions=new TerraResizeEventFunctions(this,df,tf);
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
    	RelacionamentoFigure f = new RelacionamentoFigure().init();
    	
    	f.willChange();
		f.tf.setBounds(this.tf.getBounds());
		f.df.setBounds(this.df.getBounds());
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
            else if(f instanceof DiamondFigure){
                df=(DiamondFigure)f;
            }
        }
        this.EventFunctions=new TerraResizeEventFunctions(this,df,tf);
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
