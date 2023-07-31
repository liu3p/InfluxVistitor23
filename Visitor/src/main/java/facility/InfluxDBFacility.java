package facility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;


import datamodel.InfluxDBConnection;

public class InfluxDBFacility {

	private static InfluxDBFacility facility;
	private HashMap<InfluxDBConnection, InfluxDBClient> connectionsRepository;
	
	private InfluxDBFacility() {
		connectionsRepository = new HashMap <InfluxDBConnection, InfluxDBClient> ();
	}
	public InfluxDBClient getInfluxDB(InfluxDBConnection connection) {
		if (connection != null) {
			String connectionString = "http://"+connection.getIpAddresss()+":"+connection.getPort();
			connection.setUrl(connectionString);

			InfluxDBClient influxDB = connectionsRepository.get(connection);
			if (influxDB != null){
				return influxDB;
			}
			else {
				if( (connection.getUserName() == null || connection.getUserName().trim().equalsIgnoreCase("")) &&
						(connection.getPassword() == null || connection.getPassword().trim().equalsIgnoreCase(""))){
					return null;
				}
				else {
					if(connection.getToken()!=null && connection.getToken().length() > 1)
						influxDB = InfluxDBClientFactory.create(connection.getUrl(),connection.getToken().toCharArray(),connection.getOrg(), connection.getDatabase());
					else
						influxDB = InfluxDBClientFactory.create(connection.getUrl(),connection.getUserName(),connection.getPassword().toCharArray());
				}
				connectionsRepository.put(connection, influxDB);
				return influxDB;
			}
		}
		else return null;
	}
	public void closeAndRemoveConnection(InfluxDBConnection connection) {
		InfluxDBClient influxDB  =connectionsRepository.get(connection);
		influxDB.close();
		connectionsRepository.remove(connection);
	}



	public List<FluxTable> query(String queryString , String databaseName, InfluxDBConnection connection){
		//Query query = new Query(queryString, databaseName);
		InfluxDBClient influxDB = getInfluxDB(connection);
		if(influxDB!=null) {
			QueryApi queryApi = influxDB.getQueryApi();
			try {
				List<FluxTable> list = queryApi.query(queryString);
				return list;
			}
			catch (NullPointerException exp) {
				return null;
			}
		}else{
			return null;
		}
	}
    public static InfluxDBFacility getFacilityReference(){
        if(facility == null){
        	facility = new InfluxDBFacility();
        }
        return facility;
    }

}
