/*
 * @(#)TextFigure.java  1.0.2  2007-05-02
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

/**
 * A text figure.
 * <p>
 * A DrawingEditor should provide the TextTool to create a TextFigure.
 *
 * @see TextTool
 *
 * @author Werner Randelshofer
 * @version 2.0.2 2007-05-02 Made all instance variables protected instead of
 * private. 
 * <br>2.0.1 2006-02-27 Draw UNDERLINE_LOW_ONE_PIXEL instead of UNDERLINE_ON.
 * <br>2.0 2006-01-14 Changed to support double precison coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class TextNegritoFigure extends TextFigure{

	public TextNegritoFigure() {
		super();
		this.setEditable(false);
		// TODO Auto-generated constructor stub
	}

	public TextNegritoFigure(String text) {
		super(text);
		this.setEditable(false);
		// TODO Auto-generated constructor stub
	}

}
