package org.jhotdraw.app.action;

import java.util.ArrayList;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.notation.finalversion.AtributoChaveFigureChen;
import org.jhotdraw.draw.notation.finalversion.AtributoChaveParcialFigureChen;
import org.jhotdraw.draw.notation.finalversion.AtributoFigureChen;
import org.jhotdraw.draw.notation.finalversion.EntidadeFigureChen;
import org.jhotdraw.draw.notation.finalversion.EntidadeFracaFigureChen;

public class GenerateTables {
	private String ddlBuffer;
	
	public void GenerateTables (ArrayList<Figure> strongEntity, ArrayList<Figure> weakEntity, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> partialKeyAttribute, ArrayList<Figure> derivedAttribute){
		String mycontent = new String();
		for (Figure i: strongEntity) {
			mycontent = "CREATE TABLE " + i.toString().toUpperCase().replaceAll("\\s+", "_") + "(\n";
			ddlBuffer += mycontent;
			for (Figure j: connection) {
				if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeFigureChen)i))){
					for (Figure k: attribute) {
						if (((ConnectionFigure)j).getEndFigure().equals(((AtributoFigureChen)k))) {
							mycontent = k.toString() + " " + (((AtributoFigureChen)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;                                
						}
					}
					for (Figure l: keyAttribute) {
						if (((ConnectionFigure)j).getEndFigure().equals(((AtributoChaveFigureChen)l))) {
							mycontent = l.toString() + " " + ((AtributoChaveFigureChen)l).getAttributeType() + " " + (((AtributoChaveFigureChen)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}            			           			
				} else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFigureChen)i))){
					for (Figure k: attribute) {
						if (((ConnectionFigure)j).getStartFigure().equals(((AtributoFigureChen)k))) {
							mycontent = k.toString() + " " + (((AtributoFigureChen)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}
					for (Figure l: keyAttribute) {
						if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigureChen)l))) {
							mycontent = l.toString() + " " + ((AtributoChaveFigureChen)l).getAttributeType() + " " + (((AtributoChaveFigureChen)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}            			            			
				}                      
			}
			mycontent = ");\n";
			ddlBuffer += mycontent;
		}

		for (Figure a: weakEntity) {
			mycontent = "CREATE TABLE " + a.toString().toUpperCase().replaceAll("\\s+", "_") + "(\n";
			ddlBuffer += mycontent;
			for (Figure b: connection) {
				if (((ConnectionFigure)b).getStartFigure().equals(((EntidadeFracaFigureChen)a))){
					for (Figure c: attribute) {
						if (((ConnectionFigure)b).getEndFigure().equals(((AtributoFigureChen)c))) {
							mycontent = c.toString() + " " + (((AtributoFigureChen)c).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;                                
						}
					}
					for (Figure d: partialKeyAttribute) {
						if (((ConnectionFigure)b).getEndFigure().equals(((AtributoChaveParcialFigureChen)d))) {
							mycontent = d.toString() + " " + ((AtributoChaveParcialFigureChen)d).getAttributeType() + " " + (((AtributoChaveParcialFigureChen)d).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}
				} else if (((ConnectionFigure)b).getEndFigure().equals(((EntidadeFracaFigureChen)a))){
					for (Figure c: attribute) {
						if (((ConnectionFigure)b).getStartFigure().equals(((AtributoFigureChen)c))) {
							mycontent = c.toString() + " " + (((AtributoFigureChen)c).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}
					for (Figure d: partialKeyAttribute) {
						if (((ConnectionFigure)b).getStartFigure().equals(((AtributoChaveParcialFigureChen)d))) {
							mycontent = d.toString() + " " + ((AtributoChaveParcialFigureChen)d).getAttributeType() + " " + (((AtributoChaveParcialFigureChen)d).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}
				}                      
			}
			mycontent = ");\n";
			ddlBuffer += mycontent; 
		}
	}
}
