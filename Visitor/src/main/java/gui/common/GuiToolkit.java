package gui.common;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;

import java.awt.Font;
import java.awt.Component;

public class GuiToolkit {
    
    private final static  String ICONPATH = System.getenv("InfluxIcons")+"\\";//   "D:\\mvn_prj\\Visitor\\icons\\";
    private final static Font FONT = new Font("Arial",Font.PLAIN,11);
    
    public static Font getFont(){
        return FONT;
    }
    public static void setDefaultFont() { 
        Hashtable oUIDefault = UIManager.getDefaults();
        Enumeration oKey = oUIDefault.keys();
        String oStringKey = null;
        while (oKey.hasMoreElements()){
            oStringKey = oKey.nextElement().toString();
            if (oStringKey.endsWith("font") || oStringKey.endsWith("acceleratorFont")){
                UIManager.put(oStringKey, FONT);
            }
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void applyFont(JComponent c){
        c.setFont(FONT);
        Component comp[] = c.getComponents();
        for(int i=0; i<comp.length;i++){
            comp[i].setFont(FONT);
        }
    }
    public static String getIconPath(){
        return ICONPATH;
    }
    public static Icon getIcon(String iconName){
        return getImageIcon(iconName);
    }
    public static ImageIcon getImageIcon(String iconName){
        String path = ICONPATH+iconName;

        File file = new File(path);
        if(file.exists())
            return new ImageIcon(path);
        else
            return new ImageIcon(ICONPATH + "empty.png");
        /* Not working
        if(ClassLoader.getSystemResource(path) == null){
            //return new ImageIcon(ClassLoader.getSystemResource(ICONPATH+"empty.png"));
            return new ImageIcon(ICONPATH + "empty.png");
        }
        else{
            //return new ImageIcon(ClassLoader.getSystemResource(ICONPATH+iconName));
            return new ImageIcon(path);
        }*/


    }
}