package com.phoenixx.packets;

import com.phoenixx.PhoenixxMod;
import com.phoenixx.configs.PhoenixxServerConfig;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Phoenixx on 13/06/2018
 */
public class AntiCheat_Packet implements IMessage
{
    private int messageID;
    private static String data;
    private static ArrayList<String> clientList;

    public AntiCheat_Packet()
    {

    }

    public AntiCheat_Packet(int number)
    {
        this.messageID = number;
        this.data = "NONE";
    }

    public AntiCheat_Packet(int number, String modList)
    {
        this.messageID = number;
        data = modList;
        System.out.println("List: " + modList);
    }

    public void fromBytes(ByteBuf buf)
    {
        this.messageID = buf.readInt();
        data = ByteBufUtils.readUTF8String(buf);
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.messageID);
        ByteBufUtils.writeUTF8String(buf, data);
    }

    public static class Handler implements IMessageHandler<AntiCheat_Packet, IMessage>
    {
        @Override
        public IMessage onMessage(AntiCheat_Packet message, MessageContext ctx)
        {
            if(ctx != null)
            {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;

                if(player != null)
                {
                    String playerName = player.getDisplayName();

                    if(PhoenixxServerConfig.useAntiCheat)
                    {
                        // Mods
                        if(message.messageID == 0)
                        {
                            if(!data.equals("NONE"))
                            {
                                clientList = new ArrayList<String>(Arrays.asList(data.split(",")));
                                Collection<String> extraMods = getExtraBadItems(clientList);
                                if(userHasExtraBadItems(clientList, true))
                                {
                                    if(!canUserConnectWithExtraBadItems(playerName))
                                    {
                                        ctx.getServerHandler().kickPlayerFromServer(EnumChatFormatting.GREEN + EnumChatFormatting.BOLD.toString() + "[" + PhoenixxMod.ANTICHEATNAME + " Anti-Cheat]\n" + EnumChatFormatting.RESET  +"You have been kicked.\n" + EnumChatFormatting.RED + "You have mods that this server does not support! Please remove them before connecting again.\nExtra mods: " + extraMods);
                                        PhoenixxServerConfig.addToCheaterList(playerName + " kicked due to extra mods: " + extraMods);
                                    }
                                }
                            }
                        }

                        // Texture-packs
                        if(message.messageID == 1)
                        {
                            if(!data.equals("NONE"))
                            {
                                clientList = new ArrayList<String>(Arrays.asList(data.split(",")));
                                Collection<String> extraTexturePacks = getExtraBadItems(clientList);

                                if(!PhoenixxServerConfig.allowTexturePacks && clientList != null && !clientList.isEmpty())
                                {
                                    if(!canUserConnectWithExtraBadItems(playerName))
                                    {
                                        ctx.getServerHandler().kickPlayerFromServer(EnumChatFormatting.GREEN + EnumChatFormatting.BOLD.toString() + "[" + PhoenixxMod.ANTICHEATNAME + " Anti-Cheat]\n" + EnumChatFormatting.RESET  +"You have been kicked.\n" + EnumChatFormatting.RED + "You have texture packs that this server does not support! Please remove them before connecting again.\nExtra packs: " + extraTexturePacks);
                                        PhoenixxServerConfig.addToCheaterList(playerName + " kicked due to extra texture packs: " + extraTexturePacks);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    private static boolean userHasExtraBadItems(ArrayList<String> badItems, boolean checkMods)
    {
        if(checkMods){
            ArrayList<String> serverSideMods = PhoenixxServerConfig.getWhitelistedMods();

            Collection<String> listOne = new ArrayList<>(serverSideMods);
            Collection<String> listTwo = new ArrayList<>(badItems);

            listTwo.removeAll(listOne);

            if(listTwo.isEmpty())
            {
                return false;
            }
        } else {
            Collection<String> listTwo = new ArrayList<>(badItems);

            if(listTwo.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    private static Collection<String> getExtraBadItems(ArrayList<String> clientMods)
    {
        ArrayList<String> serverSideMods = PhoenixxServerConfig.getWhitelistedMods();

        Collection<String> listOne = new ArrayList<>(serverSideMods);
        Collection<String> listTwo = new ArrayList<>(clientMods);

        listTwo.removeAll(listOne);

        return listTwo;
    }

    private static boolean canUserConnectWithExtraBadItems(String username)
    {
        ArrayList<String> playerModWhitelist = PhoenixxServerConfig.getModWhitelistedPlayers();

        return playerModWhitelist.contains(username);
    }
}