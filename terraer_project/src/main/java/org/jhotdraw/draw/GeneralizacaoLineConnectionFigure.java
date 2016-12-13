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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.jhotdraw.util.ResourceBundleUtil;

/**
 * A LineConnection with labels.
 * <p>
 * Usage:
 * <pre>
 * LineConnectionFigure lcf = new LineConnectionFigure();
 * lcf.setLayouter(new LocatorLayouter());
 * TextFigure label = new TextFigure();
 * label.setText("Hello");
 * LocatorLayouter.LAYOUT_LOCATOR.set(label, new BezierLabelLocator(0, -Math.PI / 4, 8));
 * lcf.add(label);
 * </pre>
 *
 * @author Werner Randelshofer
 * @version 1.1 2006-02-14 Do not include labels in logical bounds.
 * <br>1.0 23. Januar 2006 Created.
 */
public class GeneralizacaoLineConnectionFigure extends LabeledLineConnectionFigure {
	//private Integer indiceNode = null;
	
	public GeneralizacaoLineConnectionFigure() {
		super();
		
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

		this.title=labels.getString("createGeneralizacaoConnection");
//    	this.setLayouter(new LocatorLayouter());
//        TextFigure tf = new TextFigure("U");
//        tf.setAttribute(AttributeKeys.FONT_BOLD,Boolean.TRUE);
//        tf.setFontSize(20);
//        tf.setEditable(false);
        //LocatorLayouter.LAYOUT_LOCATOR.set(tf, new BezierLabelLocator(0, -Math.PI / 2, 0));

        //this.add(tf);
	}
	
	/*public void init(DrawingView view) {
		Point2D.Double p = this.getCenter(); 
    	this.indiceNode = this.splitSegment(p, (float) (5f / view.getScaleFactor()));
	}*/
	
	public void draw(Graphics2D g) {
		super.draw(g);
        
		final Point2D.Double startPoint =  this.getStartPoint();
		//final Point2D.Double centerPoint =  (indiceNode!=null) ? this.getNode(indiceNode).getControlPoint(0) : this.getCenter();
		final Point2D.Double centerPoint =  this.getCenter();
		
        double diffXSemAbs = startPoint.x - centerPoint.x;
        double diffYSemAbs = startPoint.y - centerPoint.y;
        
        double diffX = Math.abs(startPoint.x - centerPoint.x);
        double diffY = Math.abs(startPoint.y - centerPoint.y);
        
        double tg = diffY / diffX;
        
        double arco = Math.toDegrees(Math.atan(tg));
    
        double anguloInicial = 0;
        
        if (diffXSemAbs>0 && diffYSemAbs<0){
        	anguloInicial = (arco + 90)%360;
        }else if (diffXSemAbs>0 && diffYSemAbs>0){
        	anguloInicial = (90-arco)%360;
        }else if (diffXSemAbs<0 && diffYSemAbs>0){
        	anguloInicial = (arco+270)%360;
        }else{
        	anguloInicial = (270-arco)%360;
        }
        
        g.setStroke(new BasicStroke(1.0f));
        //g.drawString(""+arco, (int)this.getCenter().x,(int) this.getCenter().y);
        
		g.drawArc((int)centerPoint.x-10, (int)centerPoint.y-10, 20, 20, (int) anguloInicial-15, 210);
    }
	
	@Override
	public boolean handleMouseClick(Double p, MouseEvent evt, DrawingView view) {
		return false;
	}
	
}
