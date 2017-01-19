package org.jhotdraw.app.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class CopyButtonHandler implements ActionListener {
	private JButton cpy;
	private JTextArea textarea;
	
	public CopyButtonHandler(JButton cpy, JTextArea textarea){
		this.cpy = cpy;
		this.textarea = textarea;
	}

	public void actionPerformed(ActionEvent evento) {
		StringSelection stringSelection = new StringSelection(textarea.getText());
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		if(evento.getSource() == cpy){
			clpbrd.setContents(stringSelection, null);
			JOptionPane.showMessageDialog(null, "Copied to Clipboard");
		}
	}

}

