package com.community.meetup.controller;

import com.community.meetup.service.OrganiserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class OrganiserController {

    @Autowired
    private OrganiserService organiserService;

    @PostMapping("/organiser/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postOrganiser(@PathVariable(value = "name") String name) {
        log.info("adding name to organiser table: ", name);
        organiserService.addOrganiser(name);
    }
}
