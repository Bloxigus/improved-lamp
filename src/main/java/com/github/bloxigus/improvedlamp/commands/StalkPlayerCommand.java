package com.github.bloxigus.improvedlamp.commands;

import com.github.bloxigus.improvedlamp.ImprovedLamp;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StalkPlayerCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "ilstalkplayer";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";


    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            Pattern pattern = Pattern.compile("[0-9a-zA-Z_]{3,16}");
            if (pattern.asPredicate().test(args[0])) {
                if (!ImprovedLamp.config.STALKED_PLAYERS.contains(args[0])) {
                    ImprovedLamp.config.STALKED_PLAYERS.add(args[0]);
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[Stalker] Successfully added " + EnumChatFormatting.AQUA + args[0] + EnumChatFormatting.GREEN + " to the stalk list"));
                } else {
                    ImprovedLamp.config.STALKED_PLAYERS.remove(args[0]);
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[Stalker] Successfully removed " + EnumChatFormatting.AQUA + args[0] + EnumChatFormatting.GREEN + " from the stalk list"));
                }
                ImprovedLamp.config.saveConfig();
                ImprovedLamp.modules.stalker.fetchStalkedUUIDs(ImprovedLamp.config.STALKED_PLAYERS);
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[Stalker] Invalid Player name!"));
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[Stalker] You must include a person to stalk!"));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("");
    }
}