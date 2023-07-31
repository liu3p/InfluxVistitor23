package com.data;


import com.influxdb.*;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.*;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InfluxUtil {


    final static String token = "IfCCb2Xp9MMhE3o7VnolmzjPgxccj2uk6AttLP1V5O0HDbLYZIMNo7FostLUDTGmIybwfoeOGuKU0ooHwuvSmA==";
    final static String bucket = "hisdb";
    final static String org = "his";
    final static String url = "http://127.0.0.1:8086";
    static InfluxDBClient client =null;


    public static void initClient() {
        //client = InfluxDBClientFactory.create(url, token.toCharArray());

        client = InfluxDBClientFactory.create(url,token.toCharArray(),org, bucket);
        //client = InfluxDBClientFactory.create(url,"ems","his12345678".toCharArray());
    }


    public static void closeClient() {
        client.close();
    }

    /**
     * 组装query的查询字符串
     * @param dbName
     * @param oids
     * @param time
     * @param condition
     * @return
     */
    public static String formatQuery(String dbName,ArrayList<String> oids,String time,String condition){
        String queryFlux =  "from(bucket: \""+ dbName + "\")\n" ;
        if( time.length() > 0){
            queryFlux += " |> range(start:" + time + ")\n";
        }

        if(oids.size() > 0){
            int i=0;
            queryFlux += " |> filter(fn: (r) => ";
            int len = oids.size() -2;
            for(String s : oids){
                queryFlux += "r[\"attroid\"] == \"" + s + "\" ";
                if(i <= len)    queryFlux += " or ";
                i++;
            }
            queryFlux += ")\n";
        }

        return queryFlux;
    }


    public static void createBucket(){

            //char[] token = "my-token".toCharArray();
            InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://127.0.0.1:8086", "PGwv0mzyIkA1q_BPxcQxmwC3B-WrqcmUWDwGJtmJIUNOu0icFK5Om0STr0-iHtETaXaAKJze3hFb1DLDAuPVBg==".toCharArray());

            //
            // Create bucket "iot_bucket" with data retention set to 3,600 seconds
            //
            BucketRetentionRules retention = new BucketRetentionRules();
            retention.setEverySeconds(3600);

            Bucket bucket = influxDBClient.getBucketsApi().createBucket("iot-bucket", "12bdc4164c2e8141");

            List<BucketRetentionRules> retensionList = new ArrayList<BucketRetentionRules>() ;
            retensionList.add(retention);
            bucket.setRetentionRules(retensionList);
            //
            // Create access token to "iot_bucket"
            //
            PermissionResource resource = new PermissionResource();
            resource.setId(bucket.getId());
            resource.setOrgID("12bdc4164c2e8141");
            resource.setType(PermissionResource.TYPE_BUCKETS);

            // Read permission
            Permission read = new Permission();
            read.setResource(resource);
            read.setAction(Permission.ActionEnum.READ);

            // Write permission
            Permission write = new Permission();
            write.setResource(resource);
            write.setAction(Permission.ActionEnum.WRITE);

            Authorization authorization = influxDBClient.getAuthorizationsApi()
                    .createAuthorization("12bdc4164c2e8141", Arrays.asList(read, write));

            //
            // Created token that can be use for writes to "iot_bucket"
            //
            String token1 = authorization.getToken();
            System.out.println("Token: " + token1);

            influxDBClient.close();

    }



    public void testQuery(){
        QueryApi queryApi = client.getQueryApi();
        ArrayList<String> oids = new ArrayList<>();
        //oids.add("4222124656361475");
        //oids.add("4222124656427011");
        //oids.add("4222124656623619");
        //String queryFlux = formatQuery("hisdb",oids,"-1h","");

        //System.out.printf("query string [%s]",queryFlux);
        String queryFlux = "from(bucket: \"hisdb\")\n" +
                "  |> range(start: -3d)\n" +
                "  |> filter(fn: (r) => r[\"attroid\"] == \"4222124650659843\" )";
        //String queryFlux = "import \"influxdata/influxdb/schema\"\n" +
        //                    "\n" +
        //                    "schema.measurements(bucket: \"hisdb\")";
       /* String queryFlux =
                "from(bucket: \"hisdb\")\n" +
                      "  |> range(start: -7h)\n" +
                        "  |> filter(fn: (r) => r[\"attroid\"] == \"4222124656361475\")\n";
                        /*+
                        "  |> filter(fn: (r) => r[\"_field\"] == \"value\")\n" +
                        "  |> filter(fn: (r) => r[\"city\"] == \"sh\")\n" +
                        "  |> aggregateWindow(every: 5s, fn: mean, createEmpty: false)\n" +
                        "  |> yield(name: \"mean\")";*/
        List<FluxTable> list = queryApi.query(queryFlux);
        if(list!=null){
            for(int  i=0; i< list.size();i++){
                System.out.printf("--------------- %d ---------------\n",i);
                List<FluxRecord> recods = list.get(i).getRecords();
                for(int j=0 ; j< recods.size(); j++){
                    System.out.printf("*****************\n");
                    Map<String,Object> result_map = recods.get(j).getValues();
                    for(Map.Entry<String,Object> entry:result_map.entrySet() ){
                        System.out.printf("%s,%s\n",entry.getKey().toString(),entry.getValue().toString());
                    }
                }
            }
        }

        //Assertions.assertNotNull(list);
    }

    public static void  main(String [] args)
    {
        /*
        InfluxUtil m = new InfluxUtil();
        initClient();
        m.testQuery();
        closeClient();*/

        createBucket();
    }

}
