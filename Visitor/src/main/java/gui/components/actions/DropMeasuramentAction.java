package gui.components.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;



import datamodel.InfluxDBMeasuramet;
import datamodel.InfluxDBTreeElement;
import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;

public class DropMeasuramentAction extends BasicAbstractAction{
    
	private static final long serialVersionUID = 5467740924384849511L;
    public DropMeasuramentAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference){
    	super("Drop Measurament", "DropMeasurement.png", "Drop Measurament", workAreaReference, treePanelReference);
    }
	@Override
	public void actionPerformed(ActionEvent event) {
		InfluxDBTreeElement selectedTreeElement = treePanelReference.getSelectedTreeElement();
		if (selectedTreeElement != null) {
			if (selectedTreeElement.getClass().equals(InfluxDBMeasuramet.class)) {
				int response = JOptionPane.showConfirmDialog(SwingUtilities.getRoot(treePanelReference),
						"Do you want drop measurament?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
					return;
					
				} else if (response == JOptionPane.YES_OPTION){
					/*String measuramentName = ((InfluxDBMeasuramet) selectedTreeElement).toString();
					String databaseName = ((InfluxDBMeasuramet) selectedTreeElement).getDatabase();
					InfluxDBConnection connection = selectedTreeElement.getConnection();
					Query query = new Query("DROP MEASUREMENT \""+measuramentName+"\"", databaseName);
					InfluxDBFacility.getFacilityReference().getInfluxDB(connection).query(query);
					treePanelReference.removeNode(selectedTreeElement);*/

				} else if (response == JOptionPane.CLOSED_OPTION) {
					return;
				}
			}
		}
	}

}