/*
 * @(#)ConnectionTool.java  4.0  2007-05-18
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

package org.jhotdraw.draw;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jhotdraw.geom.BezierPath;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * A tool to create a connection between two figures.
 * The  {@see ConnectionFigure} to be created is specified by a prototype.
 * The location of the start and end points are controlled by {@see Connector}s.
 * <p>
 * To create a connection using the ConnectionTool, the user does the following
 * mouse gestures on a DrawingView:
 * <ol>
 * <li>Press the mouse button inside of a Figure. If the ConnectionTool can
 * find a Connector at this location, it uses it as the starting point for
 * the connection.</li>
 * <li>Drag the mouse while keeping the mouse button pressed, and then release
 * the mouse button. This defines the end point of the connection.
 * If the ConnectionTool finds a Connector at this location, it uses it
 * as the end point of the connection and creates a ConnectionFigure.</li>
 * </ol>
 *
 * @author Werner Randelshofer
 * @version 4.0 2007-05 Reworked due to changes in ConnectionFigure interface.
 * Removed split/join functionality for connection points.
 * <br>3.1 2006-07-15 Added support for prototype class name.
 * <br>3.0 2006-06-07 Reworked.
 * <br>2.1 2006-03-15 When user is not pressing the mouse button, we use
 * the mouse over view as the current view.
 * <br>2.0.1 2006-02-14 Fixed drawing code.
 * <br>2.0 2006-01-14 Changed to support double precision coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class ConnectionTool extends AbstractTool {
    private final static int ANCHOR_WIDTH = 6;
    
    /**
     * Attributes to be applied to the created ConnectionFigure.
     * These attributes override the default attributes of the
     * DrawingEditor.
     */
    private Map<AttributeKey, Object> prototypeAttributes;
    /**
     * The Connector at the start point of the connection.
     */
    private Connector startConnector;
    /**
     * The Connector at the end point of the connection.
     */
    private Connector endConnector;
    
    /**
     * The created figure.
     */
    protected ConnectionFigure createdFigure;
    
    /**
     * the prototypical figure that is used to create new
     * connections.
     */
    protected ConnectionFigure  prototype;
    
    /**
     * The figure for which we enabled drawing of connectors.
     */
    protected Figure targetFigure;
    
    protected Collection<Connector> connectors = Collections.emptyList();
    
    /**
     * A localized name for this tool. The presentationName is displayed by the
     * UndoableEdit.
     */
    private String presentationName;
    
    /** Creates a new instance.
     */
    public ConnectionTool(ConnectionFigure prototype) {
        this(prototype, null, null);
    }
    
    public ConnectionTool(ConnectionFigure prototype, String name) {
        this(prototype, null, name);
    }
    public ConnectionTool(ConnectionFigure prototype, Map<AttributeKey, Object> attributes) {
        this(prototype, attributes, null);
    }
    public ConnectionTool(ConnectionFigure prototype, Map<AttributeKey, Object> attributes, String presentationName) {
        this.prototype = prototype;
        this.prototypeAttributes = attributes;
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
        if (presentationName == null) {            
            presentationName = labels.getString("createConnectionFigure");
        } else {
        	presentationName = labels.getString(presentationName + ".desc");
        }
        this.presentationName = presentationName;
    }
    public ConnectionTool(String prototypeClassName) {
        this(prototypeClassName, null, null);
    }
    public ConnectionTool(String prototypeClassName, Map<AttributeKey, Object> attributes, String presentationName) {
        try {
            this.prototype = (ConnectionFigure) Class.forName(prototypeClassName).newInstance();
        } catch (Exception e) {
            InternalError error = new InternalError("Unable to create ConnectionFigure from "+prototypeClassName);
            error.initCause(e);
            throw error;
        }
        this.prototypeAttributes = attributes;
        if (presentationName == null) {
            ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
            presentationName = labels.getString("createConnectionFigure");
        }
        this.presentationName = presentationName;
    }
    public ConnectionFigure getPrototype() {
        return prototype;
    }
    
    public void mouseMoved(MouseEvent evt) {
        repaintConnectors(evt);
    }
    
    /**
     * Updates the list of connectors that we draw when the user
     * moves or drags the mouse over a figure to which can connect.
     */
    public void repaintConnectors(MouseEvent evt) {
        Rectangle2D.Double invalidArea = null;
        Point2D.Double targetPoint = viewToDrawing(new Point(evt.getX(), evt.getY()));
        Figure aFigure = getDrawing().findFigureExcept(targetPoint, createdFigure);
        if (aFigure != null && ! aFigure.canConnect()) aFigure = null;
        if (targetFigure != aFigure) {
            for (Connector c : connectors) {
                if (invalidArea == null) {
                    invalidArea = c.getDrawingArea();
                } else {
                    invalidArea.add(c.getDrawingArea());
                }
            }
            targetFigure = aFigure;
            if (targetFigure != null) {
                connectors = targetFigure.getConnectors(getPrototype());
                for (Connector c : connectors) {
                    if (invalidArea == null) {
                        invalidArea = c.getDrawingArea();
                    } else {
                        invalidArea.add(c.getDrawingArea());
                    }
                }
            }
        }
        if (invalidArea != null) {
            getView().getComponent().repaint(
                    getView().drawingToView(invalidArea)
                    );
        }
    }
    
    /**
     * Manipulates connections in a context dependent way. If the
     * mouse down hits a figure start a new connection. If the mousedown
     * hits a connection split a segment or join two segments.
     */
    public void mousePressed(MouseEvent evt) {
        super.mousePressed(evt);
        getView().clearSelection();
        
        Point2D.Double startPoint = viewToDrawing(anchor);
        Figure startFigure = getDrawing().findFigure(startPoint);
        startConnector = (startFigure == null) ?
            null :
            startFigure.findConnector(startPoint, prototype);
        
        if (startConnector != null && prototype.canConnect(startConnector)) {
            Point2D.Double anchor = startConnector.getAnchor();
            createdFigure = createFigure();
            createdFigure.setStartPoint(anchor);
            createdFigure.setEndPoint(anchor);
            getDrawing().add(createdFigure);
            Rectangle r = new Rectangle(getView().drawingToView(anchor));
            r.grow(ANCHOR_WIDTH,ANCHOR_WIDTH);
            fireAreaInvalidated(r);
        } else {
            startConnector = null;
            createdFigure = null;
        }
        
        endConnector = null;
    }
    
    /**
     * Adjust the created connection.
     */
    public void mouseDragged(java.awt.event.MouseEvent e) {
        repaintConnectors(e);
        if (createdFigure != null) {
            createdFigure.willChange();
            Point2D.Double endPoint = viewToDrawing(new Point(e.getX(), e.getY()));
            getView().getConstrainer().constrainPoint(endPoint);
            
            Figure endFigure = getDrawing().findFigureExcept(endPoint, createdFigure);
            endConnector = (endFigure == null) ?
                null :
                endFigure.findConnector(endPoint, prototype);
            
            if (endConnector != null && createdFigure.canConnect(startConnector, endConnector)) {
                endPoint = endConnector.getAnchor();
            }
            Rectangle r = new Rectangle(getView().drawingToView(createdFigure.getEndPoint()));
            createdFigure.setEndPoint(endPoint);
             r.add(getView().drawingToView(endPoint));
            r.grow(ANCHOR_WIDTH + 2, ANCHOR_WIDTH + 2);
            getView().getComponent().repaint(r);
            createdFigure.changed();
        }
    }
    
	public void unaryRelationshipHandle() {
		for (Figure f : getDrawing().getFigures()) {
			if (f.equals(createdFigure) || !(f instanceof LineConnectionFigure))
				continue;
			 
			if (!((createdFigure.getStartFigure().equals(((LineConnectionFigure) f).getStartFigure())
					&& createdFigure.getEndFigure().equals(((LineConnectionFigure) f).getEndFigure()))
					|| (createdFigure.getStartFigure().equals(((LineConnectionFigure) f).getEndFigure())
							&& createdFigure.getEndFigure()
									.equals(((LineConnectionFigure) f).getStartFigure()))))
				continue;
			
			Point2D.Double start = createdFigure.getStartPoint();
			Point2D.Double end = createdFigure.getEndPoint();
			Point2D.Double variacao = new Point2D.Double(end.x - start.x, end.y - start.y);

			Point2D.Double pontoCorte1 = new Point2D.Double(start.x + variacao.x / 3, start.y + variacao.y / 3);
			Point2D.Double pontoCorte2 = new Point2D.Double(start.x + 2 * variacao.x / 3, start.y + 2 * variacao.y / 3);
			((LineConnectionFigure) this.createdFigure).splitSegment(pontoCorte1,
					(float) (5f / this.getView().getScaleFactor()));
			((LineConnectionFigure) this.createdFigure).splitSegment(pontoCorte2,
					(float) (5f / this.getView().getScaleFactor()));

			double dir = Math.atan2(pontoCorte1.y - pontoCorte2.y, pontoCorte1.x - pontoCorte2.x);
			double quartoCirculo = Math.PI / 2;
			Point2D.Double n1 = new Point2D.Double(start.x + 70 * Math.cos(dir + quartoCirculo),
					start.y + 70 * Math.sin(dir + quartoCirculo));
			Point2D.Double n2 = new Point2D.Double(end.x + 70 * Math.cos(dir + quartoCirculo),
					end.y + 70 * Math.sin(dir + quartoCirculo));

			BezierPath.Node nodoPonto1 = ((BezierFigure) createdFigure).getNode(pontoCorte1);
			BezierPath.Node nodoPonto2 = ((BezierFigure) createdFigure).getNode(pontoCorte2);
			if (nodoPonto1 != null)
				nodoPonto1.moveTo(n1);

			if (nodoPonto2 != null)
				nodoPonto2.moveTo(n2);
		}

	}
    
    /**
     * Connects the figures if the mouse is released over another
     * figure.
     */
    public void mouseReleased(MouseEvent e) {
        if (createdFigure != null && startConnector != null && endConnector != null) {
            createdFigure.willChange();
            createdFigure.setStartConnector(startConnector);
            createdFigure.setEndConnector(endConnector);
            
            
            /*if (createdFigure.getClass().equals(LineConnectionFigure.class) ||
            	createdFigure.getClass().equals(LabeledLineConnectionUmFigure.class) ||
            	createdFigure.getClass().equals(LabeledLineConnectionMuitosFigure.class) ||
            	createdFigure.getClass().equals(LabeledDoubleLineConnectionUmFigure.class) ||
            	createdFigure.getClass().equals(LabeledDoubleLineConnectionMuitosFigure.class)){
            	Point2D.Double p = ((LineConnectionFigure)createdFigure).getCenter(); 
            	((LineConnectionFigure)this.createdFigure).splitSegment(p, (float) (5f / this.getView().getScaleFactor()));
            }*/
            
            /*if (createdFigure.getClass().equals(GeneralizacaoLineConnectionFigure.class)){
            	((GeneralizacaoLineConnectionFigure)createdFigure).init(this.getView());
            }*/
            
            unaryRelationshipHandle();
            
            
            /*Coloca sempre o label do lado do relacionamento*/
            numberInCorrectSideHandle();
            
            /* Atualiza os letras N, M, O, P, ... dos relacionamentos N:M*/
            if (createdFigure instanceof LabeledLineInterface){
            	updateNumbering();
            }
            
            
            createdFigure.updateConnection();
            createdFigure.changed();
            
            final Figure addedFigure = createdFigure;
            final Drawing addedDrawing = getDrawing();
            
            //TERRA
            //addedFigure.setAttribute(AttributeKeys.START_DECORATION, new ArrowTip(2,4,5));
            
            getDrawing().fireUndoableEditHappened(new AbstractUndoableEdit() {
                public String getPresentationName() {
                    return presentationName;
                }
                public void undo() throws CannotUndoException {
                    super.undo();
                    addedDrawing.remove(addedFigure);
                }
                public void redo() throws CannotRedoException {
                    super.redo();
                    addedDrawing.add(addedFigure);
                }
            });
            targetFigure = null;
            Point2D.Double anchor = startConnector.getAnchor();
            Rectangle r = new Rectangle(getView().drawingToView(anchor));
            r.grow(ANCHOR_WIDTH,ANCHOR_WIDTH);
            fireAreaInvalidated(r);
            anchor = endConnector.getAnchor();
            r = new Rectangle(getView().drawingToView(anchor));
            r.grow(ANCHOR_WIDTH,ANCHOR_WIDTH);
            fireAreaInvalidated(r);
            startConnector = endConnector = null;
            createdFigure = null;
            creationFinished(createdFigure);
        } else {
            fireToolDone();
        }
    }

    /* Atualiza os letras N, M, O, P, ... dos relacionamentos N:M*/
	private void updateNumbering() {
		List<LabeledLineInterface> connectors = new LinkedList<>();
		
		/*Pelo metodo numberInCorrectSideHandle, o relacionamento é sempre o StartConnector*/
		List<Figure> figures = ((QuadTreeDrawing)this.getDrawing()).findFigures(createdFigure.getStartConnector().getBounds());
			if (figures != null && !figures.isEmpty()){
				for (Figure f : figures){
					if (f instanceof LabeledLineInterface){
						connectors.add((LabeledLineInterface)f);
					}
				}
				String s[] = {"N","M","P","Q", "R", "S"};
				int i = 0;
				for (LabeledLineInterface c : connectors){
					((LabeledLineConnectionFigure)c).willChange();
					if (i<s.length){
						((LabeledLineInterface)c).changeText(s[i++]);
					}else{
						((LabeledLineInterface)c).changeText("N");
					}
					((LabeledLineConnectionFigure)c).changed();
				}		
			}		
	}

	/*Coloca sempre o label do lado do relacionamento*/
	private void numberInCorrectSideHandle() {
		if ((createdFigure.getStartConnector().getOwner() instanceof EntidadeFigure ||
		        		createdFigure.getStartConnector().getOwner() instanceof EntidadeFracaFigure ||
		        		createdFigure.getStartConnector().getOwner() instanceof EntidadeRelacionamentoFigure)
				&&
				(createdFigure.getEndConnector().getOwner() instanceof RelacionamentoFigure ||
				createdFigure.getEndConnector().getOwner() instanceof RelacionamentoFracoFigure ||
				createdFigure.getEndConnector().getOwner() instanceof EntidadeRelacionamentoFigure)){
			Connector aux = createdFigure.getStartConnector();
			createdFigure.setStartConnector(createdFigure.getEndConnector());
			createdFigure.setEndConnector(aux);
		}
	}
    public void activate(DrawingEditor editor) {
        super.activate(editor);
        getView().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    public void deactivate(DrawingEditor editor) {
    	if (createdFigure != null) {
            getDrawing().remove(createdFigure);
            createdFigure = null;
        }
        targetFigure = null;
        startConnector = endConnector = null;
        super.deactivate(editor);
    	if (getView() != null) {
			getView().setCursor(Cursor.getDefaultCursor());
		}
    }
    /**
     * Creates the ConnectionFigure. By default the figure prototype is
     * cloned.
     */
    protected ConnectionFigure createFigure() {
        ConnectionFigure f = (ConnectionFigure) prototype.clone();
        getEditor().applyDefaultAttributesTo(f);
        if (prototypeAttributes != null) {
            for (Map.Entry<AttributeKey, Object> entry : prototypeAttributes.entrySet()) {
                f.setAttribute((AttributeKey) entry.getKey(), entry.getValue());
            }
        }
        return f;
    }
    
    public void draw(Graphics2D g) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.transform(getView().getDrawingToViewTransform());
        if (targetFigure != null) {
            for (Connector c : targetFigure.getConnectors(getPrototype())) {
                c.draw(gg);
            }
        }
        if (createdFigure != null) {
            createdFigure.draw(gg);
                Point p = getView().drawingToView(createdFigure.getStartPoint());
                Ellipse2D.Double e = new Ellipse2D.Double(
                        p.x - ANCHOR_WIDTH / 2, p.y - ANCHOR_WIDTH / 2,
                        ANCHOR_WIDTH, ANCHOR_WIDTH
                        );
                g.setColor(Color.GREEN);
                g.fill(e);
                g.setColor(Color.BLACK);
                g.draw(e);
                 p = getView().drawingToView(createdFigure.getEndPoint());
                e = new Ellipse2D.Double(
                        p.x - ANCHOR_WIDTH / 2, p.y - ANCHOR_WIDTH / 2,
                        ANCHOR_WIDTH, ANCHOR_WIDTH
                        );
                g.setColor(Color.GREEN);
                g.fill(e);
                g.setColor(Color.BLACK);
                g.draw(e);
            
        }
        gg.dispose();
    }
    /**
     * This method allows subclasses to do perform additonal user interactions
     * after the new figure has been created.
     * The implementation of this class just invokes fireToolDone.
     */
    protected void creationFinished(Figure createdFigure) {
        fireToolDone();
    }
}
