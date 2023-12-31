package gui.components.actions;

import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class RefreshAction extends BasicAbstractAction{
    

	private static final long serialVersionUID = 5467740924384849511L;
    public RefreshAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Refresh", "Refresh.png", "Refresh", workAreaReference, treePanelReference);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
    	
    	treePanelReference.refresh();
    }
}