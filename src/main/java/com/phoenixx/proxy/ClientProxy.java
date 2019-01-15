package com.phoenixx.proxy;

import com.phoenixx.configs.PhoenixxConfig;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Phoenixx on 12/06/2018
 */
public class ClientProxy extends CommonProxy
{

    public static PhoenixxConfig phoenixxConfig;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        phoenixxConfig = new PhoenixxConfig();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);

    }

}