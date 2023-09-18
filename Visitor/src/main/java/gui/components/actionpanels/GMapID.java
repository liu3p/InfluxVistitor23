package gui.components.actionpanels;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;

public class GMapID {

    public static HashMap<String,String> gIDtoName = new HashMap<>();
    public static HashMap<String,String> gNametoID = new HashMap<>();

    public static void read()
    {
        BufferedReader reader;
        try {
            InputStream is = new FileInputStream("oid_name.txt");
            reader = new BufferedReader(new InputStreamReader(is,
                    "gbk"));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                String [] words = line.split(":");

                if(words.length > 2){
                    String id = toLong(words[2]);
                    String name = words[0]+words[1];
                    gIDtoName.put(id,name);
                    gNametoID.put(name,id);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String toLong(String oid)
    {
        String [] values = oid.split("-");
        if(values.length < 3)
            return "0";
        long value = 0;
        value = Integer.valueOf(values[0]);
        value <<= 32;
        value += Integer.valueOf(values[1]);
        value <<= 16;
        value += Integer.valueOf(values[2]);

        return String.valueOf(value);
    }
}
