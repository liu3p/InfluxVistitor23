package gui.components.actions;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;
import gui.components.actionpanels.ManageConnectionsDialog;

public class ManageAction extends BasicAbstractAction{

	private static final long serialVersionUID = 5467740924384849511L;

    public ManageAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Manage", "EditConnection.png", "Manage connections",workAreaReference, treePanelReference);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
    	ManageConnectionsDialog dialog = new ManageConnectionsDialog((JFrame)SwingUtilities.getRoot(treePanelReference), treePanelReference);
    }
}