package gui.components.actions;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;
import gui.components.actionpanels.CreateDatabaseDialog;

public class CreateDataBaseAction extends  BasicAbstractAction{
    
	private static final long serialVersionUID = 5467740924384849511L;

    public CreateDataBaseAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Create DataBase", "CreateDataBase.png", "Create DataBase", workAreaReference, treePanelReference);
    }
   
    @Override
    public void actionPerformed(ActionEvent event) {
    	CreateDatabaseDialog dialog = new CreateDatabaseDialog((JFrame) SwingUtilities.getRoot(treePanelReference),treePanelReference );
    }
}

