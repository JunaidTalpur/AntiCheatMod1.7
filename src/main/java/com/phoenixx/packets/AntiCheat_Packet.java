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
    private static ArrayList<String> clientModList;

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
        System.out.println("ModList: " + modList);
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
                    String prefix = EnumChatFormatting.RED + EnumChatFormatting.BOLD.toString() + "[" + PhoenixxMod.ANTICHEATNAME + " Anti-Cheat]" + EnumChatFormatting.RESET + " " ;
                    String proofText = EnumChatFormatting.RED + EnumChatFormatting.BOLD.toString() + "[" + PhoenixxMod.ANTICHEAT_SHORTNAME + "] " + EnumChatFormatting.LIGHT_PURPLE + "Note: Please screenshot this screen in-case proof is ever needed.";
                    if(message.messageID == 0)
                    {
                        if(!data.equals("NONE"))
                        {
                            clientModList = new ArrayList<String>(Arrays.asList(data.split(",")));
                            Collection<String> extraMods = getExtraMods(clientModList);
                            if(userHasExtraMods(clientModList))
                            {
                                if(!canUserConnectWithExtraMods(playerName))
                                {
                                    String antiCheatMessage = EnumChatFormatting.LIGHT_PURPLE + EnumChatFormatting.BOLD.toString() + "[" + PhoenixxMod.ANTICHEAT_SHORTNAME + "] " + EnumChatFormatting.AQUA + EnumChatFormatting.BOLD.toString() + player.getDisplayName() + EnumChatFormatting.GOLD + " was kicked for extra mods! Extra mods: " + extraMods;
                                    ctx.getServerHandler().kickPlayerFromServer(EnumChatFormatting.GREEN + EnumChatFormatting.BOLD.toString() + "[" + PhoenixxMod.ANTICHEATNAME + " Anti-Cheat]\n" + EnumChatFormatting.RESET  +"You have been kicked.\n" + EnumChatFormatting.RED + "You have mods that this server does not support! Please remove them before connecting again.\nExtra mods: " + extraMods);
                                    PhoenixxServerConfig.addToCheaterList(playerName + " kicked due to extra mods: " + extraMods);
                                    //MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(prefix + proofText));
                                    //MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(prefix + antiCheatMessage));
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    private static boolean userHasExtraMods(ArrayList<String> clientMods)
    {
        boolean hasMods = true;
        ArrayList<String> serverSideMods = PhoenixxServerConfig.getWhitelistedMods();

        Collection<String> listOne = new ArrayList<>(serverSideMods);
        Collection<String> listTwo = new ArrayList<>(clientMods);

        listTwo.removeAll(listOne);

        if(listTwo.isEmpty())
        {
            hasMods = false;
        }

        return hasMods;
    }

    private static Collection<String> getExtraMods(ArrayList<String> clientMods)
    {
        ArrayList<String> serverSideMods = PhoenixxServerConfig.getWhitelistedMods();

        Collection<String> listOne = new ArrayList<>(serverSideMods);
        Collection<String> listTwo = new ArrayList<>(clientMods);

        listTwo.removeAll(listOne);

        return listTwo;
    }

    private static boolean canUserConnectWithExtraMods(String username)
    {
        ArrayList<String> playerModWhitelist = PhoenixxServerConfig.getModWhitelistedPlayers();

        return playerModWhitelist.contains(username);
    }
}