package com.community.meetup.service;

import com.community.meetup.model.Event;
import com.community.meetup.model.Organiser;
import com.community.meetup.repository.EventRepo;
import com.community.meetup.repository.OrganiserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private OrganiserRepo organiserRepo;

    public void save(Event event){
        eventRepo.save(event);
    }

    public long getMostRecentOrganiser(){
        Optional<Event> optEvent = eventRepo.findFirstByOrderByCreationTimestampDesc();
        return optEvent.map(Event::getOrganiserId).orElse(1L);
    }

    public Organiser getNextOrganiser() throws Exception {
        long mostRecentOrganiser = getMostRecentOrganiser();
        long total = organiserRepo.count();
        Optional<Organiser> optOrganiser;
        if(mostRecentOrganiser == total){
            optOrganiser = organiserRepo.findById(1L);
        }
        else {
            optOrganiser = organiserRepo.findById(mostRecentOrganiser + 1);
        }
        if(optOrganiser.isPresent()){
            return optOrganiser.get();
        } else{
            throw new Exception("Could not find next organiser, please add an organiser before creating an event");
        }
    }


}
