/*
 * @(#)DrawApplicationModel.java  1.0  June 10, 2006
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

package org.jhotdraw.samples.draw;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JToolBar;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.DefaultApplicationModel;
import org.jhotdraw.app.Project;
import org.jhotdraw.draw.AbstractAttributedFigure;
import org.jhotdraw.draw.ConnectionTool;
import org.jhotdraw.draw.CreationTool;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.TextAreaTool;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.TextItalicoFigure;
import org.jhotdraw.draw.TextTool;
import org.jhotdraw.draw.action.ButtonFactory;
import org.jhotdraw.draw.notation.figure.chen.AtributoChaveFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoChaveParcialFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoDerivadoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.AtributoMultivaloradoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.ConnectionAtributoFigureChen;
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
import org.jhotdraw.util.ResourceBundleUtil;
/**
 * DrawApplicationModel.
 * 
 * 
 * 
 * @author Werner Randelshofer.
 * @version 1.0 June 10, 2006 Created.
 */
public class DrawApplicationModel extends DefaultApplicationModel {
    /**
     * This editor is shared by all projects.
     */
    private DefaultDrawingEditor sharedEditor;
    
    /** Creates a new instance. */
    public DrawApplicationModel() {
    }
    
    public DefaultDrawingEditor getSharedEditor() {
        if (sharedEditor == null) {
            sharedEditor = new DefaultDrawingEditor();
        }
        return sharedEditor;
    }
    
    public void initProject(Application a, Project p) {
        if (a.isSharingToolsAmongProjects()) {
            ((DrawProject) p).setEditor(getSharedEditor());
        }
    }
    /**
     * Creates toolbars for the application.
     * This class always returns an empty list. Subclasses may return other
     * values.
     */
    public List<JToolBar> createToolBars(Application a, Project pr) {
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
        DrawProject p = (DrawProject) pr;
        
        DrawingEditor editor;
        if (p == null) {
            editor = getSharedEditor();
        } else {
            editor = p.getEditor();
        }
        
        LinkedList<JToolBar> list = new LinkedList<JToolBar>();
        JToolBar tb;
        tb = new JToolBar();
       addCreationButtonsTo(tb, editor);
        tb.setName(labels.getString("drawToolBarTitle"));
        list.add(tb);
        
//        tb = new JToolBar();
//        ButtonFactory.addAttributesButtonsTo(tb, editor);
//        tb.setName(labels.getString("attributesToolBarTitle"));
//        list.add(tb);
        
        tb = new JToolBar();
        ButtonFactory.addAlignmentButtonsTo(tb, editor);
        tb.setName(labels.getString("alignmentToolBarTitle"));
        list.add(tb);
        return list;
    }
    private void addCreationButtonsTo(JToolBar tb, DrawingEditor editor) {
        addDefaultCreationButtonsTo(tb, editor, 
                ButtonFactory.createDrawingActions(editor), 
                ButtonFactory.createSelectionActions(editor)
                );
    }
    public void addDefaultCreationButtonsTo(JToolBar tb, final DrawingEditor editor,
            Collection<Action> drawingActions, Collection<Action> selectionActions) {
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
        
        ButtonFactory.addSelectionToolTo(tb, editor, drawingActions, selectionActions);
        tb.addSeparator();
        
        AbstractAttributedFigure af;
        CreationTool ct;
        ConnectionTool cnt;
        
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new EntidadeFigureChen().init()), "createEntidade", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new EntidadeFracaFigureChen().init()), "createEntidadeFraca", labels);
        tb.addSeparator();

        ButtonFactory.addToolTo(tb, editor, new CreationTool(new RelacionamentoFigureChen().init()), "createRelacionamento", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new RelacionamentoFracoFigureChen().init()), "createRelacionamentoFraco", labels); 
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new EntidadeRelacionamentoFigureChen().init()), "createEntidadeRelacionamento", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new TextItalicoFigure(labels.getString("createPapel"))), "createPapel", labels);
        tb.addSeparator();
        
        //ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new LineConnectionFigure()), "createElbowConnectionAtributo", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new ConnectionAtributoFigureChen()), "createElbowConnectionAtributo", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoFigureChen().init()), "createAtributo", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoChaveFigureChen().init()), "createAtributoChave", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoChaveParcialFigureChen().init()), "createAtributoChaveParcial", labels); 
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoDerivadoFigureChen().init()), "createAtributoDerivado", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoMultivaloradoFigureChen().init()), "createAtributoMultivalorado", labels);
        tb.addSeparator();
        
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new ConnectionParcialUmFigureChen() ), "createElbowUmConnection", labels);        
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new ConnectionParcialMuitosFigureChen()), "createElbowMuitosConnection", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new ConnectionTotalUmFigureChen()), "createElbowDoubleUmConnection", labels);        
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new ConnectionTotalMuitosFigureChen()), "createElbowDoubleMuitosConnection", labels);
        tb.addSeparator();
        
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new InheritanceDisjuncaoFigureChen().init()), "createDisjuncao", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new InheritanceSobreposicaoFigure().init()), "createSobreposicao", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new InheritanceUniaoFigure().init()), "createUniao", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new GeneralizacaoConnectionParcialFigureChen()), "createGeneralizacaoConnection", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new GeneralizacaoConnectionLineFigureChen() ), "createElbowConnection", labels);        
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new GeneralizacaoConnectionTotalFigureChen()), "createElbowDoubleConnection", labels);
        tb.addSeparator();
        
        ButtonFactory.addToolTo(tb, editor, new TextTool(new TextFigure()), "createText", labels);
        ButtonFactory.addToolTo(tb, editor, new TextAreaTool(new TextAreaFigure()), "createTextArea", labels);
    }
}
