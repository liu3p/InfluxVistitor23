package gui.components.actionpanels;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import datamodel.InfluxDBConnection;
import facility.InfluxDBFacility;
import gui.common.ButtonPanel;
import gui.common.EnhancedJPanel;
import gui.common.GuiToolkit;
import info.clearthought.layout.TableLayout;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewQueryActionPanel extends ActionPanel implements ActionListener {

	private static final long serialVersionUID = -624272778516469677L;
	private InfluxDBFacility influxDBFacility;
	private JSplitPane mainPanel;
	private JTextArea textArea;
	private JLabel messagelabel;
	private ButtonPanel buttonPanel;
	private JButton runQueryButton;
	private JButton clearButton;
	private JTabbedPane bottomPanel;
	private long responseTime;

	private InfluxDBConnection connection;
	private String databaseName;
	private String measurement;

	public NewQueryActionPanel(InfluxDBConnection connection, String mesurementName, String databaseName) {

		super(connection.getConnectionName() + "." + mesurementName, "NewQuery.png");

		this.connection = connection;
		this.databaseName = databaseName;
		measurement = mesurementName;

		influxDBFacility = InfluxDBFacility.getFacilityReference();
		setBorder(new EtchedBorder());
		setLayout(new BorderLayout());
		initComponets();
		layuotComponets();
		add(mainPanel);
	}

	private void initComponets() {

		mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPanel.setDividerSize(5);
		String queryString = "from(bucket: \""+ databaseName +"\")\n" +
				"  |> range(start: -3d)\n" +
				"  |> filter(fn: (r) => r[\"attroid\"] == \""+ measurement + "\" )  " +
				"  |> sort(columns: [\"_time\"])\n" +
				"  |> limit(n:10)";
		System.out.printf("query string : %s\n",queryString);
		textArea = new JTextArea(queryString);

		textArea.setPreferredSize(new Dimension(600, 600));
		messagelabel = new JLabel();
		buttonPanel = new ButtonPanel(30, 5, 2, 5);
		clearButton = new JButton("Clear results");
		clearButton.setIcon(GuiToolkit.getIcon("DropMeasurement.png"));
		clearButton.addActionListener(this);
		runQueryButton = new JButton("Run Query");
		runQueryButton.setIcon(GuiToolkit.getIcon("RunQuery.png"));
		runQueryButton.addActionListener(this);
		buttonPanel.addButton(runQueryButton);
		buttonPanel.addButton(clearButton);
		bottomPanel = new JTabbedPane();
	}

	private void layuotComponets() {
		setLayout(new BorderLayout());
		layuotTopPanel();
		layuotBottomPanel();
	}

	private void layuotTopPanel() {
		EnhancedJPanel topPanel = new EnhancedJPanel();
		double size[][] = { { TableLayout.FILL }, { TableLayout.FILL, 30, 30 } };
		topPanel.setLayout(new TableLayout(size));
		JScrollPane scrollPane = createNewScrollPaneWithEditor(textArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		topPanel.add(scrollPane, "0,0");
		topPanel.add(buttonPanel, "0,1");
		topPanel.add(messagelabel, "0,2");
		topPanel.setPreferredSize(new Dimension(0, 150));
		mainPanel.setTopComponent(topPanel);
	}

	private JScrollPane createNewScrollPaneWithEditor(JTextArea textArea) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getViewport().add(textArea);
		scrollPane.getViewport().setPreferredSize(textArea.getPreferredSize());
		return scrollPane;
	}

	private void layuotBottomPanel() {
		bottomPanel.setTabPlacement(JTabbedPane.TOP);
		bottomPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		mainPanel.setBottomComponent(bottomPanel);
	}

	private void putResulQueryTabedPanels(List<FluxTable> queryResult) {
		for (FluxTable result : queryResult) {
			putResulqueryTab(result);
		}
	}

	private void putResulqueryTab(FluxTable result) {
		if (result != null) {
			int totalNumOfRows = 0;
			QueryResultTable queryResultTable = new QueryResultTable(result);
			bottomPanel.addTab("Result " , new JScrollPane(queryResultTable));
			totalNumOfRows = totalNumOfRows + queryResultTable.getNumOfRows();
			messagelabel.setText("   Results " + totalNumOfRows + ", response time : " + responseTime + " ms");
		}
	}

	public List<FluxTable> executeQuery() {
		String query = textArea.getText();
		List<FluxTable> queryResult = influxDBFacility.query(query,databaseName,connection);
		return queryResult;
	}

	@Override
	public void runQuery(){
		long timeInit = System.currentTimeMillis();
		List<FluxTable> queryResult = executeQuery();
		long timeEnd = System.currentTimeMillis();
		responseTime = (timeEnd - timeInit);
		bottomPanel.removeAll();

		if(queryResult.size() > 20){
			JOptionPane.showMessageDialog(this, "Too many data tables, only 20 will be shown! ", "Warnning",JOptionPane.WARNING_MESSAGE);
			for(int i=20 ; i < queryResult.size(); i++)
				queryResult.remove(i);
		}

		putResulQueryTabedPanels(queryResult);
		if(queryResult==null){
			JOptionPane.showMessageDialog(this, "No record ", "Failure",JOptionPane.ERROR_MESSAGE);
		}
		else if(queryResult.size() < 1){
			JOptionPane.showMessageDialog(this, "No record ", "Failure",JOptionPane.ERROR_MESSAGE);
		}

	}
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(runQueryButton)) {
			runQuery();
		}
		if (actionEvent.getSource().equals(clearButton)) {
			bottomPanel.removeAll();
			messagelabel.setText("");
		}		
	}
}