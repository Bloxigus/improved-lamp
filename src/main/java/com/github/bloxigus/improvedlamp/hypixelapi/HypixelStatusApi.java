package com.github.bloxigus.improvedlamp.hypixelapi;

import com.github.bloxigus.improvedlamp.ImprovedLamp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class HypixelStatusApi {
    public class ApiResponse {
        public boolean success;
        public String uuid;
        public OnlineStatus session;
    }
    public class OnlineStatus {
        public boolean online;
        public String gameType;
        public String mode;
        public String map;
    }
    public static CompletableFuture<ApiResponse> requestStatus(String uuid) {
        CompletableFuture<ApiResponse> response = new CompletableFuture<>();
        Thread requestThread = new Thread(() -> {
            try {
                URL u = new URL("https://api.hypixel.net/v2/status?uuid=" + uuid);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty( "API-Key", ImprovedLamp.config.API_KEY);
                InputStream is = conn.getInputStream();
                int ch;
                StringBuilder b = new StringBuilder();
                while( ( ch = is.read() ) != -1 ){
                    b.append( (char)ch );
                }
                String s = b.toString();
                response.complete(ImprovedLamp.globalGson.fromJson(s, ApiResponse.class));
            } catch (IOException e) {
                // ignore 404 errors
                response.completeExceptionally(e);
            }
        }, "HypixelStatusThread");
        requestThread.start();
        return response;
    }
}
