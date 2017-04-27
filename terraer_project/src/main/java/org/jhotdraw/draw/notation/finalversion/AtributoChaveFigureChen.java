package org.jhotdraw.draw.notation.finalversion;

import java.awt.geom.Point2D.Double;
import java.io.IOException;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.TerraResizeEventFunctions;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.enums.AttributeTypeEnum;
import org.jhotdraw.interfaces.AttributeTypeElement;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.xml.DOMInput;
import org.jhotdraw.xml.DOMOutput;


public class AtributoChaveFigureChen extends GroupFigure implements AttributeTypeElement, IChangeNotationListern {

	private TextFigure tf;
	private EllipseFigure ef;
	private static int counter = 0;
	private TerraResizeEventFunctions EventFunctions;
	private AttributeTypeEnum attributeType = AttributeTypeEnum.INTEGER;
	private boolean nullable;
	private String currentNotation;

	public AtributoChaveFigureChen() {
		super();
		currentNotation = NotationSelectAction.SelectChenAction.ID;
		NotationSelectAction.addListern(this);
	}

	public AtributoChaveFigureChen init() {
		ef = new EllipseFigure();

		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");

		tf = new TextFigure(labels.getString("createAtributoChave") + Integer.toString(counter++));
		tf.setAttribute(AttributeKeys.FONT_BOLD, Boolean.TRUE);
		tf.setAttribute(AttributeKeys.FONT_UNDERLINE, Boolean.TRUE);

		this.add(ef);
		this.add(tf);

		this.EventFunctions = new TerraResizeEventFunctions(this, ef, tf);
		this.tf.addFigureListener(new FigureAdapter() {
			@Override
			public void figureAttributeChanged(FigureEvent e) {
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
		AtributoChaveFigureChen f = new AtributoChaveFigureChen().init();

		f.willChange();
		f.ef.setBounds(this.ef.getBounds());
		f.tf.setBounds(this.tf.getBounds());
		f.changed();

		return f;
	}

	public String toString() {
		return tf.getText().replaceAll("\\s+", "_");
	}

	public void read(DOMInput in) throws IOException {
		super.read(in);

		this.nullable = in.getAttribute("nullable", false);
		this.attributeType = AttributeTypeEnum.getAttributeTypeByString(in.getAttribute("attributeType", null));

		java.util.Collection<Figure> lst = getDecomposition();
		for (Figure f : lst) {
			if (f instanceof TextFigure) {
				tf = (TextFigure) f;
			} else if (f instanceof EllipseFigure) {
				ef = (EllipseFigure) f;
			}
		}
	}

	@Override
	public void write(DOMOutput out) throws IOException {
		super.write(out);
		out.addAttribute("nullable", this.nullable);
		out.addAttribute("attributeType", this.attributeType.getSqlType());
	}

	public AttributeTypeEnum getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeTypeEnum attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public boolean isNullable() {
		return this.nullable;
	}

	@Override
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public void notifyChange(String notation) {
		currentNotation = notation;
		if (this.currentNotation.equals(NotationSelectAction.SelectChenAction.ID)) {
			this.setVisible(true);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectCrossFootAction.ID)) {
			this.setVisible(false);
		} else if (this.currentNotation.equals(NotationSelectAction.SelectIDEF1XAction.ID)) {
			this.setVisible(false);
		}
		
	}

}