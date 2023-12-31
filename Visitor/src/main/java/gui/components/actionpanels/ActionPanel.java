package gui.components.actionpanels;

import gui.common.EnhancedJPanel;
import gui.common.GuiToolkit;

import javax.swing.*;

public abstract class  ActionPanel extends EnhancedJPanel {
	
	private static final long serialVersionUID = 8540045911471224002L;
	protected Icon icon;
	protected String name;
	protected int index;
	
	public ActionPanel( String name, String inconName  )  {
		icon = GuiToolkit.getIcon(inconName);
		this.name = name;
	}
	public Icon getIcon() {
		return icon;
	}
	public String getName() {
		return name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;

	}
	public abstract void runQuery();
}