package org.jhotdraw.draw.notation.figure.chen;

import java.awt.Color;
import java.awt.geom.Point2D.Double;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.CircleFigure;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.TextNegritoFigure;
import org.jhotdraw.util.ResourceBundleUtil;

public class InheritanceSobreposicaoFigureChen extends GroupFigure {

	private static int counter = 0;
    private String title; 
    private CircleFigure cf;
    private TextNegritoFigure tf;
	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");


    public InheritanceSobreposicaoFigureChen(){
    	super();
    }
    
    public InheritanceSobreposicaoFigureChen init(){
    	cf = new CircleFigure();
    	cf.setAttribute(AttributeKeys.FILL_COLOR, new Color(245, 242, 224));
    	
    	tf = new TextNegritoFigure(labels.getString("createSobreposicao.letra"));
    	//tf.setAttribute(tf.getAttributeKey("fontBold"), Boolean.TRUE);
    	tf.setAttribute(AttributeKeys.FONT_BOLD, Boolean.TRUE);
		tf.setFontSize(16);
		tf.setEditable(false);
		
    	title=labels.getString("createSobreposicao")+Integer.toString(counter++);
		this.add(cf);
    	this.add(tf);
    	return this;
	}

    @Override
	public String getToolTipText(Double p) {
		return this.toString();
	}
    
	public AbstractCompositeFigure clone() {
		InheritanceSobreposicaoFigureChen f = (InheritanceSobreposicaoFigureChen) super.clone();
		f.init();

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
