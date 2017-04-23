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

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.BezierFigure;
import org.jhotdraw.draw.ChopBezierConnector;
import org.jhotdraw.draw.ChopDiamondConnector;
import org.jhotdraw.draw.ChopEllipseConnector;
import org.jhotdraw.draw.ChopRectangleConnector;
//import org.jhotdraw.draw.ChopRoundRectangleConnector; @beforeCleanUp:removed_Obede
//import org.jhotdraw.draw.ChopTriangleConnector; @beforeCleanUp:removed_Obede 
import org.jhotdraw.draw.CircleFigure;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DiamondFigure;
//import org.jhotdraw.draw.ElbowLiner; @beforeCleanUp:removed_Obede
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.LabeledLineConnectionFigure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.LineFigure;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.TextItalicoFigure;
import org.jhotdraw.draw.TextNegritoFigure;
import org.jhotdraw.draw.notation.figure.chen.InheritanceDisjuncaoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.GeneralizacaoConnectionTotalFigureChen;
import org.jhotdraw.draw.notation.figure.chen.GeneralizacaoConnectionParcialFigureChen;
import org.jhotdraw.draw.notation.figure.chen.GeneralizacaoConnectionLineFigureChen;
import org.jhotdraw.draw.notation.finalversion.AtributoChaveFigureChen;
import org.jhotdraw.draw.notation.finalversion.AtributoChaveParcialFigureChen;
import org.jhotdraw.draw.notation.finalversion.AtributoDerivadoFigureChen;
import org.jhotdraw.draw.notation.finalversion.AtributoFigureChen;
import org.jhotdraw.draw.notation.finalversion.AtributoMultivaloradoFigureChen;
import org.jhotdraw.draw.notation.finalversion.ConnectionParcialMuitosFigureChen;
import org.jhotdraw.draw.notation.finalversion.ConnectionParcialUmFigureChen;
import org.jhotdraw.draw.notation.finalversion.ConnectionTotalMuitosFigureChen;
import org.jhotdraw.draw.notation.finalversion.ConnectionTotalUmFigureChen;
import org.jhotdraw.draw.notation.finalversion.EntidadeFigureChen;
import org.jhotdraw.draw.notation.finalversion.EntidadeFracaFigureChen;
import org.jhotdraw.draw.notation.finalversion.EntidadeRelacionamentoFigureChen;
import org.jhotdraw.draw.notation.finalversion.RelacionamentoFigureChen;
import org.jhotdraw.draw.notation.finalversion.RelacionamentoFracoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.InheritanceSobreposicaoFigureChen;
import org.jhotdraw.draw.notation.figure.chen.InheritanceUniaoFigureChen;
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
        { RelacionamentoFigureChen.class, "rel" },
        { EntidadeFigureChen.class, "ent" },
        { EntidadeRelacionamentoFigureChen.class, "entrel" },
        { RelacionamentoFracoFigureChen.class, "relfraco" },

        { AtributoFigureChen.class, "atr" },
        { AtributoChaveFigureChen.class, "atrchave" },
        { EntidadeFracaFigureChen.class,"entfraca"},
        { AtributoDerivadoFigureChen.class,"atrderivado"},
        { AtributoMultivaloradoFigureChen.class,"atrmulti"},
        { AtributoChaveParcialFigureChen.class, "atrchaveparcial"},
        
        { InheritanceSobreposicaoFigureChen.class, "sobreposicao"},
        { InheritanceDisjuncaoFigureChen.class, "disjuncao"},
        { InheritanceUniaoFigureChen.class, "uniao"},
        { CircleFigure.class, "circ"},
        { LineConnectionFigure.class, "lcf"},
        { LabeledLineConnectionFigure.class, "llabel"},
        { ConnectionParcialUmFigureChen.class, "llabelUm"},
        { ConnectionParcialMuitosFigureChen.class, "llabelMuitos"},
        { ConnectionTotalUmFigureChen.class, "llabelDoubleUm"},
        { ConnectionTotalMuitosFigureChen.class, "llabelDoubleMuitos"},
        { GeneralizacaoConnectionParcialFigureChen.class, "generalizacaoLine"},
        { GeneralizacaoConnectionLineFigureChen.class, "llabelGeneralizacao"},
        { GeneralizacaoConnectionTotalFigureChen.class, "llabelDoubleGeneralizacao"},

        
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
