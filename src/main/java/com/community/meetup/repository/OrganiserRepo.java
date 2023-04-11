package com.community.meetup.repository;

import com.community.meetup.model.Organiser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganiserRepo extends CrudRepository<Organiser, Long> {

}
