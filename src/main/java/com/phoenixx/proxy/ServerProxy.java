package com.phoenixx.proxy;

import com.phoenixx.configs.PhoenixxServerConfig;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Phoenixx on 12/06/2018
 */
public class ServerProxy extends CommonProxy
{
    public static PhoenixxServerConfig phoenixxServerConfig;
    @Override
    public void preInit(FMLPreInitializationEvent e)
    {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e)
    {
        super.init(e);
        phoenixxServerConfig = new PhoenixxServerConfig();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e)
    {
        super.postInit(e);
    }
}
