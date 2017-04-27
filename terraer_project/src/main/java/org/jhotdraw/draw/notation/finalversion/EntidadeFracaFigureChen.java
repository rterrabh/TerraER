

package org.jhotdraw.draw.notation.finalversion;

import java.awt.geom.Point2D.Double;

import java.io.IOException;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TerraResizeEventFunctions;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.AttributeKeys.StrokeType;
import org.jhotdraw.draw.notation.GraphicalCompositeFigure;
import org.jhotdraw.draw.notation.ListFigure;
import org.jhotdraw.draw.notation.RoundRectangleFigure;
import org.jhotdraw.draw.notation.VerticalLayouter;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;

public class EntidadeFracaFigureChen extends GroupFigure implements IChangeNotationListern {
	
    private TextFigure tf;
    private RectangleFigure rec;
    private static int counter = 0;
    private TerraResizeEventFunctions EventFunctions;
    private String currentNotation;
    private EntidadeFracaFigureIDEF1X ent_idef1x;
	
    public EntidadeFracaFigureChen(){
    	super();
    	currentNotation = NotationSelectAction.SelectChenAction.ID;
		//NotationSelectAction.addListern(this);
    }
    
    public EntidadeFracaFigureChen init(){
    	rec=new RectangleFigure();
    	rec.setAttribute(AttributeKeys.STROKE_TYPE, StrokeType.DOUBLE);
		rec.setAttribute(AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 3.0);
		
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

    	ent_idef1x = new EntidadeFracaFigureIDEF1X();
    	ent_idef1x.setVisible(false);
    	tf=new TextFigure(labels.getString("createEntidadeFraca").toUpperCase()+Integer.toString(counter++));
    	this.add(rec);
    	this.add(tf);
    	//this.add(ent_idef1x);
    	
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
    	EntidadeFracaFigureChen f = (EntidadeFracaFigureChen) super.clone();
    	f.init();
    	
    	f.willChange();
		f.tf.setBounds(this.tf.getBounds());
		f.rec.setBounds(this.rec.getBounds());
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
        }
    }

	@Override
	public void notifyChange(String notation) {
		currentNotation = notation;
		rec.setVisible(false);
		tf.setVisible(false);
		ent_idef1x.setVisible(false);
		
		if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
			rec.setVisible(true);
			tf.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			rec.setVisible(true);
			tf.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			ent_idef1x.setVisible(true);
			ent_idef1x.setName(this.toString());
		}
	}
	
	public EntidadeFracaFigureIDEF1X getEntidadeIDEF1X(){
		return ent_idef1x; //obede
	}
	
	public class EntidadeFracaFigureIDEF1X extends GraphicalCompositeFigure{
		
		private TextFigure name;
		private ListFigure keys;
		private ListFigure attributes;

		public EntidadeFracaFigureIDEF1X() {
			super(new RoundRectangleFigure());
			setLayouter(new VerticalLayouter());
			name  = new TextFigure();
			keys = new ListFigure(new RectangleFigure());
			attributes = new ListFigure(new RectangleFigure());
			this.add(name);
			this.add(keys);
			this.add(attributes);
		}
		
		public void setName(String name){
			this.name.setText(name);
		}
		
		public void setName(TextFigure name){
			this.name.setText(name.getText());
			this.name.setBounds(name.getBounds());
		}
		
		public void addAttribute(String att){
			TextFigure text = new TextFigure(att);
			attributes.add(text);
		}
		
		public void addKey(String att){
			TextFigure text = new TextFigure(att);
			keys.add(text);
		}
	}
}
