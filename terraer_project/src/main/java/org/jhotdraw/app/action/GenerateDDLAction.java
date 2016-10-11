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
        ArrayList<Figure> weakEntity = new ArrayList<Figure>();
        ArrayList<Figure> connection = new ArrayList<Figure>();
        ArrayList<Figure> attribute = new ArrayList<Figure>();
        ArrayList<Figure> keyAttribute = new ArrayList<Figure>();
        ArrayList<Figure> partialKeyAttribute = new ArrayList <Figure>();
        ArrayList<Figure> derivedAttribute = new ArrayList<Figure>();
        ArrayList<Figure> multivaluedAttribute = new ArrayList<Figure>();
        ArrayList<Figure> sLConnection1 = new ArrayList<Figure>();
        ArrayList<Figure> sLConnectionN = new ArrayList<Figure>();
        ArrayList<Figure> dLConnection1 = new ArrayList<Figure>();
        ArrayList<Figure> dLConnectionN = new ArrayList<Figure>();
        ArrayList<Figure> weakRelationship = new ArrayList<Figure>();
        ArrayList<Figure> entityRelationship = new ArrayList<Figure>();
        
        DrawProject project = (DrawProject) getCurrentProject();
        for (Figure f : project.getEditor().getActiveView().getDrawing().getFigures()){
            if (f instanceof EntidadeFigure) {
                strongEntity.add(f);                   
            }
            if (f instanceof EntidadeFracaFigure) {
                weakEntity.add(f);                   
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
            else if (f instanceof AtributoChaveParcialFigure) {
                partialKeyAttribute.add(f);
            }
            else if (f instanceof AtributoDerivadoFigure) {
                derivedAttribute.add(f);
            }
            else if (f instanceof AtributoMultivaloradoFigure) {
                multivaluedAttribute.add(f);
            }
            else if(f instanceof LabeledLineConnectionUmFigure){
            	sLConnection1.add(f);
            }
            else if(f instanceof LabeledLineConnectionMuitosFigure){
            	sLConnectionN.add(f);
            }
            else if(f instanceof LabeledDoubleLineConnectionUmFigure){
            	dLConnection1.add(f);
            }
            else if(f instanceof LabeledDoubleLineConnectionMuitosFigure){
            	dLConnectionN.add(f);
            }
            else if(f instanceof RelacionamentoFracoFigure){
            	weakRelationship.add(f);
            }
            else if(f instanceof EntidadeRelacionamentoFigure){
            	entityRelationship.add(f);
            }
            
        }
                 
        generateTables(strongEntity, connection, attribute, keyAttribute, derivedAttribute, multivaluedAttribute);
        generatePrimaryKey(strongEntity, connection, keyAttribute);
        generateWeakEntity(weakEntity, connection, attribute, partialKeyAttribute, derivedAttribute, multivaluedAttribute);
        generateEntityRelationship (entityRelationship, connection, attribute, keyAttribute, derivedAttribute, multivaluedAttribute);
        
    }
    
    public void generateTables (ArrayList<Figure> strongEntity, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> derivedAttribute, ArrayList<Figure> multivaluedAttribute){
   	 BufferedWriter bw = null;
        try {
            String mycontent = new String();
            File file = new File("/home/shinahk/Desktop/Test.sql");

            if (!file.exists()) {
              file.createNewFile();
            }

            FileWriter fw = new FileWriter(file,false);
            bw = new BufferedWriter(fw);
            
            
            for (Figure i: strongEntity) {
                mycontent = "CREATE TABLE " + i.toString().toUpperCase() + "(\n";
                bw.write(mycontent);
                for (Figure j: connection) {
                    if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeFigure)i))){
                        for (Figure k: attribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoFigure)k))) {
                                mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
                                bw.write(mycontent);                                
                            }
                        }
                        for (Figure l: keyAttribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoChaveFigure)l))) {
                                mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + (((AtributoChaveFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
                                bw.write(mycontent);
                            }
                        }
                        for (Figure m: derivedAttribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoDerivadoFigure)m))) {
                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "")  + ",\n";
                                bw.write(mycontent);
                            }
                        }
                        for (Figure n: multivaluedAttribute) {
                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoMultivaloradoFigure)n))) {
                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + (((AtributoMultivaloradoFigure)n).isNullable() != true ? "NOT NULL" : "") + ",\n";
                                bw.write(mycontent);
                            }
                        }
                    } else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFigure)i))){
                        for (Figure k: attribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoFigure)k))) {
                                mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
                                bw.write(mycontent);
                            }
                        }
                        for (Figure l: keyAttribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)l))) {
                                mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + (((AtributoChaveFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
                                bw.write(mycontent);
                            }
                        }
                        for (Figure m: derivedAttribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoDerivadoFigure)m))) {
                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "") + ",\n";
                                bw.write(mycontent);
                            }
                        }
                        for (Figure n: multivaluedAttribute) {
                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoMultivaloradoFigure)n))) {
                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + (((AtributoMultivaloradoFigure)n).isNullable() != true ? "NOT NULL" : "") + ",\n";
                                bw.write(mycontent);
                            }
                        }
                    }                      
                }
                mycontent = ");";
                bw.write(mycontent);                    
            }
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
        	br = new BufferedReader(new FileReader("/home/shinahk/Desktop/Test.sql"));
        	bw = new BufferedWriter(new FileWriter("/home/shinahk/Desktop/Test_Temp.sql"));

        	String oldLine = "";
        	String currentLine = "";
        	
        	while ((currentLine = br.readLine()) != null) {
        		oldLine += currentLine + "\n";
        	}
        	
        	String replacedLine = oldLine.replace(",\n);", "\n);\n\n");
        	bw.write(replacedLine);
        	JOptionPane.showMessageDialog(null, "File written Successfully");
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
        File oldFile = new File("/home/shinahk/Desktop/Test.sql");
        oldFile.delete();

        File newFile = new File("/home/shinahk/Desktop/Test_Temp.sql");
        newFile.renameTo(oldFile);
   }
    
   public void generatePrimaryKey(ArrayList<Figure> strongEntity, ArrayList<Figure> connection, ArrayList<Figure> keyAttribute){
	   BufferedWriter bw = null;
       try {
           String mycontent = new String();
           File file = new File("/home/shinahk/Desktop/Test.sql");
           FileWriter fw = new FileWriter(file,true);
           bw = new BufferedWriter(fw);
           
           for (Figure i: strongEntity) {
               for (Figure j: connection) {
                   if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeFigure)i))){
                       for (Figure k: keyAttribute) {
                           if (((ConnectionFigure)j).getEndFigure().equals(((AtributoChaveFigure)k))) {
                               mycontent = "ALTER TABLE " + i.toString().toUpperCase() + " ADD CONSTRAINT PK_" + i.toString().toUpperCase() + " PRIMARY KEY (" + k.toString() + ");\n\n" ;
                               bw.write(mycontent);
                           }
                       }
                   } else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFigure)i))){
                	   for (Figure k: keyAttribute) {
                           if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)k))) {
                               mycontent = "ALTER TABLE " + i.toString().toUpperCase() + " ADD CONSTRAINT PK_" + i.toString().toUpperCase() + " PRIMARY KEY (" + k.toString() + ");\n\n" ;
                               bw.write(mycontent);
                           }
                       }                       
                   }                      
               }                    
           }
           JOptionPane.showMessageDialog(null, "Primary Key Created");
       } catch (IOException ioe) {
         ioe.printStackTrace();
       } finally { 
         try {
             if(bw!=null) bw.close();
         } catch(Exception ex) {
             JOptionPane.showMessageDialog(null, "Error in closing the BufferedWriter"+ex);
         }
       }
   }
   
   public void generateWeakEntity (ArrayList<Figure> weakEntity, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> partialKeyAttribute, ArrayList<Figure> derivedAttribute, ArrayList<Figure> multivaluedAttribute){
	   	 BufferedWriter bw = null;
	        try {
	            String mycontent = new String();
	            File file = new File("/home/shinahk/Desktop/Test.sql");
	            FileWriter fw = new FileWriter(file,true);
	            bw = new BufferedWriter(fw);
	            
	            for (Figure i: weakEntity) {
	                mycontent = "CREATE TABLE " + i.toString().toUpperCase() + "(\n";
	                bw.write(mycontent);
	                for (Figure j: connection) {
	                    if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeFracaFigure)i))){
	                        for (Figure k: attribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoFigure)k))) {
	                                mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);                                
	                            }
	                        }
	                        for (Figure l: partialKeyAttribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoChaveParcialFigure)l))) {
	                                mycontent = l.toString() + " " + ((AtributoChaveParcialFigure)l).getAttributeType() + " " + (((AtributoChaveParcialFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure m: derivedAttribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoDerivadoFigure)m))) {
	                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "")  + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure n: multivaluedAttribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoMultivaloradoFigure)n))) {
	                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + (((AtributoMultivaloradoFigure)n).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                    } else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFracaFigure)i))){
	                        for (Figure k: attribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoFigure)k))) {
	                                mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure l: partialKeyAttribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveParcialFigure)l))) {
	                                mycontent = l.toString() + " " + ((AtributoChaveParcialFigure)l).getAttributeType() + " " + (((AtributoChaveParcialFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure m: derivedAttribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoDerivadoFigure)m))) {
	                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure n: multivaluedAttribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoMultivaloradoFigure)n))) {
	                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + (((AtributoMultivaloradoFigure)n).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                    }                      
	                }
	                mycontent = ");";
	                bw.write(mycontent);                    
	            }
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
	        	br = new BufferedReader(new FileReader("/home/shinahk/Desktop/Test.sql"));
	        	bw = new BufferedWriter(new FileWriter("/home/shinahk/Desktop/Test_Temp.sql"));

	        	String oldLine = "";
	        	String currentLine = "";
	        	
	        	while ((currentLine = br.readLine()) != null) {
	        		oldLine += currentLine + "\n";
	        	}
	        	
	        	String replacedLine = oldLine.replace(",\n);", "\n);\n\n");
	        	bw.write(replacedLine);
	        	JOptionPane.showMessageDialog(null, "File written Successfully");
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
	        File oldFile = new File("/home/shinahk/Desktop/Test.sql");
	        oldFile.delete();

	        File newFile = new File("/home/shinahk/Desktop/Test_Temp.sql");
	        newFile.renameTo(oldFile);
	   }
   
   public void generatePartialKey(ArrayList<Figure> strongEntity, ArrayList<Figure> weakEntity, ArrayList<Figure> connection, ArrayList<Figure> keyAttribute, ArrayList<Figure> partialKeyAttribute, ArrayList<Figure> weakRelationship,ArrayList<Figure> sLConnection1, ArrayList<Figure> sLConnectionN, ArrayList<Figure> dLConnection1, ArrayList<Figure> dLConnectionN){
	   BufferedWriter bw = null;
       try {
           String mycontent = new String();
           File file = new File("/home/shinahk/Desktop/Test.sql");
           FileWriter fw = new FileWriter(file,true);
           bw = new BufferedWriter(fw);
           
           //TO DO
           
           JOptionPane.showMessageDialog(null, "Primary Key Created");
       } catch (IOException ioe) {
         ioe.printStackTrace();
       } finally { 
         try {
             if(bw!=null) bw.close();
         } catch(Exception ex) {
             JOptionPane.showMessageDialog(null, "Error in closing the BufferedWriter"+ex);
         }
       }
   }
   
   public void generateEntityRelationship (ArrayList<Figure> entityRelationship, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> derivedAttribute, ArrayList<Figure> multivaluedAttribute){
	   	 BufferedWriter bw = null;
	        try {
	            String mycontent = new String();
	            File file = new File("/home/shinahk/Desktop/Test.sql");
	            FileWriter fw = new FileWriter(file,true);
	            bw = new BufferedWriter(fw);
	            
	            
	            for (Figure i: entityRelationship) {
	                mycontent = "CREATE TABLE " + i.toString().toUpperCase() + "(\n";
	                bw.write(mycontent);
	                for (Figure j: connection) {
	                    if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeRelacionamentoFigure)i))){
	                        for (Figure k: attribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoFigure)k))) {
	                                mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);                                
	                            }
	                        }
	                        for (Figure l: keyAttribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoChaveFigure)l))) {
	                                mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + (((AtributoChaveFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure m: derivedAttribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoDerivadoFigure)m))) {
	                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "")  + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure n: multivaluedAttribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoMultivaloradoFigure)n))) {
	                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + (((AtributoMultivaloradoFigure)n).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                    } else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeRelacionamentoFigure)i))){
	                        for (Figure k: attribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoFigure)k))) {
	                                mycontent = k.toString() + " " + (((AtributoFigure)k).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure l: keyAttribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)l))) {
	                                mycontent = l.toString() + " " + ((AtributoChaveFigure)l).getAttributeType() + " " + (((AtributoChaveFigure)l).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure m: derivedAttribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoDerivadoFigure)m))) {
	                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                        for (Figure n: multivaluedAttribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoMultivaloradoFigure)n))) {
	                                mycontent = n.toString() + " " + ((AtributoMultivaloradoFigure)n).getAttributeType() + " " + (((AtributoMultivaloradoFigure)n).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }
	                    }                      
	                }
	                mycontent = ");";
	                bw.write(mycontent);                    
	            }
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
	        	br = new BufferedReader(new FileReader("/home/shinahk/Desktop/Test.sql"));
	        	bw = new BufferedWriter(new FileWriter("/home/shinahk/Desktop/Test_Temp.sql"));

	        	String oldLine = "";
	        	String currentLine = "";
	        	
	        	while ((currentLine = br.readLine()) != null) {
	        		oldLine += currentLine + "\n";
	        	}
	        	
	        	String replacedLine = oldLine.replace(",\n);", "\n);\n\n");
	        	bw.write(replacedLine);
	        	JOptionPane.showMessageDialog(null, "File written Successfully");
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
	        File oldFile = new File("/home/shinahk/Desktop/Test.sql");
	        oldFile.delete();

	        File newFile = new File("/home/shinahk/Desktop/Test_Temp.sql");
	        newFile.renameTo(oldFile);
	   }
}