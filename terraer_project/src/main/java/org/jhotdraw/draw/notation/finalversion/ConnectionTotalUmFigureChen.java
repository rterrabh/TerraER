package org.jhotdraw.draw.notation.finalversion;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.BezierLabelLocator;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LabeledLineConnectionFigure;
import org.jhotdraw.draw.LineFigure;
import org.jhotdraw.draw.LocatorLayouter;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.util.ResourceBundleUtil;

public class ConnectionTotalUmFigureChen extends LabeledLineConnectionFigure implements IChangeNotationListern, IConnectionNotationFigure {

	private String currentNotation;
	private TextFigure tf_chen;
	private TextFigure tf_idef1x;
	private LineFigure lf_crossFootCardinalidade;
	private LineFigure lf_crossFootParticipacao;

	public ConnectionTotalUmFigureChen() {
		super();

		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

		currentNotation = NotationSelectAction.SelectChenAction.ID;

		this.title = labels.getString("createElbowDoubleUmConnection");
		this.setAttribute(AttributeKeys.STROKE_TYPE, AttributeKeys.StrokeType.DOUBLE);
		this.setAttribute(AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 3.0);
		this.setLayouter(new LocatorLayouter());

		tf_chen = new TextFigure("1");
		tf_chen.setAttribute(AttributeKeys.FONT_BOLD, Boolean.TRUE);
		tf_chen.setFontSize(16);
		tf_chen.setEditable(false);
		
		tf_idef1x = new TextFigure("1");
		tf_idef1x.setAttribute(AttributeKeys.FONT_BOLD, Boolean.TRUE);
		tf_idef1x.setFontSize(16);
		tf_idef1x.setEditable(false);
		tf_idef1x.setVisible(false);
		
		lf_crossFootCardinalidade = new LineFigure();
		lf_crossFootCardinalidade.setVisible(false);
		
		lf_crossFootParticipacao = new LineFigure();
		lf_crossFootParticipacao.setVisible(false);

		LocatorLayouter.LAYOUT_LOCATOR.set(tf_chen, new BezierLabelLocator(0.5, -Math.PI / 2, 6));
		LocatorLayouter.LAYOUT_LOCATOR.set(lf_crossFootParticipacao, new BezierLabelLocator(0.2, 0, 0));
		LocatorLayouter.LAYOUT_LOCATOR.set(lf_crossFootCardinalidade, new BezierLabelLocator(0.1, 0, 0));
		LocatorLayouter.LAYOUT_LOCATOR.set(tf_idef1x, new BezierLabelLocator(0.15, -Math.PI / 2, 6));

		this.add(tf_chen);
		this.add(lf_crossFootParticipacao);
		this.add(lf_crossFootCardinalidade);
		this.add(tf_idef1x);
		NotationSelectAction.addListern(this);
	}

	@Override
	public void notifyChange(String notation) {
		currentNotation = notation;

		tf_chen.setVisible(false);
		lf_crossFootCardinalidade.setVisible(false);
		lf_crossFootParticipacao.setVisible(false);
		tf_idef1x.setVisible(false);

		if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
			tf_chen.setVisible(true);
			this.setAttribute(AttributeKeys.STROKE_TYPE, AttributeKeys.StrokeType.DOUBLE);
			this.setAttribute(AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 3.0);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			this.setAttribute(AttributeKeys.STROKE_TYPE, AttributeKeys.StrokeType.BASIC);
			lf_crossFootCardinalidade.setVisible(true);
			lf_crossFootParticipacao.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			if (this.getStartFigure() != null && this.getEndFigure() != null && (
					this.getStartFigure().equals(EntidadeFracaFigureChen.class)
					|| this.getEndFigure().equals(EntidadeFracaFigureChen.class))) {
				this.setAttribute(AttributeKeys.STROKE_DASHES, new double[] { 5.0 });
			}
			this.setAttribute(AttributeKeys.STROKE_TYPE, AttributeKeys.StrokeType.BASIC);
			tf_idef1x.setVisible(true);
		}

	}
	
	@Override
	public LabeledLineConnectionFigure clone() {
		ConnectionTotalUmFigureChen that = (ConnectionTotalUmFigureChen)super.clone(); 
		NotationSelectAction.addListern(that);
		return that;
	}
	
	public Figure getParticipacaoFigure() {
		return lf_crossFootParticipacao;
	}
	
	public Figure getCardinalidadeFigure() {
		return lf_crossFootCardinalidade;
	}
}
