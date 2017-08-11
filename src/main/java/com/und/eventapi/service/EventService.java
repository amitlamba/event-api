package com.und.eventapi.service;

import com.und.eventapi.model.Event;
import com.und.eventapi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> findByName(String name){
        return eventRepository.findByName(name);
    }

    public Event saveEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        return savedEvent;
    }
}
