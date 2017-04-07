package org.jhotdraw.draw;

import java.awt.Color;

import org.jhotdraw.draw.notation.figure.chen.AtributoChaveFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoChaveParcialFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoDerivadoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoMultivaloradoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.InheritanceDisjuncaoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.GeneralizacaoConnectionTotalFigureChen;
import org.jhotdraw.draw.notation.figure.chen.EntidadeFigureChen;
import org.jhotdraw.draw.notation.figure.chen.EntidadeFracaFigureChen;
import org.jhotdraw.draw.notation.figure.chen.EntidadeRelacionamentoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.GeneralizacaoConnectionParcialFigureChen;
import org.jhotdraw.draw.notation.figure.chen.ConnectionTotalMuitosFigureChen;
import org.jhotdraw.draw.notation.figure.chen.ConnectionTotalUmFigureChen;
import org.jhotdraw.draw.notation.figure.chen.ConnectionParcialMuitosFigureChen;
import org.jhotdraw.draw.notation.figure.chen.ConnectionParcialUmFigureChen;
import org.jhotdraw.draw.notation.figure.chen.GeneralizacaoConnectionLineFigureChen;
import org.jhotdraw.draw.notation.figure.chen.RelacionamentoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.RelacionamentoFracoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.InheritanceSobreposicaoFigure;
import org.jhotdraw.draw.notation.figure.chen.InheritanceUniaoFigure;

public class ModelValidation{
	
	public ModelValidation(){
	};
	public Color validateLineConnection(LineConnectionFigure conn){		

		if (conn.getClass().equals(ConnectionTotalMuitosFigureChen.class) || 
				conn.getClass().equals(ConnectionTotalUmFigureChen.class)){
		//Labeled Connection 
		//Entity ---> Relationship
			if (! (conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFigureChen &&
					conn.getStartFigure() instanceof RelacionamentoFigureChen) &&					
		//Labeled Connection
		//Entity ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigureChen) &&	
				! (conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigureChen) &&
		//Labeled Connection
		//Entity ---> Entity Relationship
				! (conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen) &&
		//Labeled Connection
		//Weak Entity ---> Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFigureChen) &&
		//Labeled Connection
		//Weak Entity ---> Entity Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
					conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen) &&
		//Labeled Connection
		//Weak Entity ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigureChen) &&
		//Labeled Connection
		//Entity Relationship ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigureChen) &&
		//Labeled Connection
		//Entity Relationship ---> Relationship
				! (conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFigureChen)
					){
				return Color.red;
			}	
		}
		
		else if ( conn.getClass().equals(ConnectionParcialMuitosFigureChen.class) ||
				conn.getClass().equals(ConnectionParcialUmFigureChen.class)){
		//Labeled Connection 
		//Entity ---> Relationship
			if (! (conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFigureChen &&
					conn.getStartFigure() instanceof RelacionamentoFigureChen) &&					
		//Labeled Connection
		//Entity ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigureChen) &&	
				! (conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigureChen) &&
		//Labeled Connection
		//Entity ---> Entity Relationship
				! (conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen) &&
		//Labeled Connection
		//Weak Entity ---> Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFigureChen) &&
		//Labeled Connection
		//Weak Entity ---> Entity Relationship
				! (conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
					conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen) &&
		//Labeled Connection
		//Weak Entity ---> Weak Relationship
				/*
				! (conn.getStartFigure() instanceof EntidadeFracaFigure &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigure) &&
				! (conn.getEndFigure() instanceof EntidadeFracaFigure &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigure) &&
				*/
		//Labeled Connection
		//Entity Relationship ---> Weak Relationship
				! (conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFracoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFracoFigureChen) &&
		//Labeled Connection
		//Entity Relationship ---> Relationship
				! (conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getEndFigure() instanceof RelacionamentoFigureChen) &&
				! (conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof RelacionamentoFigureChen)
					){
				return Color.red;
			}	
		}


		else if (conn.getClass().equals(LineConnectionFigure.class) ){
		//Connection 
		//Entity <---> Attribute
			if (!(conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof AtributoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof AtributoFigureChen) &&
		//Connection 
		//Weak Entity <---> Attribute
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
					conn.getEndFigure() instanceof AtributoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
					conn.getStartFigure() instanceof AtributoFigureChen) &&
		//Connection 
		//Attribute <---> Attribute
				!(conn.getStartFigure() instanceof AtributoFigureChen &&
					conn.getEndFigure() instanceof AtributoFigureChen) &&
				!(conn.getEndFigure() instanceof AtributoFigureChen &&
					conn.getStartFigure() instanceof AtributoFigureChen) &&
		//Connection 
		//Entity Relationship <---> Attribute
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getEndFigure() instanceof AtributoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getStartFigure() instanceof AtributoFigureChen) &&
		//Connection
		//Entity <---> Key Attribute
				!(conn.getStartFigure() instanceof EntidadeFigureChen &&
						conn.getEndFigure() instanceof AtributoChaveFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof AtributoChaveFigureChen) &&
		//Connection
		//Weak Entity <---> Key Attribute (REMOVED DUE TO THE PARTIAL KEY)
					/*!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
							conn.getEndFigure() instanceof AtributoChaveFigure) &&
					!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
							conn.getStartFigure() instanceof AtributoChaveFigure) &&*/
		//Connection
		//Weak Entity <---> Partial Key Attribute
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof AtributoChaveParcialFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof AtributoChaveParcialFigureChen) &&							
		//Connection 
		//Entity Relationship <---> Key Attribute
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getEndFigure() instanceof AtributoChaveFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
					conn.getStartFigure() instanceof AtributoChaveFigureChen) &&
		//Connection
		//Entity <---> Multivalue Attribute
				!(conn.getStartFigure() instanceof EntidadeFigureChen &&
						conn.getEndFigure() instanceof AtributoMultivaloradoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof AtributoMultivaloradoFigureChen)&&
		//Connection
		//Weak Entity <---> Multivalue Attribute
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof AtributoMultivaloradoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof AtributoMultivaloradoFigureChen) &&
		//Connection
		//Entity Relationship <---> Multivalue Attribute
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getEndFigure() instanceof AtributoMultivaloradoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof AtributoMultivaloradoFigureChen) &&	
		//Connection
		//Entity <---> Derived Attribute
				!(conn.getStartFigure() instanceof EntidadeFigureChen &&
						conn.getEndFigure() instanceof AtributoDerivadoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof AtributoDerivadoFigureChen) &&
		//Connection
		//Weak Entity <---> Derived Attribute
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof AtributoDerivadoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof AtributoDerivadoFigureChen) &&
		//Connection
		//Entity Relationship <---> Derived Attribute
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getEndFigure() instanceof AtributoDerivadoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof AtributoDerivadoFigureChen) &&
		//Connection
		//Relationship <---> Attribute 
				!(conn.getStartFigure() instanceof RelacionamentoFigureChen &&
					conn.getEndFigure() instanceof AtributoFigureChen ) &&
				!(conn.getEndFigure() instanceof RelacionamentoFigureChen &&
					conn.getStartFigure() instanceof AtributoFigureChen) &&
		//Connection
		//Relationship <---> Key Attribute
				!(conn.getStartFigure() instanceof RelacionamentoFigureChen &&
						conn.getEndFigure() instanceof AtributoChaveFigureChen) &&
				!(conn.getEndFigure() instanceof RelacionamentoFigureChen &&
						conn.getStartFigure() instanceof AtributoChaveFigureChen) &&
		//Connection
		//Relationship <---> Derived Attribute
				!(conn.getStartFigure() instanceof RelacionamentoFigureChen &&
						conn.getEndFigure() instanceof AtributoDerivadoFigureChen) &&
				!(conn.getEndFigure() instanceof RelacionamentoFigureChen &&
						conn.getStartFigure() instanceof AtributoDerivadoFigureChen) &&
		//Connection
		//Relationship <---> Multivalue Attribute
				!(conn.getStartFigure() instanceof RelacionamentoFigureChen &&
						conn.getEndFigure() instanceof AtributoMultivaloradoFigureChen) &&
				!(conn.getEndFigure() instanceof RelacionamentoFigureChen &&
						conn.getStartFigure() instanceof AtributoMultivaloradoFigureChen) &&
		//Connection
		//Weak Relationship <---> Attribute 
				!(conn.getStartFigure() instanceof RelacionamentoFracoFigureChen &&
					conn.getEndFigure() instanceof AtributoFigureChen ) &&
				!(conn.getEndFigure() instanceof RelacionamentoFracoFigureChen &&
					conn.getStartFigure() instanceof AtributoFigureChen) &&
						
		//Connection
		//Weak Relationship <---> Key Attribute
				!(conn.getStartFigure() instanceof RelacionamentoFracoFigureChen &&
					conn.getEndFigure() instanceof AtributoChaveFigureChen) &&
				!(conn.getEndFigure() instanceof RelacionamentoFracoFigureChen &&
					conn.getStartFigure() instanceof AtributoChaveFigureChen) &&
		//Connection
		//Weak Relationship Relationship <---> Derived Attribute
				!(conn.getStartFigure() instanceof RelacionamentoFracoFigureChen &&
						conn.getEndFigure() instanceof AtributoDerivadoFigureChen) &&
				!(conn.getEndFigure() instanceof RelacionamentoFracoFigureChen &&
						conn.getStartFigure() instanceof AtributoDerivadoFigureChen) &&
		//Connection
		//Weak Relationship Relationship <---> Multivalue Attribute
				!(conn.getStartFigure() instanceof RelacionamentoFracoFigureChen &&
						conn.getEndFigure() instanceof AtributoMultivaloradoFigureChen) &&
				!(conn.getEndFigure() instanceof RelacionamentoFracoFigureChen &&
						conn.getStartFigure() instanceof AtributoMultivaloradoFigureChen)
					){
		
				return Color.red;
			}	
		}

		else if (conn.getClass().equals(GeneralizacaoConnectionLineFigureChen.class)){
		//Connection in Disjunction, Overlap or Union
		//Entity <---> Disjunction
			if (!(conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof InheritanceDisjuncaoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
					conn.getStartFigure() instanceof InheritanceDisjuncaoFigureChen)&&
		//Connection in Disjunction, Overlap or Union
		//Entity <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeFigureChen &&
						conn.getEndFigure() instanceof InheritanceSobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof InheritanceSobreposicaoFigure) &&
		//Connection in Disjunction, Overlap or Union
		//Entity <---> Union
				!(conn.getStartFigure() instanceof EntidadeFigureChen &&
						conn.getEndFigure() instanceof InheritanceUniaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof InheritanceUniaoFigure) &&
		//Connection in Disjunction, Overlap or Union
		//Entity Relationship <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getEndFigure() instanceof InheritanceSobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof InheritanceSobreposicaoFigure) &&
		//Connection in Disjunction, Overlap or Union
		//Entity Relationship <---> Disjunction
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getEndFigure() instanceof InheritanceDisjuncaoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof InheritanceDisjuncaoFigureChen)&&
		//Connection in Disjunction, Overlap or Union
		//Entity Relationship <---> Union
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getEndFigure() instanceof InheritanceUniaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof InheritanceUniaoFigure)&&
		//Connection in Disjunction, Overlap or Union
		//Weak Entity <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof InheritanceSobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof InheritanceSobreposicaoFigure)&&
		//Connection in Disjunction, Overlap or Union
		//Weak Entity <---> Union
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof InheritanceUniaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof InheritanceUniaoFigure)&&
		//Connection in Disjunction, Overlap or Union
		//Weak Entity <---> Disjunction
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof InheritanceDisjuncaoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof InheritanceDisjuncaoFigureChen)
					){
				return Color.red;
			}
		}
		
		else if (conn.getClass().equals(GeneralizacaoConnectionTotalFigureChen.class)){
		//Connection in Disjunction, Overlap or Union
		//Entity <---> Disjunction
			if (!(conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof InheritanceDisjuncaoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
					conn.getStartFigure() instanceof InheritanceDisjuncaoFigureChen)&&
		//Connection in Disjunction, Overlap or Union
		//Entity <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeFigureChen &&
						conn.getEndFigure() instanceof InheritanceSobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFigureChen &&
						conn.getStartFigure() instanceof InheritanceSobreposicaoFigure) &&
		//Connection in Disjunction, Overlap or Union
		//Entity <---> Union
				/*
				!(conn.getStartFigure() instanceof EntidadeFigure &&
						conn.getEndFigure() instanceof UniaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFigure &&
						conn.getStartFigure() instanceof UniaoFigure) &&
				*/
		//Connection in Disjunction, Overlap or Union
		//Entity Relationship <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getEndFigure() instanceof InheritanceSobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof InheritanceSobreposicaoFigure) &&
		//Connection in Disjunction, Overlap or Union
		//Entity Relationship <---> Disjunction
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getEndFigure() instanceof InheritanceDisjuncaoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigureChen &&
						conn.getStartFigure() instanceof InheritanceDisjuncaoFigureChen)&&
		//Connection in Disjunction, Overlap or Union
		//Entity Relationship <---> Union
				/*
				!(conn.getStartFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getEndFigure() instanceof UniaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeRelacionamentoFigure &&
						conn.getStartFigure() instanceof UniaoFigure)&&
				*/
		//Connection in Disjunction, Overlap or Union
		//Weak Entity <---> Overlap
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof InheritanceSobreposicaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof InheritanceSobreposicaoFigure)&&
		//Connection in Disjunction, Overlap or Union
		//Weak Entity <---> Union
				/*
				!(conn.getStartFigure() instanceof EntidadeFracaFigure &&
						conn.getEndFigure() instanceof UniaoFigure) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigure &&
						conn.getStartFigure() instanceof UniaoFigure)&&
				*/
		//Connection in Disjunction, Overlap or Union
		//Weak Entity <---> Disjunction
				!(conn.getStartFigure() instanceof EntidadeFracaFigureChen &&
						conn.getEndFigure() instanceof InheritanceDisjuncaoFigureChen) &&
				!(conn.getEndFigure() instanceof EntidadeFracaFigureChen &&
						conn.getStartFigure() instanceof InheritanceDisjuncaoFigureChen)
					){
				return Color.red;
			}
		}
		
		else if (conn.getClass().equals(GeneralizacaoConnectionParcialFigureChen.class)){
		//Generalization Connection
		//Disjunction ---> Entity
			if (!(conn.getStartFigure() instanceof InheritanceDisjuncaoFigureChen &&
					conn.getEndFigure() instanceof EntidadeFigureChen) &&
		//Generalization Connection
		//Overlap ---> Entity
				!(conn.getStartFigure() instanceof InheritanceSobreposicaoFigure &&
					conn.getEndFigure() instanceof EntidadeFigureChen) &&
		//Generalization Connection
		//Union ---> Entity
				!(conn.getStartFigure() instanceof InheritanceUniaoFigure &&
					conn.getEndFigure() instanceof EntidadeFigureChen) &&
		//Generalization Connection
		//Entity ---> Entity (right according to book 2nd version of book "Database Design Using Entity-Relationship Diagrams")
				!(conn.getStartFigure() instanceof EntidadeFigureChen &&
					conn.getEndFigure() instanceof EntidadeFigureChen)					
		){
				return Color.red;
			}
		}
		return Color.black;
	}
}