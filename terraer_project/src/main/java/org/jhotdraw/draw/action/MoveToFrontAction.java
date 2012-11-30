/*
 * @(#)ToFrontAction.java  1.0  24. November 2003
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
 */

package org.jhotdraw.draw.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * ToFrontAction.
 *
 * @author  Werner Randelshofer
 * @version 1.0 24. November 2003  Created.
 */
public class MoveToFrontAction extends AbstractSelectedAction {
       private ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels", Locale.getDefault());
    
       public static String ID = "moveToFront";
       
    /** Creates a new instance. */
    public MoveToFrontAction(DrawingEditor editor) {
        super(editor);
        labels.configureAction(this, ID);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        final LinkedList<Figure> figures = new LinkedList<Figure>(view.getSelectedFigures());
        bringToFront(view, figures);
        fireUndoableEditHappened(new AbstractUndoableEdit() {
            public String getPresentationName() {
       return labels.getString(ID);
            }
            public void redo() throws CannotRedoException {
                super.redo();
                MoveToFrontAction.bringToFront(view, figures);
            }
            public void undo() throws CannotUndoException {
                super.undo();
                MoveToBackAction.sendToBack(view, figures);
            }
        }
        );
    }
    public static void bringToFront(DrawingView view, Collection<Figure> figures) {
        Drawing drawing = view.getDrawing();
        Iterator i = drawing.sort(figures).iterator();
        while (i.hasNext()) {
            Figure figure = (Figure) i.next();
            drawing.bringToFront(figure);
        }
    }
    
}
