package org.jhotdraw.draw.notation.finalversion;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LineConnectionFigure;

public class ConnectionAtributoFigureChen extends LineConnectionFigure implements IChangeNotationListern{

	private String currentNotation;
	public ConnectionAtributoFigureChen(){
		super();
		currentNotation = NotationSelectAction.SelectChenAction.ID;
		NotationSelectAction.addListern(this);
	}
	
	@Override
	public LineConnectionFigure clone() {
		ConnectionAtributoFigureChen that = (ConnectionAtributoFigureChen)super.clone();
		NotationSelectAction.addListern(that);		
		return that;
	}
	
	@Override
	public void notifyChange(String notation) {
		currentNotation = notation;
		if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
			this.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			this.setVisible(false);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			sendAttribute();
			this.setVisible(false);			
		}
	}
	
	private void sendAttribute(){
		Figure start = this.getStartFigure();
		Figure end = this.getEndFigure();
		if (start != null && end != null){
			if (start.getClass().equals(EntidadeFigureChen.class)){
				if (end.getClass().equals(AtributoChaveFigureChen.class) || 
						end.getClass().equals(AtributoChaveParcialFigureChen.class))
					((EntidadeFigureChen) start).getEntidadeIDEF1X().addKey(end.toString());
				else
					((EntidadeFigureChen) start).getEntidadeIDEF1X().addAttribute(end.toString());
			} else if (start.getClass().equals(EntidadeFracaFigureChen.class)){
				if (end.getClass().equals(AtributoChaveFigureChen.class) || 
						end.getClass().equals(AtributoChaveParcialFigureChen.class))
					((EntidadeFracaFigureChen) start).getEntidadeIDEF1X().addKey(end.toString());
				else
					((EntidadeFracaFigureChen) start).getEntidadeIDEF1X().addAttribute(end.toString());
			}else if (end.getClass().equals(EntidadeFigureChen.class)){
				if (start.getClass().equals(AtributoChaveFigureChen.class) || 
						start.getClass().equals(AtributoChaveParcialFigureChen.class))
					((EntidadeFigureChen) end).getEntidadeIDEF1X().addKey(start.toString());
				else
					((EntidadeFigureChen) end).getEntidadeIDEF1X().addAttribute(start.toString());
			} else if (end.getClass().equals(EntidadeFracaFigureChen.class)){
				if (start.getClass().equals(AtributoChaveFigureChen.class) || 
						start.getClass().equals(AtributoChaveParcialFigureChen.class))
					((EntidadeFracaFigureChen) end).getEntidadeIDEF1X().addKey(start.toString());
				else
					((EntidadeFracaFigureChen) end).getEntidadeIDEF1X().addAttribute(start.toString());
			}
		}
	}
}
