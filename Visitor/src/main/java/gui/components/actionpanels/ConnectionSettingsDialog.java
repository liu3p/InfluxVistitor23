package gui.components.actionpanels;

import com.influxdb.client.InfluxDBClient;
import com.jcraft.jsch.ChannelSftp;
import datamodel.InfluxDBConnection;
import facility.InfluxDBFacility;
import gui.common.EnhancedJPanel;
import gui.common.IntegerField;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;



public class ConnectionSettingsDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -7900896869547070728L;
	private JButton cancelButton;
	private JButton pingButton;
	private JButton testButton;
	private JButton saveButton;
	private JButton getFileButton;

	private JTextField nameField;
	private JTextField addresField;
	private JTextField databaseField;
	private JTextField userNameField;
	private JTextField tokenField;
	private JTextField orgField;
	private JPasswordField passwordField;
	private IntegerField portField;

	private JCheckBox sllCheckBox;
	private ManageConnectionsDialog parentWindow;
	private String connectionID;
	

	public ConnectionSettingsDialog(ManageConnectionsDialog parentWindow, InfluxDBConnection connection) {
		super(parentWindow, "Connection Settings");
		this.parentWindow = parentWindow;
		setModal(true);
		initComponents();
		EnhancedJPanel mainPanel = new EnhancedJPanel();
		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(layoutTopPanel(),BorderLayout.NORTH);
		mainPanel.add(layoutMidPanel(),BorderLayout.CENTER);
		mainPanel.add(layoutBottomPanel(),BorderLayout.SOUTH);
		double border = 10;
		double size [][] = {
        		{border,TableLayout.FILL,border},
                {border,TableLayout.FILL,border}
        };
		
		setLayout(new TableLayout(size));
		add(mainPanel,"1,1");
		if(connection != null) {
			connectionID = connection.getID();
			loadConnectionValues(connection);
			
		}
		else {
			connectionID = null;
		}
		pack();
		setResizable(false);
    	setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
    	setVisible(true);
	}
	private void loadConnectionValues(InfluxDBConnection connection) {
		nameField.setText(connection.getConnectionName());
		addresField.setText(connection.getIpAddresss());
		databaseField.setText(connection.getDatabase());
		userNameField.setText(connection.getUserName());
		passwordField.setText(connection.getPassword());
		userNameField.setText(connection.getUserName());
		tokenField.setText(connection.getToken());
		orgField.setText(connection.getOrg());
		portField.setText(Long.toString(connection.getPort()));
	}

	private EnhancedJPanel layoutBottomPanel() {
		EnhancedJPanel bottomPenel = new EnhancedJPanel();
		double border = 5;
		double size [][] = {
        		{border,0.2,border,0.2,border,0.2, border,0.2, border,0.2,border},
                {border,20,border}
        };
		
		bottomPenel.setLayout(new TableLayout(size));
		bottomPenel.add(getFileButton,   "1,1");
		bottomPenel.add(testButton,   "3,1");
		bottomPenel.add(pingButton,   "5,1");
		bottomPenel.add(saveButton,   "7,1");
		bottomPenel.add(cancelButton, "9,1");
		
		// TODO Auto-generated method stub
		return bottomPenel;
	}

	private EnhancedJPanel layoutMidPanel() {
		
		EnhancedJPanel midPanel = new EnhancedJPanel();
		double border = 5;
		double size [][] = {
        		{border,80,border,TableLayout.FILL,border},
                {border,20,border,20,border,20,border}
        };
		
		midPanel.setLayout(new TableLayout(size));
		midPanel.add(new JLabel("User Name: "),   "1,1");
		midPanel.add(userNameField, "3,1");
		midPanel.add(new JLabel("Password: "),   "1,3");
		midPanel.add(passwordField, "3,3");
		midPanel.add(new JLabel("Security: "),   "1,5");
		midPanel.add(sllCheckBox, "3,5");
		
		return midPanel;
	}

	private EnhancedJPanel layoutTopPanel() {
		EnhancedJPanel topPanel = new EnhancedJPanel();

        double border = 5;
        double label = 60;

        double size [][] = {
        		{border,label,border, TableLayout.FILL, border, 0.3, border},
                {border,20, 20,border,20, 20, border,20,20, border,20,20,border,20,20,20}
        };
        
        topPanel.setLayout(new TableLayout(size));
        
        topPanel.add(new JLabel("Name: "), "1,1");
        topPanel.add(nameField, "3,1,5,1");
        
        JLabel messageLabel = new JLabel("Choose a connection name to identify the connection");
        messageLabel.setForeground(Color.GRAY);
        topPanel.add(messageLabel,"3,2,5,2");
		
        topPanel.add(new JLabel("Address: "), "1,4");
        topPanel.add(addresField, "3,4");
        topPanel.add(portField, "5,4");
        
        JLabel messageAddresLabel =new JLabel("Specify host and port of InfluxDB");
        messageAddresLabel.setForeground(Color.GRAY);
        topPanel.add(messageAddresLabel, "3,5,5,5");
        
        topPanel.add(new JLabel("Database: "), "1,7");
        topPanel.add(databaseField, "3,7,5,7");
        
        JLabel optionalMessageLabel =new JLabel("Optionally specify a database ");
        optionalMessageLabel.setForeground(Color.GRAY);
        topPanel.add(optionalMessageLabel, "3,8,5,8");


		topPanel.add(new JLabel("Token: "), "1,10");
		topPanel.add(tokenField, "3,10,5,10");

		JLabel tokenMessageLabel =new JLabel("Optional write token ");
		tokenMessageLabel.setForeground(Color.GRAY);
		topPanel.add(tokenMessageLabel, "3,11,5,11");


		topPanel.add(new JLabel("Org: "), "1,13");
		topPanel.add(orgField, "3,13,5,13");

		JLabel orgMessageLabel =new JLabel("Organization ");
		tokenMessageLabel.setForeground(Color.GRAY);
		topPanel.add(tokenMessageLabel, "3,14,5,14");

		return topPanel;
	}

	private void initComponents() {
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		pingButton = new JButton("Ping");
		pingButton.addActionListener(this);
		
		testButton = new JButton("Test");
		testButton.addActionListener(this);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);

		getFileButton = new JButton("ID File");
		getFileButton.addActionListener(this);

		nameField 		= new JTextField();
		addresField 	= new JTextField();
		databaseField 	= new JTextField();
		userNameField 	= new JTextField();
		passwordField 	= new JPasswordField();
		portField 		= new IntegerField();
		tokenField 		= new JTextField();
		orgField   		= new JTextField();

		sllCheckBox = new JCheckBox("Use SSL");
		// NOT IMPLEMENTED YET
		sllCheckBox.setEnabled(false);
	}
	private InfluxDBConnection collectData(){
		if (connectionID == null) {
			connectionID = Long.toString((System.currentTimeMillis()));
		}
		InfluxDBConnection connection = new InfluxDBConnection(connectionID);
		connection.setUserName(userNameField.getText());
		connection.setDatabase(databaseField.getText());
		connection.setIpAddresss(addresField.getText());
		connection.setPort(Integer.parseInt(portField.getText()));
		connection.setPassword(new String(passwordField.getPassword()));
		connection.setConnectionName(nameField.getText());
		connection.setConnectionName(nameField.getText());
		connection.setUseSLL(sllCheckBox.isSelected());
		connection.setToken(tokenField.getText());
		connection.setOrg(orgField.getText());
		return connection;

	}

	@Override
	public void actionPerformed(ActionEvent event){
		if (event.getSource().equals(cancelButton)){
			dispose();
			
		}
		if (event.getSource().equals(pingButton)){
			InfluxDBConnection connection = collectData();
			InfluxDBFacility facility = InfluxDBFacility.getFacilityReference();
		}
		if (event.getSource().equals(testButton)){
			InfluxDBConnection 	connection 	= collectData();
			InfluxDBFacility 	facility 	= InfluxDBFacility.getFacilityReference();
			InfluxDBClient 		client 		= facility.getInfluxDB(connection);
			if(client != null)
				JOptionPane.showMessageDialog(this, "Connection sucessfull","Success", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "Connection failed,please check parameter","Failed", JOptionPane.INFORMATION_MESSAGE);
		}
		if (event.getSource().equals(saveButton)){
			InfluxDBConnection connection = collectData();
			parentWindow.addConnection(connection);
			dispose();
		}
		if (event.getSource().equals(getFileButton)){
			boolean ret = getFile();
			if(ret)
				JOptionPane.showMessageDialog(this, "Get file success!","Success", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "Get file failed!","Failed", JOptionPane.INFORMATION_MESSAGE);
		}
	}


	public boolean getFile(){

		SftpUtil sftpUtil = new SftpUtil();
		sftpUtil.setServerPort(addresField.getText(),Integer.valueOf(portField.getText()));


		sftpUtil.downloadFile();


		return true;
	}






}
