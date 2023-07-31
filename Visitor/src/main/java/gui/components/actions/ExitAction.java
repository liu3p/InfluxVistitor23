package gui.components.actions;

import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class ExitAction extends BasicAbstractAction{
    

	private static final long serialVersionUID = 5467740924384849511L;
	
    public ExitAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Exit", null, null, workAreaReference, treePanelReference);
    }
    @Override
    public void actionPerformed(ActionEvent event) {
    	System.exit(0);
    }
}