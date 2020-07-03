package com.themarblefamily.marblecoin.sim;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Trader {
    private final static Logger LOGGER = Logger.getLogger(Trader.class.getName());
    private String name;
    private String eMA1;
    private String eMA2;
    private double lastMACD;
    private double lastMACDSignal;
    private double money;
    private double BTC;

    public Trader (String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public Trader setName(String name) {
        this.name = name;
        return this;
    }

    public String getEMA1() {
        return eMA1;
    }

    public Trader setEMA1(String eMA1) {
        this.eMA1 = eMA1;
        return this;
    }

    public String getEMA2() {
        return eMA2;
    }

    public Trader setEMA2(String eMA2) {
        this.eMA2 = eMA2;
        return this;
    }

    public double getMoney() {
        return money;
    }

    public Trader setMoney(double money) {
        this.money = money;
        return this;
    }

    public double getBTC() {
        return BTC;
    }

    public Trader setBTC(double BTC) {
        this.BTC = BTC;
        return this;
    }

    public double getLastMACD() {
        return lastMACD;
    }

    public Trader setLastMACD(double lastMACD) {
        this.lastMACD = lastMACD;
        return this;
    }

    public double getLastMACDSignal() {
        return lastMACDSignal;
    }

    public Trader setLastMACDSignal(double lastMACDSignal) {
        this.lastMACDSignal = lastMACDSignal;
        return this;
    }

    public String getMACD() {
        return "MACD" + eMA1 + "-" + eMA2;
    }

    public String getMACDSignal() {
        return "MACDS" + eMA1 + "-" + eMA2;
    }

    public void printWallet() {
        LOGGER.log(Level.INFO, "------------TRADER STATISTICS------------");
        LOGGER.log(Level.INFO, "Money = " + money);
        LOGGER.log(Level.INFO, "BTC = " + BTC);
        LOGGER.log(Level.INFO, "------------END STATISTICS------------");
    }
}
