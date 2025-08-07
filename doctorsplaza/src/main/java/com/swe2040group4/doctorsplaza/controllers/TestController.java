/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swe2040group4.doctorsplaza.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ping() {
        System.out.println(">>> Ping endpoint hit");
        return "Backend is alive!";
    }
}
