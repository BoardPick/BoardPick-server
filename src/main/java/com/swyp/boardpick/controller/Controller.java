package com.swyp.boardpick.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/error")
    public String test() {
        return "error";}

    @GetMapping("/auth/oauth-response/**")
    public String success() {
        return "success";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
