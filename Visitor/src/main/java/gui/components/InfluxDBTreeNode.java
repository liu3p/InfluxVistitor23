package gui.components;

import javax.swing.tree.DefaultMutableTreeNode;

import datamodel.InfluxDBTreeElement;

import java.awt.*;

public class InfluxDBTreeNode extends DefaultMutableTreeNode {
	
	private static final long serialVersionUID = 21785764894214894L;

	public InfluxDBTreeNode(InfluxDBTreeElement treeElement) {
		super(treeElement);
		
	}
}
