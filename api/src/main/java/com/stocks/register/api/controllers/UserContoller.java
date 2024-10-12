package com.stocks.register.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class UserContoller {

    @Autowired
    private Environment env;

    @GetMapping("/api")
    public String getHelp() {
        return env.getProperty("application.env");
    }

}
