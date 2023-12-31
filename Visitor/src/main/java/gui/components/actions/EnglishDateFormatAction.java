package gui.components.actions;


import java.awt.event.ActionEvent;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class EnglishDateFormatAction extends BasicAbstractAction{
    

	private static final long serialVersionUID = 5467740924384849511L;
	
    public EnglishDateFormatAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("12/31/2018 mm/dd/yyyy", null, null,workAreaReference, treePanelReference);
    }
    @Override
    public void actionPerformed(ActionEvent event) {
    }
}