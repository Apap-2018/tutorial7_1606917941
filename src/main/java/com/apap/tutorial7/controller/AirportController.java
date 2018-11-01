package com.apap.tutorial7.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.apap.tutorial7.rest.Setting;

@RestController
@RequestMapping("/airport")
public class AirportController {
	
    @GetMapping()
    private Object airportList(@RequestParam("term") String term) throws Exception {
    	String path = Setting.airportUrl+"&term="+term+"&country=ID";
    	RestTemplate rest = new RestTemplate();
    	Object airport = rest.getForObject(path, Object.class);
    	return airport;
    }
    
}
