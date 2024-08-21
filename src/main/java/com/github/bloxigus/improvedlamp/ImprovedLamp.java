package com.github.bloxigus.improvedlamp;

import com.github.bloxigus.improvedlamp.commands.SetAPIKeyCommand;
import com.github.bloxigus.improvedlamp.commands.StalkPlayerCommand;
import com.github.bloxigus.improvedlamp.config.ModConfig;
import com.github.bloxigus.improvedlamp.handlers.ServerConnectionHandler;
import com.github.bloxigus.improvedlamp.handlers.TickHandler;
import com.github.bloxigus.improvedlamp.modules.Modules;
import com.github.bloxigus.improvedlamp.mojangapi.MojangBulkUsernameApi;
import com.google.gson.Gson;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(modid = "improvedlamp", useMetadata=true)
public class ImprovedLamp {
    public static Gson globalGson;
    public static ModConfig config;
    public static Modules modules;
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        MinecraftForge.EVENT_BUS.register(new ServerConnectionHandler());
        ClientCommandHandler.instance.registerCommand(new SetAPIKeyCommand());
        ClientCommandHandler.instance.registerCommand(new StalkPlayerCommand());
        globalGson = new Gson();
        modules = new Modules();
        config = ModConfig.loadConfig();
        modules.stalker.fetchStalkedUUIDs(config.STALKED_PLAYERS);
    }
}
