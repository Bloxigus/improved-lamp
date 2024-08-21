package com.github.bloxigus.improvedlamp.mojangapi;

import com.github.bloxigus.improvedlamp.ImprovedLamp;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MojangBulkUsernameApi {
    public static class ApiResponse {
        public ArrayList<PlayerEntry> data = new ArrayList<>();
        public void appendResponse(ApiResponse other) {
            data.addAll(other.data);
        }
    }
    public static class PlayerEntry {
        public String id;
        public String name;
    }
    public static CompletableFuture<ApiResponse> requestUuids(List<String> usernames) {
        CompletableFuture<ApiResponse> response = new CompletableFuture<>();
        if (usernames.isEmpty()) {
            response.complete(new ApiResponse());
            return response;
        }
        Thread requestThread = new Thread(() -> {
            List<List<String>> partitionedUsernames = Lists.partition(usernames, 10);
            ApiResponse apiResponse = new ApiResponse();
            for (List<String> uNamesPart : partitionedUsernames) {
                String rawData = ImprovedLamp.globalGson.toJson(uNamesPart);
                String type = "application/json";
                try {
                    URL u = new URL("https://api.minecraftservices.com/minecraft/profile/lookup/bulk/byname");
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", type);
                    conn.setRequestProperty("Content-Length", String.valueOf(rawData.length()));
                    OutputStream os = conn.getOutputStream();
                    os.write(rawData.getBytes(StandardCharsets.UTF_8));
                    InputStream is = conn.getInputStream();
                    int ch;
                    StringBuilder b = new StringBuilder();
                    b.append("{\"data\":");
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
                    b.append("}");
                    String s = b.toString();
                    apiResponse.appendResponse(ImprovedLamp.globalGson.fromJson(s, ApiResponse.class));
                } catch (IOException e) {
                }
            }
            response.complete(apiResponse);
        }, "MojangBulkUsernameThread");
        requestThread.start();
        return response;
    }
}
