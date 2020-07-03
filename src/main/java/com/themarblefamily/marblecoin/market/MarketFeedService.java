package com.themarblefamily.marblecoin.market;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MarketFeedService {
    private final static Logger LOGGER = Logger.getLogger(MarketFeedService.class.getName());
    private List<Ticker> tickers = new ArrayList<>();
    private List<Ticker> newTickers = new ArrayList<>();
    private int count = 0;

    @PostConstruct
    public void startTicker() throws URISyntaxException {
        String URL = "wss://ws-feed.pro.coinbase.com";
        TickerWebSocketClient tickerWebSocketClient = new TickerWebSocketClient(new URI(URL), this);
        tickerWebSocketClient.connect();
    }

    public void restartTicker() {
        try {
            startTicker();
        } catch (URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "Failed to restart ticker " + e);
        }
    }

    private void pruneTicker() {
        tickers = tickers.stream().filter(ticker -> ticker.getLocalDateTime().isAfter(LocalDateTime.now(ZoneOffset.UTC).minusHours(5))).collect(Collectors.toList());
    }

    public void addTicker(Ticker newTicker) {
        if (tickers.contains(newTicker)) {
            LOGGER.log(Level.WARNING, "Ticker " + newTicker.getTrade_id() + " Already exists and was trying to be added");
        } else {
            tickers.add(newTicker);
        }
        count ++;
        if (count > 10000) {
            pruneTicker();
            count = 0;
        }
    }

    public List<Ticker> getTickers() {
        return tickers;
    }

    public List<Ticker> getNewTickers() {
        List<Ticker> returnTickers = new ArrayList<>(tickers);
        returnTickers.removeAll(newTickers);
        newTickers = new ArrayList<>(tickers);
        return returnTickers;
    }
}
