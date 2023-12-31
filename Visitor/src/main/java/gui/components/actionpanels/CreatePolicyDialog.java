package gui.components.actionpanels;

import datamodel.InfluxDBConnection;
import datamodel.InfluxDBRetentionPolicy;
import gui.common.EnhancedDialog;
import gui.common.IntegerField;
import info.clearthought.layout.TableLayout;

import javax.swing.*;

public class CreatePolicyDialog extends EnhancedDialog {

	private static final long serialVersionUID = 8828979599801843270L;
	private JTextField policyNameField;
	private JTextField durationField;
	private IntegerField replicationField;
	private JCheckBox defaultCheckBox;

	private InfluxDBConnection connection;
	private RetentionPolicyPanel retentionPanelReference;

	public CreatePolicyDialog(JFrame jFrame, InfluxDBConnection connection,
			RetentionPolicyPanel retentionPanelReference) {
		super(jFrame, "Create Retention Policy", true, "Create", "Cancel");

		this.connection = connection;
		this.retentionPanelReference = retentionPanelReference;

		policyNameField = new JTextField();
		durationField = new JTextField();
		replicationField = new IntegerField();
		defaultCheckBox = new JCheckBox();

		double border = 5;
		double size[][] = { { border, 120, border, 200, border },
				{ border, 30, border, 30, border, 30, border, 30, border, 30, border } };

		setLayoutToInternalPanel(new TableLayout(size));
		addComponentToInternalPanel(new JLabel("Policy Name: "), 	"1,1");
		addComponentToInternalPanel(policyNameField, 				"3,1");

		addComponentToInternalPanel(new JLabel("Duration: "),		"1,3");
		addComponentToInternalPanel(durationField, 		"3,3");

		addComponentToInternalPanel(new JLabel("Replication: "), 	"1,5");
		addComponentToInternalPanel(replicationField, 	"3,5");

		addComponentToInternalPanel(new JLabel("Default: "), 		"1,7");
		addComponentToInternalPanel(defaultCheckBox, 				"3,7");

		pack();
		setResizable(false);
		setLocationRelativeTo(jFrame);
		setVisible(true);
	}

	private InfluxDBRetentionPolicy recollectData() {

		InfluxDBRetentionPolicy influxDBRetentionPolicy = new InfluxDBRetentionPolicy(connection);
		if ((policyNameField.getText() == "") || (durationField.getText() == null)
				|| (replicationField.getText() == "")) {
			return null;
		} else {
			try {

				influxDBRetentionPolicy.setDefault(defaultCheckBox.isSelected());
				influxDBRetentionPolicy.setDuration(durationField.getText());
				influxDBRetentionPolicy.setPolicyName(policyNameField.getText());
				influxDBRetentionPolicy.setReplication(Integer.parseInt(replicationField.getText()));

				return influxDBRetentionPolicy;

			} catch (NumberFormatException numberFormatException) {
				return null;
			}
		}
	}

	@Override
	protected void okButtonPressedAction() {
		retentionPanelReference.addRetentionPolicy(recollectData());
		dispose();
	}

	@Override
	protected void cancelButtonPressedAction() {
		dispose();
	}
}
