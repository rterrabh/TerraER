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
import org.jhotdraw.draw.AtributoChaveFigure;
import org.jhotdraw.draw.AtributoChaveParcialFigure;
import org.jhotdraw.draw.AtributoDerivadoFigure;
import org.jhotdraw.draw.AtributoFigure;
import org.jhotdraw.draw.AtributoMultivaloradoFigure;
import org.jhotdraw.draw.ConnectionAttribute;
import org.jhotdraw.draw.ConnectionTool;
import org.jhotdraw.draw.CreationTool;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DisjuncaoFigure;
import org.jhotdraw.draw.DoubleLineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.EntidadeFigure;
import org.jhotdraw.draw.EntidadeFracaFigure;
import org.jhotdraw.draw.EntidadeRelacionamentoFigure;
import org.jhotdraw.draw.GeneralizacaoLineConnectionFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionUmFigure;
import org.jhotdraw.draw.LabeledLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledLineConnectionUmFigure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.LineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.RelacionamentoFigure;
import org.jhotdraw.draw.RelacionamentoFracoFigure;
import org.jhotdraw.draw.SobreposicaoFigure;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.TextAreaTool;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.TextItalicoFigure;
import org.jhotdraw.draw.TextTool;
import org.jhotdraw.draw.UniaoFigure;
import org.jhotdraw.draw.action.ButtonFactory;
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
//        ConnectionFigure lc;
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new EntidadeFigure().init(), "createEntidade"), "createEntidade", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new EntidadeFracaFigure().init(), "createEntidadeFraca"), "createEntidadeFraca", labels);
        tb.addSeparator();
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new RelacionamentoFigure().init(), "createRelacionamento"), "createRelacionamento", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new RelacionamentoFracoFigure().init(), "createRelacionamentoFraco"), "createRelacionamentoFraco", labels); 
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new EntidadeRelacionamentoFigure().init(), "createEntidadeRelacionamento"), "createEntidadeRelacionamento", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new TextItalicoFigure(labels.getString("createPapel"))), "createPapel", labels);
        //ButtonFactory.addToolTo(tb, editor, new CreationTool(new RoundRectangleFigure()), "createRoundRectangle", labels);
        
        tb.addSeparator();
        //ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new LineConnectionFigure(), "createElbowConnectionAtributo"), "createElbowConnectionAtributo", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new ConnectionAttribute(), "createElbowConnectionAtributo"), "createElbowConnectionAtributo", labels);
//        lc =  cnt.getPrototype();
//        lc.setLiner(new ElbowLiner());
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoFigure().init(),"createAtributo" ), "createAtributo", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoChaveFigure().init(), "createAtributoChave"), "createAtributoChave", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoChaveParcialFigure().init(), "createAtributoChaveParcial"), "createAtributoChaveParcial", labels); 
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoDerivadoFigure().init(), "createAtributoDerivado"), "createAtributoDerivado", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new AtributoMultivaloradoFigure().init(), "createAtributoMultivalorado"), "createAtributoMultivalorado", labels);
        tb.addSeparator();

        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new LabeledLineConnectionUmFigure(), "createElbowUmConnection" ), "createElbowUmConnection", labels);        
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new LabeledLineConnectionMuitosFigure(), "createElbowMuitosConnection"), "createElbowMuitosConnection", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new LabeledDoubleLineConnectionUmFigure(), "createElbowDoubleUmConnection"), "createElbowDoubleUmConnection", labels);        
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new LabeledDoubleLineConnectionMuitosFigure(), "createElbowDoubleMuitosConnection") , "createElbowDoubleMuitosConnection", labels);
        tb.addSeparator();

        ButtonFactory.addToolTo(tb, editor, new CreationTool(new DisjuncaoFigure().init(), "createDisjuncao"), "createDisjuncao", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new SobreposicaoFigure().init(), "createSobreposicao"), "createSobreposicao", labels);
        ButtonFactory.addToolTo(tb, editor, new CreationTool(new UniaoFigure().init(), "createUniao"), "createUniao", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new GeneralizacaoLineConnectionFigure(), "createGeneralizacaoConnection"), "createGeneralizacaoConnection", labels);
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new LineConnectionGeneralizacaoFigure(), "createElbowConnection"), "createElbowConnection", labels);        
        ButtonFactory.addToolTo(tb, editor, cnt = new ConnectionTool(new DoubleLineConnectionGeneralizacaoFigure(), "createElbowDoubleConnection"), "createElbowDoubleConnection", labels);
        
        tb.addSeparator();
        ButtonFactory.addToolTo(tb, editor, new TextTool(new TextFigure()), "createText", labels);
        ButtonFactory.addToolTo(tb, editor, new TextAreaTool(new TextAreaFigure()), "createTextArea", labels);
    }
}
