package ru.diplom.diplom.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.SaveEventDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.services.EventService;

@RestController
@RequestMapping("/inpit/events")
public class EventController {
    private EventService eventService;

    @GetMapping("/findByCategory")
    public Page<Event> findAllByCategory(Pageable pageable, @RequestParam Event.Type type) {
        return eventService.findAllByCategory(type, pageable);
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable int id) {
        return eventService.getOne(id);
    }

    @PostMapping
    public Event save(@RequestBody SaveEventDTO saveEventDto) {
        return eventService.save(saveEventDto);
    }

    @DeleteMapping("/{id}")
    public Event delete(@PathVariable int id) {
        return eventService.delete(id);
    }

}
