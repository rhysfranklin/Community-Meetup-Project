package com.community.meetup.service;

import com.community.meetup.model.Organiser;
import com.community.meetup.repository.OrganiserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostgresOrganiserService implements OrganiserService{

    @Autowired
    private OrganiserRepo organiserRepo;


    @Override
    public Optional<Organiser> getOrganiser(long id) {
        return organiserRepo.findById(id);
    }

    @Override
    public void addOrganiser(String name) {
        Organiser organiser = new Organiser(name);
        organiserRepo.save(organiser);
    }
}
