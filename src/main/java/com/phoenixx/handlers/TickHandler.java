package com.phoenixx.handlers;

import com.phoenixx.PhoenixxMod;
import com.phoenixx.packets.AntiCheat_Packet;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;


/**
 * Created by Phoenixx on 12/06/2018
 */
public class TickHandler {

    @SideOnly(Side.CLIENT)
    Minecraft mc;

    public static boolean hasSentModsList = false;

    public TickHandler()
    {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side.isClient()) {
            this.mc = FMLClientHandler.instance().getClient();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void clientTick(TickEvent.ClientTickEvent event)
    {
        if(mc.theWorld == null)
        {
            hasSentModsList = false;
        }

        if(!hasSentModsList && mc.theWorld != null && mc.getIntegratedServer() == null && !mc.isSingleplayer())
        {
            //System.out.println("SENDING ANTI-CHEAT PACKET");
            PhoenixxMod.INSTANCE.sendToServer(new AntiCheat_Packet(0, AntiCheatHandler.getModIDs()));
            PhoenixxMod.INSTANCE.sendToServer(new AntiCheat_Packet(1, AntiCheatHandler.getTexturePacks()));
            hasSentModsList = true;
        }
        if(mc.isSingleplayer())
        {
            System.out.println(AntiCheatHandler.getTexturePacks());
        }
    }
}
