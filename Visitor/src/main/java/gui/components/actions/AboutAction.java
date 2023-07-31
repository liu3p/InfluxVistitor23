package gui.components.actions;


import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class AboutAction extends BasicAbstractAction{
    
	private static final long serialVersionUID = 5467740924384849511L;
	
    public AboutAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("AboutAction", null, null,workAreaReference, treePanelReference);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
    }
}
