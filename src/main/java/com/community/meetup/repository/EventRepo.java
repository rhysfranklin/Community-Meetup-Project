package com.community.meetup.repository;

import com.community.meetup.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepo extends CrudRepository<Event, Long> {

    public Optional<Event> findFirstByOrderByDescCreationTimestamp();

}
