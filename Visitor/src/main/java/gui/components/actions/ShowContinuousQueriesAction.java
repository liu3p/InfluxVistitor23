package gui.components.actions;

import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class ShowContinuousQueriesAction extends BasicAbstractAction{
    

	private static final long serialVersionUID = 5467740924384849511L;
    public ShowContinuousQueriesAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Show Continuous Queries", "ContinuousQuery.png", "Show Continuous Queries", workAreaReference, treePanelReference);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
    }
}