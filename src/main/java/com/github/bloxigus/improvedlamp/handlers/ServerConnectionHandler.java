package com.github.bloxigus.improvedlamp.handlers;

import com.github.bloxigus.improvedlamp.ImprovedLamp;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ServerConnectionHandler {
    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        System.out.println("JOINED SERVER @@$*&@^$^!@$*^");
        ImprovedLamp.modules.stalker.isConnectedToServer = true;
    }
    @SubscribeEvent
    public void onLeaveServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ImprovedLamp.modules.stalker.isConnectedToServer = false;
    }
}
