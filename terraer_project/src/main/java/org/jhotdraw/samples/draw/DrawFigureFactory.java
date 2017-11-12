/*
 * @(#)DrawFigureFactory.java  1.0  February 17, 2004
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

//import org.jhotdraw.draw.ArrowTip; @beforeCleanUp:removed_Obede
import org.jhotdraw.draw.AtributoChaveFigure;
import org.jhotdraw.draw.AtributoChaveParcialFigure;
import org.jhotdraw.draw.AtributoDerivadoFigure;
import org.jhotdraw.draw.AtributoFigure;
import org.jhotdraw.draw.AtributoMultivaloradoFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.BezierFigure;
import org.jhotdraw.draw.ChopBezierConnector;
import org.jhotdraw.draw.ChopDiamondConnector;
import org.jhotdraw.draw.ChopEllipseConnector;
import org.jhotdraw.draw.ChopRectangleConnector;
//import org.jhotdraw.draw.ChopRoundRectangleConnector; @beforeCleanUp:removed_Obede
//import org.jhotdraw.draw.ChopTriangleConnector; @beforeCleanUp:removed_Obede 
import org.jhotdraw.draw.CircleFigure;
import org.jhotdraw.draw.ConnectionAttribute;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DiamondFigure;
import org.jhotdraw.draw.DisjuncaoFigure;
import org.jhotdraw.draw.DoubleLineConnectionGeneralizacaoFigure;
//import org.jhotdraw.draw.ElbowLiner; @beforeCleanUp:removed_Obede
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.EntidadeFigure;
import org.jhotdraw.draw.EntidadeFracaFigure;
import org.jhotdraw.draw.EntidadeRelacionamentoFigure;
import org.jhotdraw.draw.GeneralizacaoLineConnectionFigure;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledDoubleLineConnectionUmFigure;
import org.jhotdraw.draw.LabeledLineConnectionFigure;
import org.jhotdraw.draw.LabeledLineConnectionMuitosFigure;
import org.jhotdraw.draw.LabeledLineConnectionUmFigure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.LineConnectionGeneralizacaoFigure;
import org.jhotdraw.draw.LineFigure;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.RelacionamentoFigure;
import org.jhotdraw.draw.RelacionamentoFracoFigure;
//import org.jhotdraw.draw.RoundRectangleFigure; @beforeCleanUp:removed_Obede
import org.jhotdraw.draw.SobreposicaoFigure;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.TextItalicoFigure;
import org.jhotdraw.draw.TextNegritoFigure;
//import org.jhotdraw.draw.TriangleFigure; @beforeCleanUp:removed_Obede
import org.jhotdraw.draw.UniaoFigure;
import org.jhotdraw.xml.DefaultDOMFactory;
/**
 * DrawFigureFactory.
 *
 * @author  Werner Randelshofer
 * @version 1.0 February 17, 2004 Created.
 */
public class DrawFigureFactory extends DefaultDOMFactory {
    private final static Object[][] classTagArray = {
        { DefaultDrawing.class, "drawing" },
        { QuadTreeDrawing.class, "drawing" },
        { DiamondFigure.class, "diamond" },
        //{ TriangleFigure.class, "triangle" }, @beforeCleanUp:removed_Obede
        { BezierFigure.class, "bezier" },
        { RectangleFigure.class, "r" },
        //{ RoundRectangleFigure.class, "rr" }, @beforeCleanUp:removed_Obede
        { LineFigure.class, "l" },
        { BezierFigure.class, "b" },
        { EllipseFigure.class, "e" },
        { TextNegritoFigure.class, "tn"},
        { TextItalicoFigure.class, "ti"},        
        { TextFigure.class, "t" },
        { TextAreaFigure.class, "ta" },
        { ImageFigure.class, "image" },
        { GroupFigure.class, "g" },
        { RelacionamentoFigure.class, "rel" },
        { EntidadeFigure.class, "ent" },
        { EntidadeRelacionamentoFigure.class, "entrel" },
        { RelacionamentoFracoFigure.class, "relfraco" },

        { AtributoFigure.class, "atr" },
        { AtributoChaveFigure.class, "atrchave" },
        { EntidadeFracaFigure.class,"entfraca"},
        { AtributoDerivadoFigure.class,"atrderivado"},
        { AtributoMultivaloradoFigure.class,"atrmulti"},
        { AtributoChaveParcialFigure.class, "atrchaveparcial"},
        
        { SobreposicaoFigure.class, "sobreposicao"},
        { DisjuncaoFigure.class, "disjuncao"},
        { UniaoFigure.class, "uniao"},
        { CircleFigure.class, "circ"},
        { ConnectionAttribute.class, "lcf"}, //Para continuar funcionando com versões anteriores
        { ConnectionAttribute.class, "lcaf"},
        { LabeledLineConnectionFigure.class, "llabel"},
        { LabeledLineConnectionUmFigure.class, "llabelUm"},
        { LabeledLineConnectionMuitosFigure.class, "llabelMuitos"},
        { LabeledDoubleLineConnectionUmFigure.class, "llabelDoubleUm"},
        { LabeledDoubleLineConnectionMuitosFigure.class, "llabelDoubleMuitos"},
        { GeneralizacaoLineConnectionFigure.class, "generalizacaoLine"},
        { LineConnectionGeneralizacaoFigure.class, "llabelGeneralizacao"},
        { DoubleLineConnectionGeneralizacaoFigure.class, "llabelDoubleGeneralizacao"},

        
        //{ ArrowTip.class, "arrowTip" }, @beforeCleanUp:removed_Obede
        { ChopRectangleConnector.class, "rConnector" },
        { ChopEllipseConnector.class, "ellipseConnector" },
        //{ ChopRoundRectangleConnector.class, "rrConnector" }, @beforeCleanUp:removed_Obede
        //{ ChopTriangleConnector.class, "triangleConnector" }, @beforeCleanUp:removed_Obede
        { ChopDiamondConnector.class, "diamondConnector" },
        { ChopBezierConnector.class, "bezierConnector" },
        
        //{ ElbowLiner.class, "elbowLiner" }, @beforeCleanUp:removed_Obede
    };
    private final static Object[][] enumTagArray = {
        { AttributeKeys.StrokePlacement.class, "strokePlacement" },
        { AttributeKeys.StrokeType.class, "strokeType" },
        { AttributeKeys.Underfill.class, "underfill" },
        { AttributeKeys.Orientation.class, "orientation" }
    };
    
    /** Creates a new instance. */
    public DrawFigureFactory() {
        for (Object[] o : classTagArray) {
            addStorableClass((String) o[1], (Class) o[0]);
        }
        for (Object[] o : enumTagArray) {
            addEnumClass((String) o[1], (Class) o[0]);
        }
    }
}
