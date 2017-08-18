package org.jhotdraw.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jhotdraw.app.action.ConnectionRecommendationAction;
import org.jhotdraw.app.action.InsertNewConnectionAction;
import org.jhotdraw.draw.AtributoChaveFigure;
import org.jhotdraw.draw.AtributoChaveParcialFigure;
import org.jhotdraw.draw.AtributoDerivadoFigure;
import org.jhotdraw.draw.AtributoFigure;
import org.jhotdraw.draw.AtributoMultivaloradoFigure;
import org.jhotdraw.draw.ConnectionAttribute;
import org.jhotdraw.draw.DisjuncaoFigure;
import org.jhotdraw.draw.DoubleLineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.EntidadeFigure;
import org.jhotdraw.draw.EntidadeFracaFigure;
import org.jhotdraw.draw.EntidadeRelacionamentoFigure;
import org.jhotdraw.draw.GeneralizacaoLineConnectionFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionUmFigure;
import org.jhotdraw.draw.LabeledLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledLineConnectionUmFigure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.LineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.RelacionamentoFigure;
import org.jhotdraw.draw.RelacionamentoFracoFigure;
import org.jhotdraw.draw.SobreposicaoFigure;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.TextItalicoFigure;
import org.jhotdraw.draw.UniaoFigure;
import org.jhotdraw.draw.action.ModelValidationRules.ValidationRule;
import org.jhotdraw.util.ResourceBundleUtil;

public class ConnectionRecommendationView extends JFrame implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();//fechar frame
	}

	public void desenhar(ArrayList<Class> connections, ArrayList<ValidationRule> validRule, LineConnectionFigure lcf,
			Drawing draw) {

		JPanel panel = new JPanel(new GridLayout(0, 3));

		if (connections.size() == 0) {
			// mensagem de vazio
			panel.add(new JLabel());
			panel.add(new JLabel());
			panel.add(new JLabel());

		} else {
			// mensagem de conexoes que poderiam ser validas
			int metade = (connections.size() / 2) - 1;
			int i = 0;
			for (Class c : connections) {
				if (i == metade) {
					panel.add(new JLabel(getImageFigure(lcf.getStartFigure().getClass())));
				} else {
					panel.add(new JLabel());
				}

				JButton btn = new JButton(getImageFigure(c));
				InsertNewConnectionAction nca = new InsertNewConnectionAction(lcf, c, draw);
				btn.addActionListener(nca);
				btn.addActionListener(this);
				panel.add(btn);

				if (i == metade) {
					panel.add(new JLabel(getImageFigure(lcf.getEndFigure().getClass())));
				} else {
					panel.add(new JLabel());
				}

				i++;
			}
		}

		panel.add(new JLabel());
		panel.add(new JLabel());
		panel.add(new JLabel());

		//mensagem ligacoes validas para conexao
		int metade = (validRule.size() / 2) - 1;
		int i = 0;
		for (ValidationRule c : validRule) {
			panel.add(new JLabel(getImageFigure(c.getOrigem())));
			if (i == metade) {
				panel.add(new JLabel(getImageFigure(c.getConexao())));
			} else {
				panel.add(new JLabel());
			}
			panel.add(new JLabel(getImageFigure(c.getDestino())));
			i++;
		}

		add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
		this.setTitle(labels.getString(ConnectionRecommendationAction.ID));
	}

	private String getNameFigure(Class c) {
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
		return labels.getTip(getLabelFigureName(c));
	}

	private ImageIcon getImageFigure(Class c) {
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
		return labels.getImageIcon(getLabelFigureName(c), c);
	}
	
	public String getLabelFigureName(Class c){
		if (c.equals(EntidadeFigure.class)) {
			return "createEntidade";
		} else if (c.equals(EntidadeRelacionamentoFigure.class)) {
			return "createEntidadeRelacionamento";
		} else if (c.equals(EntidadeFracaFigure.class)) {
			return "createEntidadeFraca";
		} else if (c.equals(AtributoFigure.class)) {
			return "createAtributo";
		} else if (c.equals(AtributoChaveFigure.class)) {
			return "createAtributoChave";
		} else if (c.equals(AtributoChaveParcialFigure.class)) {
			return "createAtributoChaveParcial";
		} else if (c.equals(AtributoMultivaloradoFigure.class)) {
			return "createAtributoMultivalorado";
		} else if (c.equals(AtributoDerivadoFigure.class)) {
			return "createAtributoDerivado";
		} else if (c.equals(RelacionamentoFigure.class)) {
			return "createRelacionamento";
		} else if (c.equals(RelacionamentoFracoFigure.class)) {
			return "createRelacionamentoFraco";
		} else if (c.equals(UniaoFigure.class)) {
			return "createUniao";
		} else if (c.equals(SobreposicaoFigure.class)) {
			return "createSobreposicao";
		} else if (c.equals(DisjuncaoFigure.class)) {
			return "createDisjuncao";
		} else if (c.equals(ConnectionAttribute.class)) {
			return "createElbowConnectionAtributo";
		} else if (c.equals(LabeledLineConnectionUmFigure.class)) {
			return "createElbowUmConnection";
		} else if (c.equals(LabeledLineConnectionMuitosFigure.class)) {
			return "createElbowMuitosConnection";
		} else if (c.equals(LabeledDoubleLineConnectionUmFigure.class)) {
			return "createElbowDoubleUmConnection";
		} else if (c.equals(LabeledDoubleLineConnectionMuitosFigure.class)) {
			return "createElbowDoubleMuitosConnection";
		} else if (c.equals(LineConnectionGeneralizacaoFigure.class)) {
			return "createElbowConnection";
		} else if (c.equals(DoubleLineConnectionGeneralizacaoFigure.class)) {
			return "createElbowDoubleConnection";
		} else if (c.equals(GeneralizacaoLineConnectionFigure.class)) {
			return "createGeneralizacaoConnection";
		} else if (c.equals(TextItalicoFigure.class)) {
			return "createPapel";
		} else if (c.equals(TextFigure.class)) {
			return "createText";
		} else if (c.equals(TextAreaFigure.class)) {
			return "createAreaText";
		}
		return null;
	}

}
