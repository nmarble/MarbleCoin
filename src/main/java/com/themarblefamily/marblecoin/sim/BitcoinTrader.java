package com.themarblefamily.marblecoin.sim;

import com.themarblefamily.marblecoin.market.MarketStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BitcoinTrader {
    private final static Logger LOGGER = Logger.getLogger(BitcoinTrader.class.getName());
    private final static double STARTING_MONEY = 10000;
    private final static double STARTING_BTC = 0;
    private List<Trader> traders = new ArrayList<>();

    @Autowired
    MarketStatsService marketStatsService;

    @PostConstruct
    private void postConstruct() {
        Trader trader12x25 = new Trader("12 and 26");
        trader12x25.setEMA1("EMA12").setEMA2("EMA25");

        Trader trader30x60 = new Trader("30 and 60");
        trader30x60.setEMA1("EMA30").setEMA2("EMA60");

        Trader trader50x100 = new Trader("50 and 100");
        trader50x100.setEMA1("EMA50").setEMA2("EMA100");

        Trader trader100x300 = new Trader("100 and 300");
        trader100x300.setEMA1("EMA100").setEMA2("EMA300");

        traders.addAll(Arrays.asList(trader12x25, trader30x60, trader50x100, trader100x300));
        traders.forEach(trader -> trader.setMoney(STARTING_MONEY).setBTC(STARTING_BTC));

        List<String> eMAs = traders.stream().map(Trader::getEMA1).collect(Collectors.toList());
        eMAs.addAll(traders.stream().map(Trader::getEMA2).collect(Collectors.toList()));

        marketStatsService.addInitialEMAs(eMAs);
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        Map<String, Double> stats = marketStatsService.getStats();
        traders.forEach(trader -> {
            String macd = trader.getMACD();
            String macdSignal = trader.getMACDSignal();
            if(stats.get(macd) != null) {
                if(stats.get(macd) < stats.get(macdSignal) && trader.getLastMACD() > trader.getLastMACDSignal()) {
                    manageBuy(stats, trader);
                }
                if(stats.get(macd) > stats.get(macdSignal) && trader.getLastMACD() < trader.getLastMACDSignal()) {
                    manageSell(stats, trader);
                } else {
                    LOGGER.log(Level.INFO, trader.getName() + " is holding... Price at " + stats.get("Average"));
                }
                trader.setLastMACD(stats.get(trader.getMACD()));
                trader.setLastMACDSignal(stats.get(trader.getMACDSignal()));
            }
        });
    }

    private void manageSell(Map<String, Double> stats, Trader trader) {
        if (trader.getBTC() > 0) {
            double price = stats.get("Average") * trader.getBTC();
            trader.setMoney(trader.getMoney() + price);
            LOGGER.log(Level.WARNING, trader.getName() + " SOLD: " + trader.getBTC() + " btc for " + price + " price at " + stats.get("Average"));
            trader.setBTC(0);
        }
        trader.printWallet();
    }

    private void manageBuy(Map<String, Double> stats, Trader trader) {
        if (trader.getMoney() > 0) {
            double btcToBuy = (trader.getMoney() * .10) / stats.get("Average");
            double cost = btcToBuy * stats.get("Average");
            trader.setMoney(trader.getMoney() - cost);
            trader.setBTC(trader.getBTC() + btcToBuy);
            LOGGER.log(Level.WARNING, trader.getName() + " BOUGHT: " + btcToBuy + " btc for " + cost  + " price at " + stats.get("Average"));
        }
        trader.printWallet();
    }

    public List<Trader> getTraders() {
        return traders;
    }
}
