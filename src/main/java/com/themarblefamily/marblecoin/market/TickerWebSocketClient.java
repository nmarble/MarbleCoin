package com.themarblefamily.marblecoin.market;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class TickerWebSocketClient extends WebSocketClient {
    MarketFeedService marketFeedService;

    private Gson gson = new Gson();

    public TickerWebSocketClient(URI serverURI, MarketFeedService marketFeedService ) {
        super( serverURI );
        this.marketFeedService = marketFeedService;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println( "opened connection" );
        send("{\n" +
                "    \"type\": \"subscribe\",\n" +
                "    \"product_ids\": [\n" +
                "        \"BTC-USD\"\n" +
                "    ],\n" +
                "    \"channels\": [\n" +
                "        \"level2\",\n" +
                "        \"heartbeat\",\n" +
                "        {\n" +
                "            \"name\": \"ticker\",\n" +
                "            \"product_ids\": [\n" +
                "                \"BTC-USD\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}");
    }

    @Override
    public void onMessage(String s) {
        JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
        if (jsonObject.get("type").getAsString().equals("ticker"))
        {
            marketFeedService.addTicker( gson.fromJson(s, Ticker.class));
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println( "Connection closed by " + ( b ? "remote peer" : "us" ) + " Code: " + i + " Reason: " + s );
        marketFeedService.restartTicker();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
