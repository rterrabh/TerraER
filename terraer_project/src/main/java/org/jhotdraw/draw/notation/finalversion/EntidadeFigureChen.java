package org.jhotdraw.draw.notation.finalversion;

import java.awt.geom.Point2D.Double;

import java.io.IOException;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TerraResizeEventFunctions;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.notation.GraphicalCompositeFigure;
import org.jhotdraw.draw.notation.ListFigure;
import org.jhotdraw.draw.notation.VerticalLayouter;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;

public class EntidadeFigureChen extends GroupFigure implements IChangeNotationListern{
    private TextFigure tf;
    private RectangleFigure rec;
    private static int counter = 0;
    private TerraResizeEventFunctions EventFunctions;
    private String currentNotation;
    private EntidadeFigureIDEF1X ent_idef1x;
	
	public EntidadeFigureChen(){
    	super();
    	currentNotation = NotationSelectAction.SelectChenAction.ID;
		//NotationSelectAction.addListern(this);
		//this.setVisible(false);
    }
		
	public EntidadeFigureChen init(){
    	rec = new RectangleFigure();
    	
    	ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
    	
    	ent_idef1x = new EntidadeFigureIDEF1X();
    	ent_idef1x.setVisible(false);
    	
    	tf = new TextFigure(labels.getString("createEntidade").toUpperCase()+Integer.toString(counter++));
    	this.add(rec);
    	this.add(tf);
    	this.add(ent_idef1x);
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

	public TextFigure getTextFigure() {
		return tf;
	}
	
	@Override
	public String getToolTipText(Double p) {
		return this.toString();
	}

	public AbstractCompositeFigure clone() {
    	EntidadeFigureChen f = (EntidadeFigureChen) super.clone();
    	f.init();
    	
    	f.willChange();
		f.tf.setBounds(this.tf.getBounds());
		f.rec.setBounds(this.rec.getBounds());
		f.ent_idef1x.setBounds(this.ent_idef1x.getBounds());
		f.changed();
		//obede
		NotationSelectAction.addListern(f);
		//obede
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
	
	public EntidadeFigureIDEF1X getEntidadeIDEF1X(){
		return ent_idef1x; //obede
	}
	
	public class EntidadeFigureIDEF1X extends GraphicalCompositeFigure{
		
		private TextFigure name;
		private ListFigure keys;
		private ListFigure attributes;

		public EntidadeFigureIDEF1X() {
			super(new RectangleFigure());
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
