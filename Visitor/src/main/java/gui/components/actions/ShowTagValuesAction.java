package gui.components.actions;

import java.awt.event.ActionEvent;

import datamodel.InfluxDBMeasuramet;
import datamodel.InfluxDBTreeElement;
import gui.components.InfluxDBTreePanel;
import gui.components.InfluxDBWorkArea;
import gui.components.actionpanels.ShowTagValueActionPanel;

public class ShowTagValuesAction extends BasicAbstractAction {
	private static final long serialVersionUID = 5467740924384849511L;
	public ShowTagValuesAction(InfluxDBWorkArea workAreaReference, InfluxDBTreePanel treePanelReference) {
		super("Show Tag Values", "TagValues.png", "Show Tag Values",workAreaReference, treePanelReference);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		InfluxDBTreeElement treeElement = treePanelReference.getSelectedTreeElement();
		String measuramentName = "measurament";
		String database = "";
		if (treeElement != null) {
			if (treeElement.getClass().equals(InfluxDBMeasuramet.class)) {
				measuramentName = treeElement.toString();
				database = ((InfluxDBMeasuramet) treeElement).getDatabase();
			}
			workAreaReference
					.addWorkPanel(new ShowTagValueActionPanel(treeElement.getConnection(), measuramentName, database));
		}
	}
}