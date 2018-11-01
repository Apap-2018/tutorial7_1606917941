package com.apap.tutorial7.controller;

import com.apap.tutorial7.model.PilotModel;
import com.apap.tutorial7.rest.PilotDetail;
import com.apap.tutorial7.rest.Setting;
import com.apap.tutorial7.service.PilotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * PilotController
 */
@RestController
@RequestMapping("/pilot")
public class PilotController {
    @Autowired
    private PilotService pilotService;
    
    @Autowired
    RestTemplate restTemplate;
    
    @Bean
    public RestTemplate rest() {
    	return new RestTemplate();
    }
    
    @GetMapping(value="/status/{licenseNumber}")
    public String getStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception {
    	String path = Setting.pilotUrl + "/pilot?licenseNumber="+licenseNumber;
    	return restTemplate.getForEntity(path, String.class).getBody();
    }
    
    @GetMapping(value="/full/{licenseNumber}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PilotDetail postStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception {
    	String path = Setting.pilotUrl + "/pilot";
    	PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
    	PilotDetail detail = restTemplate.postForObject(path, pilot, PilotDetail.class);
    	return detail;
    }
    
    @PostMapping(value = "/add")
    private PilotModel addPilotSubmit(@RequestBody PilotModel pilot) {
        return pilotService.addPilot(pilot);
    }

    @GetMapping(value = "/view/{licenseNumber}")
    private PilotModel view(@PathVariable("licenseNumber") String licenseNumber) {
        PilotModel archivePilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
        return archivePilot;
    }

    @DeleteMapping(value = "/delete")
    private String delete(@RequestParam(value = "pilotId") long pilotId) {
        PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
        pilotService.deletePilot(pilot);
        return "success";
    }

    @PutMapping(value = "/update/{pilotId}")
    private String updatePilotSubmit(@PathVariable("pilotId") long pilotId,
    									@RequestParam("name") String name,
    									@RequestParam("flyHour") int flyHour) {
        PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
        if(pilot.equals(null)) {
        	return "Couldn't find your pilot";
        }
        
        pilot.setName(name);
        pilot.setFlyHour(flyHour);
        pilotService.updatePilot(pilotId, pilot);
        return "update";
    }
}