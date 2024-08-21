package com.github.bloxigus.improvedlamp.modules;

import com.github.bloxigus.improvedlamp.ImprovedLamp;
import com.github.bloxigus.improvedlamp.hypixelapi.HypixelStatusApi;
import com.github.bloxigus.improvedlamp.mojangapi.MojangBulkUsernameApi;
import com.github.bloxigus.improvedlamp.utils.IslandUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Stalker {
    public boolean isConnectedToServer = false;
    public boolean isStalkerRunning = false;
    private static Thread stalkThread = null;
    public MojangBulkUsernameApi.ApiResponse stalkedPlayers = null;
    public MojangBulkUsernameApi.ApiResponse getStalkedPlayers() {
        return this.stalkedPlayers;
    }
    private boolean shouldKillThread = false;
    public HashMap<String, HypixelStatusApi.OnlineStatus> lastStatus = new HashMap<>();
    public void startStalking() {
        if (stalkThread == null) stalkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!shouldKillThread) {
                    MojangBulkUsernameApi.ApiResponse localStalkedPlayers = getStalkedPlayers();
                    if (isConnectedToServer && !ImprovedLamp.config.STALKED_PLAYERS.isEmpty()) {
                        try {
                            for (MojangBulkUsernameApi.PlayerEntry playerEntry : localStalkedPlayers.data) {
                                CompletableFuture<HypixelStatusApi.ApiResponse> statusRequest = HypixelStatusApi.requestStatus(playerEntry.id);
                                statusRequest.whenComplete((apiResponse, throwable) -> {
                                    if (apiResponse.success) {
                                        String result = formatChange(playerEntry, lastStatus.getOrDefault(playerEntry.id, null), apiResponse.session);
                                        if (!result.isEmpty()) {
                                            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(result));
                                        }
                                        lastStatus.put(playerEntry.id, apiResponse.session);
                                    } else {
                                        System.out.println("Got unsuccessful request, stopping stalker thread!");
                                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "[Stalker] API request failed! Update your key with "+EnumChatFormatting.AQUA+"/ilupdatekey [API Key]"));
                                        shouldKillThread = true;
                                    }
                                });
                                Thread.sleep(ImprovedLamp.config.API_REQUEST_TIMEOUT);
                            }
                        } catch (InterruptedException ignored) {
                            throw new RuntimeException(ignored);
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                            throw new RuntimeException(ignored);
                        }
                    }
                }
            }
        }, "StalkerThread");
        if (!stalkThread.isAlive()) stalkThread.start();
        isStalkerRunning = true;
    }
    private String formatChange(MojangBulkUsernameApi.PlayerEntry playerEntry, HypixelStatusApi.OnlineStatus oldStatus, HypixelStatusApi.OnlineStatus newStatus) {
        if (oldStatus != null) {
            if (oldStatus.online == newStatus.online) {
                if (Objects.equals(oldStatus.gameType, newStatus.gameType)) {
                    if (Objects.equals(oldStatus.mode, newStatus.mode)) {
                        return "";
                    }
                    if (Objects.equals(newStatus.gameType, "SKYBLOCK")) {
                        Minecraft.getMinecraft().thePlayer.playSound("entity.ghast.scream", 2.0f, 1.0f);
                        return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " changed to " + EnumChatFormatting.AQUA + IslandUtils.prettyIslandName(newStatus.mode);
                    }
                    return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " changed to " + EnumChatFormatting.AQUA + newStatus.mode;
                }
                if (Objects.equals(newStatus.gameType, "SKYBLOCK")) {
                    return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " starting playing " + EnumChatFormatting.AQUA + "Skyblock" + EnumChatFormatting.YELLOW + " in " + EnumChatFormatting.AQUA + IslandUtils.prettyIslandName(newStatus.mode);
                }
                return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " started playing " + EnumChatFormatting.AQUA + newStatus.mode + " " + newStatus.gameType;
            }
            if (oldStatus.online) {
                return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " disconnected from the game";
            }
            if (Objects.equals(newStatus.gameType, "SKYBLOCK")) {
                return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " joined the game and is playing " + EnumChatFormatting.AQUA + " Skyblock " + EnumChatFormatting.YELLOW + " in " + EnumChatFormatting.AQUA + newStatus.gameType;
            }
            return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " joined the game and is playing " + EnumChatFormatting.AQUA + newStatus.mode + " " + newStatus.gameType;
        }
        if (newStatus.online) {
            if (Objects.equals(newStatus.gameType, "SKYBLOCK")) {
                return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " is currently playing " + EnumChatFormatting.AQUA + "Skyblock in " + EnumChatFormatting.GREEN + IslandUtils.prettyIslandName(newStatus.mode);
            } else if (Objects.equals(newStatus.mode, "LOBBY")) {
                return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " is currently in the " + EnumChatFormatting.AQUA + newStatus.gameType + " " + IslandUtils.prettyIslandName(newStatus.mode);
            }
            return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " is currently playing " + EnumChatFormatting.AQUA + newStatus.gameType + EnumChatFormatting.YELLOW + " on " + EnumChatFormatting.GREEN + IslandUtils.prettyIslandName(newStatus.mode);
        }
        return EnumChatFormatting.YELLOW + "[Stalker] " + EnumChatFormatting.AQUA + playerEntry.name + EnumChatFormatting.YELLOW + " is currently offline";
    }
    public void setStalkedPlayers(MojangBulkUsernameApi.ApiResponse players) {
        this.stalkedPlayers = players;
    }
    public void fetchStalkedUUIDs(List<String> usernames) {
        CompletableFuture<MojangBulkUsernameApi.ApiResponse> uuids = MojangBulkUsernameApi.requestUuids(usernames);
        uuids.whenComplete((apiResponse,error) -> {
            if (error == null) {
                setStalkedPlayers(apiResponse);
                startStalking();
            } else {
                throw new RuntimeException(error);
            }
        });
    }
    public boolean killThread() {
        if (this.shouldKillThread) return false;
        this.shouldKillThread = true;
        return true;
    }
}
