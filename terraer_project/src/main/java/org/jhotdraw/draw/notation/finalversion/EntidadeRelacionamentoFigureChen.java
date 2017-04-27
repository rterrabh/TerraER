
package org.jhotdraw.draw.notation.finalversion;

import java.awt.geom.Point2D.Double;

import java.io.IOException;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.DiamondFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TerraResizeEventFunctions;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;

public class EntidadeRelacionamentoFigureChen extends GroupFigure implements IChangeNotationListern {

	private TextFigure tf;
	private RectangleFigure rec;
	private DiamondFigure df;
	private static int counter = 0;
    private TerraResizeEventFunctions EventFunctions;
    private String currentNotation;
    
    public EntidadeRelacionamentoFigureChen(){
    	super();
    	currentNotation = NotationSelectAction.SelectChenAction.ID;
		NotationSelectAction.addListern(this);
    }
    
    public EntidadeRelacionamentoFigureChen init(){
    	rec=new RectangleFigure();    	
    	
    	df=new DiamondFigure();
    	
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

    	tf=new TextFigure(labels.getString("createEntidadeRelacionamento")+Integer.toString(counter++));
    	
    	this.add(rec);
    	this.add(df);
    	this.add(tf);
    	
    	this.EventFunctions=new TerraResizeEventFunctions(this,rec,df,tf);
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
    	EntidadeRelacionamentoFigureChen f = (EntidadeRelacionamentoFigureChen) super.clone();
    	f.init();
    	
    	f.willChange();
		f.tf.setBounds(this.tf.getBounds());
		f.rec.setBounds(this.rec.getBounds());
		f.df.setBounds(this.df.getBounds());
		f.changed();
		NotationSelectAction.addListern(f);
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
            else if(f instanceof DiamondFigure){
                df=(DiamondFigure)f;
            }
        }
    }
    
    @Override
	public void notifyChange(String notation) {
		currentNotation = notation;
		rec.setVisible(true);
		tf.setVisible(true);
		df.setVisible(true);
		//if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
		//} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
		if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			rec.setVisible(false);
			tf.setVisible(false);
			df.setVisible(false);				
		}
	}
	
}
