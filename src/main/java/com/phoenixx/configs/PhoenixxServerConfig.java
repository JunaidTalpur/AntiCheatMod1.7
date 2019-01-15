package com.phoenixx.configs;

import com.phoenixx.PhoenixxMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Phoenixx on 12/06/2018
 */
public class PhoenixxServerConfig
{
    private static final File configDirectory = new File(Loader.instance().getConfigDir(), "/PhoenixStudios");
    private static final File configFile = new File(configDirectory, PhoenixxMod.ANTICHEATNAME+"-AntiCheat.cfg");
    private static File playerCheatingFile = new File(configDirectory, "Player_CheatingFile.txt");

    private static final Properties serverProps = new Properties();

    public static boolean useAntiCheat;
    public static boolean allowTexturePacks;

    public static String encodedWhitelistedMods = "null";
    private static ArrayList<String> whitelistedMods = new ArrayList<String>();

    private static String encodedModWhitelistPlayers = "null";
    private static ArrayList<String> modWhitelistedPlayers = new ArrayList<String>();

    public PhoenixxServerConfig()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side.isClient()) {
            return;
        }
        reloadConfig();
    }

    public static void reloadConfig()
    {
        loadConfig();
        loadExtraFiles();
        useAntiCheat = (Boolean) PhoenixxConfig.loadProp(serverProps, useAntiCheat, Boolean.TRUE, "useAntiCheat");
        allowTexturePacks = (Boolean) PhoenixxConfig.loadProp(serverProps, allowTexturePacks, Boolean.FALSE, "allowTexturePacks");

        /** Whitelisted mods **/
        encodedWhitelistedMods = (String)PhoenixxConfig.loadProp(serverProps, encodedWhitelistedMods,"mcp,FML,Forge,"+PhoenixxMod.MODID,"whitelistMods");
        String[] decodedModWhitelist = decodeData(encodedWhitelistedMods,",");
        loadArrayData(decodedModWhitelist, whitelistedMods);

        /** Whitelisted players for anti-cheat **/
        encodedModWhitelistPlayers = (String)PhoenixxConfig.loadProp(serverProps, encodedWhitelistedMods,"Phoenixx, TestUser","modWhitelistPlayers");
        String[] decodedModWhitelistPlayers = decodeData(encodedModWhitelistPlayers,",");
        loadArrayData(decodedModWhitelistPlayers, modWhitelistedPlayers);

        saveConfig();
    }

    private static void loadExtraFiles()
    {
        if(playerCheatingFile.exists()) {
            System.out.println("Successfully loaded player cheating file");
        } else {
            try {
                System.out.println("Player cheating file not found! Creating a new one...");
                playerCheatingFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadConfig()
    {
        PhoenixxConfig.loadConfig(configDirectory, configFile, serverProps);
    }

    private static void saveConfig()
    {
        PhoenixxConfig.saveConfig(configDirectory, configFile, serverProps, "~" + PhoenixxMod.ANTICHEATNAME + " Anti-Cheat Server Setup~");
    }

    /** Add person to cheater list */
    public static void addToCheaterList(String playerData)
    {
        if(playerCheatingFile != null)
        {
            try
            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String timeNow = formatter.format(date);

                FileWriter fw = new FileWriter(playerCheatingFile,true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                pw.println(timeNow + " | " + playerData);
                pw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static ArrayList<String> getModWhitelistedPlayers()
    {
        return modWhitelistedPlayers;
    }

    public static ArrayList<String> getWhitelistedMods()
    {
        return whitelistedMods;
    }

    private static void loadArrayData(String[] data, ArrayList<String> arrayList)
    {
        arrayList.clear();
        if(data != null)
        {
            for(String d : data)
            {
                arrayList.add(d);
            }
        }
    }

    public static String[] decodeData(String loc, String splitby)
    {
        if(loc.equals("null"))
        {
            return null;
        }

        String[] data;

        if(splitby == null)
        {
            data = loc.split("\\|");
            return data;
        }else{
            data = loc.split("\\"+splitby);
        }

        if(!loc.contains(splitby))
        {
            data = new String[1];
            data[0] = loc;
        }

        return data;
    }
}