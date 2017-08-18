package org.jhotdraw.draw.action;

import java.util.ArrayList;

import org.jhotdraw.draw.AtributoChaveFigure;
import org.jhotdraw.draw.AtributoChaveParcialFigure;
import org.jhotdraw.draw.AtributoDerivadoFigure;
import org.jhotdraw.draw.AtributoFigure;
import org.jhotdraw.draw.AtributoMultivaloradoFigure;
import org.jhotdraw.draw.ConnectionAttribute;
import org.jhotdraw.draw.DisjuncaoFigure;
import org.jhotdraw.draw.DoubleLineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.EntidadeFigure;
import org.jhotdraw.draw.EntidadeFracaFigure;
import org.jhotdraw.draw.EntidadeRelacionamentoFigure;
import org.jhotdraw.draw.Figure;
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

public class ModelValidationRules {

	private static ArrayList<ValidationRule> rules = null;
	private static ModelValidationRules INSTANCE = null; 

	private ModelValidationRules() {
		init();
	}
	
	public static ModelValidationRules getInstance(){
		if (INSTANCE == null){
			INSTANCE = new ModelValidationRules();
		}
		return INSTANCE;
	}
	
	public ArrayList<Class> getOthersConnections(LineConnectionFigure lcf) {
		ArrayList<Class> connections = new ArrayList<>();
		Figure origem = lcf.getStartFigure();
		Figure destino = lcf.getEndFigure();
		for (ValidationRule regra : this.rules) {
			if ((origem.getClass().equals(regra.origem)
					&& destino.getClass().equals(regra.destino))
					|| (regra.bidirecional && destino.getClass().equals(regra.origem)
							&& origem.getClass().equals(regra.destino))) {
				connections.add(regra.conexao);
			}
		}
		return connections; // se vazio ligacao nao pd existir
	}

	public ArrayList<ValidationRule> getOthersFigures(LineConnectionFigure lcf) {
		ArrayList<ValidationRule> figures = new ArrayList<>();
		for (ValidationRule regra : this.rules) {
			if (lcf.getClass().equals(regra.conexao)) {
				figures.add(regra);
			}
		}
		return figures;
	}

	public void init() {
		rules = new ArrayList<>();

		rules.add(new ValidationRule(EntidadeFigure.class, ConnectionAttribute.class, AtributoChaveFigure.class, true));

		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, ConnectionAttribute.class,
				AtributoChaveFigure.class, true));

		rules.add(new ValidationRule(EntidadeFracaFigure.class, ConnectionAttribute.class,
				AtributoChaveParcialFigure.class, true));

		rules.add(new ValidationRule(AtributoFigure.class, ConnectionAttribute.class, AtributoFigure.class, true));

		rules.add(
				new ValidationRule(RelacionamentoFigure.class, ConnectionAttribute.class, AtributoFigure.class, true));
		rules.add(new ValidationRule(RelacionamentoFigure.class, ConnectionAttribute.class,
				AtributoDerivadoFigure.class, true));
		rules.add(new ValidationRule(RelacionamentoFigure.class, ConnectionAttribute.class,
				AtributoMultivaloradoFigure.class, true));

		rules.add(new ValidationRule(RelacionamentoFracoFigure.class, ConnectionAttribute.class, AtributoFigure.class,
				true));
		rules.add(new ValidationRule(RelacionamentoFracoFigure.class, ConnectionAttribute.class,
				AtributoDerivadoFigure.class, true));
		rules.add(new ValidationRule(RelacionamentoFracoFigure.class, ConnectionAttribute.class,
				AtributoMultivaloradoFigure.class, true));

		rules.add(new ValidationRule(EntidadeFigure.class, ConnectionAttribute.class, AtributoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, ConnectionAttribute.class, AtributoDerivadoFigure.class,
				true));
		rules.add(new ValidationRule(EntidadeFigure.class, ConnectionAttribute.class, AtributoMultivaloradoFigure.class,
				true));

		rules.add(new ValidationRule(EntidadeFracaFigure.class, ConnectionAttribute.class, AtributoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, ConnectionAttribute.class, AtributoDerivadoFigure.class,
				true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, ConnectionAttribute.class,
				AtributoMultivaloradoFigure.class, true));

		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, ConnectionAttribute.class,
				AtributoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, ConnectionAttribute.class,
				AtributoDerivadoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, ConnectionAttribute.class,
				AtributoMultivaloradoFigure.class, true));

		rules.add(new ValidationRule(EntidadeFigure.class, LabeledLineConnectionUmFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledLineConnectionMuitosFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				RelacionamentoFigure.class, true));

		rules.add(new ValidationRule(EntidadeFigure.class, LabeledLineConnectionUmFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledLineConnectionMuitosFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledLineConnectionUmFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledLineConnectionMuitosFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				RelacionamentoFigure.class, true));

		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledLineConnectionUmFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledLineConnectionMuitosFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledLineConnectionUmFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledLineConnectionMuitosFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				RelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				RelacionamentoFigure.class, true));

		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledLineConnectionUmFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledLineConnectionMuitosFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				RelacionamentoFracoFigure.class, true));
		rules.add(new ValidationRule(EntidadeRelacionamentoFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(EntidadeFigure.class, LabeledLineConnectionUmFigure.class,
				EntidadeRelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledLineConnectionMuitosFigure.class,
				EntidadeRelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				EntidadeRelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				EntidadeRelacionamentoFigure.class, true));

		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledLineConnectionUmFigure.class,
				EntidadeRelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledLineConnectionMuitosFigure.class,
				EntidadeRelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledDoubleLineConnectionUmFigure.class,
				EntidadeRelacionamentoFigure.class, true));
		rules.add(new ValidationRule(EntidadeFracaFigure.class, LabeledDoubleLineConnectionMuitosFigure.class,
				EntidadeRelacionamentoFigure.class, true));

		rules.add(new ValidationRule(SobreposicaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				EntidadeFigure.class, true));
		rules.add(new ValidationRule(SobreposicaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				EntidadeFracaFigure.class, true));
		rules.add(new ValidationRule(SobreposicaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(SobreposicaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				EntidadeFigure.class, true));
		rules.add(new ValidationRule(SobreposicaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				EntidadeFracaFigure.class, true));
		rules.add(new ValidationRule(SobreposicaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(DisjuncaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				EntidadeFigure.class, true));
		rules.add(new ValidationRule(DisjuncaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				EntidadeFracaFigure.class, true));
		rules.add(new ValidationRule(DisjuncaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(DisjuncaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				EntidadeFigure.class, true));
		rules.add(new ValidationRule(DisjuncaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				EntidadeFracaFigure.class, true));
		rules.add(new ValidationRule(DisjuncaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(UniaoFigure.class, LineConnectionGeneralizacaoFigure.class, EntidadeFigure.class,
				true));
		rules.add(new ValidationRule(UniaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				EntidadeFracaFigure.class, true));
		rules.add(new ValidationRule(UniaoFigure.class, LineConnectionGeneralizacaoFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(UniaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				EntidadeFigure.class, true));
		rules.add(new ValidationRule(UniaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				EntidadeFracaFigure.class, true));
		rules.add(new ValidationRule(UniaoFigure.class, DoubleLineConnectionGeneralizacaoFigure.class,
				RelacionamentoFracoFigure.class, true));

		rules.add(new ValidationRule(SobreposicaoFigure.class, GeneralizacaoLineConnectionFigure.class,
				EntidadeFigure.class, false));
		rules.add(new ValidationRule(DisjuncaoFigure.class, GeneralizacaoLineConnectionFigure.class,
				EntidadeFigure.class, false));
		rules.add(new ValidationRule(UniaoFigure.class, GeneralizacaoLineConnectionFigure.class, EntidadeFigure.class,
				false));

		rules.add(new ValidationRule(EntidadeFigure.class, GeneralizacaoLineConnectionFigure.class,
				EntidadeFigure.class, true));
	}

	public class ValidationRule {
		private Class origem;
		private Class destino;
		private Class conexao;
		private boolean bidirecional;

		public ValidationRule(Class origem, Class conexao, Class destino, boolean bidirecional) {
			this.bidirecional = bidirecional;
			this.origem = origem;
			this.destino = destino;
			this.conexao = conexao;
		}
		
		public Class getOrigem(){
			return origem;
		}
		
		public Class getDestino(){
			return destino;
		}
		
		public Class getConexao(){
			return conexao;
		}
		
		public boolean getBidirecional(){
			return bidirecional;
		}
	}

}
