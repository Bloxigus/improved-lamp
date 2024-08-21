package com.github.bloxigus.improvedlamp.utils;

public class IslandUtils {
    public static String prettyIslandName(String internalId) {
        switch (internalId) {
            case "mining_1": return "The Gold Mine";
            case "mining_2": return "The Deep Caverns";
            case "hub": return "The Hub";
            case "mining_3": return "The Dwarven Mines";
            case "crystal_hollows": return "The Crystal Hollows";
            case "combat_1": return "The Spider's Den";
            case "crimson_isle": return "The Crimson Isles";
            case "combat_3": return "The End";
            case "farming_1": return "The Barn";
            case "dynamic": return "Private Island";
            case "garden": return "The Garden";
            case "dark_auction": return "The Dark Auction";
            case "rift": return "The Rift";
            case "foraging_1": return "The Park";
            case "dungeon_hub": return "The Dungeon Hub";
            case "dungeon": return "A Dungeon";
            default: return internalId;
        }
    }
    public static String prettyGameTypeMode(String gameType, String gameMode) {
        return "";
    }
}
