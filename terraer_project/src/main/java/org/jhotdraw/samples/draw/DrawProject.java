/*
 * @(#)DrawProject.java  1.2  2006-12-26
 *
 * Copyright (c) 1996-2006 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 *
 */
package org.jhotdraw.samples.draw;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.jhotdraw.app.AbstractProject;
import org.jhotdraw.app.action.RedoAction;
import org.jhotdraw.app.action.UndoAction;
import org.jhotdraw.draw.DOMStorableInputOutputFormat;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingPageable;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.ImageInputFormat;
import org.jhotdraw.draw.ImageOutputFormat;
import org.jhotdraw.draw.InputFormat;
import org.jhotdraw.draw.OutputFormat;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.TextInputFormat;
import org.jhotdraw.draw.action.ButtonFactory;
import org.jhotdraw.gui.PlacardScrollPaneLayout;
import org.jhotdraw.io.ExtensionFileFilter;
import org.jhotdraw.undo.UndoRedoManager;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * A drawing project.
 *
 * @author Werner Randelshofer
 * @version 1.2 2006-12-26 Reworked I/O support. 
 * <br>1.1 2006-06-10 Extended to support DefaultDrawApplicationModel.
 * <br>1.0 2006-02-07 Created.
 */
public class DrawProject extends AbstractProject {
    
	public static DrawProject instance;
	
    /**
     * Each DrawProject uses its own undo redo manager.
     * This allows for undoing and redoing actions per project.
     */
    private UndoRedoManager undo;
    
    /**
     * Depending on the type of an application, there may be one editor per
     * project, or a single shared editor for all projects.
     */
    private DrawingEditor editor;
    
    /**
     * Creates a new Project.
     */
    public DrawProject() {
    	instance = this;
    }
    
    /**
     * Initializes the project.
     */
    public void init() {
        super.init();
        
        initComponents();
        
        JPanel zoomButtonPanel = new JPanel(new BorderLayout());
        scrollPane.setLayout(new PlacardScrollPaneLayout());
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        
        setEditor(new DefaultDrawingEditor());
        undo = new UndoRedoManager();
        view.setDrawing(createDrawing());
        view.getDrawing().addUndoableEditListener(undo);
        initActions();
        undo.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setHasUnsavedChanges(undo.hasSignificantEdits());
            }
        });
       
        
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
        
        JPanel placardPanel = new JPanel(new BorderLayout());
        javax.swing.AbstractButton pButton;
        pButton = ButtonFactory.createZoomButton(view);
        pButton.putClientProperty("Quaqua.Button.style","placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin",new Insets(0,0,0,0));
        pButton.setFont(UIManager.getFont("SmallSystemFont"));
        placardPanel.add(pButton, BorderLayout.WEST);
        pButton = ButtonFactory.createToggleGridButton(view);
        pButton.putClientProperty("Quaqua.Button.style","placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin",new Insets(0,0,0,0));
        pButton.setFont(UIManager.getFont("SmallSystemFont"));
        labels.configureToolBarButton(pButton, "alignGridSmall");
        placardPanel.add(pButton, BorderLayout.EAST);
        scrollPane.add(placardPanel, JScrollPane.LOWER_LEFT_CORNER);
    }
    
    /**
     * Creates a new Drawing for this Project.
     */
    protected Drawing createDrawing() {
        Drawing drawing = new QuadTreeDrawing();
        DOMStorableInputOutputFormat ioFormat =
                new DOMStorableInputOutputFormat(new DrawFigureFactory());
        LinkedList<InputFormat> inputFormats = new LinkedList<InputFormat>();
        inputFormats.add(ioFormat);
        inputFormats.add(new ImageInputFormat(new ImageFigure()));
        inputFormats.add(new ImageInputFormat(new ImageFigure(), "JPG","Joint Photographics Experts Group (JPEG)", "jpg", BufferedImage.TYPE_INT_RGB));
        inputFormats.add(new ImageInputFormat(new ImageFigure(), "GIF","Graphics Interchange Format (GIF)", "gif", BufferedImage.TYPE_INT_ARGB));
        inputFormats.add(new ImageInputFormat(new ImageFigure()));
        inputFormats.add(new TextInputFormat(new TextFigure()));
        TextAreaFigure taf = new TextAreaFigure();
        taf.setBounds(new Point2D.Double(10,10), new Point2D.Double(60,40));
        inputFormats.add(new TextInputFormat(taf));
        drawing.setInputFormats(inputFormats);
        LinkedList<OutputFormat> outputFormats = new LinkedList<OutputFormat>();
        outputFormats.add(ioFormat);
        outputFormats.add(new ImageOutputFormat("JPG","Joint Photographics Experts Group (JPEG)", "jpg", BufferedImage.TYPE_INT_RGB));
        outputFormats.add(new ImageOutputFormat("GIF","Graphics Interchange Format (GIF)", "gif", BufferedImage.TYPE_INT_ARGB));
        drawing.setOutputFormats(outputFormats);
        return drawing;
    }
    
    public DrawingEditor getEditor() {
        return editor;
    }
    public void setEditor(DrawingEditor newValue) {
        DrawingEditor oldValue = editor;
        if (oldValue != null) {
            oldValue.remove(view);
        }
        editor = newValue;
        if (newValue != null) {
            newValue.add(view);
        }
    }
    
    /**
     * Creates a Pageable object for printing the project.
     */
    public Pageable createPageable() {
        return new DrawingPageable(view.getDrawing());
        
    }
    
    
    /**
     * Initializes project specific actions.
     */
    private void initActions() {
        putAction(UndoAction.ID, undo.getUndoAction());
        putAction(RedoAction.ID, undo.getRedoAction());
    }
    
    //Era protected
    public void setHasUnsavedChanges(boolean newValue) {
        super.setHasUnsavedChanges(newValue);
        undo.setHasSignificantEdits(newValue);
    }
    
    @Override
    public boolean hasUnsavedChanges() {
    	/*DrawingView dv = this.editor.getActiveView();
    	if (dv.getDrawing().getFigureCount()!=0){
    		return true;
    	}*/
    	return super.hasUnsavedChanges();
    }
    
    /**
     * Writes the project to the specified file.
     */
    public void write(File f) throws IOException {
            Drawing drawing = view.getDrawing();
            OutputFormat outputFormat = drawing.getOutputFormats().get(0);
            outputFormat.write(f, drawing);
    }
    
    public void writeImage(File f) throws IOException {
        Drawing drawing = view.getDrawing();
        ImageOutputFormat outputFormat = (ImageOutputFormat) drawing.getOutputFormats().get(1);
        outputFormat.write(f, drawing);
    }
    
    public JFileChooser createSaveImageChooser() {
    	ExtensionFileFilter ff = (ExtensionFileFilter) view.getDrawing().getOutputFormats().get(1).getFileFilter();
        JFileChooser c = super.createSaveChooser();
        c.addChoosableFileFilter(ff);
        return c;
    }
    
    /**
     * Reads the project from the specified file.
     */
    public void read(File f) throws IOException {
        try {
            final Drawing drawing = createDrawing();
            InputFormat inputFormat = drawing.getInputFormats().get(0);
            inputFormat.read(f, drawing);
            SwingUtilities.invokeAndWait(new Runnable() { public void run() {
                view.getDrawing().removeUndoableEditListener(undo);
                view.setDrawing(drawing);
                view.getDrawing().addUndoableEditListener(undo);
                undo.discardAllEdits();
            }});
            //org.jhotdraw.draw.TerraFigureTree.getInstance().removeAll();
            //org.jhotdraw.draw.TerraFigureTree.getInstance().buildDrawing(drawing);
        } catch (InterruptedException e) {
            InternalError error = new InternalError();
            e.initCause(e);
            throw error;
        } catch (InvocationTargetException e) {
            InternalError error = new InternalError();
            e.initCause(e);
            throw error;
        }
    }
    
    
    /**
     * Sets a drawing editor for the project.
     */
    public void setDrawingEditor(DrawingEditor newValue) {
        if (editor != null) {
            editor.remove(view);
        }
        editor = newValue;
        if (editor != null) {
            editor.add(view);
        }
    }
    
    /**
     * Gets the drawing editor of the project.
     */
    public DrawingEditor getDrawingEditor() {
        return editor;
    }
    
    /**
     * Clears the project.
     */
    public void clear() {
        //view.setDrawing(new DefaultDrawing());
    	//TODO: Colocar o mesmo Drawing que existia, porém limpo.
    	view.getDrawing().clear();
        undo.discardAllEdits();
    }
    
    @Override protected JFileChooser createOpenChooser() {
        JFileChooser c = super.createOpenChooser();
        c.addChoosableFileFilter(new ExtensionFileFilter("Drawing (xml)","xml"));
        return c;
    }
    @Override protected JFileChooser createSaveChooser() {
        JFileChooser c = super.createSaveChooser();
        c.addChoosableFileFilter(new ExtensionFileFilter("Drawing (xml)","xml"));
        return c;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        scrollPane = new javax.swing.JScrollPane();
        view = new org.jhotdraw.draw.DefaultDrawingView();

        setLayout(new java.awt.BorderLayout());

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(view);

        add(scrollPane, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private org.jhotdraw.draw.DefaultDrawingView view;
    // End of variables declaration//GEN-END:variables
    
}
