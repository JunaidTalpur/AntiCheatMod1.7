package com.phoenixx.handlers;

import com.phoenixx.configs.PhoenixxConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phoenixx on 12/06/2018
 */
public class AntiCheatHandler
{
    public static String getModIDs()
    {
        ArrayList<String> modIDs = new ArrayList<String>();
        List<ModContainer> mods = Loader.instance().getModList();

        for(ModContainer mod : mods)
        {
            String name = mod.getName();
            String modID = mod.getModId();
            modIDs.add(modID);
            if(PhoenixxConfig.debugClient)
            {
                PhoenixxConfig.addToModList(name, modID);
            }
        }
        //modIDs.add("Cheating-Essentials");
        String allModsIDsToString = String.join(",", modIDs);

        if(PhoenixxConfig.debugClient)
        {
            PhoenixxConfig.addEntireListToModFile(allModsIDsToString);
        }

        return allModsIDsToString;
    }
}