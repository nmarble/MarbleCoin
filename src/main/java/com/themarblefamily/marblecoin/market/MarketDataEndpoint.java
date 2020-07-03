package com.themarblefamily.marblecoin.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MarketDataEndpoint {

    @Autowired
    MarketDataService marketDataService;

    @GetMapping(value = "/historic/{start}/{stop}/{granularity}")
    public @ResponseBody List<HistoricRate> getHistoricData(@PathVariable String start, @PathVariable String stop,
                                                            @PathVariable int granularity) {
        return marketDataService.getHistoricRate(start, stop, granularity);
    }
}
