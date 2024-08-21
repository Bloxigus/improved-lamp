package com.github.bloxigus.improvedlamp.config;

import com.github.bloxigus.improvedlamp.ImprovedLamp;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public String API_KEY = "<INVALID_API_KEY>";
    public List<String> STALKED_PLAYERS = new ArrayList<>();
    public void saveConfig() {
        File configFolder = new File(Minecraft.getMinecraft().mcDataDir, "config/improvedlamp");
        File configFile = new File(configFolder, "config.json");
        if (!configFolder.exists()) {
            if (configFolder.mkdirs()) {

            }
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(ImprovedLamp.globalGson.toJson(this));
        } catch (IOException e) {
            return;
        }
    }
    public static ModConfig loadConfig() {
        File configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/improvedlamp/config.json");
        if (!configFile.exists()) return new ModConfig();
        try(BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String config = sb.toString();
            return ImprovedLamp.globalGson.fromJson(config, ModConfig.class);
        } catch (IOException e) {
            return new ModConfig();
        }
    }
}
