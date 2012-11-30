/*
 * @(#)SaveAsAction.java  1.0  28. September 2005
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

package org.jhotdraw.application.action;

/**
 * SaveAsAction.
 *
 * @author  Werner Randelshofer
 * @version 1.0 28. September 2005 Created.
 */
public class SaveAsAction extends SaveAction {
    public final static String ID = "File.saveAs";

    /** Creates a new instance. */
    public SaveAsAction() {
        super(true);
        initActionProperties(ID);
    }
}