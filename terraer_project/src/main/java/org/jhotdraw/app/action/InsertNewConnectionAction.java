package org.jhotdraw.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jhotdraw.draw.ConnectionAttribute;
import org.jhotdraw.draw.DoubleLineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.GeneralizacaoLineConnectionFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionUmFigure;
import org.jhotdraw.draw.LabeledLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledLineConnectionUmFigure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.LineConnectionGeneralizacaoFigure;

public class InsertNewConnectionAction implements ActionListener {
	
	private Class c;
	private LineConnectionFigure lcf;
	private Drawing draw;
	
	public InsertNewConnectionAction(LineConnectionFigure lcf, Class c, Drawing draw) {
		this.c = c;
		this.lcf = lcf;
		this.draw = draw;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LineConnectionFigure newConection = createConnection(c);
		newConection.setStartConnector(lcf.getStartConnector());
		newConection.setEndConnector(lcf.getEndConnector());
		draw.add(newConection);
		draw.remove(lcf);
	}
	
	public LineConnectionFigure createConnection(Class c){
		if (c.equals(ConnectionAttribute.class)){
			return new ConnectionAttribute();
		} else if (c.equals(LabeledLineConnectionUmFigure.class)){
			return new LabeledLineConnectionUmFigure();
		} else if (c.equals(LabeledLineConnectionMuitosFigure.class)){
			return new LabeledLineConnectionMuitosFigure();
		} else if (c.equals(LabeledDoubleLineConnectionUmFigure.class)){
			return new LabeledDoubleLineConnectionUmFigure();
		} else if (c.equals(LabeledDoubleLineConnectionMuitosFigure.class)){
			return new LabeledDoubleLineConnectionMuitosFigure();
		} else if (c.equals(LineConnectionGeneralizacaoFigure.class)){
			return new LineConnectionGeneralizacaoFigure();
		} else if (c.equals(DoubleLineConnectionGeneralizacaoFigure.class)){
			return new DoubleLineConnectionGeneralizacaoFigure();
		} else if (c.equals(GeneralizacaoLineConnectionFigure.class)){
			return new GeneralizacaoLineConnectionFigure();
		}
		return null;
	}

}
