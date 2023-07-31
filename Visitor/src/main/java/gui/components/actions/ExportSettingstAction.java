package gui.components.actions;


import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class ExportSettingstAction extends BasicAbstractAction{
    

	private static final long serialVersionUID = 5467740924384849511L;
	
    public ExportSettingstAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	
    	super("Settings", null, null,workAreaReference, treePanelReference);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
    }
}