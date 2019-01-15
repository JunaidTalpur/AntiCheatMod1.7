package com.phoenixx.configs;

import com.phoenixx.PhoenixxMod;
import cpw.mods.fml.common.Loader;

import java.io.*;
import java.util.Properties;

/**
 * Created by Phoenixx on 12/06/2018
 */
public class PhoenixxConfig
{
    private static final Properties clientProps = new Properties();
    private static final File configDirectory = new File(Loader.instance().getConfigDir(), "/PhoenixStudios/");
    private static final File configFile = new File(configDirectory, PhoenixxMod.ANTICHEATNAME+"-Client.cfg");
    private static File allLoadedMods = new File(configDirectory, "LoadedMods_FromLastStart.txt");

    public static boolean debugClient = false;

    private static String defComment = "~" + PhoenixxMod.ANTICHEATNAME + "-AntiCheat Client Configuration File~";

    public PhoenixxConfig()
    {
        loadConfig(configDirectory, configFile, clientProps);
        loadExtraFiles();

        debugClient = (Boolean) loadProp(clientProps, debugClient, Boolean.FALSE, "debugClient");

        saveConfig(configDirectory, configFile, clientProps, defComment);
    }

    private static void loadExtraFiles()
    {
        if(allLoadedMods.exists()) {
            //System.out.println("Successfully loaded player cheating file");
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(allLoadedMods);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            writer.print("");
            writer.close();
        } else {
            try {
                //System.out.println("Player cheating file not found! Creating a new one...");
                allLoadedMods.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Add all loaded mods to file */
    public static void addToModList(String modName, String modID)
    {
        if(allLoadedMods != null)
        {
            try
            {
                FileWriter fw = new FileWriter(allLoadedMods,true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                pw.println(modName + " | " + modID);
                pw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Add whole loaded list to file */
    public static void addEntireListToModFile(String allMods)
    {
        if(allLoadedMods != null)
        {
            try
            {
                FileWriter fw = new FileWriter(allLoadedMods,true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                pw.println("");
                pw.println("COPY THIS ENTIRE LIST AND PASTE IT IN THE SERVER SIDE ANTI-CHEAT CONFIG:");
                pw.println(allMods);
                pw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object loadProp(Properties p, Object o, Object def, String pname)
    {
        if (p.containsKey(pname))
        {
            o = parseObject(o, p.getProperty(pname));
            System.out.println("Loaded config property '" + pname + "' = " + o);
            return o;
        }
        p.setProperty(pname, toStringObject(def));
        System.out.println("Setup config property '" + pname + "'(" + def + ")");
        return def;
    }

    private static String toStringObject(Object o)
    {
        if ((o instanceof Boolean)) {
            return ((Boolean)o).toString();
        }
        if ((o instanceof String)) {
            return (String)o;
        }
        if ((o instanceof Integer)) {
            return ((Integer)o).toString();
        }
        if ((o instanceof Float)) {
            return o.toString();
        }
        return "";
    }

    private static Object parseObject(Object o, String property)
    {
        if(o instanceof Boolean)
        {
            return Boolean.parseBoolean(property);
        }

        if(o instanceof String)
        {
            return property;
        }

        if(o instanceof Integer)
        {
            return Integer.parseInt(property);
        }

        if(o instanceof Float)
        {
            return Float.parseFloat(property);
        }

        return "";
    }

    public static void loadConfig(File dir, File file, Properties prop)
    {
        try
        {
            dir.mkdir();
            if ((!file.exists()) && (!file.createNewFile())) {
                return;
            }
            if (file.canRead())
            {
                FileInputStream fileinputstream = new FileInputStream(file);
                prop.load(fileinputstream);
                fileinputstream.close();
            }
        }
        catch (IOException ex)
        {
            System.out.println("Could not load "+PhoenixxMod.ANTICHEATNAME+"-AntiCheat Config file.");
        }
    }

    public static void saveConfig(File dir, File file, Properties prop, String comment)
    {
        try
        {
            dir.mkdir();
            if ((!file.exists()) && (!file.createNewFile())) {
                return;
            }
            if (file.canWrite())
            {
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                prop.store(fileoutputstream, comment);
                fileoutputstream.close();
            }
        }
        catch (IOException ex)
        {
            System.out.println("Could not save "+PhoenixxMod.ANTICHEATNAME+"-AntiCheat Config file.");
        }
    }
}
