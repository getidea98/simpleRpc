package com.example.nettyrpc.client.controller;

import com.example.nettyrpc.api.interfaces.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private HiService hiService;


    @GetMapping("/login")
    public String login() {
        return hiService.hi("word space");
    }
}
