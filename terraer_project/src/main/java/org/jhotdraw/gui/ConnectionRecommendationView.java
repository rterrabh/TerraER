package org.jhotdraw.gui;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
		this.dispose();
	}

	public void desenhar(ArrayList<Class> connections, ArrayList<ValidationRule> validRule, LineConnectionFigure lcf,
			Drawing draw) {

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cons;
		cons = new GridBagConstraints();

		cons.gridx = 1;
		cons.gridy = 0;
		cons.insets = new Insets(0, 5, 5, 5);

		cons.ipadx = 10;
		cons.anchor = GridBagConstraints.CENTER;
		panel.add(new JLabel(), cons);
		for (int i=2; i<=8; i++){
			cons = new GridBagConstraints();

			cons.gridx = i;
			cons.gridy = 0;
			cons.insets = new Insets(0, 5, 5, 5);

			cons.ipadx = 40;
			cons.anchor = GridBagConstraints.CENTER;
			panel.add(new JLabel(), cons);
		}
		cons = new GridBagConstraints();

		cons.gridx = 9;
		cons.gridy = 0;
		cons.insets = new Insets(0, 5, 5, 5);

		cons.ipadx = 10;
		cons.anchor = GridBagConstraints.CENTER;
		panel.add(new JLabel(), cons);

		if (connections.size() == 0) {
			cons = new GridBagConstraints();
			cons.gridwidth = 8;
			cons.gridx = 1;
			cons.gridy = 1;
			cons.insets = new Insets(5, 5, 5, 5);

			cons.anchor = GridBagConstraints.CENTER;
			String presentationName = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels")
					.getString("recomendationConnectionsNo");
			JLabel text =new JLabel(presentationName); 
			panel.add(text, cons);

		} else {
			int metade = (connections.size() / 2);
			int i = 1;
			cons = new GridBagConstraints();
			cons.gridwidth = 8;
			cons.gridx = 1;
			cons.gridy = i;
			cons.insets = new Insets(5, 5, 5, 5);

			cons.anchor = GridBagConstraints.CENTER;
			String presentationName = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels")
					.getString("recomendationConnections");
			JLabel text = new JLabel(presentationName);
			panel.add(text, cons);
			for (Class c : connections) {

				if (i == metade || metade == -1 || metade == 0) {
					cons = new GridBagConstraints();

					cons.gridx = 2;
					cons.gridy = i + 1;
					cons.insets = new Insets(0, 5, 5, 5);

					cons.anchor = GridBagConstraints.CENTER;
					panel.add(new JLabel(getImageFigure(lcf.getStartFigure().getClass())), cons);
				}

				JButton btn = new JButton(getImageFigure(c));
				InsertNewConnectionAction nca = new InsertNewConnectionAction(lcf, c, draw);
				btn.addActionListener(nca);
				btn.addActionListener(this);
				cons = new GridBagConstraints();

				cons.gridx = 4;
				cons.gridy = i + 1;
				cons.insets = new Insets(0, 5, 5, 5);

				cons.anchor = GridBagConstraints.CENTER;
				panel.add(btn, cons);

				if (i == metade || metade == -1 || metade == 0) {
					cons = new GridBagConstraints();

					cons.gridx = 7;
					cons.gridy = i + 1;
					cons.insets = new Insets(0, 5, 5, 5);

					cons.anchor = GridBagConstraints.CENTER;
					panel.add(new JLabel(getImageFigure(lcf.getEndFigure().getClass())), cons);
				}
				i++;
			}
		}

		int i = connections.size() + 2;
		int metade = (numRows(validRule) / 2) + i - 1;

		cons = new GridBagConstraints();
		cons.gridwidth = 8;
		cons.gridx = 1;
		cons.gridy = i;
		cons.insets = new Insets(5, 5, 5, 5);

		cons.anchor = GridBagConstraints.CENTER;
		String presentationName = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels")
				.getString("recomendationElements");
		JLabel text = new JLabel(presentationName);
		panel.add(text, cons);

		for (ValidationRule c : validRule) {
			for (int j = 0; j < c.getOrigem().length; j++) {
				cons = new GridBagConstraints();

				cons.gridx = 2;
				cons.gridy = i + 1;
				cons.insets = new Insets(0, 5, 5, 5);

				cons.anchor = GridBagConstraints.CENTER;
				panel.add(new JLabel(getImageFigure(c.getOrigem()[j])), cons);

				if (i == metade) {
					cons = new GridBagConstraints();

					cons.gridx = 4;
					cons.gridy = i+1;
					cons.insets = new Insets(0, 5, 5, 5);

					cons.anchor = GridBagConstraints.CENTER;
					panel.add(new JLabel(getImageFigure(c.getConexao())), cons);
				}
				for (int k = 0; k < c.getDestino().length; k++) {
					cons = new GridBagConstraints();
					cons.gridx = 6 + k;
					cons.gridy = i + 1;
					cons.insets = new Insets(0, 5, 5, 5);
					cons.anchor = GridBagConstraints.CENTER;
					panel.add(new JLabel(getImageFigure(c.getDestino()[k])), cons);
				}
				i++;
			}
		}

		add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
		this.setTitle(labels.getString(ConnectionRecommendationAction.ID));
	}

	private int numRows(ArrayList<ValidationRule> rules) {
		int sum = 0;
		for (ValidationRule r : rules) {
			for (Class c : r.getOrigem()) {
				sum++;
			}
		}
		return sum;
	}

	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	private String getNameFigure(Class c) {
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
		return labels.getTip(getLabelFigureName(c));
	}

	private ImageIcon getImageFigure(Class c) {
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
		// return labels.getImageIcon(getLabelFigureName(c), c);		
		return new ImageIcon(getScaledImage(labels.getImageIconRecommendation(getLabelFigureName(c), c).getImage(), 40, 40));
	}

	public String getLabelFigureName(Class c) {
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
