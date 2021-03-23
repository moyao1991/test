package com.moyao.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moyao.dto.UserDto;
import com.moyao.service.UserService;

@RestController
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/user")
    public UserDto user() {
        return userService.findUser(47L);
    }

}
