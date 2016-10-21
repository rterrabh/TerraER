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

import jdk.nashorn.internal.scripts.JO;
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
        ArrayList<Figure> weakRelationship = new ArrayList<Figure>();
        ArrayList<Figure> entityRelationship = new ArrayList<Figure>();
        ArrayList<Figure> genspecDisjoint = new ArrayList<Figure>();
        ArrayList<Figure> genspecOverlap = new ArrayList<Figure>();
        ArrayList<Figure> singleLineConnectionUm = new ArrayList<Figure>();
        ArrayList<Figure> singleLineConnectionN = new ArrayList<Figure>();
        ArrayList<Figure> doubleLineConnectionUm = new ArrayList<Figure>();
        ArrayList<Figure> doubleLineConnectionN = new ArrayList<Figure>();
        ArrayList<Figure> singleLineGenSpecConn = new ArrayList<Figure>();
        ArrayList<Figure> doubleLineGenSpecConn = new ArrayList<Figure>();
        ArrayList<Figure> genSpecLineConn = new ArrayList<Figure>();
        
        
        DrawProject project = (DrawProject) getCurrentProject();
        for (Figure f : project.getEditor().getActiveView().getDrawing().getFigures()){
            if (f.getClass().equals(EntidadeFigure.class)) {
                strongEntity.add(f);                   
            } else if (f.getClass().equals(EntidadeFracaFigure.class)) {
                weakEntity.add(f);                   
            } else if (f.getClass().equals(LineConnectionFigure.class)){
                connection.add(f);
            } else if (f.getClass().equals(LabeledLineConnectionUmFigure.class)){
                singleLineConnectionUm.add(f);
            } else if (f.getClass().equals(LabeledLineConnectionMuitosFigure.class)){
                singleLineConnectionN.add(f);
            } else if (f.getClass().equals(LabeledDoubleLineConnectionUmFigure.class)){
                doubleLineConnectionUm.add(f);
            } else if (f.getClass().equals(LabeledDoubleLineConnectionMuitosFigure.class)){
                doubleLineConnectionN.add(f);
            } else if (f.getClass().equals(AtributoFigure.class)) {
                attribute.add(f);
            } else if (f.getClass().equals(AtributoChaveFigure.class)) {
                keyAttribute.add(f);
            } else if (f.getClass().equals(AtributoChaveParcialFigure.class)) {
                partialKeyAttribute.add(f);
            } else if (f.getClass().equals(AtributoDerivadoFigure.class)) {
                derivedAttribute.add(f);
            } else if (f.getClass().equals(AtributoMultivaloradoFigure.class)) {
                multivaluedAttribute.add(f);
            } else if (f.getClass().equals(RelacionamentoFracoFigure.class)){
            	weakRelationship.add(f);
            } else if (f.getClass().equals(EntidadeRelacionamentoFigure.class)){
            	entityRelationship.add(f);
            } else if (f.getClass().equals(DisjuncaoFigure.class)) {
				genspecDisjoint.add(f);
			} else if (f.getClass().equals(SobreposicaoFigure.class)) {
				genspecOverlap.add(f);
			} else if (f.getClass().equals(LineConnectionGeneralizacaoFigure.class)) {
				singleLineGenSpecConn.add(f);
			} else if (f.getClass().equals(DoubleLineConnectionGeneralizacaoFigure.class)) {
				doubleLineGenSpecConn.add(f);
			} else if (f.getClass().equals(GeneralizacaoLineConnectionFigure.class)) {
				genSpecLineConn.add(f);
			}
        }
        
        generateTables(strongEntity, weakEntity, connection, attribute, keyAttribute, partialKeyAttribute, derivedAttribute, multivaluedAttribute);
        generatePrimaryKey(strongEntity, connection, keyAttribute);
        generatePartialKey(strongEntity, weakEntity, connection, singleLineConnectionUm, singleLineConnectionN, doubleLineConnectionUm, doubleLineConnectionN, keyAttribute, partialKeyAttribute, weakRelationship);
        generateEntityRelationship (entityRelationship, connection, attribute, keyAttribute, derivedAttribute, multivaluedAttribute);
        generateGenSpec(strongEntity, connection, keyAttribute, genspecDisjoint, genspecOverlap, singleLineGenSpecConn, doubleLineGenSpecConn, genSpecLineConn);
        
    }

	public void generateTables (ArrayList<Figure> strongEntity, ArrayList<Figure> weakEntity, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> partialKeyAttribute, ArrayList<Figure> derivedAttribute, ArrayList<Figure> multivaluedAttribute){
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
            	mycontent = "CREATE TABLE " + i.toString().toUpperCase().replaceAll("\\s+", "_") + "(\n";
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

            for (Figure a: weakEntity) {
            	mycontent = "CREATE TABLE " + a.toString().toUpperCase().replaceAll("\\s+", "_") + "(\n";
            	bw.write(mycontent);
            	for (Figure b: connection) {
            		if (((ConnectionFigure)b).getStartFigure().equals(((EntidadeFracaFigure)a))){
            			for (Figure c: attribute) {
            				if (((ConnectionFigure)b).getEndFigure().equals(((AtributoFigure)c))) {
            					mycontent = c.toString() + " " + (((AtributoFigure)c).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);                                
            				}
            			}
            			for (Figure d: partialKeyAttribute) {
            				if (((ConnectionFigure)b).getEndFigure().equals(((AtributoChaveParcialFigure)d))) {
            					mycontent = d.toString() + " " + ((AtributoChaveParcialFigure)d).getAttributeType() + " " + (((AtributoChaveParcialFigure)d).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);
            				}
            			}
            			for (Figure e: derivedAttribute) {
            				if (((ConnectionFigure)b).getEndFigure().equals(((AtributoDerivadoFigure)e))) {
            					mycontent = e.toString() + " " + ((AtributoDerivadoFigure)e).getAttributeType() + " " + (((AtributoDerivadoFigure)e).isNullable() != true ? "NOT NULL" : "")  + ",\n";
            					bw.write(mycontent);
            				}
            			}
            			for (Figure f: multivaluedAttribute) {
            				if (((ConnectionFigure)b).getEndFigure().equals(((AtributoMultivaloradoFigure)f))) {
            					mycontent = f.toString() + " " + ((AtributoMultivaloradoFigure)f).getAttributeType() + " " + (((AtributoMultivaloradoFigure)f).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);
            				}
            			}
            		} else if (((ConnectionFigure)b).getEndFigure().equals(((EntidadeFracaFigure)a))){
            			for (Figure c: attribute) {
            				if (((ConnectionFigure)b).getStartFigure().equals(((AtributoFigure)c))) {
            					mycontent = c.toString() + " " + (((AtributoFigure)c).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);
            				}
            			}
            			for (Figure d: partialKeyAttribute) {
            				if (((ConnectionFigure)b).getStartFigure().equals(((AtributoChaveParcialFigure)d))) {
            					mycontent = d.toString() + " " + ((AtributoChaveParcialFigure)d).getAttributeType() + " " + (((AtributoChaveParcialFigure)d).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);
            				}
            			}
            			for (Figure e: derivedAttribute) {
            				if (((ConnectionFigure)b).getStartFigure().equals(((AtributoDerivadoFigure)e))) {
            					mycontent = e.toString() + " " + ((AtributoDerivadoFigure)e).getAttributeType() + " " + (((AtributoDerivadoFigure)e).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);
            				}
            			}
            			for (Figure f: multivaluedAttribute) {
            				if (((ConnectionFigure)b).getStartFigure().equals(((AtributoMultivaloradoFigure)f))) {
            					mycontent = f.toString() + " " + ((AtributoMultivaloradoFigure)f).getAttributeType() + " " + (((AtributoMultivaloradoFigure)f).isNullable() != true ? "NOT NULL" : "") + ",\n";
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
                               mycontent = "ALTER TABLE " + i.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT PK_" + i.toString().toUpperCase().replaceAll("\\s+", "_") + " PRIMARY KEY (" + k.toString() + ");\n\n" ;
                               bw.write(mycontent);
                           }
                       }
                   } else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFigure)i))){
                	   for (Figure k: keyAttribute) {
                           if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)k))) {
                               mycontent = "ALTER TABLE " + i.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT PK_" + i.toString().toUpperCase().replaceAll("\\s+", "_") + " PRIMARY KEY (" + k.toString() + ");\n\n" ;
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
   
   public void generatePartialKey(ArrayList<Figure> strongEntity, ArrayList<Figure> weakEntity, ArrayList<Figure> connection, ArrayList<Figure> singleLineConnectionUm, ArrayList<Figure> singleLineConnectionN, ArrayList<Figure> doubleLineConnectionUm, ArrayList<Figure> doubleLineConnectionN, ArrayList<Figure> keyAttribute, ArrayList<Figure> partialKeyAttribute, ArrayList<Figure> weakRelationship){
	   BufferedWriter bw = null;
       try {

           ArrayList<Figure> lineconnectors = new ArrayList<Figure>();
           lineconnectors.addAll(singleLineConnectionUm);
           lineconnectors.addAll(singleLineConnectionN);
           lineconnectors.addAll(doubleLineConnectionUm);
           lineconnectors.addAll(doubleLineConnectionN);
           
           String mycontent = new String();
           String mycontent2 = new String();
           String mycontent3 = new String();
           String ownerEntity = new String(); 
           String keyAtt = new String();
           String keyAttType = new String();
           String keyAttNullable = new String();
           File file = new File("/home/shinahk/Desktop/Test.sql");
           FileWriter fw = new FileWriter(file,true);
           bw = new BufferedWriter(fw);

           for (Figure f : weakRelationship) {
        	   for (Figure g : lineconnectors) {
        		   if (((ConnectionFigure)g).getEndFigure().equals(((RelacionamentoFracoFigure)f))) {
        			   for (Figure h : strongEntity) {
        				   if (((ConnectionFigure)g).getStartFigure().equals(((EntidadeFigure)h))) {
        					   for (Figure i: connection) {
        						   if (((ConnectionFigure)i).getStartFigure().equals(((EntidadeFigure)h))){
        							   for (Figure j: keyAttribute) {
        								   if (((ConnectionFigure)i).getEndFigure().equals(((AtributoChaveFigure)j))) {
        									   ownerEntity = h.toString().toUpperCase().replaceAll("\\s+", "_");
        									   keyAtt = j.toString();
        									   keyAttType = ((AtributoChaveFigure)j).getAttributeType().toString();
        									   keyAttNullable = (((AtributoChaveFigure)j).isNullable() != true ? "NOT NULL" : "");
        								   }
        							   }
        						   } else if (((ConnectionFigure)i).getEndFigure().equals(((EntidadeFigure)h))){
        							   for (Figure j: keyAttribute) {
        								   if (((ConnectionFigure)i).getStartFigure().equals(((AtributoChaveFigure)j))) {
        									   ownerEntity = h.toString().toUpperCase().replaceAll("\\s+", "_");
        									   keyAtt = j.toString();
        									   keyAttType = ((AtributoChaveFigure)j).getAttributeType().toString();
        									   keyAttNullable = (((AtributoChaveFigure)j).isNullable() != true ? "NOT NULL" : "");
        								   }
        							   }                       
        						   }                      
        					   }    
        				   }
        			   }
        			   for (Figure h : weakEntity) {
        				   if (((ConnectionFigure)g).getStartFigure().equals(((EntidadeFracaFigure)h))) {
        					   for (Figure i: connection) {
        						   if (((ConnectionFigure)i).getStartFigure().equals(((EntidadeFracaFigure)h))){
        							   for (Figure j: partialKeyAttribute) {
        								   if (((ConnectionFigure)i).getEndFigure().equals(((AtributoChaveParcialFigure)j))) {
        									   mycontent = "ALTER TABLE " + h.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD owner_" + keyAtt + " " + keyAttType + " " + keyAttNullable + ";\n";  
        									   mycontent2 = "ALTER TABLE " + h.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + h.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + "owner_" + keyAtt + ") REFERENCES " + ownerEntity + " (" + keyAtt + ");\n";
        									   mycontent3 = "ALTER TABLE " + h.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT PK_" + h.toString().toUpperCase().replaceAll("\\s+", "_") + " PRIMARY KEY (" + j.toString() + ", owner_" + keyAtt + ");\n" ;
        		                               bw.write(mycontent);
        									   bw.write(mycontent2);
        									   bw.write(mycontent3);
        								   }
        							   }
        						   } else if (((ConnectionFigure)i).getEndFigure().equals(((EntidadeFracaFigure)h))){
        							   for (Figure j: partialKeyAttribute) {
        								   if (((ConnectionFigure)i).getStartFigure().equals(((AtributoChaveParcialFigure)j))) {
        									   mycontent = "ALTER TABLE " + h.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD owner_" + keyAtt + " " + keyAttType + " " + keyAttNullable + ";\n";  
        									   mycontent2 = "ALTER TABLE " + h.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + h.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + "owner_" + keyAtt + ") REFERENCES " + ownerEntity + " (" + keyAtt + ");\n";
        									   mycontent3 = "ALTER TABLE " + h.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT PK_" + h.toString().toUpperCase().replaceAll("\\s+", "_") + " PRIMARY KEY (" + j.toString() + ", owner_" + keyAtt + ");\n" ;
        		                               bw.write(mycontent);
        									   bw.write(mycontent2);
        									   bw.write(mycontent3);
        								   }
        							   }                       
        						   }                      
        					   }    
        				   }
        			   }
        		   }
        	   }
           }           
           JOptionPane.showMessageDialog(null, "Partial Key Created");
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
   
   public void generateGenSpec(ArrayList<Figure> strongEntity, ArrayList<Figure> connection, ArrayList<Figure> keyAttribute, ArrayList<Figure> genspecDisjoint, ArrayList<Figure> genspecOverlap, ArrayList<Figure> singleLineGenSpecConn, ArrayList<Figure> doubleLineGenSpecConn, ArrayList<Figure> genSpecLineConn) {
	   BufferedWriter bw = null;
	   try {

		   String mycontent = new String();
		   String ownerEntity = new String(); 
		   String keyAtt = new String();
		   File file = new File("/home/shinahk/Desktop/Test.sql");
		   FileWriter fw = new FileWriter(file,true);
		   bw = new BufferedWriter(fw);

		   for (Figure a : genspecOverlap) {
			   for (Figure b : singleLineGenSpecConn) {
				   if (((ConnectionFigure)b).getEndFigure().equals(a)) {
					   for (Figure c : strongEntity) {
						   if (((ConnectionFigure)b).getStartFigure().equals(c)) {
							   for (Figure d: connection) {
								   if (((ConnectionFigure)d).getStartFigure().equals(((EntidadeFigure)c))){
									   for (Figure e: keyAttribute) {
										   if (((ConnectionFigure)d).getEndFigure().equals(((AtributoChaveFigure)e))) {
											   ownerEntity = c.toString().toUpperCase().replaceAll("\\s+", "_");
											   keyAtt = e.toString();
										   }
									   }
								   } else if (((ConnectionFigure)d).getEndFigure().equals(((EntidadeFigure)c))){
									   for (Figure e: keyAttribute) {
										   if (((ConnectionFigure)d).getStartFigure().equals(((AtributoChaveFigure)e))) {
											   ownerEntity = c.toString().toUpperCase().replaceAll("\\s+", "_");
											   keyAtt = e.toString();
										   }
									   }                       
								   }                      
							   }  
						   }
					   }
				   } else if (((ConnectionFigure)b).getStartFigure().equals(a)) {
					   for (Figure c : strongEntity) {
						   if (((ConnectionFigure)b).getEndFigure().equals(c)) {
							   for (Figure d: connection) {
								   if (((ConnectionFigure)d).getStartFigure().equals(((EntidadeFigure)c))){
									   for (Figure e: keyAttribute) {
										   if (((ConnectionFigure)d).getEndFigure().equals(((AtributoChaveFigure)e))) {
											   ownerEntity = c.toString().toUpperCase().replaceAll("\\s+", "_");
											   keyAtt = e.toString();
										   }
									   }
								   } else if (((ConnectionFigure)d).getEndFigure().equals(((EntidadeFigure)c))){
									   for (Figure e: keyAttribute) {
										   if (((ConnectionFigure)d).getStartFigure().equals(((AtributoChaveFigure)e))) {
											   ownerEntity = c.toString().toUpperCase().replaceAll("\\s+", "_");
											   keyAtt = e.toString();
										   }
									   }                       
								   }                      
							   }  
						   }
					   }
				   }				   
			   }
			   for (Figure f : genSpecLineConn) {
				   if (((ConnectionFigure)f).getStartFigure().equals(a)) {
					   for (Figure g : strongEntity) {
						   if (((ConnectionFigure)f).getEndFigure().equals(g)) {
							   for (Figure h: connection) {
								   if (((ConnectionFigure)h).getStartFigure().equals(((EntidadeFigure)g))){
									   for (Figure i: keyAttribute) {
										   if (((ConnectionFigure)h).getEndFigure().equals(((AtributoChaveFigure)i))) {
											   mycontent = "ALTER TABLE " + g.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + g.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + i.toString() + ") REFERENCES " + ownerEntity + " (" + keyAtt + ") ON DELETE CASCADE;\n";   
											   bw.write(mycontent);
										   }
									   }
								   } else if (((ConnectionFigure)h).getEndFigure().equals(((EntidadeFigure)g))){
									   for (Figure i: keyAttribute) {
										   if (((ConnectionFigure)h).getStartFigure().equals(((AtributoChaveFigure)i))) {
											   mycontent = "ALTER TABLE " + g.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + g.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + i.toString() + ") REFERENCES " + ownerEntity + " (" + keyAtt + ") ON DELETE CASCADE;\n";   
											   bw.write(mycontent);
										   }
									   }                       
								   }                      
							   }
						   }
					   }
				   }
			   }
		   }
		   
		   JOptionPane.showMessageDialog(null, "GenSpec Created");
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
   
}