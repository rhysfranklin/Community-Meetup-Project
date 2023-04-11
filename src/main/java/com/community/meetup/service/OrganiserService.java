package com.community.meetup.service;

import com.community.meetup.model.Organiser;

import java.util.Optional;

public interface OrganiserService {

    Optional<Organiser> getOrganiser(long id);
    void addOrganiser(String name);
}
