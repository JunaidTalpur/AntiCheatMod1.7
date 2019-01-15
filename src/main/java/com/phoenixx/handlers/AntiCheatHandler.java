package com.phoenixx.handlers;

import com.phoenixx.configs.PhoenixxConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phoenixx on 12/06/2018
 */
public class AntiCheatHandler
{
    private static Minecraft mc = Minecraft.getMinecraft();

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

    public static String getTexturePacks()
    {
        if(!mc.gameSettings.resourcePacks.isEmpty())
        {
            ArrayList<String> texturePacks = new ArrayList<String>();
            texturePacks.addAll(mc.gameSettings.resourcePacks);

            String allModsIDsToString = String.join(",", texturePacks);

            if(PhoenixxConfig.debugClient)
            {
                PhoenixxConfig.addEntireListToModFile(allModsIDsToString);
            }
            return allModsIDsToString;
        }
        return "NONE";
    }
}