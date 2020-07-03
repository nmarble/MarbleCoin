package com.themarblefamily.marblecoin.market;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MarketStatsService {
    private final static Logger LOGGER = Logger.getLogger(MarketStatsService.class.getName());
    private Map<String, Double> stats = new HashMap<>();

    @Autowired
    MarketFeedService marketFeedService;

    @Autowired
    MarketDataService marketDataService;

    @Scheduled(fixedRate = 60000)
    private void calculateStats() {
        List<Ticker> tickers = new ArrayList<>(marketFeedService.getTickers());
        Map<String, Double> currentStats = new HashMap<>();
        List<Ticker> lastMinTickers = marketFeedService.getNewTickers();
        if(stats.get("Total") != null) {
            currentStats.put("Total", (double) tickers.size());
            currentStats.put("Sold in Last min", (double) tickers.size() - stats.get("Total"));
            currentStats.put("Highest", lastMinTickers.stream().mapToDouble(Ticker::getPrice).max().orElse(0));
            currentStats.put("Volume", lastMinTickers.stream().mapToDouble(Ticker::getLast_size).sum());
            currentStats.put("Average", lastMinTickers.get(lastMinTickers.size() - 1).getPrice());
            updateEMAS(currentStats.get("Average"));
            updateMACDs();
            updateMACDSignals();
        } else {
            currentStats.put("Total", (double) tickers.size());
        }
        currentStats.forEach((key, value) -> stats.put(key, value));

        printStats(stats);
    }

    private void updateMACDs() {
        List<String> EMAS = stats.keySet().stream().filter(key -> key.startsWith("EMA")).collect(Collectors.toList());
        EMAS.forEach(EMA1 -> EMAS.forEach(EMA2 -> {
            if(!EMA1.equals(EMA2)) {
                stats.put("MACD" + EMA1 + "-" + EMA2, stats.get(EMA1) - stats.get(EMA2));
            }
        }));
    }
    
    private void updateMACDSignals() {
        List<String> MACDs = stats.keySet().stream().filter(key -> key.startsWith("MACDE")).collect(Collectors.toList());
        MACDs.forEach(macd -> {
            String macdSignalName = "MACDS" + macd.split("MACD")[1];
            double smallest = Double.parseDouble(StringUtils.chop(macdSignalName.split("EMA")[1]));
            stats.put(macdSignalName, calculateEMA(stats.get(macd), stats.get(macdSignalName), smallest * .75, 2));
        });
    }

    private void updateEMAS(double newPrice) {
        List<String> EMAS = stats.keySet().stream().filter(key -> key.startsWith("EMA")).collect(Collectors.toList());
        EMAS.forEach(ema -> stats.put(ema, calculateEMA(newPrice, stats.get(ema), Double.parseDouble(ema.split("EMA")[1]), 2)));
    }

    private double calculateEMA(double nowPrice, double lastPrice, double period, double smoothing) {
        return (nowPrice * (smoothing / (1 + period))) + (lastPrice * (1 - (smoothing / (1 + period))));
    }

    public void addInitialEMAs(List<String> EMAs) {
        EMAs.forEach(ema -> addInitialEMA(Integer.parseInt(ema.split("EMA")[1])));

        List<String> EMAS = stats.keySet().stream().filter(key -> key.contains("EMA")).collect(Collectors.toList());
        EMAS.forEach(EMA1 -> EMAS.forEach(EMA2 -> {
            if(!EMA1.equals(EMA2)) {
                stats.put("MACDS" + EMA1 + "-" + EMA2, stats.get(EMA1) - stats.get(EMA2));
                LOGGER.log(Level.INFO, "ADDED MACD SIGNAL for " + EMA1 + " and " + EMA2);
            }
        }));
    }

    private void addInitialEMA(int period) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'z'");
        String nowTime = LocalDateTime.now(ZoneOffset.UTC).format(formatter);
        String xPeriod = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(period - 1).format(formatter);
        int gran = 60;
        List<HistoricRate> historicRates = marketDataService.getHistoricRate(xPeriod, nowTime, gran);
        if (historicRates != null) {
            stats.put("EMA" + period, historicRates.stream().mapToDouble(HistoricRate::getClose).sum() / (period - 1));
            LOGGER.log(Level.INFO, "Added Initial EMA for " + period + " minutes");
        } else {
            LOGGER.log(Level.WARNING, "Error getting AMA for " + period);
        }
        TimeUtils.waitSec(2);
    }

    private void printStats(Map<String, Double> currentStats) {
        LOGGER.log(Level.INFO, "------------MARKET STATISTICS------------");
        currentStats.forEach((key, value) -> LOGGER.log(Level.INFO, key + ": " + value));
        LOGGER.log(Level.INFO, "------------END STATISTICS------------");
    }

    public Map<String, Double> getStats() {
        return stats;
    }
}
