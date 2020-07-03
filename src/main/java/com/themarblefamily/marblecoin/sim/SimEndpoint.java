package com.themarblefamily.marblecoin.sim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SimEndpoint {

    @Autowired
    BitcoinTrader bitcoinTrader;

    @GetMapping(value = "/traders/all")
    public @ResponseBody
    List<Trader> getAllTraderData() {
        return bitcoinTrader.getTraders();
    }
}
