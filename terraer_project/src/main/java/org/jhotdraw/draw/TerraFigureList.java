package org.jhotdraw.draw;

public class TerraFigureList extends javax.swing.JList {

	private static TerraFigureList instance = null;

	private javax.swing.DefaultListModel FigureListModel = null;
	private DrawingView ActiveView = null;

	private boolean ListSelectionEventHandle = true;
	private boolean DrawSelectionEventHandle = true;

	protected TerraFigureList() {
		super();
		FigureListModel = new javax.swing.DefaultListModel();
		this.setModel(FigureListModel);

		this.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent e) {
						selectionChanged();
					}
				});
		this.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyPressed(java.awt.event.KeyEvent e){
				if(e.getKeyCode()==java.awt.event.KeyEvent.VK_DELETE){
					removeFromDrawing();
				}
			}
			
		});
	}

	public static TerraFigureList getInstance() {
		if (instance == null) {
			instance = new TerraFigureList();
		}
		return instance;
	}

	public void setView(DrawingView d) {
		this.ActiveView = d;
		this.ActiveView.addFigureSelectionListener(new FigureSelectionListener() {
					public void selectionChanged(FigureSelectionEvent evt) {
						setSelectedFigure(evt);
					}
				});

	}

	public void add(Figure f) {
		FigureListModel.addElement(f);
		this.setVisible(true);
	}

	public void add(int index, Figure f) {
		FigureListModel.add(index, f);
		this.setVisible(true);
	}

	public void selectionChanged() {
		if (ListSelectionEventHandle) {
			DrawSelectionEventHandle=false;
			
			int[] Indexes = this.getSelectedIndices();
			if (Indexes != null) {
				ActiveView.clearSelection();

				for (int i = 0; i < Indexes.length; i++) {
					Figure f = (Figure) FigureListModel
							.getElementAt(Indexes[i]);
					ActiveView.addToSelection(f);
				}
			}
			DrawSelectionEventHandle=true;
		}
	}

	public void remove(Figure f) {
		int Index = FigureListModel.indexOf(f);
		if (Index >= 0) {
			FigureListModel.removeElement(f);
			this.setVisible(true);
		}
	}
	
	protected void removeFromDrawing(){
		int[] Indexes = this.getSelectedIndices();
		if (Indexes != null) {
			
			java.util.Vector<Figure> vct=new java.util.Vector<Figure>();
			for(int i=0; i<Indexes.length; i++) {
				vct.add( (Figure)FigureListModel.elementAt(Indexes[i]) );
			}
			
			for(Figure f : vct){
				FigureListModel.removeElement(f);
				ActiveView.getDrawing().remove(f);
			}
			this.setVisible(true);
		}
	}

	protected void setSelectedFigure(FigureSelectionEvent evt) {
		if(DrawSelectionEventHandle){
			ListSelectionEventHandle = false; // disable this object selection event
			// to avoid a vicious circle

			java.util.Set<Figure> figsel = evt.getNewSelection();
			int[] Indexes = new int[figsel.size()];
			int i = 0;
			for (Figure f : figsel) {
				Indexes[i] = FigureListModel.indexOf(f);
				i++;
			}

			this.setSelectedIndices(Indexes);

			ListSelectionEventHandle = true;
		}
	}

}
