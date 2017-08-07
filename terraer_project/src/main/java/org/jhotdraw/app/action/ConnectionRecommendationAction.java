package org.jhotdraw.app.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.Color;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.jhotdraw.draw.AtributoChaveFigure;
import org.jhotdraw.draw.AtributoChaveParcialFigure;
import org.jhotdraw.draw.AtributoDerivadoFigure;
import org.jhotdraw.draw.AtributoFigure;
import org.jhotdraw.draw.AtributoMultivaloradoFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ConnectionAttribute;
import org.jhotdraw.draw.DisjuncaoFigure;
import org.jhotdraw.draw.DoubleLineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
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
import org.jhotdraw.draw.UniaoFigure;
import org.jhotdraw.draw.action.AbstractSelectedAction;
import org.jhotdraw.draw.action.ModelValidationRules;
import org.jhotdraw.draw.action.ModelValidationRules.ValidationRule;
import org.jhotdraw.util.ResourceBundleUtil;

public class ConnectionRecommendationAction extends AbstractSelectedAction {

	public final static String ID = "recomendation";

	public ConnectionRecommendationAction(DrawingEditor editor) {
		super(editor);
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
		labels.configureAction(this, ID);
		//putValue(AbstractAction.NAME, labels.get);
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DrawingView v = this.getView();
		if (v.getSelectedFigures() != null && v.getSelectedFigures().size() == 1
				&& v.getSelectedFigures().toArray()[0] instanceof LineConnectionFigure) {
			LineConnectionFigure line = (LineConnectionFigure) v.getSelectedFigures().iterator().next();
			if (line.getAttribute(AttributeKeys.TEXT_COLOR).equals(Color.red)) {
				ModelValidationRules mvr = new ModelValidationRules();
				desenhar(line, mvr.getOthersConnections(line), mvr.getOthersFigures(line));
			}
		}
	}

	

	public void desenhar(LineConnectionFigure lcf, ArrayList<Class> connections, ArrayList<ValidationRule> validRule) {
		
		JPanel panel = new JPanel();
		JTextArea ta = new JTextArea();
		ta.setText("Para que essa ligação seja válida utilize uma das opções abaixo:"); //internacionalizacao
		ta.setEditable(false);
		panel.add(ta);
		if (connections.size() == 0){
			ta = new JTextArea();
			ta.setText("A ligação entre esses dois elementos não possui nenhuma conexão válida"); //internacionalizacao
			ta.setEditable(false);
			panel.add(ta);
		} else {
			for (Class c : connections) {
	//			ta = new JTextArea();
	//			ta.setText(getNameFigure(c));
	//			ta.setEditable(false);
	//			panel.add(ta);
				String name = getNameFigure(c);
				JButton btn = new JButton(name);
				InsertNewConnectionAction nca = new InsertNewConnectionAction(lcf, c, this.getDrawing());
				btn.addActionListener(nca);
				panel.add(btn);
			}
		}
		ta = new JTextArea();
		ta.setText("A conexão utilizada é válida entre os seguintes elementos:"); //internacionalizacao
		ta.setEditable(false);
		panel.add(ta);
		StringBuilder sb;
		for (ValidationRule c : validRule) {
			ta = new JTextArea();
			sb = new StringBuilder();
//			sb.append("A conexão ");
//			sb.append(getNameFigure(c.getConexao()));
			sb.append("Do elemento ");
			sb.append(getNameFigure(c.getOrigem()));
			sb.append(" para o elemento ");
			sb.append(getNameFigure(c.getDestino()));
			if (c.getBidirecional()){
				sb.append(" e vice-versa.");
			}
			ta.setText(sb.toString());
			ta.setEditable(false);
			panel.add(ta);
		}
		
		panel.add(new JScrollPane());
		JFrame frame = new JFrame("");
		frame.add(panel);
		frame.setSize(600, 480);
		frame.setVisible(true);
	}
	
	private String getNameFigure(Class c){ //trocar para internacionalização
		if (c.equals(EntidadeFigure.class)){
			return "Entidade";
		} else if (c.equals(EntidadeRelacionamentoFigure.class)){
			return "Entidade Relacionamento";
		} else if (c.equals(EntidadeFracaFigure.class)){
			return "Entidade Fraca";
		} else if (c.equals(AtributoFigure.class)){
			return "Atributo";
		} else if (c.equals(AtributoChaveFigure.class)){
			return "Atributo Chave";
		} else if (c.equals(AtributoChaveParcialFigure.class)){
			return "Atributo Chave Parcial";
		} else if (c.equals(AtributoMultivaloradoFigure.class)){
			return "Atributo Multivalorado";
		} else if (c.equals(AtributoDerivadoFigure.class)){
			return "Atributo Derivado";
		} else if (c.equals(RelacionamentoFigure.class)){
			return "Relacionamento";
		} else if (c.equals(RelacionamentoFracoFigure.class)){
			return "Relacionamento Fraco";
		} else if (c.equals(UniaoFigure.class)){
			return "União";
		} else if (c.equals(SobreposicaoFigure.class)){
			return "Sobreposição";
		} else if (c.equals(DisjuncaoFigure.class)){
			return "Disjunção";
		} else if (c.equals(ConnectionAttribute.class)){
			return "Conexão de atributo";
		} else if (c.equals(LabeledLineConnectionUmFigure.class)){
			return "Conexão opcional '1 para'";
		} else if (c.equals(LabeledLineConnectionMuitosFigure.class)){
			return "Conexão opcional 'n para'";
		} else if (c.equals(LabeledDoubleLineConnectionUmFigure.class)){
			return "Conexão obrigatória '1 para'";
		} else if (c.equals(LabeledDoubleLineConnectionMuitosFigure.class)){
			return "Conexão obrigatória 'n para'";
		} else if (c.equals(LineConnectionGeneralizacaoFigure.class)){
			return "Conexão geral opcional";
		} else if (c.equals(DoubleLineConnectionGeneralizacaoFigure.class)){
			return "Conexão geral obrigatória";
		} else if (c.equals(GeneralizacaoLineConnectionFigure.class)){
			return "Conexão de generalização";
		}  
		return null;
	}

}
