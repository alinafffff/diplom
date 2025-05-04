package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.EventHackathonDTO;
import ru.diplom.diplom.dto.EventPartnersHackathonDTO;
import ru.diplom.diplom.dto.EventVolunteeringDTO;
import ru.diplom.diplom.dto.NewsDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.EventType;
import ru.diplom.diplom.services.EventService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/findByCategory")
    public List<Event> findAllByCategory(@RequestParam EventType type) {
        return eventService.findByType(type);
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable int id) {
        return eventService.getOne(id);
    }

    @PostMapping("/createVolunteering/{authorId}")
    public EventVolunteeringDTO createVolunteering(@PathVariable Integer authorId, @RequestBody EventVolunteeringDTO event) {
        return eventService.createVolunteeringEvent(authorId,event);
    }

    @PostMapping("/createHackathon/{authorId}")
    public EventHackathonDTO createHackathon(@PathVariable Integer authorId, @RequestBody EventHackathonDTO event) {
        return eventService.createHackathonEvent(authorId,event);
    }

    @PostMapping("/createPartnersHackathon/{authorId}")
    public EventPartnersHackathonDTO createPartnersHackathon(@PathVariable Integer authorId, @RequestBody EventPartnersHackathonDTO event) {
        return eventService.createPartnersHackathonEvent(authorId,event);
    }

    @DeleteMapping("/deleteById/{id}")
    public Event delete(@PathVariable int id) {
        return eventService.delete(id);
    }

    @GetMapping("/allEvents")
    public List<?> getAllEvents() {
        return eventService.getAllEventsWithTypeSpecificDTOs();
    }

}
