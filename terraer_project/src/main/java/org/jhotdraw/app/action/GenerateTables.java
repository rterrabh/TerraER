package org.jhotdraw.app.action;

import java.util.ArrayList;

import org.jhotdraw.draw.AtributoChaveFigure;
import org.jhotdraw.draw.AtributoChaveParcialFigure;
import org.jhotdraw.draw.AtributoFigure;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.EntidadeFigure;
import org.jhotdraw.draw.EntidadeFracaFigure;
import org.jhotdraw.draw.Figure;

public class GenerateTables {
	private String ddlBuffer;
	
	public void GenerateTables (ArrayList<Figure> strongEntity, ArrayList<Figure> weakEntity, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> partialKeyAttribute, ArrayList<Figure> derivedAttribute){
		String mycontent = new String();
		for (Figure i: strongEntity) {
			mycontent = "CREATE TABLE " + i.toString().toUpperCase().replaceAll("\\s+", "_") + "(\n";
			ddlBuffer += mycontent;
			for (Figure j: connection) {
				if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeFigure)i))){
					for (Figure k: attribute) {
						if (((ConnectionFigure)j).getEndFigure().equals(((AtributoFigure)k))) {
							mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;                                
						}
					}
					for (Figure l: keyAttribute) {
						if (((ConnectionFigure)j).getEndFigure().equals(((AtributoChaveFigure)l))) {
							mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + (((AtributoChaveFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}            			           			
				} else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFigure)i))){
					for (Figure k: attribute) {
						if (((ConnectionFigure)j).getStartFigure().equals(((AtributoFigure)k))) {
							mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}
					for (Figure l: keyAttribute) {
						if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)l))) {
							mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + (((AtributoChaveFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
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
				if (((ConnectionFigure)b).getStartFigure().equals(((EntidadeFracaFigure)a))){
					for (Figure c: attribute) {
						if (((ConnectionFigure)b).getEndFigure().equals(((AtributoFigure)c))) {
							mycontent = c.toString() + " " + (((AtributoFigure)c).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;                                
						}
					}
					for (Figure d: partialKeyAttribute) {
						if (((ConnectionFigure)b).getEndFigure().equals(((AtributoChaveParcialFigure)d))) {
							mycontent = d.toString() + " " + ((AtributoChaveParcialFigure)d).getAttributeType() + " " + (((AtributoChaveParcialFigure)d).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}
				} else if (((ConnectionFigure)b).getEndFigure().equals(((EntidadeFracaFigure)a))){
					for (Figure c: attribute) {
						if (((ConnectionFigure)b).getStartFigure().equals(((AtributoFigure)c))) {
							mycontent = c.toString() + " " + (((AtributoFigure)c).isNullable() != true ? "NOT NULL" : "") + ",\n";
							ddlBuffer += mycontent;
						}
					}
					for (Figure d: partialKeyAttribute) {
						if (((ConnectionFigure)b).getStartFigure().equals(((AtributoChaveParcialFigure)d))) {
							mycontent = d.toString() + " " + ((AtributoChaveParcialFigure)d).getAttributeType() + " " + (((AtributoChaveParcialFigure)d).isNullable() != true ? "NOT NULL" : "") + ",\n";
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
