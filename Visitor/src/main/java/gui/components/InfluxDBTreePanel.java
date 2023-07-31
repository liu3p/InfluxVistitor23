package gui.components;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.Bucket;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;


import datamodel.InfluxDBConnection;
import datamodel.InfluxDBDataBase;
import datamodel.InfluxDBMeasuramet;
import datamodel.InfluxDBTreeElement;
import facility.InfluxDBFacility;
import gui.common.EnhancedJPanel;
import gui.components.actionpanels.NewQueryActionPanel;
import gui.components.actions.*;
import info.clearthought.layout.TableLayout;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class InfluxDBTreePanel extends EnhancedJPanel {

	private static final long serialVersionUID = -5995287400775901972L;
	private InfluxDBWorkArea workAreaReference;
	private InfluxDBToolBar toolbarReference;

	private InfluxDBJTree tree;
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel model;
	private ArrayList<InfluxDBConnection> connectedServers;

	public InfluxDBTreePanel(InfluxDBWorkArea workAreaReference) {
		this.workAreaReference = workAreaReference;
		connectedServers = new ArrayList<InfluxDBConnection>();
		rootNode = new DefaultMutableTreeNode("ROOT");
		setBorder(new EtchedBorder());
		tree = new InfluxDBJTree(rootNode);
		model = (DefaultTreeModel) tree.getModel();

		JScrollPane scrollPane = new JScrollPane(tree);
		double size[][] = { { 5, TableLayout.FILL, 5 }, { 5, TableLayout.FILL, 15 } };
		setLayout(new TableLayout(size));
		add(scrollPane, "1,1");

		tree.addMouseListener(new InfluxDBTreeMouseListener(tree, this));

	}

	public InfluxDBTreeElement getSelectedTreeElement() {
		TreePath[] paths = tree.getSelectionPaths();
		if (paths != null) {
			InfluxDBTreeNode selectedNode = (InfluxDBTreeNode) paths[0].getLastPathComponent();
			return (InfluxDBTreeElement) selectedNode.getUserObject();

		} else {
			return null;
		}
	}

	public void addInfluxDBServer(InfluxDBConnection influxDBConnection) {
		if (isAlreadyConnected(influxDBConnection)) {
			return;
		} else {
			InfluxDBTreeNode server = createServerNode(influxDBConnection);
			rootNode.add(server);
			model.reload(rootNode);
			tree.repaint();
			connectedServers.add(influxDBConnection);
		}
	}
	
	private InfluxDBTreeNode createServerNode(InfluxDBConnection influxDBConnection) {
		InfluxDBTreeNode server = new InfluxDBTreeNode(influxDBConnection);
		InfluxDBFacility facility = InfluxDBFacility.getFacilityReference();
		InfluxDBClient influxDB = facility.getInfluxDB(influxDBConnection);
		if(influxDBConnection.getDatabase().equals("")){
			for (Bucket bucket : influxDB.getBucketsApi().findBuckets()) {
				if(influxDBConnection.getDatabase().equals(bucket.getName()))
					createDatabaseNode( bucket.getName(), influxDBConnection,  facility,  server);
			}
		}
		else {
			createDatabaseNode( influxDBConnection.getDatabase(), influxDBConnection,  facility,  server);
		}
		return server;
	}
	
	private void createDatabaseNode(String databaseName,InfluxDBConnection influxDBConnection, InfluxDBFacility facility, InfluxDBTreeNode server) {
		InfluxDBDataBase influxDBDataBase = new InfluxDBDataBase(databaseName, influxDBConnection);
		InfluxDBTreeNode database = new InfluxDBTreeNode(influxDBDataBase);
		String queryString = "import \"influxdata/influxdb/schema\"\n" +
				"schema.tagValues(bucket: \"" + databaseName + "\", tag: \"attroid\")";
		System.out.printf("query string is %s\n",queryString);
		List<FluxTable> queryResult = facility.query(queryString,
				databaseName,
				influxDBConnection);
		if (queryResult != null) {
			List<FluxRecord> results = queryResult.get(0).getRecords();
			if (results != null) {
				for (FluxRecord resut : results) {
					InfluxDBTreeNode measurement = new InfluxDBTreeNode(
							//Here, the result value could be changed duo to different type of measurement
							//Ringo 2023-07-25
							new InfluxDBMeasuramet(resut.getValue().toString(), database.toString(),
									influxDBConnection));
					database.add(measurement);

				}
			}
			server.add(database);
		}
	}
	public void addDatabase(InfluxDBConnection connection, String databaseName){
		for (int index = 0; index < rootNode.getChildCount(); index++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(index);
				InfluxDBConnection existingConnection = (InfluxDBConnection) child.getUserObject();
				if (existingConnection.getID().equals(connection.getID())) {
					child.add(new InfluxDBTreeNode(new InfluxDBDataBase(databaseName, connection)));
					model.reload(child);
				}
		}
		tree.repaint();
	}

	public void removeSever(InfluxDBConnection connection) {
		for (int index = 0; index < rootNode.getChildCount(); index++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(index);
			InfluxDBConnection existingConnection = (InfluxDBConnection) child.getUserObject();

			if (existingConnection.getID().equals(connection.getID())) {
				rootNode.remove(child);
			}
		}
		model.reload(rootNode);
		tree.repaint();
	}

	public void disconnectServer(InfluxDBConnection connection) {
		removeSever(connection);
		connectedServers.remove(connection);
		toolbarReference.setEnableAll(false);
		InfluxDBFacility.getFacilityReference().closeAndRemoveConnection(connection);
	}

	public boolean isAlreadyConnected(InfluxDBConnection influxDBConnection) {
		for (InfluxDBConnection conn : connectedServers) {
			if (conn.getID().equals(influxDBConnection.getID()))
				return true;
		}
		return false;

	}

	public void removeNode(InfluxDBTreeElement selectedTreeElement) {

		Enumeration<DefaultMutableTreeNode> enumeration = rootNode.preorderEnumeration();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode node = enumeration.nextElement();
			if (node.getUserObject().equals(selectedTreeElement)) {
				node.removeFromParent();
			}
		}
		model.reload(rootNode);
		tree.repaint();

	}
	public void refresh() {
		// TODO   Pendind to implement this feature

		
	}
	public void setToolBarReference(InfluxDBToolBar toolBar) {
		this.toolbarReference = toolBar;

	}

	class InfluxDBTreeMouseListener extends MouseAdapter {

		private InfluxDBJTree tree;
		private InfluxDBTreePanel treePanel;

		private static final int SERVER = 2;
		private static final int DATABASE = 3;
		private static final int MEASURAMENT = 4;

		public InfluxDBTreeMouseListener(InfluxDBJTree tree, InfluxDBTreePanel treePanel) {
			this.tree = tree;
			this.treePanel = treePanel;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() > 1) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (node == null)
					return;
				InfluxDBTreeElement treeElement = (InfluxDBTreeElement) node.getUserObject();
				String measuramentName = "measurament";
				if (treeElement.getClass().equals(InfluxDBMeasuramet.class)) {
					measuramentName = ((InfluxDBMeasuramet) treeElement).toString();
					String databaseName = ((InfluxDBMeasuramet) treeElement).getDatabase();
					workAreaReference.addWorkPanel(
							new NewQueryActionPanel(treeElement.getConnection(), measuramentName, databaseName));
					super.mouseClicked(e);
				} else {
					super.mouseClicked(e);
				}
			}
		}
		public void mousePressed(MouseEvent mouseEvent) {

			if (SwingUtilities.isRightMouseButton(mouseEvent)) {
				TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
				Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);

				if (pathBounds != null && pathBounds.contains(mouseEvent.getX(), mouseEvent.getY())) {
					JPopupMenu menu = new JPopupMenu();
					switch (path.getPathCount()) {
					case SERVER:
						toolbarReference.setConnectionEnabled();
						
						//                                       //
						// Some actions are not implemented yet  // 
						//                                       //
						
						//menu.add(new JMenuItem(new RefreshAction(workAreaReference, treePanel)));
						menu.add(new JMenuItem(new CreateDataBaseAction(workAreaReference, treePanel)));
						menu.add(new JMenuItem(new ShowRetentionPoliciesAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new ShowUsersAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new ShowQueriesAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new ShowStaticsAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new DiagnosticsAction(workAreaReference, treePanel)));
						menu.add(new JMenuItem(new DisconnectAction(workAreaReference, treePanel)));
						break;
					case DATABASE:
						toolbarReference.setDatabaseEnabled();
						//menu.add(new JMenuItem(new RefreshAction(workAreaReference, treePanel)));
						menu.add(new JMenuItem(new NewQueryAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new ShowContinuousQueriesAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new RunBackFillAction(workAreaReference, treePanel)));
						menu.add(new JMenuItem(new DropDatabaseAction(workAreaReference, treePanel)));
						break;
					case MEASURAMENT:
						toolbarReference.setMeasuramentEnabled();
						menu.add(new JMenuItem(new NewQueryAction(workAreaReference, treePanel)));
						menu.add(new JMenuItem(new ShowTagKeysAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new ShowTagValuesAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new ShowFieldKeysAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new ShowSeriesAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new DropMeasuramentAction(workAreaReference, treePanel)));
						//menu.add(new JMenuItem(new DropSeriesAction(workAreaReference, treePanel)));
						break;
					}
					menu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
				}
			} else {
				TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
				Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);
				if (pathBounds != null && pathBounds.contains(mouseEvent.getX(), mouseEvent.getY())) {
					switch (path.getPathCount()) {
					case SERVER:
						toolbarReference.setConnectionEnabled();
						break;
					case DATABASE:
						toolbarReference.setDatabaseEnabled();
						break;
					case MEASURAMENT:
						toolbarReference.setMeasuramentEnabled();
						break;
					}
				}
			}
		}
	}
}