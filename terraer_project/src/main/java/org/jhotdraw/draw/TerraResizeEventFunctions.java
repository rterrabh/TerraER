package org.jhotdraw.draw;

import java.awt.geom.Rectangle2D;

public class TerraResizeEventFunctions {
	
	protected GroupFigure Owner;
	protected AbstractAttributedFigure Geo;
	protected AbstractAttributedFigure Geo2;
	protected TextFigure Txf;
	
	public TerraResizeEventFunctions(GroupFigure Owner,AbstractAttributedFigure Geo,TextFigure Txf){
		this(Owner,Geo,null,Txf);
	}

    public TerraResizeEventFunctions(GroupFigure Owner,AbstractAttributedFigure Geo,AbstractAttributedFigure Geo2,TextFigure Txf){
		this.Owner=Owner;
		this.Geo=Geo;
		this.Txf=Txf;
		this.Geo2=Geo2;
	}
	
    /**
     * Position the text in the center of the geometric figure
     * and dont allow the geometric figure to be smaller than the text.
     */
    public void figureSizeChanged(){
      	Rectangle2D.Double GeoBounds=Geo.getBounds();
    	Rectangle2D.Double TxfBounds=Txf.getBounds();
    	if(GeoBounds.getWidth()>TxfBounds.getWidth()){//Horizontal centering
    		TxfBounds.x=GeoBounds.x+(GeoBounds.width-TxfBounds.width)/2;
    		Txf.setBounds(TxfBounds);
    	}
    	else { //Dont allow the figure to be smaller than the text
    		GeoBounds.width=TxfBounds.width;
    		Geo.setBounds(GeoBounds);
    		if(Geo2!=null) Geo2.setBounds(GeoBounds);
    	}

    	if(GeoBounds.getHeight()>TxfBounds.getHeight()){ //vertical centering
    		TxfBounds.y=GeoBounds.y+(GeoBounds.height-TxfBounds.height)/2;
    		Txf.setBounds(TxfBounds);
    	}
    	else { //dont allow the figure to be smaller than the text
    		GeoBounds.height=TxfBounds.height;
    		Geo.setBounds(GeoBounds);
    		if(Geo2!=null) Geo2.setBounds(GeoBounds);
    	}
    }
    
    /**
     * Position the text in the horizontal center of the geometric figure
     * and and allow the geometric figure to be smaller than the text.
     */
    public void figureCenterText(){
      	Rectangle2D.Double GeoBounds=Geo.getBounds();
    	Rectangle2D.Double TxfBounds=Txf.getBounds();
    	if(GeoBounds.getWidth()>TxfBounds.getWidth()){
    		TxfBounds.x=GeoBounds.x+(GeoBounds.width-TxfBounds.width)/2;
    	}
    	if(GeoBounds.getHeight()>TxfBounds.getHeight()){
    		TxfBounds.y=GeoBounds.y+(GeoBounds.height-TxfBounds.height)/2;
    	}
    	Txf.setBounds(TxfBounds);
    }    
    
    /**
     * The text inside the figure has changed, must reload the ObjectTree 
     * and resize the figure according to the new text. 
     */
    public void figureTextChanged(FigureEvent e){
    	Object e1=e.getNewValue();
    	Object e2=e.getOldValue();

    	if(e1!=null && e2!=null && !e1.toString().equals(e2.toString())){

    		TerraFigureTree.getInstance().refresh(Owner);

    		//Resize rectangle according to the new text if necessary
    		Rectangle2D.Double GeoBounds=Geo.getBounds();
    		Rectangle2D.Double TxfBounds=Txf.getBounds();
    		if(GeoBounds.getWidth()<TxfBounds.getWidth()){
    			GeoBounds.width = TxfBounds.width+20;
    			Geo.setBounds(GeoBounds);
    			if(Geo2!=null) Geo2.setBounds(GeoBounds);
    		}
    	}
    }

	
	
}
