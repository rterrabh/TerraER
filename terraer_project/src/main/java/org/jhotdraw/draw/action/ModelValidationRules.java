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
			if((regra.origemContains(origem.getClass()) && regra.destinoContains(destino.getClass())) || 
					(regra.bidirecional && regra.origemContains(destino.getClass()) && regra.destinoContains(origem.getClass()))){
				connections.add(regra.conexao);
			}
		}
		return connections;
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
		
		Class[] destino = {EntidadeFigure.class, EntidadeFracaFigure.class, EntidadeRelacionamentoFigure.class};
		Class[] origem = {RelacionamentoFigure.class, RelacionamentoFracoFigure.class};
		rules.add(new ValidationRule (origem, LabeledLineConnectionUmFigure.class, destino, true));
		rules.add(new ValidationRule (origem, LabeledLineConnectionMuitosFigure.class, destino, true));
		rules.add(new ValidationRule (origem, LabeledDoubleLineConnectionUmFigure.class, destino, true));
		rules.add(new ValidationRule (origem, LabeledDoubleLineConnectionMuitosFigure.class, destino, true));
		
		Class[] origem2 = {EntidadeFigure.class, EntidadeFracaFigure.class, EntidadeRelacionamentoFigure.class};
		Class[] destino2 = {UniaoFigure.class, SobreposicaoFigure.class, DisjuncaoFigure.class};
		rules.add(new ValidationRule (origem2, LineConnectionGeneralizacaoFigure.class, destino2, true));
		rules.add(new ValidationRule (origem2, DoubleLineConnectionGeneralizacaoFigure.class, destino2, true));
		
		Class[] origem3 = {EntidadeFigure.class, EntidadeFracaFigure.class, EntidadeRelacionamentoFigure.class};
		Class[] destino3 = {AtributoFigure.class, AtributoMultivaloradoFigure.class, AtributoDerivadoFigure.class};
		rules.add(new ValidationRule (origem3, ConnectionAttribute.class, destino3, true));
		
		Class[] origem4 = {RelacionamentoFigure.class, RelacionamentoFracoFigure.class};
		Class[] destino4 = {AtributoFigure.class, AtributoMultivaloradoFigure.class, AtributoDerivadoFigure.class};
		rules.add(new ValidationRule (origem4, ConnectionAttribute.class, destino4, true));
				
		Class[] destino6 = {UniaoFigure.class, SobreposicaoFigure.class, DisjuncaoFigure.class};
		Class[] origem6 = {EntidadeFigure.class};
		rules.add(new ValidationRule (origem6, GeneralizacaoLineConnectionFigure.class, destino6, false));		
		
		Class[] origem8 = {EntidadeFigure.class};
		Class[] destino8 = {EntidadeRelacionamentoFigure.class};
		rules.add(new ValidationRule (origem8, LabeledLineConnectionUmFigure.class, destino8, true));
		rules.add(new ValidationRule (origem8, LabeledLineConnectionMuitosFigure.class, destino8, true));
		rules.add(new ValidationRule (origem8, LabeledDoubleLineConnectionUmFigure.class, destino8, true));
		rules.add(new ValidationRule (origem8, LabeledDoubleLineConnectionMuitosFigure.class, destino8, true));
		
		Class[] origem9 = {EntidadeFracaFigure.class};
		Class[] destino9 = {EntidadeRelacionamentoFigure.class};
		rules.add(new ValidationRule (origem9, LabeledLineConnectionUmFigure.class, destino9, true));
		rules.add(new ValidationRule (origem9, LabeledLineConnectionMuitosFigure.class, destino9, true));
		rules.add(new ValidationRule (origem9, LabeledDoubleLineConnectionUmFigure.class, destino9, true));
		rules.add(new ValidationRule (origem9, LabeledDoubleLineConnectionMuitosFigure.class, destino9, true));
		
		Class[] destino10 = {EntidadeFigure.class, EntidadeRelacionamentoFigure.class};
		Class[] origem10 = {AtributoChaveFigure.class};
		rules.add(new ValidationRule (origem10, ConnectionAttribute.class, destino10, true));
		
		Class[] origem7 = {EntidadeFigure.class};
		Class[] destino7 = {EntidadeFigure.class};
		rules.add(new ValidationRule (origem7, GeneralizacaoLineConnectionFigure.class, destino7, true));
		
		Class[] origem5 = {AtributoFigure.class};
		Class[] destino5 = {AtributoFigure.class};
		rules.add(new ValidationRule (origem5, ConnectionAttribute.class, destino5, true));
		
		Class[] destino12 = {EntidadeFracaFigure.class};
		Class[] origem12 = {AtributoChaveParcialFigure.class};
		rules.add(new ValidationRule (origem12, ConnectionAttribute.class, destino12, true));
	}

	public class ValidationRule {
		private Class[] origem;
		private Class[] destino;
		private Class conexao;
		private boolean bidirecional;

		public ValidationRule(Class[] origem, Class conexao, Class[] destino, boolean bidirecional) {
			this.bidirecional = bidirecional;
			this.origem = origem;
			this.destino = destino;
			this.conexao = conexao;
		}
		
		public Class[] getOrigem(){
			return origem;
		}
		
		public Class[] getDestino(){
			return destino;
		}
		
		public Class getConexao(){
			return conexao;
		}
		
		public boolean getBidirecional(){
			return bidirecional;
		}
		
		public boolean origemContains(Class c){
			for (Class c2 : origem){
				if(c2.equals(c)){
					return true;
				}
			}
			return false;
		}
		
		public boolean destinoContains(Class c){
			for (Class c2 : destino){
				if(c2.equals(c)){
					return true;
				}
			}
			return false;
		}
	}

}
