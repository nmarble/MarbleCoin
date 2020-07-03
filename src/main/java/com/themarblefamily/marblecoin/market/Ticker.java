package com.themarblefamily.marblecoin.market;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticker {
    private double sequence;
    private String product_id;
    private double price;
    private double open_24h;
    private double volume_24h;
    private double low_24h;
    private double high_24h;
    private double volume_30d;
    private double best_bid;
    private double best_ask;
    private String side;
    private String time;
    private double trade_id;
    private double last_size;


    public double getSequence() {
        return sequence;
    }

    public void setSequence(double sequence) {
        this.sequence = sequence;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOpen_24h() {
        return open_24h;
    }

    public void setOpen_24h(double open_24h) {
        this.open_24h = open_24h;
    }

    public double getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(double volume_24h) {
        this.volume_24h = volume_24h;
    }

    public double getLast_size() {
        return last_size;
    }

    public void setLast_size(double last_size) {
        this.last_size = last_size;
    }

    public double getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(double trade_id) {
        this.trade_id = trade_id;
    }

    public LocalDateTime getLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSz");
        return LocalDateTime.parse(time, formatter);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public double getBest_ask() {
        return best_ask;
    }

    public void setBest_ask(double best_ask) {
        this.best_ask = best_ask;
    }

    public double getBest_bid() {
        return best_bid;
    }

    public void setBest_bid(double best_bid) {
        this.best_bid = best_bid;
    }

    public double getVolume_30d() {
        return volume_30d;
    }

    public void setVolume_30d(double volume_30d) {
        this.volume_30d = volume_30d;
    }

    public double getHigh_24h() {
        return high_24h;
    }

    public void setHigh_24h(double high_24h) {
        this.high_24h = high_24h;
    }

    public double getLow_24h() {
        return low_24h;
    }

    public void setLow_24h(double low_24h) {
        this.low_24h = low_24h;
    }

    @Override
    public boolean equals(Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Ticker)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Ticker o = (Ticker) obj;

        // Compare the data members and return accordingly
        return getTrade_id() == o.getTrade_id();
    }
}
