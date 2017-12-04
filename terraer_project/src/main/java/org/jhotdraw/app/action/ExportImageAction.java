package org.jhotdraw.app.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.jhotdraw.app.Application;
import org.jhotdraw.gui.JSheet;
import org.jhotdraw.gui.event.SheetEvent;
import org.jhotdraw.gui.event.SheetListener;
import org.jhotdraw.io.ExtensionFileFilter;
import org.jhotdraw.samples.draw.DrawProject;
import org.jhotdraw.util.ResourceBundleUtil;

public class ExportImageAction extends AbstractProjectAction {

	public final static String ID = "exportImage";

	private Component oldFocusOwner;

	public ExportImageAction(Application app) {
		super(app);
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
		labels.configureAction(this, ID);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final DrawProject project = (DrawProject)getCurrentProject();
		if (project.isEnabled()) {
			oldFocusOwner = SwingUtilities.getWindowAncestor(project.getComponent()).getFocusOwner();
			project.setEnabled(false);
			
			JFileChooser fileChooser = project.createSaveImageChooser();
			
			if (project.getFile()!=null){
				fileChooser.setSelectedFile(new File(project.getSimpleName()));
			}else{
				ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
				fileChooser.setSelectedFile(new File(labels.getString("unnamedFile")));
			}
			
			JSheet.showSaveSheet(fileChooser, project.getComponent(), new SheetListener() {
				public void optionSelected(final SheetEvent evt) {
					if (evt.getOption() == JFileChooser.APPROVE_OPTION) {
						final File file;
						if (evt.getFileChooser().getFileFilter() instanceof ExtensionFileFilter) {
							file = ((ExtensionFileFilter) evt.getFileChooser().getFileFilter())
									.makeAcceptable(evt.getFileChooser().getSelectedFile());
						} else {
							file = evt.getFileChooser().getSelectedFile();
						}
						try {
							project.writeImage(file);
		                } catch (IOException e) {
		                }
						
					} else {
						project.setEnabled(true);
						if (oldFocusOwner != null) {
							oldFocusOwner.requestFocus();
						}
					}
				}
			});
			project.setEnabled(true);
		}
	}

}
