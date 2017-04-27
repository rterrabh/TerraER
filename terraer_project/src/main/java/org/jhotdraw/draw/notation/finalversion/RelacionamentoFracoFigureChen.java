package org.jhotdraw.draw.notation.finalversion;

import java.awt.geom.Point2D.Double;
import java.io.IOException;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DiamondFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.TerraResizeEventFunctions;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.AttributeKeys.StrokeType;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;

public class RelacionamentoFracoFigureChen extends GroupFigure implements IChangeNotationListern {

    private TextFigure tf;
    private TextFigure tf_idef1x;
    private DiamondFigure df;
	private static int counter = 0;
    private TerraResizeEventFunctions EventFunctions;
    private String currentNotation;
	
	public RelacionamentoFracoFigureChen(){
    	super();
    	currentNotation = NotationSelectAction.SelectChenAction.ID;
		NotationSelectAction.addListern(this);
    }
    
    public RelacionamentoFracoFigureChen init(){
    	df=new DiamondFigure();
    	df.setAttribute(AttributeKeys.STROKE_TYPE, StrokeType.DOUBLE);
		df.setAttribute(AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 3.0);
    	
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

    	tf=new TextFigure(labels.getString("createRelacionamentoFraco")+Integer.toString(counter++));
    	tf_idef1x=new TextFigure(tf.getText());
    	tf_idef1x.setVisible(false);
    	this.add(df);
    	this.add(tf);
    	this.add(tf_idef1x);
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
    	RelacionamentoFracoFigureChen f = new RelacionamentoFracoFigureChen().init();
    	
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
    }

	@Override
	public void notifyChange(String notation) {
		currentNotation = notation;
		df.setVisible(false);
		tf.setVisible(false);
		tf_idef1x.setVisible(false);
		if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
			df.setVisible(true);
			tf.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			df.setVisible(true);
			tf.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			tf_idef1x.setVisible(true);
		}
	}	
    
}
