package gui.components.actions;


import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;


public class DiagnosticsAction extends BasicAbstractAction{
    
	private static final long serialVersionUID = 5467740924384849511L;
    public DiagnosticsAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Show Users", "Diagnostics.png", "Show Users",workAreaReference, treePanelReference);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
    	
    }
}
