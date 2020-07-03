package com.themarblefamily.marblecoin.market;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MarketDataService {
    private final static Logger LOGGER = Logger.getLogger(MarketDataService.class.getName());

    private Gson gson = new Gson();

    protected List<HistoricRate> getHistoricRate(String start, String stop, int granularity) {
        StringBuilder url = new StringBuilder("https://api.pro.coinbase.com/products/BTC-USD/candles?")
                .append("start=")
                .append(StringEscapeUtils.escapeHtml4(start))
                .append("&end=")
                .append(StringEscapeUtils.escapeHtml4(stop))
                .append("&granularity=")
                .append(granularity);
        String result = runGetCall(url.toString());
        if (result == null) {
          LOGGER.log(Level.WARNING, "Failed to get result from url: " + url.toString());
          return null;
        }
        Type type = new TypeToken<List<Double[]>>() {}.getType();
        List<Double[]> values = gson.fromJson(result, type);
        return toHistoricRates(values);
    }

    private String runGetCall(String getUrl) {
        String result = null;
        try {
            URL url = new URL(getUrl);
            HttpsURLConnection con = null;
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.connect();

            result = new BufferedReader(new InputStreamReader(con.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<HistoricRate> toHistoricRates(List<Double[]> values) {
        return values.stream().map(value -> {
            HistoricRate newRate = new HistoricRate();
            newRate.setTime(value[0]);
            newRate.setLow(value[1]);
            newRate.setHigh(value[2]);
            newRate.setOpen(value[3]);
            newRate.setClose(value[4]);
            newRate.setVolume(value[5]);
            return newRate;
        }).collect(Collectors.toList());
    }
}
