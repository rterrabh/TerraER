package org.jhotdraw.draw.notation.finalversion;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.action.AbstractApplicationAction;
import org.jhotdraw.util.ResourceBundleUtil;

public class NotationSelectAction extends AbstractApplicationAction {
    public final static String ID = "Notation Select";
    public static String current = SelectChenAction.ID; 
    
    private static JCheckBoxMenuItem chenSelection;
    private static JCheckBoxMenuItem crossSelection;
    private static JCheckBoxMenuItem idef1xSelection;
    
    private static ArrayList<IChangeNotationListern> listerns = new ArrayList<>(); 
    
    public static SelectChenAction chenAction;
    public static SelectCrossFootAction crossFootAction;
    public static SelectIDEF1XAction idef1xAction;
    
    public NotationSelectAction(Application app) {
        super(app);
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, ID);
        chenAction = new SelectChenAction(app);
        crossFootAction = new SelectCrossFootAction(app);
        idef1xAction = new SelectIDEF1XAction(app);
    }
    
    public class SelectChenAction extends AbstractApplicationAction {
    	public final static String ID = "Notation Chen";
    	public SelectChenAction(Application app) {
            super(app);
            ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
            labels.configureAction(this, ID);
        }
    	public void actionPerformed(ActionEvent evt) {
    		current = SelectChenAction.ID;
    		NotationSelectAction.update();
        }
    }
    
    public class SelectCrossFootAction extends AbstractApplicationAction {
    	public final static String ID = "Notation CrossFoot";
    	public SelectCrossFootAction(Application app) {
            super(app);
            ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
            labels.configureAction(this, ID);
        }
    	public void actionPerformed(ActionEvent evt) {
    		current = SelectCrossFootAction.ID;
    		NotationSelectAction.update();
        }
    }
    
    public class SelectIDEF1XAction extends AbstractApplicationAction {
    	public final static String ID = "Notation IDEF1X";
    	public SelectIDEF1XAction(Application app) {
            super(app);
            ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
            labels.configureAction(this, ID);
        }
    	public void actionPerformed(ActionEvent evt) {
    		current = SelectIDEF1XAction.ID;
    		NotationSelectAction.update();
        }
    }
    
    private static void update(){
    	chenSelection.setSelected(false);
    	crossSelection.setSelected(false);
    	idef1xSelection.setSelected(false);
    	if (current.equals(SelectChenAction.ID)){
    		chenSelection.setSelected(true);
    	} else if (current.equals(SelectCrossFootAction.ID)){
    		crossSelection.setSelected(true);
    	} else if (current.equals(SelectIDEF1XAction.ID)){
    		idef1xSelection.setSelected(true);
    	}    	
    	for (IChangeNotationListern l : listerns){
    		l.notifyChange(current);
    	}
    }
    
    public static void addListern(IChangeNotationListern l){
    	listerns.add(l);
    }
    
    public static void setSelection(JCheckBoxMenuItem chen, JCheckBoxMenuItem crossFoot, JCheckBoxMenuItem idef1x){
    	chenSelection = chen;
    	crossSelection = crossFoot;
    	idef1xSelection = idef1x;
    	chenSelection.setSelected(true);
    	crossSelection.setSelected(false);
    	idef1xSelection.setSelected(false);    	
    }
    
    public void actionPerformed(ActionEvent evt) {
    	
    }
}
