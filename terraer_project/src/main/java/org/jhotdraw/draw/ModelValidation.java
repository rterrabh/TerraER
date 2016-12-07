package org.jhotdraw.draw;

import java.awt.Color;

public class ModelValidation{
	
	public ModelValidation(){
	};
	public Color validateLineConnection(LineConnectionFigure conn){		
		//Labeled Connection 
		//Entity ---> Relationship
		if (conn.getClass().equals(LabeledDoubleLineConnectionMuitosFigure.class) || 
				conn.getClass().equals(LabeledDoubleLineConnectionUmFigure.class) ||
				conn.getClass().equals(LabeledLineConnectionMuitosFigure.class) ||
				conn.getClass().equals(LabeledLineConnectionUmFigure.class)){
			if (! (conn.getStartFigure() instanceof EntidadeFigure &&
					conn.getEndFigure() instanceof RelacionamentoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeFigure &&
					conn.getStartFigure() instanceof RelacionamentoFigure) &&					
		//Labeled Connection
		//Entity ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeFigure &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigure) &&	
				! (conn.getEndFigure() instanceof EntidadeFigure &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigure) &&
		//Labeled Connection
		//Entity ---> Entity Relationship
				! (conn.getStartFigure() instanceof EntidadeFigure &&
					conn.getEndFigure() instanceof EntidadeRelacionamentoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeFigure &&
						conn.getStartFigure() instanceof EntidadeRelacionamentoFigure) &&
		//Labeled Connection
		//Weak Entity ---> Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigure &&
					conn.getEndFigure() instanceof RelacionamentoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigure &&
						conn.getStartFigure() instanceof RelacionamentoFigure) &&
		//Labeled Connection
		//Weak Entity ---> Entity Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigure &&
					conn.getEndFigure() instanceof EntidadeRelacionamentoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigure &&
						conn.getStartFigure() instanceof EntidadeRelacionamentoFigure) &&
		//Labeled Connection
		//Weak Entity ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigure &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigure &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigure) &&
		//Labeled Connection
		//Entity Relationship ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigure) &&
		//Labeled Connection
		//Entity Relationship ---> Relationship
				! (conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
					conn.getEndFigure() instanceof RelacionamentoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getStartFigure() instanceof RelacionamentoFigure)
					){
				return Color.red;
			}	
		}
		//Connection 
		//Entity <---> Attribute
		else if (conn.getClass().equals(LineConnectionFigure.class) ){
			if (!(conn.getStartFigure() instanceof EntidadeFigure &&
					conn.getEndFigure() instanceof AtributoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFigure &&
						conn.getStartFigure() instanceof AtributoFigure) &&
		//Connection 
		//Weak Entity <---> Attribute
				!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
					conn.getEndFigure() instanceof AtributoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
					conn.getStartFigure() instanceof AtributoFigure) &&
		//Connection 
		//Attribute <---> Attribute
				!(conn.getStartFigure() instanceof AtributoFigure &&
					conn.getEndFigure() instanceof AtributoFigure) &&
				!(conn.getEndFigure() instanceof AtributoFigure &&
					conn.getStartFigure() instanceof AtributoFigure) &&
		//Connection 
		//Entity Relationship <---> Attribute
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
					conn.getEndFigure() instanceof AtributoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
					conn.getStartFigure() instanceof AtributoFigure) &&
		//Connection
		//Entity <---> Key Attribute
					!(conn.getStartFigure() instanceof EntidadeFigure &&
							conn.getEndFigure() instanceof AtributoChaveFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFigure &&
							conn.getStartFigure() instanceof AtributoChaveFigure) &&
		//Connection
		//Weak Entity <---> Key Attribute (REMOVED DUE TO THE PARTIAL KEY)
					/*!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
							conn.getEndFigure() instanceof AtributoChaveFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
							conn.getStartFigure() instanceof AtributoChaveFigure) &&*/
		//Connection
		//Weak Entity <---> Partial Key Attribute
					!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
							conn.getEndFigure() instanceof AtributoChaveParcialFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
							conn.getStartFigure() instanceof AtributoChaveParcialFigure) &&							
		//Connection 
		//Entity Relationship <---> Key Attribute
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
					conn.getEndFigure() instanceof AtributoChaveFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
					conn.getStartFigure() instanceof AtributoChaveFigure) &&
		//Connection
		//Entity <---> Multivalue Attribute
					!(conn.getStartFigure() instanceof EntidadeFigure &&
							conn.getEndFigure() instanceof AtributoMultivaloradoFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFigure &&
							conn.getStartFigure() instanceof AtributoMultivaloradoFigure)&&
		//Connection
		//Weak Entity <---> Multivalue Attribute
					!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
							conn.getEndFigure() instanceof AtributoMultivaloradoFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
							conn.getStartFigure() instanceof AtributoMultivaloradoFigure) &&
		//Connection
		//Entity Relationship <---> Multivalue Attribute
					!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
							conn.getEndFigure() instanceof AtributoMultivaloradoFigure) &&
					!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
							conn.getStartFigure() instanceof AtributoMultivaloradoFigure) &&	
		//Connection
		//Entity <---> Derived Attribute
					!(conn.getStartFigure() instanceof EntidadeFigure &&
							conn.getEndFigure() instanceof AtributoDerivadoFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFigure &&
							conn.getStartFigure() instanceof AtributoDerivadoFigure) &&
		//Connection
		//Weak Entity <---> Derived Attribute
					!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
							conn.getEndFigure() instanceof AtributoDerivadoFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
							conn.getStartFigure() instanceof AtributoDerivadoFigure) &&
		//Connection
		//Entity Relationship <---> Derived Attribute
					!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
							conn.getEndFigure() instanceof AtributoDerivadoFigure) &&
					!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
							conn.getStartFigure() instanceof AtributoDerivadoFigure) &&
		//Connection
		//Relationship <---> Attribute 
					!(conn.getStartFigure() instanceof RelacionamentoFigure &&
						conn.getEndFigure() instanceof AtributoFigure ) &&
					!(conn.getEndFigure() instanceof RelacionamentoFigure &&
						conn.getStartFigure() instanceof AtributoFigure) &&
						
		//Connection
		//Relationship <---> Key Attribute
					!(conn.getStartFigure() instanceof RelacionamentoFigure &&
							conn.getEndFigure() instanceof AtributoChaveFigure) &&
					!(conn.getEndFigure() instanceof RelacionamentoFigure &&
							conn.getStartFigure() instanceof AtributoChaveFigure) &&
		//Connection
		//Relationship <---> Derived Attribute
					!(conn.getStartFigure() instanceof RelacionamentoFigure &&
							conn.getEndFigure() instanceof AtributoDerivadoFigure) &&
					!(conn.getEndFigure() instanceof RelacionamentoFigure &&
							conn.getStartFigure() instanceof AtributoDerivadoFigure) &&
		//Connection
		//Relationship <---> Multivalue Attribute
					!(conn.getStartFigure() instanceof RelacionamentoFigure &&
							conn.getEndFigure() instanceof AtributoMultivaloradoFigure) &&
					!(conn.getEndFigure() instanceof RelacionamentoFigure &&
							conn.getStartFigure() instanceof AtributoMultivaloradoFigure) &&
		//Connection
		//Weak Relationship <---> Attribute 
					!(conn.getStartFigure() instanceof RelacionamentoFracoFigure &&
						conn.getEndFigure() instanceof AtributoFigure ) &&
					!(conn.getEndFigure() instanceof RelacionamentoFracoFigure &&
						conn.getStartFigure() instanceof AtributoFigure) &&
						
		//Connection
		//Weak Relationship <---> Key Attribute
					!(conn.getStartFigure() instanceof RelacionamentoFracoFigure &&
							conn.getEndFigure() instanceof AtributoChaveFigure) &&
					!(conn.getEndFigure() instanceof RelacionamentoFracoFigure &&
							conn.getStartFigure() instanceof AtributoChaveFigure) &&
		//Connection
		//Weak Relationship Relationship <---> Derived Attribute
					!(conn.getStartFigure() instanceof RelacionamentoFracoFigure &&
							conn.getEndFigure() instanceof AtributoDerivadoFigure) &&
					!(conn.getEndFigure() instanceof RelacionamentoFracoFigure &&
							conn.getStartFigure() instanceof AtributoDerivadoFigure) &&
		//Connection
		//Weak Relationship Relationship <---> Multivalue Attribute
					!(conn.getStartFigure() instanceof RelacionamentoFracoFigure &&
							conn.getEndFigure() instanceof AtributoMultivaloradoFigure) &&
					!(conn.getEndFigure() instanceof RelacionamentoFracoFigure &&
							conn.getStartFigure() instanceof AtributoMultivaloradoFigure)
					){
		
				return Color.red;
			}	
		}
		//Connection in Disjunction or Overlap
		//Entity <---> Disjunction
		else if (conn.getClass().equals(LineConnectionGeneralizacaoFigure.class) ||
				conn.getClass().equals(DoubleLineConnectionGeneralizacaoFigure.class)){
			if (!(conn.getStartFigure() instanceof EntidadeFigure &&
					conn.getEndFigure() instanceof DisjuncaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFigure &&
					conn.getStartFigure() instanceof DisjuncaoFigure)&&
		//Connection in Disjunction or Overlap
		//Entity <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeFigure &&
						conn.getEndFigure() instanceof SobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFigure &&
						conn.getStartFigure() instanceof SobreposicaoFigure) &&
		//Connection in Disjunction or Overlap
		//Entity Relationship <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getEndFigure() instanceof SobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getStartFigure() instanceof SobreposicaoFigure) &&
		//Connection in Disjunction or Overlap
		//Entity Relationship <---> Disjunction
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getEndFigure() instanceof DisjuncaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getStartFigure() instanceof DisjuncaoFigure)&&
		//Connection in Disjunction or Overlap
		//Weak Entity <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
						conn.getEndFigure() instanceof SobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
						conn.getStartFigure() instanceof SobreposicaoFigure)&&
		//Connection in Disjunction or Overlap
		//Weak Entity <---> Disjunction
				!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
						conn.getEndFigure() instanceof DisjuncaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
						conn.getStartFigure() instanceof DisjuncaoFigure)
					){
				return Color.red;
			}
		}
		//Generalization Connection
		//Disjunction ---> Entity
		else if (conn.getClass().equals(GeneralizacaoLineConnectionFigure.class)){
			if (!(conn.getStartFigure() instanceof DisjuncaoFigure &&
					conn.getEndFigure() instanceof EntidadeFigure) &&
		//Generalization Connection
		//Overlap ---> Entity
				!(conn.getStartFigure() instanceof SobreposicaoFigure &&
					conn.getEndFigure() instanceof EntidadeFigure) &&
		//Generalization Connection
		//Entity ---> Entity (right according to book 2nd version of book "Database Design Using Entity-Relationship Diagrams")
				!(conn.getStartFigure() instanceof EntidadeFigure &&
					conn.getEndFigure() instanceof EntidadeFigure)					
		){
				return Color.red;
			}
		}
		return Color.black;
	}
}