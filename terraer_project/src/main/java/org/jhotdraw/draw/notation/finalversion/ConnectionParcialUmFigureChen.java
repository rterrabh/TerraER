package org.jhotdraw.draw.notation.finalversion;

import java.awt.Graphics2D;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.BezierLabelLocator;
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LabeledLineConnectionFigure;
import org.jhotdraw.draw.LineFigure;
import org.jhotdraw.draw.LocatorLayouter;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.util.ResourceBundleUtil;

public class ConnectionParcialUmFigureChen extends LabeledLineConnectionFigure implements IChangeNotationListern, IConnectionNotationFigure {

	private String currentNotation;
	private TextFigure tf_chen;
	private LineFigure lf_crossFoot;
	private EllipseFigure ef_crossFoot;
	private EllipseFigure ef_idef1x;
	private TextFigure tf_idefix;

	public ConnectionParcialUmFigureChen() {
		super();

		currentNotation = NotationSelectAction.SelectChenAction.ID;

		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
		this.title = labels.getString("createElbowUmConnection");
		this.setLayouter(new LocatorLayouter());

		tf_chen = new TextFigure("1");
		tf_chen.setAttribute(AttributeKeys.FONT_BOLD, Boolean.TRUE);
		tf_chen.setFontSize(16);
		tf_chen.setEditable(false);

		tf_idefix = new TextFigure("Z");
		tf_idefix.setAttribute(AttributeKeys.FONT_BOLD, Boolean.TRUE);
		tf_idefix.setFontSize(16);
		tf_idefix.setEditable(false);
		tf_idefix.setVisible(false);

		lf_crossFoot = new LineFigure();
		lf_crossFoot.setVisible(false);

		ef_crossFoot = new EllipseFigure();
		ef_crossFoot.setVisible(false);
		
		ef_idef1x = new EllipseFigure();
		ef_idef1x.setVisible(false);

		LocatorLayouter.LAYOUT_LOCATOR.set(tf_chen, new BezierLabelLocator(0.5, -Math.PI / 2, 6));
		
		LocatorLayouter.LAYOUT_LOCATOR.set(lf_crossFoot, new BezierLabelLocator(0.1, 0, 0));
		LocatorLayouter.LAYOUT_LOCATOR.set(ef_crossFoot, new BezierLabelLocator(0.2, 0, 0));
		
		LocatorLayouter.LAYOUT_LOCATOR.set(tf_idefix, new BezierLabelLocator(0.15, -Math.PI / 2, 6));
		LocatorLayouter.LAYOUT_LOCATOR.set(ef_idef1x, new BezierLabelLocator(0.0, 0, 0));

		this.add(tf_chen);
		
		this.add(lf_crossFoot);
		this.add(ef_crossFoot);
		
		this.add(tf_idefix);
		this.add(ef_idef1x);

		//NotationSelectAction.addListern(this);
	}
	
	@Override
	public void notifyChange(String notation) {
		currentNotation = notation;

		tf_chen.setVisible(false);
		lf_crossFoot.setVisible(false);
		ef_crossFoot.setVisible(false);
		tf_idefix.setVisible(false);
		ef_idef1x.setVisible(false);
		if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
			tf_chen.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			lf_crossFoot.setVisible(true);
			ef_crossFoot.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			/*if (this.getStartFigure() != null && this.getEndFigure() != null
					&& (this.getStartFigure().equals(EntidadeFracaFigureChen.class)
							|| this.getEndFigure().equals(EntidadeFracaFigureChen.class))) {
				this.setAttribute(AttributeKeys.STROKE_DASHES, new double[] { 5.0 });
			}*/
			tf_idefix.setVisible(true);
			ef_idef1x.setVisible(true);
		}
	}
	
	@Override
	public LabeledLineConnectionFigure clone() {
		ConnectionParcialUmFigureChen that = (ConnectionParcialUmFigureChen)super.clone(); 
		NotationSelectAction.addListern(that);
		return that;
	}
	
	@Override
	public Figure getCardinalidadeFigure() {
		return lf_crossFoot;
	}

	@Override
	public Figure getParticipacaoFigure() {
		return ef_crossFoot;
	}

}
