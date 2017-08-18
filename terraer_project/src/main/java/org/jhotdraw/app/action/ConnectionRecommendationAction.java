package org.jhotdraw.app.action;

import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.AbstractAction;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;

import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.action.AbstractSelectedAction;
import org.jhotdraw.draw.action.ModelValidationRules;
import org.jhotdraw.gui.ConnectionRecommendationView;
import org.jhotdraw.util.ResourceBundleUtil;

public class ConnectionRecommendationAction extends AbstractSelectedAction {

	public final static String ID = "recomendation";
	private static ConnectionRecommendationAction INSTANCE = null;

	private ConnectionRecommendationAction(DrawingEditor editor) {
		super(editor);
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
		putValue(AbstractAction.NAME, labels.getString(ID));
		this.setEnabled(false);
	}
	
	public static ConnectionRecommendationAction getInstance(DrawingEditor editor){
		if (INSTANCE == null && editor != null){
			INSTANCE = new ConnectionRecommendationAction(editor);
		}
		return INSTANCE;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DrawingView v = this.getView();
		if (v.getSelectedFigures() != null && v.getSelectedFigures().size() == 1
				&& v.getSelectedFigures().toArray()[0] instanceof LineConnectionFigure) {
			LineConnectionFigure line = (LineConnectionFigure) v.getSelectedFigures().iterator().next();
			if (line.getAttribute(AttributeKeys.TEXT_COLOR).equals(Color.red)) {
				ModelValidationRules mvr = ModelValidationRules.getInstance();
				ConnectionRecommendationView view = new ConnectionRecommendationView();
				view.desenhar(mvr.getOthersConnections(line), mvr.getOthersFigures(line), line, this.getDrawing());
			}
		}
	}
	
	public void actionFromConnection(LineConnectionFigure line){
		if (line.getAttribute(AttributeKeys.TEXT_COLOR).equals(Color.red)) {
			ModelValidationRules mvr = ModelValidationRules.getInstance();
			ConnectionRecommendationView view = new ConnectionRecommendationView();
			view.desenhar(mvr.getOthersConnections(line), mvr.getOthersFigures(line), line, this.getDrawing());
		}
	}
	
}
