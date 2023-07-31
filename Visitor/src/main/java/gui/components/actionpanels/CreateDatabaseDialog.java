package gui.components.actionpanels;

import datamodel.InfluxDBConnection;
import datamodel.InfluxDBTreeElement;
import facility.InfluxDBFacility;
import gui.common.EnhancedDialog;
import gui.components.InfluxDBTreePanel;

import javax.swing.*;
import java.awt.*;

public class CreateDatabaseDialog extends EnhancedDialog{
	
	private JTextField dataBaseField;
	private static final long serialVersionUID = 1L;
	private InfluxDBTreePanel treePanelReference;
	
	public CreateDatabaseDialog(JFrame parent, InfluxDBTreePanel treePanelReference) {
		
		super(parent,"Create Database", true, "Create", "Cancel");
		this.treePanelReference = treePanelReference;
		
		dataBaseField = new JTextField ();
		setLayoutToInternalPanel(new BorderLayout());
		addComponentToInternalPanel(new JLabel ("RaspberryPi"), BorderLayout.NORTH);
		addComponentToInternalPanel(dataBaseField, BorderLayout.SOUTH);
		pack();
		setResizable(false);
    	setLocationRelativeTo(parent);
    	setVisible(true);
    	
	}
	@Override
	protected void okButtonPressedAction(){
    	String databaseName = dataBaseField.getText();
    	if (databaseName != null) {
        	InfluxDBTreeElement selectedTreeElement = treePanelReference.getSelectedTreeElement();
        	if (selectedTreeElement != null) {
            	InfluxDBConnection connection = selectedTreeElement.getConnection();
            	//InfluxDBFacility.getFacilityReference().getInfluxDB(connection).createDatabase(databaseName);
            	treePanelReference.addDatabase(connection, databaseName);
        	}
    	}
		dispose();
	}

	@Override
	protected void cancelButtonPressedAction() {
		dispose();
	}
}
