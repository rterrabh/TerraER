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

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
        ArrayList<Figure> relationship = new ArrayList<Figure>();        
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
            } else if (f.getClass().equals(RelacionamentoFigure.class)){
            	relationship.add(f);
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
        
        generateTables(strongEntity, weakEntity, connection, attribute, keyAttribute, partialKeyAttribute, derivedAttribute);
        generatePrimaryKey(strongEntity, connection, keyAttribute);
        generatePartialKey(strongEntity, weakEntity, connection, singleLineConnectionUm, singleLineConnectionN, doubleLineConnectionUm, doubleLineConnectionN, keyAttribute, partialKeyAttribute, weakRelationship);
        generateEntityRelationship (entityRelationship, connection, attribute, keyAttribute, derivedAttribute);
        generateGenSpec(strongEntity, connection, keyAttribute, genspecDisjoint, genspecOverlap, singleLineGenSpecConn, doubleLineGenSpecConn, genSpecLineConn);
        generateRelationships(strongEntity, connection, keyAttribute, singleLineConnectionUm, singleLineConnectionN, doubleLineConnectionUm, doubleLineConnectionN, relationship, entityRelationship);
        //generateMultivaluedAttribute(strongEntity, weakEntity, entityRelationship, connection, keyAttribute, multivaluedAttribute, singleLineConnectionN, doubleLineConnectionN);
        
    }

	public void generateTables (ArrayList<Figure> strongEntity, ArrayList<Figure> weakEntity, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> partialKeyAttribute, ArrayList<Figure> derivedAttribute){
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
            			/*for (Figure m: derivedAttribute) {
            				if (((ConnectionFigure)j).getEndFigure().equals(((AtributoDerivadoFigure)m))) {
            					mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "")  + ",\n";
            					bw.write(mycontent);
            				}
            			}*/            			
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
            			/*for (Figure m: derivedAttribute) {
            				if (((ConnectionFigure)j).getStartFigure().equals(((AtributoDerivadoFigure)m))) {
            					mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);
            				}
            			}*/            			
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
            			/*for (Figure e: derivedAttribute) {
            				if (((ConnectionFigure)b).getEndFigure().equals(((AtributoDerivadoFigure)e))) {
            					mycontent = e.toString() + " " + ((AtributoDerivadoFigure)e).getAttributeType() + " " + (((AtributoDerivadoFigure)e).isNullable() != true ? "NOT NULL" : "")  + ",\n";
            					bw.write(mycontent);
            				}
            			}*/
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
            			/*for (Figure e: derivedAttribute) {
            				if (((ConnectionFigure)b).getStartFigure().equals(((AtributoDerivadoFigure)e))) {
            					mycontent = e.toString() + " " + ((AtributoDerivadoFigure)e).getAttributeType() + " " + (((AtributoDerivadoFigure)e).isNullable() != true ? "NOT NULL" : "") + ",\n";
            					bw.write(mycontent);
            				}
            			}*/
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
   
   public void generateEntityRelationship (ArrayList<Figure> entityRelationship, ArrayList<Figure> connection, ArrayList<Figure> attribute, ArrayList<Figure> keyAttribute, ArrayList<Figure> derivedAttribute){
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
	                        /*for (Figure m: derivedAttribute) {
	                            if (((ConnectionFigure)j).getEndFigure().equals(((AtributoDerivadoFigure)m))) {
	                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "")  + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }*/	                        
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
	                        /*for (Figure m: derivedAttribute) {
	                            if (((ConnectionFigure)j).getStartFigure().equals(((AtributoDerivadoFigure)m))) {
	                                mycontent = m.toString() + " " + ((AtributoDerivadoFigure)m).getAttributeType() + " " + (((AtributoDerivadoFigure)m).isNullable() != true ? "NOT NULL" : "") + ",\n";
	                                bw.write(mycontent);
	                            }
	                        }*/
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
		   String mycontent2 = new String();
		   String ownerEntity = new String(); 
		   String keyAtt = new String();
		   ArrayList<String> specName = new ArrayList<String>();
		   ArrayList<String> specKey = new ArrayList<String>();
		   File file = new File("/home/shinahk/Desktop/Test.sql");
		   FileWriter fw = new FileWriter(file,true);
		   bw = new BufferedWriter(fw);
		   
		   if (genspecOverlap.size() > 0){
			   for (Figure a : genspecOverlap) {
				   if (singleLineGenSpecConn.size() > 0){
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
				   } else {
					   for (Figure b : doubleLineGenSpecConn) {
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
													   specName.add(g.toString().toUpperCase().replaceAll("\\s+", "_"));
													   specKey.add(i.toString());
													   mycontent = "ALTER TABLE " + g.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + g.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + i.toString() + ") REFERENCES " + ownerEntity + " (" + keyAtt + ") INITIALLY DEFERRED DEFERRABLE;\n";   
													   bw.write(mycontent);
												   }
											   }
										   } else if (((ConnectionFigure)h).getEndFigure().equals(((EntidadeFigure)g))){
											   for (Figure i: keyAttribute) {
												   if (((ConnectionFigure)h).getStartFigure().equals(((AtributoChaveFigure)i))) {
													   specName.add(g.toString().toUpperCase().replaceAll("\\s+", "_"));
													   specKey.add(i.toString());
													   mycontent = "ALTER TABLE " + g.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + g.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + i.toString() + ") REFERENCES " + ownerEntity + " (" + keyAtt + ") INITIALLY DEFERRED DEFERRABLE;\n";   
													   bw.write(mycontent);
												   }
											   }                       
										   }                      
									   }
								   }
							   }
						   }
					   }
					   mycontent = "\nCREATE OR REPLACE TRIGGER genspecTrigger_" + ownerEntity + " AFTER INSERT OR DELETE OR UPDATE ON " + ownerEntity + "\nREFERENCING NEW AS n OLD as o FOR EACH ROW\nDECLARE ";
					   bw.write(mycontent);
					   for (int i = 0; i < specName.size(); i++) {
						   mycontent2 = "X" + i + " number; ";
						   bw.write(mycontent2);
					   }
					   mycontent = "\nBEGIN\n\tIF INSTERTING THEN\n";
					   bw.write(mycontent);
					   for (int i = 0; i < specName.size(); i++) {
						   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + i + " FROM " + specName.get(i) + " c WHERE c." + specKey.get(i) + " = :n." + keyAtt + ";\n";
						   bw.write(mycontent2);
					   }
					   mycontent = "\t\tIF(";
					   bw.write(mycontent);
					   for (int i = 0; i < specName.size(); i++) {
						   mycontent2 = "X" + i + "+ ";
						   bw.write(mycontent2);
					   }
					   mycontent = "< 1) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\nEND;\n";
					   bw.write(mycontent);


					   for (int i = 0; i < specName.size(); i++) {
						   mycontent = "\nCREATE OR REPLACE TRIGGER genspecTrigger_" + specName.get(i) + " AFTER INSERT OR DELETE OR UPDATE ON " + specName.get(i) + "\nREFERENCING NEW AS n OLD as o FOR EACH ROW\nDECLARE ";
						   bw.write(mycontent);
						   for (int j = 0; j < specName.size()-1; j++) {
							   mycontent2 = "X" + j + " number; ";
							   bw.write(mycontent2);
						   }
						   mycontent = "\nBEGIN\n\tIF DELETING THEN\n";
						   bw.write(mycontent);
						   int aux = 0;
						   for (int l = 0; l < specName.size(); l++){
							   if(i != l){
								   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + aux + " FROM " + specName.get(l) + " c WHERE c." + specKey.get(l) + " = :n." + specKey.get(i) + ";\n";
								   bw.write(mycontent2);
								   aux++;
							   }   
						   }					   
						   mycontent = "\t\tIF(";
						   bw.write(mycontent);
						   for (int l = 0; l < specName.size()-1; l++) {
							   mycontent2 = "X" + l + "+ ";
							   bw.write(mycontent2);
						   }
						   mycontent = "< 1) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\nEND;\n";
						   bw.write(mycontent);					   
					   }
				   }
			   }
		   } else {
			   for (Figure a : genspecDisjoint) {
				   if (singleLineGenSpecConn.size() > 0){
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
													   specName.add(g.toString().toUpperCase().replaceAll("\\s+", "_"));
													   specKey.add(i.toString());
													   mycontent = "ALTER TABLE " + g.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + g.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + i.toString() + ") REFERENCES " + ownerEntity + " (" + keyAtt + ") ON DELETE CASCADE;\n";   
													   bw.write(mycontent);
												   }
											   }
										   } else if (((ConnectionFigure)h).getEndFigure().equals(((EntidadeFigure)g))){
											   for (Figure i: keyAttribute) {
												   if (((ConnectionFigure)h).getStartFigure().equals(((AtributoChaveFigure)i))) {
													   specName.add(g.toString().toUpperCase().replaceAll("\\s+", "_"));
													   specKey.add(i.toString());
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
					   for (int i = 0; i < specName.size(); i++) {
						   mycontent = "\nCREATE OR REPLACE TRIGGER genspecTrigger_" + specName.get(i) + " AFTER INSERT OR DELETE OR UPDATE ON " + specName.get(i) + "\nREFERENCING NEW AS n OLD as o FOR EACH ROW\nDECLARE ";
						   bw.write(mycontent);
						   for (int j = 0; j < specName.size()-1; j++) {
							   mycontent2 = "X" + j + " number; ";
							   bw.write(mycontent2);
						   }
						   mycontent = "\nBEGIN\n\tIF INSERTING THEN\n";
						   bw.write(mycontent);
						   int aux = 0;
						   for (int l = 0; l < specName.size(); l++){
							   if(i != l){
								   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + aux + " FROM " + specName.get(l) + " c WHERE c." + specKey.get(l) + " = :n." + specKey.get(i) + ";\n";
								   bw.write(mycontent2);
								   aux++;
							   }   
						   }					   
						   mycontent = "\t\tIF(";
						   bw.write(mycontent);
						   for (int l = 0; l < specName.size()-1; l++) {
							   mycontent2 = "X" + l + "+ ";
							   bw.write(mycontent2);
						   }
						   mycontent = "!= 0) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\nEND;\n";
						   bw.write(mycontent);					   
					   }
				   } else {
					   for (Figure b : doubleLineGenSpecConn) {
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
													   specName.add(g.toString().toUpperCase().replaceAll("\\s+", "_"));
													   specKey.add(i.toString());
													   mycontent = "ALTER TABLE " + g.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + g.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + i.toString() + ") REFERENCES " + ownerEntity + " (" + keyAtt + ") INITIALLY DEFERRED DEFERRABLE;\n";   
													   bw.write(mycontent);
												   }
											   }
										   } else if (((ConnectionFigure)h).getEndFigure().equals(((EntidadeFigure)g))){
											   for (Figure i: keyAttribute) {
												   if (((ConnectionFigure)h).getStartFigure().equals(((AtributoChaveFigure)i))) {
													   specName.add(g.toString().toUpperCase().replaceAll("\\s+", "_"));
													   specKey.add(i.toString());
													   mycontent = "ALTER TABLE " + g.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + g.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + i.toString() + ") REFERENCES " + ownerEntity + " (" + keyAtt + ") INITIALLY DEFERRED DEFERRABLE;\n";   
													   bw.write(mycontent);
												   }
											   }                       
										   }                      
									   }
								   }
							   }
						   }
					   }
					   mycontent = "\nCREATE OR REPLACE TRIGGER genspecTrigger_" + ownerEntity + " AFTER INSERT OR DELETE OR UPDATE ON " + ownerEntity + "\nREFERENCING NEW AS n OLD as o FOR EACH ROW\nDECLARE ";
					   bw.write(mycontent);
					   for (int i = 0; i < specName.size(); i++) {
						   mycontent2 = "X" + i + " number; ";
						   bw.write(mycontent2);
					   }
					   mycontent = "\nBEGIN\n\tIF INSTERTING THEN\n";
					   bw.write(mycontent);
					   for (int i = 0; i < specName.size(); i++) {
						   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + i + " FROM " + specName.get(i) + " c WHERE c." + specKey.get(i) + " = :n." + keyAtt + ";\n";
						   bw.write(mycontent2);
					   }
					   mycontent = "\t\tIF(";
					   bw.write(mycontent);
					   for (int i = 0; i < specName.size(); i++) {
						   mycontent2 = "X" + i + "+ ";
						   bw.write(mycontent2);
					   }
					   mycontent = "< 1) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\nEND;\n";
					   bw.write(mycontent);


					   for (int i = 0; i < specName.size(); i++) {
						   mycontent = "\nCREATE OR REPLACE TRIGGER genspecTrigger_" + specName.get(i) + " AFTER INSERT OR DELETE OR UPDATE ON " + specName.get(i) + "\nREFERENCING NEW AS n OLD as o FOR EACH ROW\nDECLARE ";
						   bw.write(mycontent);
						   for (int j = 0; j < specName.size()-1; j++) {
							   mycontent2 = "X" + j + " number; ";
							   bw.write(mycontent2);
						   }
						   mycontent = "\nBEGIN\n\tIF INSERTING THEN\n";
						   bw.write(mycontent);
						   int aux = 0;
						   for (int l = 0; l < specName.size(); l++){
							   if(i != l){
								   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + aux + " FROM " + specName.get(l) + " c WHERE c." + specKey.get(l) + " = :n." + specKey.get(i) + ";\n";
								   bw.write(mycontent2);
								   aux++;
							   }   
						   }					   
						   mycontent = "\t\tIF(";
						   bw.write(mycontent);
						   for (int l = 0; l < specName.size()-1; l++) {
							   mycontent2 = "X" + l + "+ ";
							   bw.write(mycontent2);
						   }
						   mycontent = "!= 0) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\n";
						   bw.write(mycontent);

						   mycontent = "\tIF DELETING THEN\n";
						   bw.write(mycontent);
						   int aux2 = 0;
						   for (int l = 0; l < specName.size(); l++){
							   if(i != l){
								   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + aux2 + " FROM " + ownerEntity + " c WHERE c." + keyAtt + " = :o." + specKey.get(i) + ";\n";
								   bw.write(mycontent2);
								   aux2++;
							   }   
						   }					   
						   mycontent = "\t\tIF(";
						   bw.write(mycontent);
						   for (int l = 0; l < specName.size()-1; l++) {
							   mycontent2 = "X" + l + "+ ";
							   bw.write(mycontent2);
						   }
						   mycontent = "!= 0) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\n";
						   bw.write(mycontent);

						   mycontent = "\tIF UPDATING THEN\n";
						   bw.write(mycontent);
						   int aux3 = 0;
						   for (int l = 0; l < specName.size(); l++){
							   if(i != l){
								   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + aux3 + " FROM " + ownerEntity + " c WHERE c." + keyAtt + " = :o." + specKey.get(i) + ";\n";
								   bw.write(mycontent2);
								   aux3++;
							   }   
						   }
						   mycontent = "\t\tIF(";
						   bw.write(mycontent);
						   for (int l = 0; l < specName.size()-1; l++) {
							   mycontent2 = "X" + l + "+ ";
							   bw.write(mycontent2);
						   }
						   mycontent = "!= 0) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\n";
						   bw.write(mycontent);
						   int aux4 = 0;
						   for (int m = 0; m < specName.size(); m++){
							   if(i != m){
								   mycontent2 = "\t\tSELECT COUNT(*) INTO X" + aux4 + " FROM " + specName.get(m) + " c WHERE c." + specKey.get(m) + " = :n." + specKey.get(i) + ";\n";
								   bw.write(mycontent2);
								   aux4++;
							   }   
						   }
						   mycontent = "\t\tIF(";
						   bw.write(mycontent);
						   for (int l = 0; l < specName.size()-1; l++) {
							   mycontent2 = "X" + l + "+ ";
							   bw.write(mycontent2);
						   }
						   mycontent = "!= 0) THEN RAISE_APPLICATION_ERROR(-20000, 'Violação detectada!'); END IF;\n\tEND IF;\nEND;\n";
						   bw.write(mycontent);
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
   
   public void generateRelationships(ArrayList<Figure> strongEntity, ArrayList<Figure> connection, ArrayList<Figure> keyAttribute, ArrayList<Figure> singleLineConnectionUm,	ArrayList<Figure> singleLineConnectionN, ArrayList<Figure> doubleLineConnectionUm,	ArrayList<Figure> doubleLineConnectionN, ArrayList<Figure> relationship, ArrayList<Figure> entityRelationship) {
	   BufferedWriter bw = null;
	   try {
		   ArrayList<Figure> slcN = new ArrayList<Figure>();
		   ArrayList<Figure> dlcN = new ArrayList<Figure>();
		   String entRel = new String();
		   String ownerEntity = new String();
		   String ownerKey = new String();
		   String ownerEntity2 = new String();
		   String ownerKey2 = new String();
		   String mycontent = new String();
		   String mycontent2 = new String();
		   String mycontent3 = new String();
		   File file = new File("/home/shinahk/Desktop/Test.sql");
		   FileWriter fw = new FileWriter(file,true);
		   bw = new BufferedWriter(fw);	
		   
		   if (entityRelationship.size() < 0) {
			   relationshipGenerator(strongEntity, connection, keyAttribute, singleLineConnectionUm, singleLineConnectionN, doubleLineConnectionUm, doubleLineConnectionN, relationship);
		   } else if (entityRelationship.size() > 0) {
			   relationshipGenerator(strongEntity, connection, keyAttribute, singleLineConnectionUm, singleLineConnectionN, doubleLineConnectionUm, doubleLineConnectionN, relationship);
			   for (Figure a : entityRelationship) {
				   //Check the number of connections to check the type of relationship
				   for (Figure b : singleLineConnectionN) {
					   if(((ConnectionFigure)b).getEndFigure().equals(a)) slcN.add(b);
				   }
				   for (Figure c : doubleLineConnectionN) {
					   if(((ConnectionFigure)c).getEndFigure().equals(a)) dlcN.add(c);
				   }

				   entRel = a.toString().toUpperCase().replaceAll("\\s+", "_");
				   
				   if(slcN.size() == 2){
					   for (int i = 0; i < slcN.size()-1; i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   for (int i = 1; i < slcN.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   mycontent = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK_" + entRel + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
					   mycontent2 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK2_" + entRel + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
					   mycontent3 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT PK_" + entRel + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
					   bw.write(mycontent);
					   bw.write(mycontent2);
					   bw.write(mycontent3);
				   } else if (slcN.size() == 1 && dlcN.size() == 1){
					   for (int i = 0; i < slcN.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   for (int i = 0; i < dlcN.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   mycontent = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK_" + entRel + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
					   mycontent2 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK2_" + entRel + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
					   mycontent3 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT PK_" + entRel + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
					   bw.write(mycontent);
					   bw.write(mycontent2);
					   bw.write(mycontent3);
				   } else if (dlcN.size() == 2){
					   for (int i = 0; i < dlcN.size()-1; i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   for (int i = 1; i < dlcN.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   mycontent = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK_" + entRel + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
					   mycontent2 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK2_" + entRel + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
					   mycontent3 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT PK_" + entRel + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
					   bw.write(mycontent);
					   bw.write(mycontent2);
					   bw.write(mycontent3);
				   }
				   slcN.clear();
				   dlcN.clear();
			   }
		   }
		   JOptionPane.showMessageDialog(null, "Relationship Created");
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
   
   public void relationshipGenerator(ArrayList<Figure> strongEntity, ArrayList<Figure> connection, ArrayList<Figure> keyAttribute, ArrayList<Figure> singleLineConnectionUm,	ArrayList<Figure> singleLineConnectionN, ArrayList<Figure> doubleLineConnectionUm,	ArrayList<Figure> doubleLineConnectionN, ArrayList<Figure> relationship) {
	   BufferedWriter bw = null;
	   try {
		   ArrayList<Figure> slcUm = new ArrayList<Figure>();
		   ArrayList<Figure> slcN = new ArrayList<Figure>();
		   ArrayList<Figure> dlcUm = new ArrayList<Figure>();
		   ArrayList<Figure> dlcN = new ArrayList<Figure>();
		   String ownerEntity = new String();
		   String ownerKey = new String();
		   String ownerKeyType = new String();
		   String ownerEntity2 = new String();
		   String ownerKey2 = new String();
		   String ownerKeyType2 = new String();
		   String mycontent = new String();
		   String mycontent2 = new String();
		   String mycontent3 = new String();
		   String mycontent4 = new String();
		   File file = new File("/home/shinahk/Desktop/Test.sql");
		   FileWriter fw = new FileWriter(file,true);
		   bw = new BufferedWriter(fw);

		   for (Figure a : relationship) {
			   //Check the number of connections to check the type of relationship
			   for (Figure b : singleLineConnectionUm) {
				   if(((ConnectionFigure)b).getEndFigure().equals(a)) slcUm.add(b);
			   }
			   for (Figure c : singleLineConnectionN) {
				   if(((ConnectionFigure)c).getEndFigure().equals(a)) slcN.add(c);
			   }
			   for (Figure d : doubleLineConnectionUm) {
				   if(((ConnectionFigure)d).getEndFigure().equals(a)) dlcUm.add(d);
			   }
			   for (Figure e : doubleLineConnectionN) {
				   if(((ConnectionFigure)e).getEndFigure().equals(a)) dlcN.add(e);
			   }
			   //1-1 Relationship (both partial participation)
			   if (slcUm.size() == 2){  
				   for (int i = 0; i < slcUm.size()-1; i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcUm.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   for (int i = 1; i < slcUm.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcUm.get(i)).getStartFigure().equals(f)) {
							   mycontent = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD " + ownerKey + " " + ownerKeyType + ";\n";
							   mycontent2 = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + f.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
							   bw.write(mycontent);
							   bw.write(mycontent2);
						   }
					   }
				   }
			   } else if (slcUm.size() == 1) {
				   for (int i = 0; i < slcUm.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcUm.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   if (dlcUm.size() > 0) {
					   for (int i = 0; i < dlcUm.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcUm.get(i)).getStartFigure().equals(f)) {
								   mycontent = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD " + ownerKey + " " + ownerKeyType + " NOT NULL;\n";
								   mycontent2 = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + f.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
								   bw.write(mycontent);
								   bw.write(mycontent2);
							   }
						   }
					   } 
				   } else if (slcN.size() > 0) {
					   for (int i = 0; i < slcN.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
								   mycontent = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD " + ownerKey + " " + ownerKeyType + ";\n";
								   mycontent2 = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + f.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
								   bw.write(mycontent);
								   bw.write(mycontent2);
							   }
						   }
					   } 
				   } else if (dlcN.size() > 0) {
					   for (int i = 0; i < dlcN.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
								   mycontent = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD " + ownerKey + " " + ownerKeyType + " NOT NULL;\n";
								   mycontent2 = "ALTER TABLE " + f.toString().toUpperCase().replaceAll("\\s+", "_") + " ADD CONSTRAINT FK_" + f.toString().toUpperCase().replaceAll("\\s+", "_") + " FOREIGN KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
								   bw.write(mycontent);
								   bw.write(mycontent2);
							   }
						   }
					   } 
				   }
			   } else if (dlcUm.size() > 0) {
				   if(dlcUm.size() == 2){
					   for (int i = 0; i < dlcUm.size()-1; i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcUm.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
												   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
												   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   for (int i = 1; i < dlcUm.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcUm.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
												   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey2 = h.toString();
												   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   mycontent = "CREATE TABLE " + ownerEntity + "_" + ownerEntity2 + "(\n\t" + ownerKey + " " + ownerKeyType + " NOT NULL,\n\t" + ownerKey2 + " " + ownerKeyType2 + " NOT NULL\n);\n";
					   mycontent2 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
					   mycontent3 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK2_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
					   mycontent4 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT PK_" + ownerEntity + "_" + ownerEntity2 + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
					   bw.write(mycontent);
					   bw.write(mycontent2);
					   bw.write(mycontent3);
					   bw.write(mycontent4);
				   } else if (dlcUm.size() == 1) {
					   for (int i = 0; i < dlcUm.size(); i++) {
						   for (Figure f : strongEntity) {
							   if (((ConnectionFigure)dlcUm.get(i)).getStartFigure().equals(f)) {
								   for (Figure g: connection) {
									   if (((ConnectionFigure)g).getStartFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
												   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
											   }
										   }		            			
									   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
										   for (Figure h: keyAttribute) {
											   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
												   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
												   ownerKey = h.toString();
												   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
											   }
										   }			            			
									   }                      
								   }
							   }
						   }
					   }
					   if (dlcN.size() > 0) {
						   for (int j = 0; j < dlcN.size(); j++) {
							   for (Figure k : strongEntity) {
								   if (((ConnectionFigure)dlcN.get(j)).getStartFigure().equals(k)) {
									   ownerEntity2 = k.toString().toUpperCase().replaceAll("\\s+", "_");
								   }
							   }
						   }
					   }
					   mycontent = "ALTER TABLE " + ownerEntity2 + " ADD " + ownerKey + " " + ownerKeyType + " NOT NULL;\n";
					   mycontent2 = "\nALTER TABLE " + ownerEntity2 + " ADD CONSTRAINT FK_" + ownerEntity2 + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ") INITIALLY DEFERRED DEFERREABLE;\n";
					   bw.write(mycontent);
					   bw.write(mycontent2);
					   mycontent = "\nCREATE OR REPLACE TRIGGER relTrigger_" + ownerEntity + " AFTER INSERT OR DELETE OR UPDATE ON " + ownerEntity + "\nREFERENCING NEW AS n OLD AS o FOR EACH ROW\nDECLARE X number;\nBEGIN\n\tIF INSERTING THEN";
					   mycontent2 = "\n\t\tSELECT COUNT (*) INTO X FROM " + ownerEntity2 + " c WHERE c." + ownerKey + " = :n." + ownerKey + ";\n\t\tIF (X = 0) THEN RAISE_APPLICATION_ERROR(-20000, 'Violacao detectada!'); END IF;\n\tELSEIF UPDATING THEN\n\t\tIF(:n." + ownerKey + " != :o." + ownerKey + ") THEN";
					   mycontent3 = "\n\t\t\tSELECT COUNT (*) INTO X FROM " + ownerEntity2 + " c WHERE c." + ownerKey + " = :o." + ownerKey + ";\n\t\t\tIF (X != 0) THEN RAISE_APPLICATION_ERROR(-20001, 'Violacao detectada!'); END IF;";
					   mycontent4 = "\n\t\t\tSELECT COUNT (*) INTO X FROM " + ownerEntity2 + " c WHERE c." + ownerKey + " = :n." + ownerKey + ";\n\t\t\tIF (X = 0) THEN RAISE_APPLICATION_ERROR(-20002, 'Violacao detectada!'); END IF;\n\t\tEND IF;\n\tELSEIF DELETING THEN";
					   bw.write(mycontent);
					   bw.write(mycontent2);
					   bw.write(mycontent3);
					   bw.write(mycontent4);
					   mycontent = "\n\t\t\tSELECT COUNT (*) INTO X FROM " + ownerEntity2 + " c WHERE c." + ownerKey + " = :o." + ownerKey + ";\n\t\tIF (X != 0) THEN RAISE_APPLICATION_ERROR(-20003, 'Violacao detectada!'); END IF;\n\tEND IF\nEND;\n";
					   mycontent2 = "\nCREATE OR REPLACE TRIGGER relTrigger_" + ownerEntity2 + " AFTER INSERT OR DELETE OR UPDATE ON " + ownerEntity2 + "\nREFERENCING NEW AS n OLD AS o FOR EACH ROW\nDECLARE Y number; PRAGMA AUTONOMOUS_TRANSACTION;\nBEGIN\n\tIF DELETING THEN";
					   mycontent3 = "\n\t\tSELECT COUNT (*) INTO Y FROM " + ownerEntity2 + " c WHERE c." + ownerKey + " = :o." + ownerKey + ";\n\t\tIF (Y != 0) THEN RAISE_APPLICATION_ERROR(-20004, 'Violacao detectada!'); END IF;\n\tELSEIF UPDATING THEN\n\t\tIF(:o." + ownerKey + " != :n." + ownerKey + ") THEN";
					   mycontent4 = "\n\t\t\tSELECT COUNT (*) INTO Y FROM " + ownerEntity2 + " c WHERE c." + ownerKey + " = :o." + ownerKey + ";\n\t\t\tIF (Y != 0) THEN RAISE_APPLICATION_ERROR(-20005, 'Violacao detectada!'); END IF;\n\t\tEND IF;\n\tEND IF;\nEND;";
					   bw.write(mycontent);
					   bw.write(mycontent2);
					   bw.write(mycontent3);
					   bw.write(mycontent4);
					} 
			   }else if(slcN.size() == 2){
				   for (int i = 0; i < slcN.size()-1; i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   for (int i = 1; i < slcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
											   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
											   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   mycontent = "\nCREATE TABLE " + ownerEntity + "_" + ownerEntity2 + "(\n\t" + ownerKey + " " + ownerKeyType + " NOT NULL,\n\t" + ownerKey2 + " " + ownerKeyType2 + " NOT NULL\n);\n";
				   mycontent2 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
				   mycontent3 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK2_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
				   mycontent4 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT PK_" + ownerEntity + "_" + ownerEntity2 + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
				   bw.write(mycontent);
				   bw.write(mycontent2);
				   bw.write(mycontent3);
				   bw.write(mycontent4);
			   } else if (slcN.size() == 1 && dlcN.size() == 1){
				   for (int i = 0; i < slcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   for (int i = 0; i < dlcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
											   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
											   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   mycontent = "\nCREATE TABLE " + ownerEntity + "_" + ownerEntity2 + "(\n\t" + ownerKey + " " + ownerKeyType + " NOT NULL,\n\t" + ownerKey2 + " " + ownerKeyType2 + " NOT NULL\n);\n";
				   mycontent2 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
				   mycontent3 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK2_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
				   mycontent4 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT PK_" + ownerEntity + "_" + ownerEntity2 + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
				   bw.write(mycontent);
				   bw.write(mycontent2);
				   bw.write(mycontent3);
				   bw.write(mycontent4);
			   } else if (dlcN.size() == 2){
				   for (int i = 0; i < dlcN.size()-1; i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
											   ownerKeyType = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   for (int i = 1; i < dlcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
											   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
											   ownerKeyType2 = ((AtributoChaveFigure)h).getAttributeType().toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   mycontent = "\nCREATE TABLE " + ownerEntity + "_" + ownerEntity2 + "(\n\t" + ownerKey + " " + ownerKeyType + " NOT NULL,\n\t" + ownerKey2 + " " + ownerKeyType2 + " NOT NULL\n);\n";
				   mycontent2 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
				   mycontent3 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT FK2_" + ownerEntity + "_" + ownerEntity2 + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
				   mycontent4 = "\nALTER TABLE " + ownerEntity + "_" + ownerEntity2 + " ADD CONSTRAINT PK_" + ownerEntity + "_" + ownerEntity2 + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
				   bw.write(mycontent);
				   bw.write(mycontent2);
				   bw.write(mycontent3);
				   bw.write(mycontent4);
			   }
			   slcUm.clear();
			   slcN.clear();
			   dlcUm.clear();
			   dlcN.clear();
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
   }
   
   private void generateMultivaluedAttribute(ArrayList<Figure> strongEntity, ArrayList<Figure> weakEntity, ArrayList<Figure> entityRelationship, ArrayList<Figure> connection, ArrayList<Figure> keyAttribute, ArrayList<Figure> multivaluedAttribute, ArrayList<Figure> singleLineConnectionN, ArrayList<Figure> doubleLineConnectionN) {
	   BufferedWriter bw = null;
       try {
    	   ArrayList<Figure> slcN = new ArrayList<Figure>();
		   ArrayList<Figure> dlcN = new ArrayList<Figure>();
		   String entRel = new String();
		   String ownerEntity = new String();
		   String ownerKey = new String();
		   String ownerEntity2 = new String();
		   String ownerKey2 = new String();
           String mycontent = new String();
           String mycontent2 = new String();
           String mycontent3 = new String();
           File file = new File("/home/shinahk/Desktop/Test.sql");
		   FileWriter fw = new FileWriter(file,true);
		   bw = new BufferedWriter(fw);
           
           for (Figure i: strongEntity) {
        	   for (Figure j: connection) {
        		   if (((ConnectionFigure)j).getStartFigure().equals(((EntidadeFigure)i))){
        			   for (Figure k: keyAttribute) {
						   if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)k))) {
						   
						   }
					   }
        			   for (Figure l: multivaluedAttribute) {
        				   if (((ConnectionFigure)j).getEndFigure().equals(((AtributoMultivaloradoFigure)l))) {
        					                                   
        				   }
        			   }          			
        		   } else if (((ConnectionFigure)j).getEndFigure().equals(((EntidadeFigure)i))){
        			   for (Figure k: keyAttribute) {
						   if (((ConnectionFigure)j).getStartFigure().equals(((AtributoChaveFigure)k))) {
						   
						   }
					   }
        			   for (Figure l: multivaluedAttribute) {
        				   if (((ConnectionFigure)j).getEndFigure().equals(((AtributoMultivaloradoFigure)l))) {
        					                                   
        				   }
        			   }           			            			
        		   }                      
        	   }
           }

           for (Figure a: weakEntity) {
        	   for (Figure b: connection) {
        		   if (((ConnectionFigure)b).getStartFigure().equals(((EntidadeFracaFigure)a))){
        			   for (Figure c: keyAttribute) {
						   if (((ConnectionFigure)b).getStartFigure().equals(((AtributoChaveFigure)c))) {
						   
						   }
					   }        			   
        			   for (Figure d: multivaluedAttribute) {
        				   if (((ConnectionFigure)b).getEndFigure().equals(((AtributoMultivaloradoFigure)d))) {
        					                                   
        				   }
        			   }
        		   } else if (((ConnectionFigure)b).getEndFigure().equals(((EntidadeFracaFigure)a))){
        			   for (Figure c: keyAttribute) {
						   if (((ConnectionFigure)b).getStartFigure().equals(((AtributoChaveFigure)c))) {
						   
						   }
					   }        			   
        			   for (Figure d: multivaluedAttribute) {
        				   if (((ConnectionFigure)b).getEndFigure().equals(((AtributoMultivaloradoFigure)d))) {
        					                                   
        				   }
        			   }
        		   }                      
        	   }
           }
           
           for (Figure a : entityRelationship) {
			   //Check the number of connections to check the type of relationship
			   for (Figure b : singleLineConnectionN) {
				   if(((ConnectionFigure)b).getEndFigure().equals(a)) slcN.add(b);
			   }
			   for (Figure c : doubleLineConnectionN) {
				   if(((ConnectionFigure)c).getEndFigure().equals(a)) dlcN.add(c);
			   }

			   entRel = a.toString().toUpperCase().replaceAll("\\s+", "_");
			   
			   if(slcN.size() == 2){
				   for (int i = 0; i < slcN.size()-1; i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   for (int i = 1; i < slcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   mycontent = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK_" + entRel + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
				   mycontent2 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK2_" + entRel + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
				   mycontent3 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT PK_" + entRel + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
				   bw.write(mycontent);
				   bw.write(mycontent2);
				   bw.write(mycontent3);
			   } else if (slcN.size() == 1 && dlcN.size() == 1){
				   for (int i = 0; i < slcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)slcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   for (int i = 0; i < dlcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   mycontent = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK_" + entRel + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
				   mycontent2 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK2_" + entRel + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
				   mycontent3 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT PK_" + entRel + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
				   bw.write(mycontent);
				   bw.write(mycontent2);
				   bw.write(mycontent3);
			   } else if (dlcN.size() == 2){
				   for (int i = 0; i < dlcN.size()-1; i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey = h.toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   for (int i = 1; i < dlcN.size(); i++) {
					   for (Figure f : strongEntity) {
						   if (((ConnectionFigure)dlcN.get(i)).getStartFigure().equals(f)) {
							   for (Figure g: connection) {
								   if (((ConnectionFigure)g).getStartFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getEndFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
										   }
									   }		            			
								   } else if (((ConnectionFigure)g).getEndFigure().equals(f)){
									   for (Figure h: keyAttribute) {
										   if (((ConnectionFigure)g).getStartFigure().equals(((AtributoChaveFigure)h))) {
											   ownerEntity2 = f.toString().toUpperCase().replaceAll("\\s+", "_");
											   ownerKey2 = h.toString();
										   }
									   }			            			
								   }                      
							   }
						   }
					   }
				   }
				   mycontent = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK_" + entRel + " FOREING KEY (" + ownerKey + ") REFERENCES " + ownerEntity + " (" + ownerKey + ");\n";
				   mycontent2 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT FK2_" + entRel + " FOREING KEY (" + ownerKey2 + ") REFERENCES " + ownerEntity2 + " (" + ownerKey2 + ");\n";
				   mycontent3 = "\nALTER TABLE " + entRel + " ADD CONSTRAINT PK_" + entRel + " PRIMARY KEY (" + ownerKey + ", " + ownerKey2 + ");\n";
				   bw.write(mycontent);
				   bw.write(mycontent2);
				   bw.write(mycontent3);
			   }
			   slcN.clear();
			   dlcN.clear();
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
   }
}