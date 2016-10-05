/*
 * @(#)CutAction.java  2.0  2007-04-13
 *
 * Copyright (c) 1996-2007 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */

package org.jhotdraw.app.action;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

import org.jhotdraw.app.Application;
import org.jhotdraw.draw.*;
import org.jhotdraw.samples.draw.DrawProject;
import org.jhotdraw.util.ResourceBundleUtil;
/**
 * Cuts the selected region and places its contents into the system clipboard.
 * Acts on the EditableComponent or JTextComponent which had the focus when
 * the ActionEvent was generated.
 *
 * @author Werner Randelshofer
 * @version 2.0 2007-04-13 Use javax.swing.TransferHandler instead of 
 * interface EditableComponent. 
 * <br>1.0 October 9, 2005 Created.
 */
public class GenerateDDLAction extends AbstractProjectAction {
    public final static String ID = "generateDDL";
   
    /** Creates a new instance. */
    public GenerateDDLAction(Application app) {
        super(app);
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, ID);
        
    }
    
    public void actionPerformed(ActionEvent evt) {
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");

        ArrayList<Figure> strongEntity = new ArrayList<Figure>();
        ArrayList<Figure> connection = new ArrayList<Figure>();
        ArrayList<Figure> attribute = new ArrayList<Figure>();
        ArrayList<Figure> keyAttribute = new ArrayList<Figure>();
        ArrayList<Figure> derivedAttribute = new ArrayList<Figure>();
        ArrayList<Figure> multivaluedAttribute = new ArrayList<Figure>();
        
        DrawProject project = (DrawProject) getCurrentProject();
        for (Figure f : project.getEditor().getActiveView().getDrawing().getFigures()){
            if (f instanceof EntidadeFigure) {
                strongEntity.add(f);                   
            }
            else if (f instanceof ConnectionFigure){
                connection.add(f);
            }
            else if (f instanceof AtributoFigure) {
                attribute.add(f);
            }
            else if (f instanceof AtributoChaveFigure) {
                keyAttribute.add(f);
            }
            else if (f instanceof AtributoDerivadoFigure) {
                derivedAttribute.add(f);
            }
            else if (f instanceof AtributoMultivaloradoFigure) {
                multivaluedAttribute.add(f);
            }
        }
        
        generateTables(strongEntity, connection, attribute, keyAttribute, derivedAttribute, multivaluedAttribute);
    }
    
    public void generateTables (ArrayList<Figure> strongEntity, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> derivedAttribute, ArrayList<Figure> multivaluedAttribute){
     BufferedWriter bw = null;
        try {
            String mycontent = new String();
            File file = new File("C:\\Users\\ah-00\\Desktop\\Test.sql");

            if (!file.exists()) {
              file.createNewFile();
            }

            FileWriter fw = new FileWriter(file,false);
            bw = new BufferedWriter(fw);
            
            
            for (Figure i: strongEntity) {
                mycontent = "CREATE TABLE " + i.toString() + "(\n";
                bw.write(mycontent);
                for (Figure j: connection) {
                    if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeFigure)i))){
                        for (Figure k: attribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoFigure)k))) {
                                mycontent = k.toString() + " " + ((AtributoFigure)k).getAttributeType() + " " + ((AtributoFigure)k).isNullable() + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                        for (Figure l: keyAttribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoChaveFigure)l))) {
                                mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + ((AtributoFigure)l).isNullable() + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                        for (Figure m: derivedAttribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoDerivadoFigure)m))) {
                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + ((AtributoFigure)m).isNullable()  + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                        for (Figure n: multivaluedAttribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoMultivaloradoFigure)n))) {
                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + ((AtributoFigure)n).isNullable() + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                    } else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFigure)i))){
                        for (Figure k: attribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoFigure)k))) {
                                mycontent = k.toString() + " " + ((AtributoFigure)k).getAttributeType() + " " + ((AtributoFigure)k).isNullable() + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                        for (Figure l: keyAttribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)l))) {
                                mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + ((AtributoFigure)l).isNullable() + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                        for (Figure m: derivedAttribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoDerivadoFigure)m))) {
                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + ((AtributoFigure)m).isNullable() + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                        for (Figure n: multivaluedAttribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoMultivaloradoFigure)n))) {
                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + ((AtributoFigure)n).isNullable() + ",\n";
                                bw.write(mycontent);
                                JOptionPane.showMessageDialog(null, "Ok");
                            }
                        }
                    }                      
                }
                mycontent = ");";
                bw.write(mycontent);                    
            }
            JOptionPane.showMessageDialog(null, "File written Successfully");
        } catch (IOException ioe) {
          ioe.printStackTrace();
        } finally { 
          try {
              if(bw!=null) bw.close();
          } catch(Exception ex) {
              JOptionPane.showMessageDialog(null, "Error in closing the BufferedWriter"+ex);
          }
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\ah-00\\Desktop\\Test.sql"));
            bw = new BufferedWriter(new FileWriter("C:\\Users\\ah-00\\Desktop\\Test_Temp.sql"));

            String oldLine = "";
            String currentLine = "";
            
            while ((currentLine = br.readLine()) != null) {
                oldLine += currentLine;
            }
            
            JOptionPane.showMessageDialog(null, oldLine);
            String replacedLine = oldLine.replace(",);", "\n);\n");
            replacedLine = replacedLine.replace(",", ",\n");
            replacedLine = replacedLine.replace("(", "(\n");
            JOptionPane.showMessageDialog(null, replacedLine);
            bw.write(replacedLine);
        } catch (Exception e) {
            return;
        } finally {
            try {
                if(br != null)
                    br.close();
            } catch (IOException e) {
                //
            }
            try {
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                //
            }
        }
        // Once everything is complete, delete old file..
        File oldFile = new File("C:\\Users\\ah-00\\Desktop\\Test.sql");
        oldFile.delete();

        // And rename tmp file's name to old file name
        File newFile = new File("C:\\Users\\ah-00\\Desktop\\Test_Temp.sql");
        newFile.renameTo(oldFile);
   }
}