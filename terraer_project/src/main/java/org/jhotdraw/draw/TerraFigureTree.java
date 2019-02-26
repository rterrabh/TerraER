package org.jhotdraw.draw;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jhotdraw.util.ResourceBundleUtil;

public class TerraFigureTree extends JTree {

	private static TerraFigureTree instance = null;
	private DrawingView ActiveView = null;
	private DefaultTreeModel dtmTreeModel = null;
	private DefaultMutableTreeNode nodRoot = null;
	private DefaultMutableTreeNode nodEntidades = null;
	private DefaultMutableTreeNode nodRelacionamentos = null;
	private DefaultMutableTreeNode nodAtributos = null;
	private DefaultMutableTreeNode nodOutros = null;
	
	private boolean DrawSelectionEventHandle = true;
	private boolean TreeSelectionEventHandle = true;

	protected TerraFigureTree() {
		super();

		buildTreeNodes();

		dtmTreeModel = new DefaultTreeModel(nodRoot);

		this.setModel(dtmTreeModel);
		this.setRootVisible(false);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		this.putClientProperty("JTree.lineStyle", "Angled");

		this.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
				treeSelectionChanged();
			}
		});
		
		this.addKeyListener(new java.awt.event.KeyAdapter(){
			public void keyPressed(java.awt.event.KeyEvent e){
				if(e.getKeyCode()==java.awt.event.KeyEvent.VK_DELETE){
					removeFromDrawing();
				}
			}});		
		
	}

	public static TerraFigureTree getInstance() {
		if (instance == null) {
			instance = new TerraFigureTree();
		}
		return instance;
	}

	protected void buildTreeNodes() {
		ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
		
		nodRoot = new DefaultMutableTreeNode("Root");

		nodEntidades = new DefaultMutableTreeNode(labels.getString("node.entities"));
		nodRelacionamentos = new DefaultMutableTreeNode(labels.getString("node.relationships"));
		nodAtributos = new DefaultMutableTreeNode(labels.getString("node.attributes"));
		nodOutros = new DefaultMutableTreeNode(labels.getString("node.others"));

		nodRoot.add(nodEntidades);
		nodRoot.add(nodRelacionamentos);
		nodRoot.add(nodAtributos);
		nodRoot.add(nodOutros);
		
		java.util.Vector<DefaultMutableTreeNode> vctTopNodes=new java.util.Vector<DefaultMutableTreeNode>();
		vctTopNodes.add(nodEntidades);
		vctTopNodes.add(nodRelacionamentos);
		vctTopNodes.add(nodAtributos);
		vctTopNodes.add(nodOutros);
		
		this.setCellRenderer(new TerraTreeCellRenderer(vctTopNodes));
		
	}

	public void setView(DrawingView d) {
		this.ActiveView = d;
		this.ActiveView.addFigureSelectionListener(new FigureSelectionListener() {
					public void selectionChanged(FigureSelectionEvent evt) {
						setSelectedFigure(evt);
					}
				});

	}
	
	public void removeAll(){
	    nodEntidades.removeAllChildren();
	    nodRelacionamentos.removeAllChildren();
	    nodAtributos.removeAllChildren();
	    nodOutros.removeAllChildren();
	    dtmTreeModel.reload(nodRoot);
	}

	public void add(Figure f) {
		DefaultMutableTreeNode Node = chooseApropriateNode(f);
		Node.add(new DefaultMutableTreeNode(f));
		dtmTreeModel.reload(Node);

	}

	public void remove(Figure f) {
		DefaultMutableTreeNode Node = chooseApropriateNode(f);
		java.util.Enumeration<TreeNode> NodeChildren = Node.children();
		
		while(NodeChildren.hasMoreElements()){
			DefaultMutableTreeNode Child= (DefaultMutableTreeNode)NodeChildren.nextElement();
			if(Child.getUserObject()==f){
				Node.remove(Child);
				break;
			}
		}
		dtmTreeModel.reload(Node);
	}
	
	protected DefaultMutableTreeNode find(Figure f){
		DefaultMutableTreeNode Ret = null;
		DefaultMutableTreeNode Node = chooseApropriateNode(f);
		java.util.Enumeration<TreeNode> NodeChildren = Node.children();
		
		while(NodeChildren.hasMoreElements()){
			DefaultMutableTreeNode Child= (DefaultMutableTreeNode) NodeChildren.nextElement();
			if(Child.getUserObject()==f){
				Ret=Child;
				break;
			}
		}
		
		return Ret;		
	}

	protected DefaultMutableTreeNode chooseApropriateNode(Figure f) {
		DefaultMutableTreeNode ChosenNode = null;
		if (f instanceof EntidadeFigure || f instanceof EntidadeFracaFigure) {
			ChosenNode = nodEntidades;
		} else if (f instanceof RelacionamentoFigure
				|| f instanceof RelacionamentoFracoFigure
				|| f instanceof EntidadeRelacionamentoFigure) {
			ChosenNode = nodRelacionamentos;
		} else if (f instanceof AtributoFigure
				|| f instanceof AtributoChaveFigure
				|| f instanceof AtributoChaveParcialFigure
				|| f instanceof AtributoDerivadoFigure
				|| f instanceof AtributoMultivaloradoFigure) {
			ChosenNode = nodAtributos;
		} else {
			ChosenNode = nodOutros;
		}

		return ChosenNode;
	}
	
	protected void setSelectedFigure(FigureSelectionEvent evt){
		if(DrawSelectionEventHandle){
			TreeSelectionEventHandle = false; // disable this object selection event
			// to avoid a vicious circle

			java.util.Set<Figure> figsel = evt.getNewSelection();
			TreePath[] SelectedNodesPath = new TreePath[figsel.size()];
			int i = 0;
			for (Figure f : figsel) {
				DefaultMutableTreeNode Node=find(f);
				SelectedNodesPath[i]=new TreePath( Node.getPath() );
				i++;
			}

			this.setSelectionPaths(SelectedNodesPath);
			TreeSelectionEventHandle = true;
		}
	}
	
	public void treeSelectionChanged() {
		if (TreeSelectionEventHandle) {
			DrawSelectionEventHandle=false;
			
			TreePath[] SelectedNodesPath = this.getSelectionPaths();
			if (SelectedNodesPath != null) {
				ActiveView.clearSelection();

				for (int i = 0; i < SelectedNodesPath.length; i++) {
					DefaultMutableTreeNode Node=(DefaultMutableTreeNode)SelectedNodesPath[i].getLastPathComponent();
					Object O=Node.getUserObject();
					if(O instanceof Figure){
						Figure f=(Figure)O;
						ActiveView.addToSelection(f);
					}
				}
			}
			DrawSelectionEventHandle=true;
		}
	}
	
	public void removeFromDrawing(){
		TreePath[] SelectedNodesPath = this.getSelectionPaths();
		if (SelectedNodesPath != null) {

			for (int i = 0; i < SelectedNodesPath.length; i++) {
				DefaultMutableTreeNode Node=(DefaultMutableTreeNode)SelectedNodesPath[i].getLastPathComponent();
				Object O=Node.getUserObject();
				if(O instanceof Figure){
					Figure f=(Figure)O;
					ActiveView.getDrawing().remove(f);
				}
			}
		}
	}
	
	public void refresh(Figure f){
		TreeSelectionEventHandle = false;
		DrawSelectionEventHandle = false;
		
		TreePath[] SelectedNodesPath=this.getSelectionPaths();
		dtmTreeModel.reload( chooseApropriateNode(f) );
		this.setSelectionPaths(SelectedNodesPath);
		
		TreeSelectionEventHandle = true;
		DrawSelectionEventHandle = true;
	}
	
	
	class TerraTreeCellRenderer extends DefaultTreeCellRenderer {
		protected java.util.Vector<DefaultMutableTreeNode> vctTopNodes;
		
		public TerraTreeCellRenderer(java.util.Vector<DefaultMutableTreeNode> TopNodes){
			this.vctTopNodes=TopNodes;
		}

		public java.awt.Component getTreeCellRendererComponent(JTree tree,  Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
		{
	        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	        
	        ImageIcon Img=findIcon(value);
	        if(Img!=null){
	        	setIcon(Img);
	        }
	        return this;
	    }

	    protected ImageIcon findIcon(Object value) 
		{
			javax.swing.ImageIcon Ret=null;
			DefaultMutableTreeNode Node =(DefaultMutableTreeNode)value;
			
			if(Node!=null && !Node.isRoot()){
				
				if(vctTopNodes.contains(Node)){ //Node is not a Figure Element
					//Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/openfolder.png"));
				}
				else if (Node.getUserObject() instanceof Figure){ //Node is a Figure Element, need to know which one
					Figure f = (Figure)Node.getUserObject();
					if(f instanceof EntidadeFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createEntidade.png"));
					}
					else if(f instanceof EntidadeFracaFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createEntidadeFraca.png"));
					}
					else if(f instanceof RelacionamentoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createRelacionamento.png"));
					}
					else if(f instanceof RelacionamentoFracoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createRelacionamentoFraco.png"));
					}
					else if(f instanceof EntidadeRelacionamentoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createEntidadeRelacionamento.png"));
					}
					else if(f instanceof AtributoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createAtributo.png"));
					}
					else if(f instanceof AtributoChaveFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createAtributoChave.png"));
					}
					else if(f instanceof AtributoChaveParcialFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createAtributoChaveParcial.png"));
					}
					else if(f instanceof AtributoDerivadoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createAtributoDerivado.png"));
					}
					else if(f instanceof AtributoMultivaloradoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createAtributoMultivalorado.png"));
					}
					else if(f instanceof LineConnectionGeneralizacaoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createElbowConnectionSmall.png"));
					}
					else if(f instanceof DoubleLineConnectionGeneralizacaoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createElbowDoubleConnectionSmall.png"));
					}
					else if(f instanceof GeneralizacaoLineConnectionFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createGeneralizacaoConnectionSmall.png"));
					}
					else if(f instanceof LabeledDoubleLineConnectionMuitosFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createElbowDoubleMuitosConnectionSmall.png"));
					}
					else if(f instanceof LabeledDoubleLineConnectionUmFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createElbowDoubleUmConnectionSmall.png"));
					}
					else if(f instanceof LabeledLineConnectionUmFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createElbowUmConnectionSmall.png"));
					}
					else if(f instanceof LabeledLineConnectionMuitosFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createElbowMuitosConnectionSmall.png"));
					}
					else if(f instanceof SobreposicaoFigure){
						ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
						Ret=labels.getImageIcon("createSobreposicaoSmall", SobreposicaoFigure.class);
					}
					else if(f instanceof UniaoFigure){
						ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
						Ret=labels.getImageIcon("createUniaoSmall", UniaoFigure.class);
					}
					else if(f instanceof DisjuncaoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createDisjuncaoSmall.png"));
					}
					else if(f instanceof TextItalicoFigure){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createPapelSmall.png"));
					}
					else if(f instanceof ConnectionAttribute){
						Ret=new ImageIcon(this.getClass().getResource("/org/jhotdraw/draw/action/images/createElbowConnectionAtributoSmall.png"));
					}
					
					
				}
			}
			return Ret;
	    }
	}

}
