package com.und.eventapi.repository;

import com.und.eventapi.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {

    public List<Event> findByName(String eventName);

}



