# InfluxVistitor23
This project is all based on InfluxDBWorkbench(https://github.com/JorgeMaker/InfluxDBWorkBench).
Because the influx changed so fase, it's not easy to find a tool. 
This tool is changed for influxdb version 2.3. For the version 1.x , it can not be used. And for 
other version, it's should be changed by yourself.

Notification：
1 The icons path could be changed. The icons path is searched by environment path InfluxIcons.
  Code is as following.
  
   public static ImageIcon getImageIcon(String iconName){
        String path = ICONPATH+iconName;

        File file = new File(path);
        if(file.exists())
            return new ImageIcon(path);
        else
            return new ImageIcon(ICONPATH + "empty.png");
    }


