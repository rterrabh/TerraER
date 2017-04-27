package org.jhotdraw.draw.notation.finalversion;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.BezierLabelLocator;
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LabeledLineConnectionFigure;
import org.jhotdraw.draw.LineFigure;
import org.jhotdraw.draw.LocatorLayouter;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.util.ResourceBundleUtil;

public class ConnectionParcialMuitosFigureChen extends LabeledLineConnectionFigure implements IChangeNotationListern, IConnectionNotationFigure {

	private String currentNotation;
	private TextFigure tf_chen;
	private EllipseFigure ef_crossFoot;
	private EllipseFigure ef_idef1x;

	public ConnectionParcialMuitosFigureChen() {
		super();

		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

		currentNotation = NotationSelectAction.SelectChenAction.ID;

		this.title = labels.getString("createElbowMuitosConnection");
		this.setLayouter(new LocatorLayouter());

		tf_chen = new TextFigure("N");
		tf_chen.setAttribute(AttributeKeys.FONT_BOLD, Boolean.TRUE);
		tf_chen.setFontSize(16);
		tf_chen.setEditable(false);

		ef_crossFoot = new EllipseFigure();
		ef_crossFoot.setVisible(false);

		ef_idef1x = new EllipseFigure();
		ef_idef1x.setVisible(false);

		LocatorLayouter.LAYOUT_LOCATOR.set(tf_chen, new BezierLabelLocator(0.5, -Math.PI / 2, 6));
		LocatorLayouter.LAYOUT_LOCATOR.set(ef_crossFoot, new BezierLabelLocator(0.2, 0, 0));
		LocatorLayouter.LAYOUT_LOCATOR.set(ef_idef1x, new BezierLabelLocator(0, 0, 0));

		this.add(tf_chen);
		this.add(ef_crossFoot);
		this.add(ef_idef1x);
		NotationSelectAction.addListern(this);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			System.out.println("AA4");
			Point2D.Double start = this.getStartPoint();
			Point2D.Double end = this.getEndPoint();
			Point2D.Double var = new Point2D.Double(end.getX() - start.getX(), end.getY() - start.getY());
			double direcao = Math.atan2(var.getY(), var.getX());
			double tam = Math.sqrt(Math.pow(var.getX(), 2) + Math.pow(var.getY(), 2));
			double ang = Math.PI / 2;
			Point2D.Double p1 = new Point2D.Double(start.getX() + (tam / 10) * Math.cos(direcao + ang),
					start.getY() + (tam / 10) * Math.sin(direcao + ang));
			int[] xPoints = { (int) (p1.getX() + 15 * Math.cos(direcao + ang)), (int) p1.getX(),
					(int) (p1.getX() + 15 * Math.cos(direcao - ang)) };
			int[] yPoints = { (int) (p1.getY() + 15 * Math.sin(direcao + ang)), (int) p1.getY(),
					(int) (p1.getY() + 15 * Math.sin(direcao - ang)) };
			g.drawPolyline(xPoints, yPoints, 3);
		}
	}

	@Override
	public void notifyChange(String notation) {
		currentNotation = notation;

		tf_chen.setVisible(false);
		ef_idef1x.setVisible(false);
		ef_crossFoot.setVisible(false);

		if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
			tf_chen.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			ef_crossFoot.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			if (this.getStartFigure() != null && this.getEndFigure() != null && (
					this.getStartFigure().equals(EntidadeFracaFigureChen.class)
					|| this.getEndFigure().equals(EntidadeFracaFigureChen.class))) {
				this.setAttribute(AttributeKeys.STROKE_DASHES, new double[] { 5.0 });
			}
			ef_idef1x.setVisible(true);
		}
	}
	
	@Override
	public LabeledLineConnectionFigure clone() {
		ConnectionParcialMuitosFigureChen that = (ConnectionParcialMuitosFigureChen)super.clone(); 
		NotationSelectAction.addListern(that);
		return that;
	}

	@Override
	public Figure getCardinalidadeFigure() {
		return null;
	}

	@Override
	public Figure getParticipacaoFigure() {
		return ef_crossFoot;
	}
	
}
