package gui.components.actions;

import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class RunQueryAction extends BasicAbstractAction{
   
	private static final long serialVersionUID = 5467740924384849511L;

	
    public RunQueryAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference ){
    	super("Run Query", "RunQuery.png", "Run Query", workAreaReference, treePanelReference);

    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
    	workAreaReference.runQuery();
    	
    }
}