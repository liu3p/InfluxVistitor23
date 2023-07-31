package gui.components.actions;

import java.awt.event.ActionEvent;

import datamodel.InfluxDBTreeElement;
import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;
import gui.components.actionpanels.RetentionPolicyPanel;

public class ShowRetentionPoliciesAction extends BasicAbstractAction{
	private static final long serialVersionUID = 5467740924384849511L;
    public ShowRetentionPoliciesAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Show Retention Policies", "RetentionPolicy.png", "Show Retention Policies",workAreaReference, treePanelReference);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
    	InfluxDBTreeElement treeElement = treePanelReference.getSelectedTreeElement();
    	if ( treeElement !=null) {
    		workAreaReference.addWorkPanel(new RetentionPolicyPanel(treePanelReference,treeElement.getConnection()));
    	}
    	
    }
}