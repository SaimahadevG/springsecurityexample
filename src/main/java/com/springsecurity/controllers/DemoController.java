package com.springsecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController 
{
    @GetMapping("/admin")
    public String getAdmin()
    {
    	return "hiadmin";
    }
    
    @GetMapping("/user")
    public String getUser()
    {
    	return "hiuser";
    }
}
