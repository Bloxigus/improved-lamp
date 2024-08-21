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

public class SetAPIKeyCommand  extends CommandBase {

    @Override
    public String getCommandName() {
        return "ilupdatekey";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";


    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-??[0-9a-fA-F]{12}");
            if (pattern.asPredicate().test(args[0])) {
                ImprovedLamp.config.API_KEY = args[0];
                ImprovedLamp.config.saveConfig();
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[Stalker] Invalid API Key!"));
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[Stalker] You must include your API key with this command!"));
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