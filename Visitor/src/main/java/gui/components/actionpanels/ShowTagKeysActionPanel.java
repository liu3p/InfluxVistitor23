package gui.components.actionpanels;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import datamodel.InfluxDBConnection;
import facility.InfluxDBFacility;
import gui.common.TableRecord;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ShowTagKeysActionPanel extends ActionPanel {

	private static final long serialVersionUID = -7077134831254192379L;
	private InfluxDBConnection connection;
	private String mesurementName;
	private String databaseName;
	
	private JScrollPane mainPanel;
	private TagKeysTable tagsKeyTable;

	public ShowTagKeysActionPanel(InfluxDBConnection connection, String mesurementName, String databaseName) {
	
	super(connection.getConnectionName()+ "."+ mesurementName+".tagKeys", "TagKeys.png");
	
	this.connection = connection;
	this.mesurementName = mesurementName;
	this.databaseName = databaseName;
	
	setBorder(new EtchedBorder());
	setLayout(new BorderLayout());
	initComponets();
	runQuery();
	add(mainPanel);
	
	}
	private void initComponets() {
		tagsKeyTable = new TagKeysTable();
		mainPanel  = new JScrollPane(tagsKeyTable);
	
	}
	@Override
	public void runQuery() {
		tagsKeyTable.removeAllRows();
		InfluxDBFacility facility = InfluxDBFacility.getFacilityReference();

		String queryString = 	"import schema2 \"influxdata/influxdb/schema\"\n" +
								"schema2.tagKeys(bucket:\"hisdb\")";
		InfluxDBClient influxDB = facility.getInfluxDB(connection);
		QueryApi queryApi = influxDB.getQueryApi();
		List<FluxTable> reuslt = queryApi.query(queryString);
		for (FluxRecord result : reuslt.get(0).getRecords()) {
			//result.getValues();
			//for()
			tagsKeyTable.addTableRecord(new TagKeyTableRecord(result.getTable(),(String)result.getValueByKey("_value")));
		}
	}
	
	class TagKeyTableRecord implements TableRecord{
		private int table;
		private String tagKey;
		
		public TagKeyTableRecord(int table, String tagKey) {
			this.table =  table;
			this.tagKey = tagKey;
		}
		@Override
		public Object getFieldAt(int fieldIndex) {
			if (fieldIndex==0) return table;
			if (fieldIndex==1) return tagKey;
			return null;
		}

		@Override
		public int getNumberOfField() {
			return 2;
		}

		@Override
		public void setFieldAt(int fieldIndex, Object field) {
			
		}

		@Override
		public Object getFiledbyKey(String key) {
			return null;
		}
	}

}
